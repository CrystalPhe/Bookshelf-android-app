package com.example.mybookshelf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        insertBooks()
    }

    fun insertBooks() {
        val db = DBHelper(this)

        // Reset database by deleting all books
        db.clearDatabase()

        val books = listOf(
            Book(R.drawable.book1, "Harry Potter", "J.K. Rowling",
                "A young wizard discovers his destiny at Hogwarts School of Witchcraft and Wizardry.", 1997, "read"),
            Book(R.drawable.book2, "Snow White", "Brothers Grimm",
                "A princess living with seven dwarfs after her stepmother's jealousy turns dangerous.", 1812, "read"),
            Book(R.drawable.book3, "Shatter Me", "Tahereh Mafi",
                "A girl with a deadly touch is locked away but finds love and purpose in a dystopian world.", 2011, "tbr"),
            Book(R.drawable.book4, "White Nights", "Fyodor Dostoevsky",
                "A dreamer meets a mysterious woman over four nights in St. Petersburg.", 1848, "tbr"),
            Book(R.drawable.book5, "Nobody's Boy", "Hector Malot",
                "A young orphan travels across France searching for his real family.", 1878, "list"),
            Book(R.drawable.book6, "Cinderella", "Charles Perrault",
                "A kind-hearted girl, mistreated by her stepfamily, finds magic and love.", 1697, "list"),
            Book(R.drawable.book7, "The Cruel Prince", "Holly Black",
                "A mortal girl fights for power in the world of Faeries, full of deception and danger.", 2018, "list"),
            Book(R.drawable.book8, "Powerless", "Lauren Roberts",
                "A girl hides her identity in a world where powers define status, fighting to change her fate.", 2023, "list"),
            Book(R.drawable.book9, "Me Before You", "Jojo Moyes",
                "A woman becomes a caregiver to a disabled man, with an unexpected emotional journey.", 2012, "list"),
            Book(R.drawable.book10, "White Fang", "Jack London",
                "A wild wolf-dog endures struggles and changes as he learns to live and survive.", 1906, "list"),
            Book(R.drawable.book11, "Sherlock Holmes", "Arthur Conan Doyle",
                "A brilliant detective solves difficult cases using his keen observation and deductive skills.", 1887, "list"),
            Book(R.drawable.book12, "Romeo and Juliet", "William Shakespeare",
                "A tragic love story between two young lovers whose families are enemies.", 1597, "list"),
            Book(R.drawable.book13, "The Hobbit", "J.R.R. Tolkien",
                "A reluctant hobbit embarks on a dangerous journey to help dwarves reclaim their homeland.", 1937, "list"),
            Book(R.drawable.book14, "The Hunger Games", "Suzanne Collins",
                "A girl is forced to compete in a brutal televised fight for survival.", 2008, "list")
        )

        for (book in books) {
            db.insertBook(book.cover, book.name, book.author, book.description, book.releaseYear, book.status)
        }
        Log.d("App", "Database books inserted")
    }

}