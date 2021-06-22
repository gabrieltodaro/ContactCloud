package tech.gapps.contactcloud.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tech.gapps.contactcloud.R
import tech.gapps.contactcloud.helper.ViewAnimation
import tech.gapps.contactcloud.model.Contact
import kotlin.random.Random

class ContactDetailActivity: AppCompatActivity() {

    private lateinit var moreOptionsFloatingButton: FloatingActionButton
    private lateinit var saveFloatingButton: FloatingActionButton
    private lateinit var callFloatingButton: FloatingActionButton
    private lateinit var deleteFloatingButton: FloatingActionButton
    private lateinit var contactDetailImageView: ImageView
    private lateinit var fullNameEditText: EditText
    private lateinit var nickNameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText

    private var isRotate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        moreOptionsFloatingButton = findViewById(R.id.moreContactDetailFloatingButton)
        saveFloatingButton = findViewById(R.id.saveOrEditContactDetailFloatingButton)
        callFloatingButton = findViewById(R.id.callContactDetailFloatingButton)
        deleteFloatingButton = findViewById(R.id.deleteContactDetailFloatingButton)

        contactDetailImageView = findViewById(R.id.contactDetailImageView)
        fullNameEditText = findViewById(R.id.fullNameEditText)
        nickNameEditText = findViewById(R.id.nickNameEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        emailEditText = findViewById(R.id.emailEditText)

        moreOptionsFloatingButton.setOnClickListener(View.OnClickListener { view ->
            isRotate = ViewAnimation.rotateFab(view, !isRotate)
            if(isRotate){
                ViewAnimation.showIn(saveFloatingButton)
                ViewAnimation.showIn(callFloatingButton)
                ViewAnimation.showIn(deleteFloatingButton)
            }else{
                ViewAnimation.showOut(saveFloatingButton)
                ViewAnimation.showOut(callFloatingButton)
                ViewAnimation.showOut(deleteFloatingButton)
            }
        })

        saveFloatingButton.setOnClickListener{
            saveContact()
        }

        callFloatingButton.setOnClickListener{
            Toast.makeText(this@ContactDetailActivity, "Available soon.", Toast.LENGTH_LONG).show()
        }

        deleteFloatingButton.setOnClickListener{
            Toast.makeText(this@ContactDetailActivity, "Available soon.", Toast.LENGTH_LONG).show()
        }

    }

    fun newIntent(context: Context, contact: Contact?): Intent {
        val detailIntent = Intent(context, ContactDetailActivity::class.java)

        return detailIntent
    }

    private fun saveContact() {
        val fullName = fullNameEditText.text.toString()
        val nickName = nickNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val phone = phoneEditText.text.toString().toLong()

        val contact = Contact(Random.nextLong(), fullName, nickName, phone, email, "")
        contactList.add(contact)
        Toast.makeText(this@ContactDetailActivity, "Contact added successfully!", Toast.LENGTH_SHORT).show()
        Log.i("Adicionando contato", "Contato ${contact.nickname} adicionado.")

        dismissActivity()
    }

    private fun dismissActivity() {
        finish()
    }
}