package be.condictum.move_up.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Goals
import java.text.SimpleDateFormat
import java.util.*

class GoalRecyclerAdapter(private val context: Context, private val dataSet: ArrayList<Goals>) :
    RecyclerView.Adapter<GoalRecyclerAdapter.GoalViewHolder>() {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    class GoalViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        var goalNameText: TextView = view.findViewById(R.id.row_item_goal_name_text)
        var goalDateText: TextView = view.findViewById(R.id.row_item_goal_date_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.goals_list_row_item, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.goalNameText.text = dataSet[position].dataName
        holder.goalDateText.text = dateFormat.format(dataSet[position].dataDate)
    }

    override fun getItemCount(): Int = dataSet.size
}