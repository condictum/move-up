package be.condictum.move_up.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.fragment.MainFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

class GoalRecyclerAdapter(private val context: Context, private val dataSet: ArrayList<Goals>) :
    RecyclerView.Adapter<GoalRecyclerAdapter.GoalViewHolder>() {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    class GoalViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val goalNameText: TextView = view.findViewById(R.id.row_item_goal_name_text)
        val goalDateText: TextView = view.findViewById(R.id.row_item_goal_date_text)
        val goalsListCardView: CardView = view.findViewById(R.id.goals_list_card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.goals_list_row_item, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.goalNameText.text = dataSet[position].dataName
        holder.goalDateText.text = dateFormat.format(dataSet[position].dataDate)
        holder.goalsListCardView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToGoalScreenFragment()
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = dataSet.size
}