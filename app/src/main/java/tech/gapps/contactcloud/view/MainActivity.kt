package tech.gapps.contactcloud.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parse.Parse
import com.parse.ParseObject
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

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build())

        val firstObject = ParseObject("FirstClass")
        firstObject.put("message","Hey ! First message from android. Parse is now connected")
        firstObject.saveInBackground {
            if (it != null){
                it.localizedMessage?.let { message -> Log.e("MainActivity", message) }
            } else{
                Log.d("MainActivity","Object saved.")
            }
        }

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
            Toast.makeText(this@MainActivity, R.string.call_error_empty_phone, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, R.string.call_permission_granted, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.call_permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }

}