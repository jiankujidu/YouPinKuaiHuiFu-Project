package com.quickreply.wechat.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.quickreply.wechat.model.Phrase;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

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
        return "INSERT OR ABORT INTO `phrases` (`id`,`content`,`categoryId`,`categoryName`,`categoryType`,`color`,`sortOrder`,`createTime`,`updateTime`,`useCount`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Phrase entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getContent() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getContent());
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCategoryId());
        }
        if (entity.getCategoryName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategoryName());
        }
        if (entity.getCategoryType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCategoryType());
        }
        statement.bindLong(6, entity.getColor());
        statement.bindLong(7, entity.getSortOrder());
        statement.bindLong(8, entity.getCreateTime());
        statement.bindLong(9, entity.getUpdateTime());
        statement.bindLong(10, entity.getUseCount());
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
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfPhrase = new EntityDeletionOrUpdateAdapter<Phrase>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `phrases` SET `id` = ?,`content` = ?,`categoryId` = ?,`categoryName` = ?,`categoryType` = ?,`color` = ?,`sortOrder` = ?,`createTime` = ?,`updateTime` = ?,`useCount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Phrase entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getContent() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getContent());
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCategoryId());
        }
        if (entity.getCategoryName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategoryName());
        }
        if (entity.getCategoryType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCategoryType());
        }
        statement.bindLong(6, entity.getColor());
        statement.bindLong(7, entity.getSortOrder());
        statement.bindLong(8, entity.getCreateTime());
        statement.bindLong(9, entity.getUpdateTime());
        statement.bindLong(10, entity.getUseCount());
        if (entity.getId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getId());
        }
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
        final String _query = "UPDATE phrases SET useCount = useCount + 1, updateTime = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Phrase phrase) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPhrase.insert(phrase);
      __db.setTransactionSuccessful();
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
  public void deleteById(final String phraseId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    if (phraseId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, phraseId);
    }
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
  public void incrementUseCount(final String phraseId, final long time) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementUseCount.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, time);
    _argIndex = 2;
    if (phraseId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, phraseId);
    }
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
  public LiveData<List<Phrase>> getAllPhrases() {
    final String _sql = "SELECT * FROM phrases ORDER BY sortOrder ASC, updateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"phrases"}, false, new Callable<List<Phrase>>() {
      @Override
      @Nullable
      public List<Phrase> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfUseCount = CursorUtil.getColumnIndexOrThrow(_cursor, "useCount");
          final List<Phrase> _result = new ArrayList<Phrase>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Phrase _item;
            _item = new Phrase();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            _item.setContent(_tmpContent);
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            _item.setCategoryId(_tmpCategoryId);
            final String _tmpCategoryName;
            if (_cursor.isNull(_cursorIndexOfCategoryName)) {
              _tmpCategoryName = null;
            } else {
              _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
            }
            _item.setCategoryName(_tmpCategoryName);
            final String _tmpCategoryType;
            if (_cursor.isNull(_cursorIndexOfCategoryType)) {
              _tmpCategoryType = null;
            } else {
              _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            }
            _item.setCategoryType(_tmpCategoryType);
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            _item.setColor(_tmpColor);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item.setSortOrder(_tmpSortOrder);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            _item.setCreateTime(_tmpCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            _item.setUpdateTime(_tmpUpdateTime);
            final int _tmpUseCount;
            _tmpUseCount = _cursor.getInt(_cursorIndexOfUseCount);
            _item.setUseCount(_tmpUseCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Phrase>> getPhrasesByCategory(final String categoryId) {
    final String _sql = "SELECT * FROM phrases WHERE categoryId = ? ORDER BY sortOrder ASC, updateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (categoryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, categoryId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"phrases"}, false, new Callable<List<Phrase>>() {
      @Override
      @Nullable
      public List<Phrase> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfUseCount = CursorUtil.getColumnIndexOrThrow(_cursor, "useCount");
          final List<Phrase> _result = new ArrayList<Phrase>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Phrase _item;
            _item = new Phrase();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            _item.setContent(_tmpContent);
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            _item.setCategoryId(_tmpCategoryId);
            final String _tmpCategoryName;
            if (_cursor.isNull(_cursorIndexOfCategoryName)) {
              _tmpCategoryName = null;
            } else {
              _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
            }
            _item.setCategoryName(_tmpCategoryName);
            final String _tmpCategoryType;
            if (_cursor.isNull(_cursorIndexOfCategoryType)) {
              _tmpCategoryType = null;
            } else {
              _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            }
            _item.setCategoryType(_tmpCategoryType);
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            _item.setColor(_tmpColor);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item.setSortOrder(_tmpSortOrder);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            _item.setCreateTime(_tmpCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            _item.setUpdateTime(_tmpUpdateTime);
            final int _tmpUseCount;
            _tmpUseCount = _cursor.getInt(_cursorIndexOfUseCount);
            _item.setUseCount(_tmpUseCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Phrase>> searchPhrases(final String keyword) {
    final String _sql = "SELECT * FROM phrases WHERE content LIKE '%' || ? || '%' ORDER BY useCount DESC, updateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (keyword == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, keyword);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"phrases"}, false, new Callable<List<Phrase>>() {
      @Override
      @Nullable
      public List<Phrase> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfUseCount = CursorUtil.getColumnIndexOrThrow(_cursor, "useCount");
          final List<Phrase> _result = new ArrayList<Phrase>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Phrase _item;
            _item = new Phrase();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            _item.setContent(_tmpContent);
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            _item.setCategoryId(_tmpCategoryId);
            final String _tmpCategoryName;
            if (_cursor.isNull(_cursorIndexOfCategoryName)) {
              _tmpCategoryName = null;
            } else {
              _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
            }
            _item.setCategoryName(_tmpCategoryName);
            final String _tmpCategoryType;
            if (_cursor.isNull(_cursorIndexOfCategoryType)) {
              _tmpCategoryType = null;
            } else {
              _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            }
            _item.setCategoryType(_tmpCategoryType);
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            _item.setColor(_tmpColor);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item.setSortOrder(_tmpSortOrder);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            _item.setCreateTime(_tmpCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            _item.setUpdateTime(_tmpUpdateTime);
            final int _tmpUseCount;
            _tmpUseCount = _cursor.getInt(_cursorIndexOfUseCount);
            _item.setUseCount(_tmpUseCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Phrase>> getPhrasesByType(final String type) {
    final String _sql = "SELECT * FROM phrases WHERE categoryType = ? ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"phrases"}, false, new Callable<List<Phrase>>() {
      @Override
      @Nullable
      public List<Phrase> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final int _cursorIndexOfUseCount = CursorUtil.getColumnIndexOrThrow(_cursor, "useCount");
          final List<Phrase> _result = new ArrayList<Phrase>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Phrase _item;
            _item = new Phrase();
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            _item.setContent(_tmpContent);
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            _item.setCategoryId(_tmpCategoryId);
            final String _tmpCategoryName;
            if (_cursor.isNull(_cursorIndexOfCategoryName)) {
              _tmpCategoryName = null;
            } else {
              _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
            }
            _item.setCategoryName(_tmpCategoryName);
            final String _tmpCategoryType;
            if (_cursor.isNull(_cursorIndexOfCategoryType)) {
              _tmpCategoryType = null;
            } else {
              _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            }
            _item.setCategoryType(_tmpCategoryType);
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            _item.setColor(_tmpColor);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item.setSortOrder(_tmpSortOrder);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            _item.setCreateTime(_tmpCreateTime);
            final long _tmpUpdateTime;
            _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
            _item.setUpdateTime(_tmpUpdateTime);
            final int _tmpUseCount;
            _tmpUseCount = _cursor.getInt(_cursorIndexOfUseCount);
            _item.setUseCount(_tmpUseCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Phrase getPhraseById(final String phraseId) {
    final String _sql = "SELECT * FROM phrases WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (phraseId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, phraseId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
      final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryName");
      final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final int _cursorIndexOfUseCount = CursorUtil.getColumnIndexOrThrow(_cursor, "useCount");
      final Phrase _result;
      if (_cursor.moveToFirst()) {
        _result = new Phrase();
        final String _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getString(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _result.setContent(_tmpContent);
        final String _tmpCategoryId;
        if (_cursor.isNull(_cursorIndexOfCategoryId)) {
          _tmpCategoryId = null;
        } else {
          _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
        }
        _result.setCategoryId(_tmpCategoryId);
        final String _tmpCategoryName;
        if (_cursor.isNull(_cursorIndexOfCategoryName)) {
          _tmpCategoryName = null;
        } else {
          _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
        }
        _result.setCategoryName(_tmpCategoryName);
        final String _tmpCategoryType;
        if (_cursor.isNull(_cursorIndexOfCategoryType)) {
          _tmpCategoryType = null;
        } else {
          _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
        }
        _result.setCategoryType(_tmpCategoryType);
        final int _tmpColor;
        _tmpColor = _cursor.getInt(_cursorIndexOfColor);
        _result.setColor(_tmpColor);
        final int _tmpSortOrder;
        _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
        _result.setSortOrder(_tmpSortOrder);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _result.setCreateTime(_tmpCreateTime);
        final long _tmpUpdateTime;
        _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        _result.setUpdateTime(_tmpUpdateTime);
        final int _tmpUseCount;
        _tmpUseCount = _cursor.getInt(_cursorIndexOfUseCount);
        _result.setUseCount(_tmpUseCount);
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
  public int getPhraseCount() {
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
  public int getPhraseCountByCategory(final String categoryId) {
    final String _sql = "SELECT COUNT(*) FROM phrases WHERE categoryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (categoryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, categoryId);
    }
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
