package tech.gapps.contactcloud.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tech.gapps.contactcloud.R
import tech.gapps.contactcloud.helper.ContactAdapter
import tech.gapps.contactcloud.model.Contact

var contactList: ArrayList<Contact> = arrayListOf()

class MainActivity : AppCompatActivity() {

    private lateinit var contactListView: ListView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var addButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactListView = findViewById(R.id.contact_list_view)
        addButton = findViewById(R.id.add_floating_button)
        addButton.setOnClickListener(View.OnClickListener {
            val intent = ContactDetailActivity().newIntent(this)
            startActivity(intent)
        })

        contactList = startContactList()
        startListView()
    }

    override fun onStart() {
        super.onStart()
        contactAdapter.notifyDataSetChanged()
    }

    private fun startContactList(): ArrayList<Contact> {
        // This function will retrieve the contact list from database and/or backend.
        val contacts = arrayListOf(
            Contact(
                1,
                "Gabriel Todaro",
                "Gabz",
                123123123,
                "gabriel.todaro@outlook.com",
                "http://i.imgur.com/DvpvklR.png"
            )
        )

        return contacts
    }

    private fun startListView() {
        contactAdapter = ContactAdapter(this, contactList)
        contactListView.adapter = contactAdapter
    }
}