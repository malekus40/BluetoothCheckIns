package com.example.bluetoothcheckins.data

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothcheckins.R
import com.example.bluetoothcheckins.data.OrgListAdapter.*
import com.example.bluetoothcheckins.model.Organizasion
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.collection.LLRBNode

class OrgListAdapter(private val list: ArrayList<Organizasion>,
    private val context: Context) :
        RecyclerView.Adapter<ViewHolder>()
{
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
//
        fun bindItem(org: Organizasion){
            val organizasion : TextView = itemView.findViewById(R.id.orgName) as TextView
            //val lengthInDays : TextView = itemView.findViewById(R.id.)
            organizasion.text = org.organizasion
            itemView.setOnClickListener{
                Snackbar.make(itemView,organizasion.text,Snackbar.LENGTH_LONG).setAction("ok"){}.setActionTextColor(
                    Color.RED).show()

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

    fun goToListOfPeople(name:String){

    }
}