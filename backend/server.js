const express = require('express');
const cors = require('cors');
const mysql = require('mysql2/promise');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { v4: uuidv4 } = require('uuid');
require('dotenv').config();

const app = express();
app.use(cors());
app.use(express.json());

// 数据库连接池
const pool = mysql.createPool({
  host: process.env.DB_HOST || 'localhost',
  user: process.env.DB_USER || 'root',
  password: process.env.DB_PASSWORD || '',
  database: process.env.DB_NAME || 'youpin_kuaihuifu',
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

// JWT 验证中间件
const authMiddleware = async (req, res, next) => {
  const token = req.headers.authorization?.split(' ')[1];
  if (!token) {
    return res.status(401).json({ error: '未提供 token' });
  }
  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET || 'your-secret-key');
    req.userId = decoded.userId;
    req.teamId = decoded.teamId;
    next();
  } catch (err) {
    res.status(401).json({ error: 'token 无效' });
  }
};

// ==================== 用户相关 API ====================

// 用户注册
app.post('/api/auth/register', async (req, res) => {
  const { username, password, nickname } = req.body;
  try {
    const hashedPassword = await bcrypt.hash(password, 10);
    const userId = uuidv4();
    await pool.execute(
      'INSERT INTO users (id, username, password, nickname, created_at) VALUES (?, ?, ?, ?, NOW())',
      [userId, username, hashedPassword, nickname]
    );
    res.json({ success: true, userId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// 用户登录
app.post('/api/auth/login', async (req, res) => {
  const { username, password } = req.body;
  try {
    const [rows] = await pool.execute('SELECT * FROM users WHERE username = ?', [username]);
    if (rows.length === 0) {
      return res.status(401).json({ error: '用户不存在' });
    }
    const user = rows[0];
    const valid = await bcrypt.compare(password, user.password);
    if (!valid) {
      return res.status(401).json({ error: '密码错误' });
    }
    const token = jwt.sign(
      { userId: user.id, teamId: user.team_id },
      process.env.JWT_SECRET || 'your-secret-key',
      { expiresIn: '7d' }
    );
    res.json({
      success: true,
      token,
      user: {
        id: user.id,
        username: user.username,
        nickname: user.nickname,
        teamId: user.team_id
      }
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==================== 话术相关 API ====================

// 获取话术列表
app.get('/api/phrases', authMiddleware, async (req, res) => {
  const { type, keyword } = req.query;
  try {
    let sql = 'SELECT * FROM phrases WHERE user_id = ? OR team_id = ?';
    let params = [req.userId, req.teamId];
    
    if (type) {
      sql += ' AND type = ?';
      params.push(type);
    }
    if (keyword) {
      sql += ' AND (title LIKE ? OR content LIKE ?)';
      params.push(`%${keyword}%`, `%${keyword}%`);
    }
    sql += ' ORDER BY updated_at DESC';
    
    const [rows] = await pool.execute(sql, params);
    res.json({ success: true, data: rows });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// 创建话术
app.post('/api/phrases', authMiddleware, async (req, res) => {
  const { title, content, type, categoryId } = req.body;
  try {
    const id = uuidv4();
    await pool.execute(
      'INSERT INTO phrases (id, user_id, team_id, title, content, type, category_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())',
      [id, req.userId, type === 'team' ? req.teamId : null, title, content, type, categoryId]
    );
    res.json({ success: true, id });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// 更新话术
app.put('/api/phrases/:id', authMiddleware, async (req, res) => {
  const { title, content, type, categoryId } = req.body;
  try {
    await pool.execute(
      'UPDATE phrases SET title = ?, content = ?, type = ?, category_id = ?, updated_at = NOW() WHERE id = ? AND (user_id = ? OR team_id = ?)',
      [title, content, type, categoryId, req.params.id, req.userId, req.teamId]
    );
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// 删除话术
app.delete('/api/phrases/:id', authMiddleware, async (req, res) => {
  try {
    await pool.execute(
      'DELETE FROM phrases WHERE id = ? AND (user_id = ? OR team_id = ?)',
      [req.params.id, req.userId, req.teamId]
    );
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==================== 团队相关 API ====================

// 创建团队
app.post('/api/teams', authMiddleware, async (req, res) => {
  const { name, description } = req.body;
  try {
    const teamId = uuidv4();
    await pool.execute(
      'INSERT INTO teams (id, name, description, owner_id, created_at) VALUES (?, ?, ?, ?, NOW())',
      [teamId, name, description, req.userId]
    );
    // 更新用户所属团队
    await pool.execute('UPDATE users SET team_id = ? WHERE id = ?', [teamId, req.userId]);
    res.json({ success: true, teamId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// 获取团队成员
app.get('/api/teams/members', authMiddleware, async (req, res) => {
  try {
    const [rows] = await pool.execute(
      'SELECT id, username, nickname, role FROM users WHERE team_id = ?',
      [req.teamId]
    );
    res.json({ success: true, data: rows });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==================== 同步 API ====================

// 获取最后更新时间
app.get('/api/sync/last-update', authMiddleware, async (req, res) => {
  try {
    const [rows] = await pool.execute(
      'SELECT MAX(updated_at) as last_update FROM phrases WHERE user_id = ? OR team_id = ?',
      [req.userId, req.teamId]
    );
    res.json({ success: true, lastUpdate: rows[0].last_update });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// 批量同步话术
app.post('/api/sync/phrases', authMiddleware, async (req, res) => {
  const { phrases } = req.body;
  try {
    for (const phrase of phrases) {
      await pool.execute(
        `INSERT INTO phrases (id, user_id, team_id, title, content, type, category_id, usage_count, created_at, updated_at)
         VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
         ON DUPLICATE KEY UPDATE
         title = VALUES(title), content = VALUES(content), type = VALUES(type),
         category_id = VALUES(category_id), usage_count = VALUES(usage_count), updated_at = NOW()`,
        [phrase.id, req.userId, phrase.teamId, phrase.title, phrase.content, phrase.type, phrase.categoryId, phrase.usageCount || 0]
      );
    }
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// 健康检查
app.get('/api/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date().toISOString() });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`🚀 Server running on port ${PORT}`);
});
