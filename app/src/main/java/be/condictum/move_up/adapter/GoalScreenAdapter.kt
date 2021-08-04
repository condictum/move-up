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

class GoalScreenAdapter(private val mContext: Context, private var data: List<Goals>) :
    RecyclerView.Adapter<GoalScreenAdapter.GoalsViewHolder>() {
    var dateFormatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

    class GoalsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val goalNameTextView = view.findViewById<TextView>(R.id.row_item_goals_name_text)
        val goalDateTextView = view.findViewById<TextView>(R.id.row_item_goals_date_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.goals_list_row_item, parent, false)
        return GoalsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalsViewHolder, position: Int) {
        holder.goalNameTextView.text = data[position].dataName
        holder.goalDateTextView.text = dateFormatter.format(data[position].dataDate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setDataset(data: List<Goals>) {
        this.data = data
    }
}