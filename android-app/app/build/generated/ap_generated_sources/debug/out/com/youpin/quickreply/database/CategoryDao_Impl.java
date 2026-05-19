package com.youpin.quickreply.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.youpin.quickreply.model.Category;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CategoryDao_Impl implements CategoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Category> __insertionAdapterOfCategory;

  private final EntityDeletionOrUpdateAdapter<Category> __deletionAdapterOfCategory;

  private final EntityDeletionOrUpdateAdapter<Category> __updateAdapterOfCategory;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public CategoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCategory = new EntityInsertionAdapter<Category>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `categories` (`id`,`name`,`type`,`parentId`,`sortOrder`,`color`,`createTime`,`updateTime`,`phraseCount`,`expanded`,`level`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Category entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getType());
        }
        if (entity.getParentId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getParentId());
        }
        statement.bindLong(5, entity.getSortOrder());
        if (entity.getColor() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getColor());
        }
        statement.bindLong(7, entity.getCreateTime());
        statement.bindLong(8, entity.getUpdateTime());
        statement.bindLong(9, entity.getPhraseCount());
        final int _tmp = entity.isExpanded() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindLong(11, entity.getLevel());
      }
    };
    this.__deletionAdapterOfCategory = new EntityDeletionOrUpdateAdapter<Category>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `categories` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Category entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCategory = new EntityDeletionOrUpdateAdapter<Category>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `categories` SET `id` = ?,`name` = ?,`type` = ?,`parentId` = ?,`sortOrder` = ?,`color` = ?,`createTime` = ?,`updateTime` = ?,`phraseCount` = ?,`expanded` = ?,`level` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Category entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getType());
        }
        if (entity.getParentId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getParentId());
        }
        statement.bindLong(5, entity.getSortOrder());
        if (entity.getColor() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getColor());
        }
        statement.bindLong(7, entity.getCreateTime());
        statement.bindLong(8, entity.getUpdateTime());
        statement.bindLong(9, entity.getPhraseCount());
        final int _tmp = entity.isExpanded() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindLong(11, entity.getLevel());
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM categories WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Category category) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfCategory.insertAndReturnId(category);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Category category) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCategory.handle(category);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Category category) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCategory.handle(category);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteById.release(_stmt);
    }
  }

  @Override
  public List<Category> getLevel1Categories(final String type) {
    final String _sql = "SELECT * FROM categories WHERE type = ? AND parentId IS NULL ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfPhraseCount = CursorUtil.getColumnIndexOrThrow(_cursor, "phraseCount");
      final int _cursorIndexOfExpanded = CursorUtil.getColumnIndexOrThrow(_cursor, "expanded");
      final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
      final List<Category> _result = new ArrayList<Category>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Category _item;
        _item = new Category();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Long _tmpParentId;
        if (_cursor.isNull(_cursorIndexOfParentId)) {
          _tmpParentId = null;
        } else {
          _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
        }
        _item.setParentId(_tmpParentId);
        final int _tmpSortOrder;
        _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
        _item.setSortOrder(_tmpSortOrder);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _item.setUpdateTime(_tmpUpdateTime);
        final int _tmpPhraseCount;
        _tmpPhraseCount = _cursor.getInt(_cursorIndexOfPhraseCount);
        _item.setPhraseCount(_tmpPhraseCount);
        final boolean _tmpExpanded;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfExpanded);
        _tmpExpanded = _tmp != 0;
        _item.setExpanded(_tmpExpanded);
        final int _tmpLevel;
        _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
        _item.setLevel(_tmpLevel);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Category> getLevel2Categories(final long parentId) {
    final String _sql = "SELECT * FROM categories WHERE parentId = ? ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, parentId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfPhraseCount = CursorUtil.getColumnIndexOrThrow(_cursor, "phraseCount");
      final int _cursorIndexOfExpanded = CursorUtil.getColumnIndexOrThrow(_cursor, "expanded");
      final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
      final List<Category> _result = new ArrayList<Category>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Category _item;
        _item = new Category();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Long _tmpParentId;
        if (_cursor.isNull(_cursorIndexOfParentId)) {
          _tmpParentId = null;
        } else {
          _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
        }
        _item.setParentId(_tmpParentId);
        final int _tmpSortOrder;
        _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
        _item.setSortOrder(_tmpSortOrder);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _item.setUpdateTime(_tmpUpdateTime);
        final int _tmpPhraseCount;
        _tmpPhraseCount = _cursor.getInt(_cursorIndexOfPhraseCount);
        _item.setPhraseCount(_tmpPhraseCount);
        final boolean _tmpExpanded;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfExpanded);
        _tmpExpanded = _tmp != 0;
        _item.setExpanded(_tmpExpanded);
        final int _tmpLevel;
        _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
        _item.setLevel(_tmpLevel);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Category> getAllCategories(final String type) {
    final String _sql = "SELECT * FROM categories WHERE type = ? ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfPhraseCount = CursorUtil.getColumnIndexOrThrow(_cursor, "phraseCount");
      final int _cursorIndexOfExpanded = CursorUtil.getColumnIndexOrThrow(_cursor, "expanded");
      final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
      final List<Category> _result = new ArrayList<Category>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Category _item;
        _item = new Category();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Long _tmpParentId;
        if (_cursor.isNull(_cursorIndexOfParentId)) {
          _tmpParentId = null;
        } else {
          _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
        }
        _item.setParentId(_tmpParentId);
        final int _tmpSortOrder;
        _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
        _item.setSortOrder(_tmpSortOrder);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _item.setUpdateTime(_tmpUpdateTime);
        final int _tmpPhraseCount;
        _tmpPhraseCount = _cursor.getInt(_cursorIndexOfPhraseCount);
        _item.setPhraseCount(_tmpPhraseCount);
        final boolean _tmpExpanded;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfExpanded);
        _tmpExpanded = _tmp != 0;
        _item.setExpanded(_tmpExpanded);
        final int _tmpLevel;
        _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
        _item.setLevel(_tmpLevel);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Category> getCategoriesByType(final String type) {
    final String _sql = "SELECT * FROM categories WHERE type = ? ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfPhraseCount = CursorUtil.getColumnIndexOrThrow(_cursor, "phraseCount");
      final int _cursorIndexOfExpanded = CursorUtil.getColumnIndexOrThrow(_cursor, "expanded");
      final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
      final List<Category> _result = new ArrayList<Category>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Category _item;
        _item = new Category();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Long _tmpParentId;
        if (_cursor.isNull(_cursorIndexOfParentId)) {
          _tmpParentId = null;
        } else {
          _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
        }
        _item.setParentId(_tmpParentId);
        final int _tmpSortOrder;
        _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
        _item.setSortOrder(_tmpSortOrder);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _item.setUpdateTime(_tmpUpdateTime);
        final int _tmpPhraseCount;
        _tmpPhraseCount = _cursor.getInt(_cursorIndexOfPhraseCount);
        _item.setPhraseCount(_tmpPhraseCount);
        final boolean _tmpExpanded;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfExpanded);
        _tmpExpanded = _tmp != 0;
        _item.setExpanded(_tmpExpanded);
        final int _tmpLevel;
        _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
        _item.setLevel(_tmpLevel);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getPhraseCountByCategory(final long categoryId) {
    final String _sql = "SELECT COUNT(*) FROM phrases WHERE categoryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getPhraseCountByParentCategory(final long parentId) {
    final String _sql = "SELECT COUNT(*) FROM phrases WHERE parentCategoryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, parentId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Category getCategoryById(final long id) {
    final String _sql = "SELECT * FROM categories WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfPhraseCount = CursorUtil.getColumnIndexOrThrow(_cursor, "phraseCount");
      final int _cursorIndexOfExpanded = CursorUtil.getColumnIndexOrThrow(_cursor, "expanded");
      final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
      final Category _result;
      if (_cursor.moveToFirst()) {
        _result = new Category();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _result.setType(_tmpType);
        final Long _tmpParentId;
        if (_cursor.isNull(_cursorIndexOfParentId)) {
          _tmpParentId = null;
        } else {
          _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
        }
        _result.setParentId(_tmpParentId);
        final int _tmpSortOrder;
        _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
        _result.setSortOrder(_tmpSortOrder);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _result.setColor(_tmpColor);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _result.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _result.setUpdateTime(_tmpUpdateTime);
        final int _tmpPhraseCount;
        _tmpPhraseCount = _cursor.getInt(_cursorIndexOfPhraseCount);
        _result.setPhraseCount(_tmpPhraseCount);
        final boolean _tmpExpanded;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfExpanded);
        _tmpExpanded = _tmp != 0;
        _result.setExpanded(_tmpExpanded);
        final int _tmpLevel;
        _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
        _result.setLevel(_tmpLevel);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
