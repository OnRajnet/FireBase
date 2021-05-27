package com.rajnet.ondrej.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_layout.view.*

class DataAdapter(var list:ArrayList<DatabaseModel>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var name=itemView.tv_name
        var email=itemView.tv_email

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_layout,parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text=list[position].name
        holder.email.text=list[position].email
    }

    override fun getItemCount(): Int {
        return list.size
    }

}