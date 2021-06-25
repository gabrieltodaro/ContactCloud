package tech.gapps.contactcloud.model

import android.util.Log
import android.widget.Toast
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import java.io.Serializable
import kotlin.random.Random


data class Contact(
        val id: Long = Random.nextLong(),
        var fullName: String,
        var nickname: String,
        var phoneNumber: Long,
        var email: String
) : Serializable {
        fun createObject() {
                val entity = ParseObject("Contact")

                entity.put("objectId", this.id)
                entity.put("FullName", this.fullName)
                entity.put("NickName", this.nickname)
                entity.put("PhoneNumber", this.fullName)
                entity.put("Email", this.email)

                entity.saveInBackground { e: ParseException? ->
                        if (e == null) {
                                Log.d("CONTACT", "Success to generate contact")
                        } else {
                                Log.e("CONTACT", "Error to generate contact")
                        }
                }
        }

        fun readObject() {
                val query = ParseQuery.getQuery<ParseObject>("Contact")
                // The query will search for a ParseObject, given its objectId.
                // When the query finishes running, it will invoke the GetCallback
                // with either the object, or the exception thrown
                query.getInBackground(this.id.toString())
                query.getInBackground(this.id.toString()) { parseObject: ParseObject, e: ParseException ->
                        if (e == null) {
                                val contact = parseObject as Contact
                                Log.d("CONTACT", "Success to generate contact")
                        } else {
                                Log.e("CONTACT", "Error to generate contact")
                        }
                }
        }
}