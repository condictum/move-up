package be.condictum.move_up.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.fragment.GoalScreenFragment
import be.condictum.move_up.fragment.MainFragment.Companion.SHARED_PREFERENCES_KEY_PROFILE_ID
import java.text.FieldPosition

class GoalScreenAdapter (val context, val goals:List<Goals>:RecyclerView.Adapter<GoalScreenAdapter.MyViewHolder>) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
      val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.goals_list_row_item, parent, false)
      return MyViewHolder(view)

  }
    override  fun getItemCount():Int{
        return goals.size
    }

    override fun onBindViewHolder(holder:MyViewHolder , position: Int){
        var name = goals[position].dataName
        var date = goals[position].dataDate

        holder.GoalsNameText.text = name
        holder.GoalsDateText.text = date

    }
    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val GoalsNameText: TextView = itemView.findViewById(R.id.row_item_goals_name_text)
        val GoalsDateText: TextView = itemView.findViewById(R.id.row_item_goals_date_text)

        holder.goalsCardView.setOnClickListener {
            val id = goals[position].id

            val sharedPreferences =
                context.getSharedPreferences(context.packageName, context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(GoalScreenFragment.SHARED_PREFERENCES_KEY_PROFILE_ID, id)
                .apply()

            val action = GoalScreenFragmentDirections.actionMainFragmentToGoalScreenFragment()
            holder.itemView.findNavController().navigate(action)
        }




    }
}