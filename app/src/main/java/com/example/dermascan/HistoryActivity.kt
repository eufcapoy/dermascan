package com.example.dermascan


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

data class ListItem(val title: String, val subtitle: String)

class HistoryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val bckBtn = findViewById<ImageView>(R.id.backBtn)

        bckBtn.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        val listView = findViewById<ListView>(R.id.listview)

        val items = listOf(
            ListItem("Scan 1", "April 15, 2025"),
            ListItem("Scan 2", "April 16, 2025"),
            ListItem("Scan 3", "April 17, 2025"),
            ListItem("Scan 4", "April 18, 2025"),
            ListItem("Scan 5", "April 19, 2025"),
            ListItem("Scan 6", "April 20, 2025"),
        )

        val adapter = CustomAdapter(this, items)
        listView.adapter = adapter
    }
}


class CustomAdapter(context: Context, private val items: List<ListItem>) :
    ArrayAdapter<ListItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item, parent, false)

        val titleView = view.findViewById<TextView>(R.id.item_title)
        val subtitleView = view.findViewById<TextView>(R.id.item_subtitle)

        val item = getItem(position)
        titleView.text = item?.title
        subtitleView.text = item?.subtitle

        return view
    }
}