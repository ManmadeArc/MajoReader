package com.example.majoreader
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.jvm.internal.Intrinsics


class DataBase(context: Context?) {
    private val db: SQLiteDatabase

    private inner class CustomSQLiteOpenHelper(val aa: DataBase,
                                                              context: Context?
    ) : SQLiteOpenHelper(context, DB_NAME, null, 2) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("create table anime_favorite (_id integer primary key autoincrement not null,name text not null,image text not null,link text not null,lastChapterRead integer not null default 0);")
            db.execSQL("create table manga_favorite (_id integer primary key autoincrement not null,name text not null,image text not null,genre text not null,link text not null,lastChapterRead integer not null default 0);")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            if (oldVersion == 1) {
                db.execSQL(UpgradeQuery1A)
                db.execSQL(UpgradeQuery1M)
            }
        }
    }

    fun insert(queries: SearchQuery) {
        val query = "INSERT INTO anime_favorite(name, image, link) VALUES ('${queries.title}', '${queries.imageUrl}', '${queries.animId}');"
        db.execSQL(query)
        Log.i("insert() =", query)
    }

    fun insert(queries: MangaData) {
        Intrinsics.checkNotNullParameter(queries, "queries")
        var query: String = ""
        query+="INSERT INTO manga_favorite (name, image , link , genre) VALUES ('"
        query+=queries.title
        val str = "'"
        query+=str
        val str2 = ", "
        query+=str2
        query+=str
        query+=queries.imageUrl
        query+=str
        query+=str2
        query+=str
        query+=queries.id
        query+=str
        query+=str2
        query+=str
        query+=queries.genre
        query+=str
        query+=");"
        query = query
        Log.i("insert() = ", query)
        db.execSQL(query)
    }

    fun delete(queries: SearchQuery) {
        Intrinsics.checkNotNullParameter(queries, "queries")
        var query: String = ""
        query+="DELETE FROM anime_favorite WHERE name = '"
        query+=queries.title
        query+="';"
        query = query
        Log.i("delete() = ", query)
        db.execSQL(query)
    }

    fun delete(queries: MangaData) {
        Intrinsics.checkNotNullParameter(queries, "queries")
        var query: String = ""
        query+="DELETE FROM manga_favorite WHERE name = '"
        query+=queries.title
        query+="';"
        query = query
        Log.i("delete() = ", query)
        db.execSQL(query)
    }

    fun updateChapter(queries: SearchQuery, num: Int) {

        if (itsIn(queries)) {
            val data = get(queries)
            if (data.moveToNext()) {
                val idx = data.getInt(4)
                data.close()
                if (idx > num) {
                    return
                }
            }
            var query: String = ""
            query+="UPDATE anime_favorite SET lastChapterRead = "
            query+=num
            query+=" WHERE link = '"
            query+=queries.animId
            query+="';"
            db.execSQL(query)
        }
    }

    fun updateChapter(queries: MangaData, num: Int) {

        if (itsIn(queries)) {
            val data = get(queries)
            if ( data.moveToNext()) {
                val idx = data.getInt(5)
                data.close()
                if (idx > num) {
                    return
                }
            }
            var query: String = ""
            query+="UPDATE manga_favorite SET lastChapterRead = "
            query+=num
            query+=" WHERE link = '"
            query+=queries.id
            query+="';"
            db.execSQL(query)
        }
    }

    operator fun get(queries: SearchQuery): Cursor {
        Intrinsics.checkNotNullParameter(queries, "queries")
        var query: String = ""
        query+=" SELECT * from anime_favorite WHERE link= \""
        query+=queries.animId
        query+="\";"
        return db.rawQuery(query, null)
    }

    operator fun get(queries: MangaData): Cursor {
        Intrinsics.checkNotNullParameter(queries, "queries")
        var query: String = ""
        query+=" SELECT * from manga_favorite WHERE link= \""
        query+=queries.id
        query+="\";"
        return db.rawQuery(query, null)
    }

    fun selectAll(boolean_: Boolean): Cursor {
        var string = Table_A_FAV
        if (boolean_) {
            string = Table_M_FAV
        }
        val sQLiteDatabase = db
        var stringBuilder = ""
        stringBuilder += "SELECT * from "
        stringBuilder += string
        stringBuilder += " ORDER BY "
        stringBuilder += TABLE_ROW_NAME

        return sQLiteDatabase.rawQuery(stringBuilder.toString(), null)
    }

    fun itsIn(data: MangaData): Boolean {
        Intrinsics.checkNotNullParameter(data, "data")
        var query: String = ""
        query+=" SELECT * from manga_favorite WHERE link= \""
        query+=data.id
        query+="\";"
        val cursor = db.rawQuery(query, null)
        val result = cursor.count
        cursor.close()
        return result > 0
    }

    fun itsIn(data: SearchQuery): Boolean {
        Intrinsics.checkNotNullParameter(data, "data")
        var query: String = ""
        query+=" SELECT * from anime_favorite WHERE link= \""
        query+=data.animId
        query+="\";"
        val result= db.rawQuery(query, null)

        return result.count > 0
    }

    companion object {
        private const val DB_NAME = "FAVORITES"
        private const val DB_VERSION = 2
        const val TABLE_ROW_GENRE = "genre"
        const val TABLE_ROW_ID = "_id"
        const val TABLE_ROW_IMG = "image"
        const val TABLE_ROW_LAST_CHAPTER = "lastChapterRead"
        const val TABLE_ROW_LINK = "link"
        const val TABLE_ROW_NAME = "name"
        private const val Table_A_FAV = "anime_favorite"
        private const val Table_M_FAV = "manga_favorite"
        private const val UpgradeQuery1A =
            "ALTER TABLE anime_favorite ADD COLUMN lastChapterRead integer not null DEFAULT 0;"
        private const val UpgradeQuery1M =
            "ALTER TABLE manga_favorite ADD COLUMN lastChapterRead integer not null DEFAULT 0;"
    }

    init {
        val writableDatabase = CustomSQLiteOpenHelper(this, context).writableDatabase
        db = writableDatabase
    }
}