package com.example.bluetoothcheckins.data

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothcheckins.AddDeviceListener
import com.example.bluetoothcheckins.R
import com.example.bluetoothcheckins.model.Attandent

class ScanerAdapter(private val list: ArrayList<Attandent>,
                    private val context: Context,
                    private val onDataChangeListener: AddDeviceListener
) : RecyclerView.Adapter<ScanerAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun bindItem(attendent: Attandent){
            val name : TextView = itemView.findViewById(R.id.name) as TextView
            val bluetoothID = itemView.findViewById(R.id.bluetoothID) as TextView
            val addButton = itemView.findViewById<Button>(R.id.addAttendant)
            name.text = attendent.name
            bluetoothID.text = attendent.bluetoothID
            addButton.setOnClickListener {
                addButton.setBackgroundColor(Color.parseColor("#FFFFE0"))
                addButton.setText("Added")
                addButton.setTextColor(Color.parseColor("#000000"))

                onDataChangeListener.addDeviceToDB(name.text.toString(), bluetoothID.text.toString())
            }


        }



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.add_attendant_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

}