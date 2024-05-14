package com.example.bluetoothcheckins.data

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothcheckins.R
import com.example.bluetoothcheckins.model.Attandent


class PeopleListAdapter(private val list: ArrayList<Attandent>,
                        private val context: Context
) :
    RecyclerView.Adapter<PeopleListAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        //
        fun bindItem(attendent: Attandent){
            val name : TextView = itemView.findViewById(R.id.name) as TextView
            val bluetoothID = itemView.findViewById(R.id.bluetoothID) as TextView
            val daysAttended = itemView.findViewById(R.id.daysAttanded) as TextView
            name.text = attendent.name
            bluetoothID.text = attendent.bluetoothID
            daysAttended.text = attendent.daysAttended.toString()
            if (attendent.checked == true){
                val backgroundLayout: ConstraintLayout = itemView.findViewById(R.id.background)
                backgroundLayout.setBackgroundColor(Color.parseColor("#77f048"))
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.attendant_card_layout, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindItem(list[position])
    }

    override fun getItemCount(): Int
    {
        return list.size
    }




}