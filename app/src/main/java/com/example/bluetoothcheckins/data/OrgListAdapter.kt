package com.example.bluetoothcheckins.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothcheckins.people.ListOfPeopleInOrg
import com.example.bluetoothcheckins.OnDataChangeListener
import com.example.bluetoothcheckins.R
import com.example.bluetoothcheckins.data.OrgListAdapter.*
import com.example.bluetoothcheckins.model.Organizasion
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class OrgListAdapter(private val list: ArrayList<Organizasion>,
                     private val context: Context,
                     private val onDataChangeListener: OnDataChangeListener

) :
        RecyclerView.Adapter<ViewHolder>()
{

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var db = Firebase.firestore
        var userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        fun bindItem(org: Organizasion){
            val organizasion : TextView = itemView.findViewById(R.id.name) as TextView
            val delete: Button = itemView.findViewById(R.id.deleteOrg) as Button

            //val lengthInDays : TextView = itemView.findViewById(R.id.)
            organizasion.text = org.organizasion
            delete.setOnClickListener {
                deleteOrg(org.organizasion.toString())
            }
            itemView.setOnClickListener{
                goToListOfPeople(org.organizasion.toString())

            }
        }
        fun goToListOfPeople(name:String){
            var intent = Intent(itemView.context, ListOfPeopleInOrg::class.java)
            intent.putExtra("orgName", name)
            itemView.context.startActivity(intent)
        }
        fun deleteOrg(org: String){
            db.collection(userId).whereEqualTo("meetingName", org)
                .get()
                .addOnSuccessListener { documents ->
                    for (doc in documents){
                        doc.reference.delete()
                    }

                    onDataChangeListener.onDataChanged(org)
                }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.org_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindItem(list[position])
    }


}