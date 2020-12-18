package com.epam.mentoring.mviarchitecture.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.epam.mentoring.mviarchitecture.R
import com.epam.mentoring.mviarchitecture.data.model.Task

class MainAdapter(
    private val tasks: ArrayList<Task>,
    private val clickListener: ListItemClickListener
):
    RecyclerView.Adapter<MainAdapter.ViewHolder>()  {

    class ViewHolder(
        itemView: View,
        val name: TextView = itemView.findViewById(R.id.task_item_name),
        val description: TextView = itemView.findViewById(R.id.task_item_description)
    ) : RecyclerView.ViewHolder(itemView)



    interface ListItemClickListener {
        fun onListItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
holder.name.text = task.name
holder.description.text = task.description
        holder.itemView.setOnClickListener { clickListener.onListItemClick(position) }
    }

    override fun getItemCount(): Int = tasks.size

}