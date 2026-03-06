package com.example.mybookshelf

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

val DATABASE_NAME = "BookTable"
val TABLE_NAME = "Books"
val COL_ID = "id"
val COL_COVER = "cover"
val COL_NAME = "name"
val COL_AUTHOR = "author"
val COL_DESCRIPTION = "description"
val COL_YEAR = "releaseYear"
val COL_STATUS = "status"  // list, tbr or read

class DBHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        // Create the table
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COL_COVER INTEGER, 
                $COL_NAME TEXT, 
                $COL_AUTHOR TEXT, 
                $COL_DESCRIPTION TEXT, 
                $COL_YEAR INTEGER, 
                $COL_STATUS TEXT
            )
        """.trimIndent()
        db?.execSQL(createTable)
        Log.d("AppLogs", "Database created with table $TABLE_NAME")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert book into the table function (used in Main)
    fun insertBook(cover: Int, name: String, author: String, description: String, releaseYear: Int, status: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COL_COVER, cover)
        contentValues.put(COL_NAME, name)
        contentValues.put(COL_AUTHOR, author)
        contentValues.put(COL_DESCRIPTION, description)
        contentValues.put(COL_YEAR, releaseYear)
        contentValues.put(COL_STATUS, status)

        val result = db.insert(TABLE_NAME, null, contentValues)
        if (result == (0).toLong()) {
            Log.d("AppLogs", "Insert Book Failed")
        }
        else {
            Log.d("AppLogs", "Book Inserted Successfully")
        }
    }

    // Delete all books
    fun clearDatabase() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
        Log.d("AppLogs", "Database cleared")
    }

    // Fetch books filtered by status to display for each section
    fun getBooksByStatus(status: String): MutableList<Book> {
        val db = this.readableDatabase
        val list: MutableList<Book> = ArrayList()

        val result = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COL_STATUS = ?", arrayOf(status))

        if (result.moveToFirst()) {
            do {
                val book = Book(
                    result.getInt(result.getColumnIndexOrThrow(COL_COVER)),
                    result.getString(result.getColumnIndexOrThrow(COL_NAME)),
                    result.getString(result.getColumnIndexOrThrow(COL_AUTHOR)),
                    result.getString(result.getColumnIndexOrThrow(COL_DESCRIPTION)),
                    result.getInt(result.getColumnIndexOrThrow(COL_YEAR)),
                    result.getString(result.getColumnIndexOrThrow(COL_STATUS)),
                    result.getInt(result.getColumnIndexOrThrow(COL_ID))
                )
                list.add(book)
                Log.d("AppLogs", "Book fetched for '$status': ${book.name}")
            } while (result.moveToNext())
        }
        result.close()
        return list
    }

    // Function to get book by ID, used in the BookInfo after ID passed from RecyclerViewAdapter
    fun getBookById(id: Int): Book? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COL_ID = ?", arrayOf(id.toString()))

        var book: Book? = null

        if (cursor.moveToFirst()) {
            val cover = cursor.getInt(cursor.getColumnIndexOrThrow(COL_COVER))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION))
            val year = cursor.getInt(cursor.getColumnIndexOrThrow(COL_YEAR))
            val status = cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS))
            val bookId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))

            book = Book(cover, name, author, description, year, status, bookId)
            Log.d("AppLogs", "Book ID $id: ${book.name}")
        }

        cursor.close()
        return book
    }

    // Delete a book from DB using ID, used in BookInfo
    fun deleteBookById(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        Log.d("AppLogs", "Book deleted by ID: $id")
    }

    // Update book status (list/tbr/read), used in BookInfo
    fun updateBookStatus(id: Int, newStatus: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_STATUS, newStatus)

        db.update(TABLE_NAME, contentValues, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        Log.d("AppLogs", "Book status updated to $newStatus")
    }
}
