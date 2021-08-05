package be.condictum.move_up.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.fragment.GoalScreenFragment
import be.condictum.move_up.fragment.MainFragment
import be.condictum.move_up.fragment.MainFragment.Companion.SHARED_PREFERENCES_KEY_PROFILE_ID
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat

class GoalScreenAdapter(private val mContext: Context, private var data: List<Goals>) :
    RecyclerView.Adapter<GoalScreenAdapter.GoalsViewHolder>() {
    var dateFormatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

    class GoalsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val goalNameTextView = view.findViewById<TextView>(R.id.row_item_goals_name_text)
        val goalDateTextView = view.findViewById<TextView>(R.id.row_item_goals_date_text)
        val editButtonView=view.findViewById<Button>(R.id.row_item_goals_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.goals_list_row_item, parent, false)
        return GoalsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalsViewHolder, position: Int) {
        holder.goalNameTextView.text = data[position].dataName
        holder.goalDateTextView.text = dateFormatter.format(data[position].dataDate)
        holder.editButtonView.setOnClickListener {
            val sharedPreferences =
                mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)
            sharedPreferences.edit().remove(GoalScreenFragment.SHARED_PREFERENCES_KEY_PROFILE_ID).apply()

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
    }
private fun showDeleteItemDialog(currentData: Goals) {
    val alertDialog = MaterialAlertDialogBuilder(mContext)
    alertDialog.setCancelable(false)

    alertDialog.setTitle(mContext.getString(R.string.are_you_sure_text))
    alertDialog.setMessage(mContext.getString(R.string.profile_is_deleting_text))
    alertDialog.setPositiveButton(mContext.getString(R.string.yes_buttontext)) { , _ ->
        viewModel.deleteProfile(currentData)

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
    alertDialog.setNegativeButton(mContext.getString(R.string.no_buttontext)) { , _ ->
        Toast.makeText(
            mContext,
            mContext.getString(R.string.profile_isnt_deleted_text),
            Toast.LENGTH_SHORT
        ).show()
    }
    alertDialog.show()
}

    override fun getItemCount(): Int {
        return data.size
    }

    fun setDataset(data: List<Goals>) {
        this.data = data
    }
}