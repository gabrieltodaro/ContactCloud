package tech.gapps.contactcloud.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso
import tech.gapps.contactcloud.R
import tech.gapps.contactcloud.model.Contact
import tech.gapps.contactcloud.view.MainActivity
import java.net.URL

class ContactAdapter(private val context: Context,
                     private val dataSource: ArrayList<Contact>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_item_contact, parent, false)

        val contactImageView = rowView.findViewById(R.id.contactImageView) as ImageView
        val nicknameTextView = rowView.findViewById(R.id.nicknameTextView) as TextView
        val callButton = rowView.findViewById(R.id.contactCallButton) as Button

        val contact = getItem(position) as Contact
        nicknameTextView.text = contact.nickname
//        Picasso.get().load(contact.imageUrl).placeholder(R.drawable.ic_baseline_person_24).into(contactImageView)

        callButton.setOnClickListener{
            val ctx = context as MainActivity
            ctx.makePhoneCall(contact)
        }
        return rowView
    }

}