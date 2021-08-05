package be.condictum.move_up.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.fragment.GoalScreenFragment
import be.condictum.move_up.fragment.GoalScreenFragmentDirections
import be.condictum.move_up.fragment.MainFragment
import be.condictum.move_up.fragment.MainFragmentDirections
import be.condictum.move_up.viewmodel.GoalsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat

class GoalScreenAdapter(private val mContext: Context, private var data: List<Goals>, private val viewModel: GoalsViewModel,) :
    RecyclerView.Adapter<GoalScreenAdapter.GoalsViewHolder>() {
    var dateFormatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

    class GoalsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val goalNameTextView = view.findViewById<TextView>(R.id.row_item_goals_name_text)
        val goalDateTextView = view.findViewById<TextView>(R.id.row_item_goals_date_text)
        val profileVerticalMenu: ImageButton =
            view.findViewById(R.id.row_item_goals_menu)
        val goalscard: CardView = view.findViewById(R.id.goals_list_card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.goals_list_row_item, parent, false)
        return GoalsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalsViewHolder, position: Int) {
        val currentData = data[position]
        holder.goalNameTextView.text = data[position].dataName
        holder.goalDateTextView.text = dateFormatter.format(data[position].dataDate)

        holder.goalscard.setOnClickListener {
            val id = data[position].id

            val sharedPreferences =
                mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(GoalScreenFragment.SHARED_PREFERENCES_KEY_GOAL_ID, id)
                .apply()

            val action = GoalScreenFragmentDirections.actionGoalScreenFragmentToGoalResultFragment()
            holder.itemView.findNavController().navigate(action)
        }

        holder.profileVerticalMenu.setOnClickListener {
            val sharedPreferences =
                mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)
            sharedPreferences.edit().remove(MainFragment.SHARED_PREFERENCES_KEY_PROFILE_ID).apply()

            val popupMenu = PopupMenu(mContext, it)
            popupMenu.menuInflater.inflate(R.menu.goal_screen_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.ac_main_edit_goal -> {
                        updateProfile(position)
                        true
                    }
                    R.id.ac_main_delete_goal -> {
                        showDeleteItemDialog(currentData)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popupMenu.show()
        }
    }


    private fun showDeleteItemDialog(currentData: Goals) {
        val alertDialog = MaterialAlertDialogBuilder(mContext)
        alertDialog.setCancelable(false)

        alertDialog.setTitle(mContext.getString(R.string.are_you_sure_text))
        alertDialog.setMessage(mContext.getString(R.string.profile_is_deleting_text))
        alertDialog.setPositiveButton(mContext.getString(R.string.yes_button_text)) { _, _ ->
            viewModel.deleteProfileById(currentData.id)

            val sharedPreferences =
                mContext.getSharedPreferences(
                    mContext.packageName,
                    Context.MODE_PRIVATE
                )
            sharedPreferences.edit()
                .remove(MainFragment.SHARED_PREFERENCES_KEY_PROFILE_ID).apply()

            Toast.makeText(
                mContext,
                mContext.getString(R.string.profile_deleted_text),
                Toast.LENGTH_SHORT
            ).show()
        }
        alertDialog.setNegativeButton(mContext.getString(R.string.no_button_text)) { _, _ ->
            Toast.makeText(
                mContext,
                mContext.getString(R.string.profile_isnt_deleted_text),
                Toast.LENGTH_SHORT
            ).show()
        }
        alertDialog.show()
    }
    private fun updateProfile(position: Int) {

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setDataset(data: List<Goals>) {
        this.data = data
    }
}