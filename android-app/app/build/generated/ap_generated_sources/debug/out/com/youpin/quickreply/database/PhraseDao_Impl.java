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
import com.youpin.quickreply.model.Phrase;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PhraseDao_Impl implements PhraseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Phrase> __insertionAdapterOfPhrase;

  private final EntityDeletionOrUpdateAdapter<Phrase> __deletionAdapterOfPhrase;

  private final EntityDeletionOrUpdateAdapter<Phrase> __updateAdapterOfPhrase;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfIncrementUseCount;

  public PhraseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPhrase = new EntityInsertionAdapter<Phrase>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `phrases` (`id`,`title`,`content`,`type`,`categoryId`,`parentCategoryId`,`usageCount`,`imagePath`,`filePath`,`fileName`,`fileSize`,`createTime`,`updateTime`,`categoryName`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Phrase entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getContent() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getContent());
        }
        if (entity.getType() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getType());
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCategoryId());
        }
        if (entity.getParentCategoryId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getParentCategoryId());
        }
        statement.bindLong(7, entity.getUsageCount());
        if (entity.getImagePath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getImagePath());
        }
        if (entity.getFilePath() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getFilePath());
        }
        if (entity.getFileName() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getFileName());
        }
        statement.bindLong(11, entity.getFileSize());
        statement.bindLong(12, entity.getCreateTime());
        statement.bindLong(13, entity.getUpdateTime());
        if (entity.getCategoryName() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getCategoryName());
        }
      }
    };
    this.__deletionAdapterOfPhrase = new EntityDeletionOrUpdateAdapter<Phrase>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `phrases` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Phrase entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPhrase = new EntityDeletionOrUpdateAdapter<Phrase>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `phrases` SET `id` = ?,`title` = ?,`content` = ?,`type` = ?,`categoryId` = ?,`parentCategoryId` = ?,`usageCount` = ?,`imagePath` = ?,`filePath` = ?,`fileName` = ?,`fileSize` = ?,`createTime` = ?,`updateTime` = ?,`categoryName` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Phrase entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getContent() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getContent());
        }
        if (entity.getType() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getType());
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCategoryId());
        }
        if (entity.getParentCategoryId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getParentCategoryId());
        }
        statement.bindLong(7, entity.getUsageCount());
        if (entity.getImagePath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getImagePath());
        }
        if (entity.getFilePath() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getFilePath());
        }
        if (entity.getFileName() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getFileName());
        }
        statement.bindLong(11, entity.getFileSize());
        statement.bindLong(12, entity.getCreateTime());
        statement.bindLong(13, entity.getUpdateTime());
        if (entity.getCategoryName() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getCategoryName());
        }
        statement.bindLong(15, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM phrases WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementUseCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE phrases SET usageCount = usageCount + 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Phrase phrase) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfPhrase.insertAndReturnId(phrase);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Phrase phrase) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfPhrase.handle(phrase);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Phrase phrase) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPhrase.handle(phrase);
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
  public void incrementUseCount(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementUseCount.acquire();
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
      __preparedStmtOfIncrementUseCount.release(_stmt);
    }
  }

  @Override
  public List<Phrase> getPhrasesByType(final String type) {
    final String _sql = "SELECT * FROM phrases WHERE type = ? ORDER BY updateTime DESC";
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
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
      final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
      final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
      final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
      final List<Phrase> _result = new ArrayList<Phrase>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Phrase _item;
        _item = new Phrase();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Long _tmpCategoryId;
        if (_cursor.isNull(_cursorIndexOfCategoryId)) {
          _tmpCategoryId = null;
        } else {
          _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
        }
        _item.setCategoryId(_tmpCategoryId);
        final Long _tmpParentCategoryId;
        if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
          _tmpParentCategoryId = null;
        } else {
          _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
        }
        _item.setParentCategoryId(_tmpParentCategoryId);
        final int _tmpUsageCount;
        _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
        _item.setUsageCount(_tmpUsageCount);
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _item.setFilePath(_tmpFilePath);
        final String _tmpFileName;
        if (_cursor.isNull(_cursorIndexOfFileName)) {
          _tmpFileName = null;
        } else {
          _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
        }
        _item.setFileName(_tmpFileName);
        final long _tmpFileSize;
        _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
        _item.setFileSize(_tmpFileSize);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _item.setUpdateTime(_tmpUpdateTime);
        final String _tmpCategoryName;
        if (_cursor.isNull(_cursorIndexOfCategoryName)) {
          _tmpCategoryName = null;
        } else {
          _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
        }
        _item.setCategoryName(_tmpCategoryName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Phrase> getPhrasesByCategory(final long categoryId) {
    final String _sql = "SELECT * FROM phrases WHERE categoryId = ? ORDER BY updateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
      final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
      final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
      final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
      final List<Phrase> _result = new ArrayList<Phrase>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Phrase _item;
        _item = new Phrase();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Long _tmpCategoryId;
        if (_cursor.isNull(_cursorIndexOfCategoryId)) {
          _tmpCategoryId = null;
        } else {
          _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
        }
        _item.setCategoryId(_tmpCategoryId);
        final Long _tmpParentCategoryId;
        if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
          _tmpParentCategoryId = null;
        } else {
          _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
        }
        _item.setParentCategoryId(_tmpParentCategoryId);
        final int _tmpUsageCount;
        _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
        _item.setUsageCount(_tmpUsageCount);
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _item.setFilePath(_tmpFilePath);
        final String _tmpFileName;
        if (_cursor.isNull(_cursorIndexOfFileName)) {
          _tmpFileName = null;
        } else {
          _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
        }
        _item.setFileName(_tmpFileName);
        final long _tmpFileSize;
        _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
        _item.setFileSize(_tmpFileSize);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _item.setUpdateTime(_tmpUpdateTime);
        final String _tmpCategoryName;
        if (_cursor.isNull(_cursorIndexOfCategoryName)) {
          _tmpCategoryName = null;
        } else {
          _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
        }
        _item.setCategoryName(_tmpCategoryName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Phrase> getPhrasesByParentCategory(final long parentId) {
    final String _sql = "SELECT * FROM phrases WHERE parentCategoryId = ? ORDER BY updateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, parentId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
      final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
      final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
      final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
      final List<Phrase> _result = new ArrayList<Phrase>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Phrase _item;
        _item = new Phrase();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Long _tmpCategoryId;
        if (_cursor.isNull(_cursorIndexOfCategoryId)) {
          _tmpCategoryId = null;
        } else {
          _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
        }
        _item.setCategoryId(_tmpCategoryId);
        final Long _tmpParentCategoryId;
        if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
          _tmpParentCategoryId = null;
        } else {
          _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
        }
        _item.setParentCategoryId(_tmpParentCategoryId);
        final int _tmpUsageCount;
        _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
        _item.setUsageCount(_tmpUsageCount);
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _item.setFilePath(_tmpFilePath);
        final String _tmpFileName;
        if (_cursor.isNull(_cursorIndexOfFileName)) {
          _tmpFileName = null;
        } else {
          _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
        }
        _item.setFileName(_tmpFileName);
        final long _tmpFileSize;
        _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
        _item.setFileSize(_tmpFileSize);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _item.setUpdateTime(_tmpUpdateTime);
        final String _tmpCategoryName;
        if (_cursor.isNull(_cursorIndexOfCategoryName)) {
          _tmpCategoryName = null;
        } else {
          _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
        }
        _item.setCategoryName(_tmpCategoryName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Phrase> searchPhrases(final String keyword) {
    final String _sql = "SELECT * FROM phrases WHERE title LIKE '%' || ? || '%' OR content LIKE '%' || ? || '%' ORDER BY updateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (keyword == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, keyword);
    }
    _argIndex = 2;
    if (keyword == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, keyword);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
      final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
      final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
      final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
      final List<Phrase> _result = new ArrayList<Phrase>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Phrase _item;
        _item = new Phrase();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Long _tmpCategoryId;
        if (_cursor.isNull(_cursorIndexOfCategoryId)) {
          _tmpCategoryId = null;
        } else {
          _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
        }
        _item.setCategoryId(_tmpCategoryId);
        final Long _tmpParentCategoryId;
        if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
          _tmpParentCategoryId = null;
        } else {
          _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
        }
        _item.setParentCategoryId(_tmpParentCategoryId);
        final int _tmpUsageCount;
        _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
        _item.setUsageCount(_tmpUsageCount);
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _item.setFilePath(_tmpFilePath);
        final String _tmpFileName;
        if (_cursor.isNull(_cursorIndexOfFileName)) {
          _tmpFileName = null;
        } else {
          _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
        }
        _item.setFileName(_tmpFileName);
        final long _tmpFileSize;
        _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
        _item.setFileSize(_tmpFileSize);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _item.setUpdateTime(_tmpUpdateTime);
        final String _tmpCategoryName;
        if (_cursor.isNull(_cursorIndexOfCategoryName)) {
          _tmpCategoryName = null;
        } else {
          _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
        }
        _item.setCategoryName(_tmpCategoryName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Phrase> searchPhrasesByType(final String type, final String keyword) {
    final String _sql = "SELECT * FROM phrases WHERE type = ? AND (title LIKE '%' || ? || '%' OR content LIKE '%' || ? || '%') ORDER BY updateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    _argIndex = 2;
    if (keyword == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, keyword);
    }
    _argIndex = 3;
    if (keyword == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, keyword);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
      final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
      final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
      final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
      final List<Phrase> _result = new ArrayList<Phrase>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Phrase _item;
        _item = new Phrase();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Long _tmpCategoryId;
        if (_cursor.isNull(_cursorIndexOfCategoryId)) {
          _tmpCategoryId = null;
        } else {
          _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
        }
        _item.setCategoryId(_tmpCategoryId);
        final Long _tmpParentCategoryId;
        if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
          _tmpParentCategoryId = null;
        } else {
          _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
        }
        _item.setParentCategoryId(_tmpParentCategoryId);
        final int _tmpUsageCount;
        _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
        _item.setUsageCount(_tmpUsageCount);
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _item.setFilePath(_tmpFilePath);
        final String _tmpFileName;
        if (_cursor.isNull(_cursorIndexOfFileName)) {
          _tmpFileName = null;
        } else {
          _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
        }
        _item.setFileName(_tmpFileName);
        final long _tmpFileSize;
        _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
        _item.setFileSize(_tmpFileSize);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _item.setUpdateTime(_tmpUpdateTime);
        final String _tmpCategoryName;
        if (_cursor.isNull(_cursorIndexOfCategoryName)) {
          _tmpCategoryName = null;
        } else {
          _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
        }
        _item.setCategoryName(_tmpCategoryName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Phrase> searchPhrasesByCategory(final long categoryId, final String keyword) {
    final String _sql = "SELECT * FROM phrases WHERE categoryId = ? AND (title LIKE '%' || ? || '%' OR content LIKE '%' || ? || '%') ORDER BY updateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    _argIndex = 2;
    if (keyword == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, keyword);
    }
    _argIndex = 3;
    if (keyword == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, keyword);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
      final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
      final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
      final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
      final List<Phrase> _result = new ArrayList<Phrase>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Phrase _item;
        _item = new Phrase();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Long _tmpCategoryId;
        if (_cursor.isNull(_cursorIndexOfCategoryId)) {
          _tmpCategoryId = null;
        } else {
          _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
        }
        _item.setCategoryId(_tmpCategoryId);
        final Long _tmpParentCategoryId;
        if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
          _tmpParentCategoryId = null;
        } else {
          _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
        }
        _item.setParentCategoryId(_tmpParentCategoryId);
        final int _tmpUsageCount;
        _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
        _item.setUsageCount(_tmpUsageCount);
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _item.setFilePath(_tmpFilePath);
        final String _tmpFileName;
        if (_cursor.isNull(_cursorIndexOfFileName)) {
          _tmpFileName = null;
        } else {
          _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
        }
        _item.setFileName(_tmpFileName);
        final long _tmpFileSize;
        _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
        _item.setFileSize(_tmpFileSize);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _item.setUpdateTime(_tmpUpdateTime);
        final String _tmpCategoryName;
        if (_cursor.isNull(_cursorIndexOfCategoryName)) {
          _tmpCategoryName = null;
        } else {
          _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
        }
        _item.setCategoryName(_tmpCategoryName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Phrase getPhraseById(final long id) {
    final String _sql = "SELECT * FROM phrases WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
      final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
      final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
      final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
      final Phrase _result;
      if (_cursor.moveToFirst()) {
        _result = new Phrase();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _result.setContent(_tmpContent);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _result.setType(_tmpType);
        final Long _tmpCategoryId;
        if (_cursor.isNull(_cursorIndexOfCategoryId)) {
          _tmpCategoryId = null;
        } else {
          _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
        }
        _result.setCategoryId(_tmpCategoryId);
        final Long _tmpParentCategoryId;
        if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
          _tmpParentCategoryId = null;
        } else {
          _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
        }
        _result.setParentCategoryId(_tmpParentCategoryId);
        final int _tmpUsageCount;
        _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
        _result.setUsageCount(_tmpUsageCount);
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _result.setImagePath(_tmpImagePath);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _result.setFilePath(_tmpFilePath);
        final String _tmpFileName;
        if (_cursor.isNull(_cursorIndexOfFileName)) {
          _tmpFileName = null;
        } else {
          _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
        }
        _result.setFileName(_tmpFileName);
        final long _tmpFileSize;
        _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
        _result.setFileSize(_tmpFileSize);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _result.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _result.setUpdateTime(_tmpUpdateTime);
        final String _tmpCategoryName;
        if (_cursor.isNull(_cursorIndexOfCategoryName)) {
          _tmpCategoryName = null;
        } else {
          _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
        }
        _result.setCategoryName(_tmpCategoryName);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getTotalCount() {
    final String _sql = "SELECT COUNT(*) FROM phrases";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
  public int getTotalUseCount() {
    final String _sql = "SELECT COALESCE(SUM(usageCount), 0) FROM phrases";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
  public List<Long> getAllCategoryIds() {
    final String _sql = "SELECT DISTINCT categoryId FROM phrases WHERE categoryId IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<Long> _result = new ArrayList<Long>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Long _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getLong(0);
        }
        _result.add(_item);
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
