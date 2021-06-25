package tech.gapps.contactcloud.view

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tech.gapps.contactcloud.R
import tech.gapps.contactcloud.db.DatabaseHelper
import tech.gapps.contactcloud.helper.ViewAnimation
import tech.gapps.contactcloud.model.Contact

class ContactDetailActivity: AppCompatActivity() {

    private lateinit var moreOptionsFloatingButton: FloatingActionButton
    private lateinit var saveFloatingButton: FloatingActionButton
    private lateinit var callFloatingButton: FloatingActionButton
    private lateinit var deleteFloatingButton: FloatingActionButton
    private lateinit var contactDetailImageView: ImageView
    private lateinit var changeImageButton: Button
    private lateinit var fullNameEditText: EditText
    private lateinit var nickNameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText

    private var isRotate = false
    private var isEditing = false
    private var contact: Contact? = null
    private val requestCall = 1
    private val pickImage = 100
    private var imageUri: Uri? = null

    private val databaseHandler = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        moreOptionsFloatingButton = findViewById(R.id.moreContactDetailFloatingButton)
        saveFloatingButton = findViewById(R.id.saveOrEditContactDetailFloatingButton)
        callFloatingButton = findViewById(R.id.callContactDetailFloatingButton)
        deleteFloatingButton = findViewById(R.id.deleteContactDetailFloatingButton)

        contactDetailImageView = findViewById(R.id.contactDetailImageView)
        changeImageButton = findViewById(R.id.changeImageButton)
        fullNameEditText = findViewById(R.id.fullNameEditText)
        nickNameEditText = findViewById(R.id.nickNameEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        emailEditText = findViewById(R.id.emailEditText)

        val bundle = intent.extras
        bundle?.let {
            contact = it.get("contact") as Contact
        }

        changeImageButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        setActionOnButtons()
        checkIfNewContact()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            contactDetailImageView.setImageURI(imageUri)
        }
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

        contact?.let {
            it.fullName = fullName
            it.nickname = nickName
            it.email = email
            it.phoneNumber = phone

            databaseHandler.updateContact(it)

            val updated = this.getText(R.string.contact_updated) as String
            showToast(updated)
            isEditing = false
            dismissActivity()
            return
        }

        databaseHandler.addContact(Contact(fullName = fullName, nickname = nickName, email = email, phoneNumber = phone))
        val added = this.getText(R.string.contact_added) as String
        showToast(added)

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

    private fun showRemoveContactDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.contact_remove_alert_title)
        builder.setMessage(R.string.contact_remove_alert_message)

        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            removeContact()
        }

        builder.setNegativeButton(android.R.string.no) { _, _ ->
            val cancel = this.getText(R.string.contact_removed_canceled) as String
            showToast(cancel)
        }

        builder.show()
    }

    private fun removeContact() {
        contact?.let {
            databaseHandler.deleteContact(it.id)
        }

        val success = this.getText(R.string.contact_removed_success) as String
        showToast(success)
        dismissActivity()
    }

    /** View **/
    private fun setActionOnButtons() {
        if (contact != null && !isEditing) {
            // We should show the more options button
            moreOptionsFloatingButton.setOnClickListener(View.OnClickListener { view ->
                isRotate = ViewAnimation.rotateFab(view, !isRotate)
                if (isRotate) {
                    ViewAnimation.showIn(saveFloatingButton)
                    ViewAnimation.showIn(callFloatingButton)
                    ViewAnimation.showIn(deleteFloatingButton)
                } else {
                    dismissButtons()
                }
            })

            saveFloatingButton.setImageResource(R.drawable.ic_baseline_edit_24)
            saveFloatingButton.setOnClickListener{
                editContact()
                dismissButtons()
            }

            callFloatingButton.setOnClickListener{
                contact?.let {
                    makePhoneCall(it)
                }
                dismissButtons()
            }

            deleteFloatingButton.setOnClickListener{
                showRemoveContactDialog()
                dismissButtons()
            }
        } else {
            // We should show only the save button.
            changeImageButton.visibility = View.VISIBLE
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
        isRotate = ViewAnimation.rotateFab(moreOptionsFloatingButton, !isRotate)
        ViewAnimation.showOut(saveFloatingButton)
        ViewAnimation.showOut(callFloatingButton)
        ViewAnimation.showOut(deleteFloatingButton)
    }

    private fun dismissActivity() {
        finish()
    }

    /** Call **/
    private fun makePhoneCall(contact: Contact) {
        val number: String = contact.phoneNumber.toString()
        if (number.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                            this@ContactDetailActivity,
                            Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                        this@ContactDetailActivity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        requestCall
                )
            } else {
                val dial = "tel:$number"
                val intent = Intent(Intent.ACTION_CALL, Uri.parse(dial))
                startActivity(intent)
            }
        } else {
            Toast.makeText(this@ContactDetailActivity, R.string.call_error_empty_phone, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, R.string.call_permission_granted, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.call_permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }
}