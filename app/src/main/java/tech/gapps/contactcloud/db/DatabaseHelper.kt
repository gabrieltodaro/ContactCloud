package tech.gapps.contactcloud.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import tech.gapps.contactcloud.model.Contact

class DatabaseHelper(ctx: Context) : SQLiteOpenHelper(ctx, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE =
                "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $FULL_NAME TEXT, $NICKNAME TEXT, $EMAIL TEXT, $PHONE NUMBER);"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addContact(contact: Contact): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(FULL_NAME, contact.fullName)
        values.put(NICKNAME, contact.nickname)
        values.put(EMAIL, contact.email)
        values.put(PHONE, contact.phoneNumber)
        val _success = db.insert(TABLE_NAME, null, values)
        return (("$_success").toInt() != -1)
    }

    fun getContact(_id: Int): Contact {
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor?.moveToFirst()

        val id = cursor.getLong(cursor.getColumnIndex(ID))
        val fullName = cursor.getString(cursor.getColumnIndex(FULL_NAME))
        val nickname = cursor.getString(cursor.getColumnIndex(NICKNAME))
        val phone = cursor.getLong(cursor.getColumnIndex(PHONE))
        val email = cursor.getString(cursor.getColumnIndex(EMAIL))

        val contact = Contact(id = id, fullName = fullName, nickname = nickname, phoneNumber = phone, email = email)

        cursor.close()
        return contact
    }

    fun listAllContacts(): ArrayList<Contact> {
        val contactList = ArrayList<Contact>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(ID))
                    val fullName = cursor.getString(cursor.getColumnIndex(FULL_NAME))
                    val nickname = cursor.getString(cursor.getColumnIndex(NICKNAME))
                    val phone = cursor.getLong(cursor.getColumnIndex(PHONE))
                    val email = cursor.getString(cursor.getColumnIndex(EMAIL))

                    val contact = Contact(id = id, fullName = fullName, nickname = nickname, phoneNumber = phone, email = email)
                    contactList.add(contact)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return contactList
    }

    fun updateContact(contact: Contact): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FULL_NAME, contact.fullName)
            put(NICKNAME, contact.nickname)
            put(EMAIL, contact.email)
            put(PHONE, contact.phoneNumber)
        }
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(contact.id.toString())).toLong()
        db.close()
        return ("$_success").toInt() != -1
    }

    fun deleteContact(_id: Long): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString()))
        return ("$_success").toInt() != -1
    }

    fun deleteAllContacts(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return ("$_success").toInt() != -1
    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "DB_CONTACT_CLOUD"
        private val TABLE_NAME = "Contact"
        private val ID = "Id"
        private val FULL_NAME = "FullName"
        private val NICKNAME = "NickName"
        private val EMAIL = "Email"
        private val PHONE = "Phone"
    }
}