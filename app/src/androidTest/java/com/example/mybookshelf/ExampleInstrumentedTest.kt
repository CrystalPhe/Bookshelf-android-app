package com.example.mybookshelf

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivity_Display() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
}

// Since List, Tbr and Read are quite similar i will only make test for list
@RunWith(AndroidJUnit4::class)
class ListActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ListActivity::class.java)

    @Test
    fun IfEmpty() {
        val context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        val db = DBHelper(context)
        db.clearDatabase()

        launch(ListActivity::class.java)
        onView(withId(R.id.emptyTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun DisplayData() {
        //  insertBooks from MainActivity
        launch(MainActivity::class.java)
        launch(ListActivity::class.java)

        // Check if a book from the 'list' status is shown
        onView(withText("Cinderella")).check(matches(isDisplayed()))
    }

}

@RunWith(AndroidJUnit4::class)
class BookInfoActivityTest {

    private lateinit var db: DBHelper
    private var bookId: Int = -1

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = DBHelper(context)
        db.clearDatabase()

        // Insert books directly here (same as in MainActivity)
        val books = listOf(
            Book(R.drawable.book1, "Harry Potter", "J.K. Rowling", "A young wizard discovers his destiny at Hogwarts School of Witchcraft and Wizardry.", 1997, "read"),
            Book(R.drawable.book2, "Snow White", "Brothers Grimm", "A princess living with seven dwarfs after her stepmother's jealousy turns dangerous.", 1812, "read"),
            Book(R.drawable.book3, "Shatter Me", "Tahereh Mafi", "A girl with a deadly touch is locked away but finds love and purpose in a dystopian world.", 2011, "tbr"),
            Book(R.drawable.book4, "White Nights", "Fyodor Dostoevsky", "A dreamer meets a mysterious woman over four nights in St. Petersburg.", 1848, "tbr"),
            Book(R.drawable.book5, "Nobody's Boy", "Hector Malot", "A young orphan travels across France searching for his real family.", 1878, "list"),
            Book(R.drawable.book6, "Cinderella", "Charles Perrault", "A kind-hearted girl, mistreated by her stepfamily, finds magic and love.", 1697, "list")
        )

        // Insert books into the database
        for (book in books) {
            db.insertBook(book.cover, book.name, book.author, book.description, book.releaseYear, book.status)
        }

        // Fetch a book from the database to get its ID (using the 'list' status)
        val booksFromDb = db.getBooksByStatus("list")
        if (booksFromDb.isNotEmpty()) {
            bookId = booksFromDb[0].id // Use the first book's ID
        }
    }

    private fun launchActivity(): ActivityScenario<BookInfoActivity> {
        val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, BookInfoActivity::class.java)
        intent.putExtra("BOOK_ID", bookId)  // Pass the inserted book ID to the intent
        return ActivityScenario.launch(intent)
    }

    @Test
    fun bookDetails() {
        // Launch BookInfoActivity
        launchActivity()

        // Check that the book details are displayed correctly
        onView(withId(R.id.bookTitle)).check(matches(withText("Nobody's Boy"))) // Example book title
        onView(withId(R.id.bookAuthor)).check(matches(withSubstring("Hector Malot")))
        onView(withId(R.id.bookDescription)).check(matches(withSubstring("A young orphan")))
        onView(withId(R.id.bookRelease)).check(matches(withSubstring("1878")))
    }

    @Test
    fun tbrButton() {
        launchActivity()
        onView(withId(R.id.btnAddTBR)).perform(click())
        // Check if the TBR button text changes to "Remove from TBR"
        onView(withId(R.id.btnAddTBR)).check(matches(withText("Remove from TBR")))

        onView(withId(R.id.btnAddTBR)).perform(click())
        // Check if the TBR button text changes back to "Add to TBR"
        onView(withId(R.id.btnAddTBR)).check(matches(withText("Add to TBR")))
    }

    @Test
    fun readButton() {
        launchActivity()
        onView(withId(R.id.btnAddRead)).perform(click())
        // Check if the Read button text changes to "Remove from Read"
        onView(withId(R.id.btnAddRead)).check(matches(withText("Remove from Read")))

        onView(withId(R.id.btnAddRead)).perform(click())
        // Check if the Read button text changes back to "Add to Read"
        onView(withId(R.id.btnAddRead)).check(matches(withText("Add to Read")))
    }

    @Test
    fun deleteButton_RemovesBookAndNavigatesBack() {
        launchActivity()
        onView(withId(R.id.btnDelete)).perform(click())
        // Check if the recycler view is displayed (indicating that the book was removed)
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }
}
