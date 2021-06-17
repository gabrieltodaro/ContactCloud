package tech.gapps.contactcloud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tech.gapps.contactcloud.helper.ContactAdapter
import tech.gapps.contactcloud.model.Contact

class MainActivity : AppCompatActivity() {

    private lateinit var contactListView: ListView
    private lateinit var addButton: FloatingActionButton

    private lateinit var contactList: ArrayList<Contact>

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

    private fun startContactList(): ArrayList<Contact> {
        val contacts = arrayListOf(
            Contact(
                1,
                "Gabriel Todaro",
                "Gabz",
                123123123,
                "gabriel.todaro@outlook.com",
                "http://i.imgur.com/DvpvklR.png"
            ),
            Contact(
                2,
                "Gabriel Patane Todaro",
                "Gabz1",
                123123123,
                "gabriel.todaro@outlook.com",
                "http://i.imgur.com/DvpvklR.png"
            )
        )

        return contacts
    }

    private fun startListView() {
        val adapter = ContactAdapter(this, contactList)
        contactListView.adapter = adapter
    }
}