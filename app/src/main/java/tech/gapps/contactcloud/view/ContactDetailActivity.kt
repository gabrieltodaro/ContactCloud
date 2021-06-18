package tech.gapps.contactcloud.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tech.gapps.contactcloud.R
import tech.gapps.contactcloud.model.Contact

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var contactDetailImageView: ImageView
    private lateinit var fullNameEditText: EditText
    private lateinit var nickNameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText

    private var isEditing = false
//    private var contact = Contact?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        contactDetailImageView = findViewById(R.id.contactDetailImageView)
        fullNameEditText = findViewById(R.id.fullNameEditText)
        nickNameEditText = findViewById(R.id.nickNameEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        emailEditText = findViewById(R.id.emailEditText)

        fullNameEditText.isEnabled = isEditing
        nickNameEditText.isEnabled = isEditing
        phoneEditText.isEnabled = isEditing
        emailEditText.isEnabled = isEditing
    }

    fun newIntent(context: Context, contact: Contact?): Intent {
        val detailIntent = Intent(context, ContactDetailActivity::class.java)

        if (contact == null) {
            isEditing = true
        } else {
            Toast.makeText(this, "Contact is not null", Toast.LENGTH_LONG).show()
//            this.contact = contact
        }

        return detailIntent
    }
}