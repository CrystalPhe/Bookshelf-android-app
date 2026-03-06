package com.example.mybookshelf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class BookInfoActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var book: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = DBHelper(this)
        val bookId = intent.getIntExtra("BOOK_ID", 0) // Get book ID
        book = db.getBookById(bookId) ?: return // Get book data

        Log.d("AppLogs", "Displaying details for book: ${book.name}")

        // Set book details
        findViewById<ImageView>(R.id.bookCover).setImageResource(book.cover)
        findViewById<TextView>(R.id.bookTitle).text = book.name
        findViewById<TextView>(R.id.bookAuthor).text = "By ${book.author}"
        findViewById<TextView>(R.id.bookDescription).text = "Book Description:\n ${book.description}"
        findViewById<TextView>(R.id.bookRelease).text = "Released On: ${book.releaseYear}"

        // Handle button clicks
        val btnTBR = findViewById<Button>(R.id.btnAddTBR)
        val btnRead = findViewById<Button>(R.id.btnAddRead)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        updateButtons(btnTBR, btnRead)

        // TBR button actions
        btnTBR.setOnClickListener {
            if (book.status == "tbr") {
                db.updateBookStatus(book.id, "list")
                showSnackbar("Book removed from TBR", null, null)
                Log.d("AppLogs", "Book removed from TBR")
            } else {
                db.updateBookStatus(book.id, "tbr")
                showSnackbar("Book added to TBR!", "Go to TBR") {
                    startActivity(Intent(this, TbrActivity::class.java))
                    Log.d("AppLogs", "Book added to TBR")
                }
            }
            book = db.getBookById(book.id)!! // Refresh book data
            updateButtons(btnTBR, btnRead)
        }

        // Read button actions
        btnRead.setOnClickListener {
            if (book.status == "read") {
                db.updateBookStatus(book.id, "list")
                showSnackbar("Book removed from Read", null, null)
                Log.d("AppLogs", "Book removed from Read")
            } else {
                db.updateBookStatus(book.id, "read")
                showSnackbar("Book added to Read!", "Go to Read") {
                    startActivity(Intent(this, ReadActivity::class.java))
                    Log.d("AppLogs", "Book added to Read")
                }
            }
            book = db.getBookById(book.id)!! // Refresh book data
            updateButtons(btnTBR, btnRead)
        }

        // Delete
        btnDelete.setOnClickListener {
            db.deleteBookById(book.id) // remove book from db
            Toast.makeText(this, "Book deleted successfully", Toast.LENGTH_SHORT).show()
            Log.d("AppLogs", "Book deleted")
            // Navigate back to the list activity
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Update button texts based on the book's status
    private fun updateButtons(btnTBR: Button, btnRead: Button) {
        when (book.status) {
            "tbr" -> {
                btnTBR.text = "Remove from TBR"
                btnRead.text = "Add to Read"
            }
            "read" -> {
                btnTBR.text = "Add to TBR"
                btnRead.text = "Remove from Read"
            }
            else -> {
                btnTBR.text = "Add to TBR"
                btnRead.text = "Add to Read"
            }
        }
    }

    // Show snack bar to navigate once a book status change
    private fun showSnackbar(message: String, actionText: String?, action: (() -> Unit)?) {
        val snackbar = Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG)
        if (actionText != null && action != null) {
            snackbar.setAction(actionText) { action() }
        }
        snackbar.show()
    }
}
