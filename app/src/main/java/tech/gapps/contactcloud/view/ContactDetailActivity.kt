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
    private var isEditing = false
    private var contact: Contact? = null

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

        val bundle = intent.extras
        bundle?.let {
            contact = it.get("contact") as Contact
        }

        setActionOnButtons()
        checkIfNewContact()
    }

    private fun checkIfNewContact() {
        contact?.let {
            fullNameEditText.setText(it.fullName)
            nickNameEditText.setText(it.nickname)
            emailEditText.setText(it.email)

            val phoneNumber = "${it.phoneNumber}"
            phoneEditText.setText(phoneNumber)

            fullNameEditText.isEnabled = false
            nickNameEditText.isEnabled = false
            emailEditText.isEnabled = false
            phoneEditText.isEnabled = false
        }
    }

    /** CRUD in memory **/
    private fun saveContact() {
        val fullName = fullNameEditText.text.toString()
        val nickName = nickNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val phone = phoneEditText.text.toString().toLong()

        var contactHelper: Contact? = null
        contact?.let {
            it.fullName = fullName
            it.nickname = nickName
            it.email = email
            it.phoneNumber = phone

            for (i in contactList.indices) {
                val contact = contactList[i]
                if (it.id == contact.id) {
                    contactList[i] = it
                }
            }

            showToast("Contact updated successfully!")
            isEditing = false
            dismissActivity()
            return
        }

        if (contactHelper == null) {
            contactHelper = Contact(fullName = fullName, nickname = nickName, email = email, phoneNumber = phone, imageUrl = "")
        }

        contactList.add(contactHelper!!)
        showToast("Contact added successfully!")

        isEditing = false
        dismissActivity()
    }

    private fun editContact() {
        isEditing = true

        fullNameEditText.isEnabled = true
        nickNameEditText.isEnabled = true
        emailEditText.isEnabled = true
        phoneEditText.isEnabled = true

        reloadMoreOptionsButton()
    }

    private fun removeContact() {
        contact?.let {
            for (i in contactList.indices) {
                val contact = contactList[i]
                if (it.id == contact.id) {
                    contactList.removeAt(i)
                }
            }
        }

        showToast("Contact removed successfully!")
        dismissActivity()
    }

    /** View **/
    private fun setActionOnButtons() {
        if (contact != null && !isEditing) {
            // We should show the more options button
            moreOptionsFloatingButton.setOnClickListener(View.OnClickListener { view ->
                isRotate = ViewAnimation.rotateFab(view, !isRotate)
                if(isRotate){
                    ViewAnimation.showIn(saveFloatingButton)
                    ViewAnimation.showIn(callFloatingButton)
                    ViewAnimation.showIn(deleteFloatingButton)
                }else{
                    dismissButtons()
                }
            })

            saveFloatingButton.setImageResource(R.drawable.ic_baseline_edit_24)
            saveFloatingButton.setOnClickListener{
                editContact()
                dismissButtons()
            }

            callFloatingButton.setOnClickListener{
                showToast("Available soon.")
                dismissButtons()
            }

            deleteFloatingButton.setOnClickListener{
                removeContact()
                dismissButtons()
            }
        } else {
            // We should show only the save button.
            moreOptionsFloatingButton.setImageResource(R.drawable.ic_baseline_save_24)
            moreOptionsFloatingButton.setOnClickListener{
                saveContact()
                dismissButtons()
            }
        }
    }

    private fun reloadMoreOptionsButton() {
        setActionOnButtons()
    }

    /** Helpers **/
    private fun showToast(text: String) {
        Toast.makeText(this@ContactDetailActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun dismissButtons() {
        ViewAnimation.showOut(saveFloatingButton)
        ViewAnimation.showOut(callFloatingButton)
        ViewAnimation.showOut(deleteFloatingButton)
    }

    private fun dismissActivity() {
        finish()
    }
}