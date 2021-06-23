package tech.gapps.contactcloud.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tech.gapps.contactcloud.R
import tech.gapps.contactcloud.db.DatabaseHelper
import tech.gapps.contactcloud.helper.ContactAdapter
import tech.gapps.contactcloud.model.Contact

var contactList: ArrayList<Contact> = arrayListOf()

class MainActivity : AppCompatActivity() {

    private lateinit var contactListView: ListView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var addButton: FloatingActionButton

    private var databaseHandler = DatabaseHelper(this)

    private val requestCall = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactListView = findViewById(R.id.contact_list_view)
        addButton = findViewById(R.id.add_floating_button)

        val context = this
        addButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ContactDetailActivity::class.java)
            startActivity(intent)
        })

        startContactList()
        startListView()
    }

    override fun onStart() {
        super.onStart()
        startContactList()

        contactAdapter.notifyDataSetChanged()
        contactListView.invalidateViews()
        contactListView.refreshDrawableState()
    }

    private fun startContactList() {
        // This function will retrieve the contact list from database and/or backend.

        contactList.clear()
        contactList.addAll(databaseHandler.listAllContacts())

    }

    private fun startListView() {
        contactAdapter = ContactAdapter(this, contactList)
        contactListView.adapter = contactAdapter

        val context = this
        contactListView.setOnItemClickListener { _, _, position, _ ->
            val selectedContact = contactList[position]

            val intent = Intent(context, ContactDetailActivity::class.java)
            intent.putExtra("contact", selectedContact)

            startActivity(intent)
        }
    }

    fun makePhoneCall(contact: Contact) {
        val number: String = contact.phoneNumber.toString()
        if (number.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        requestCall
                )
            } else {
                val dial = "tel:$number"
                val intent = Intent(Intent.ACTION_CALL, Uri.parse(dial))
                startActivity(intent)
            }
        } else {
            Toast.makeText(this@MainActivity, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String?>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                makePhoneCall()
                Toast.makeText(this, "Permission ALLOWED, CALL AGAIN", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

}