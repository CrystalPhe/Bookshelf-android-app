package com.example.mybookshelf

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val bookList: List<Book>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>() {

    // ViewHolder for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        return ViewHolderClass(itemView)
    }

    //Bind data to ViewHolder
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val book = bookList[position]
        holder.textViewTitle.text = book.name
        holder.textViewAuthor.text = book.author
        holder.imageView.setImageResource(book.cover)

        // Set click listener to open BookInfoActivity
        holder.itemView.setOnClickListener {
            Log.d("AppLogs", "Book clicked: ${book.name}, ID: ${book.id}") // Log book click event
            val context = it.context
            val intent = Intent(context, BookInfoActivity::class.java) // Show book info
            intent.putExtra("BOOK_ID", book.id)
            context.startActivity(intent)
        }
    }

    // Total number of items
    override fun getItemCount(): Int {
        return bookList.size
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewTitle: TextView = itemView.findViewById(R.id.textView)
        val textViewAuthor: TextView = itemView.findViewById(R.id.textView2)
    }
}
