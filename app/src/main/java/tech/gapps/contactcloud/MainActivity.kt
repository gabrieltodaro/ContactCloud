package tech.gapps.contactcloud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tech.gapps.contactcloud.model.Contact

class MainActivity : AppCompatActivity() {

    private lateinit var contactListView: ListView
    private lateinit var addButton: FloatingActionButton

    private lateinit var contactList: List<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactListView = findViewById(R.id.contact_list_view)
        addButton = findViewById(R.id.add_floating_button)
        addButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "This should navigate to the Create Contact view.", Toast.LENGTH_LONG).show()
        })

        contactList = startContactList()
        startListView()
    }

    private fun startContactList(): List<Contact> {
        val contacts = listOf(
            Contact(
                1,
                "Gabriel Todaro",
                "Gabz",
                123123123,
                "gabriel.todaro@outlook.com"
            ),
            Contact(
                2,
                "Gabriel Patane Todaro",
                "Gabz1",
                123123123,
                "gabriel.todaro@outlook.com"
            )
        )

        return contacts
    }

    private fun startListView() {
        val listItems = arrayOfNulls<String>(contactList.size)
        for (i in 0 until contactList.size) {
            val contact = contactList[i]
            listItems[i] = contact.nickname
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        contactListView.adapter = adapter
    }
}