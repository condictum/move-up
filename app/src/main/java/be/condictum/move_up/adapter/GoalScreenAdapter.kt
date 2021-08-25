package be.condictum.move_up.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import be.condictum.move_up.viewmodel.GoalsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class GoalScreenAdapter(
    private val mContext: Context,
    private val requiredView: View,
    private var data: List<Goals>,
    private val viewModel: GoalsViewModel,
) :
    RecyclerView.Adapter<GoalScreenAdapter.GoalsViewHolder>() {
    var dateFormatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

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
            val popupMenu = PopupMenu(mContext, it)
            popupMenu.menuInflater.inflate(R.menu.goal_screen_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.ac_main_edit_goal -> {
                        updateGoal(position)
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
        alertDialog.setMessage(mContext.getString(R.string.goal_is_deleting_text))
        alertDialog.setPositiveButton(mContext.getString(R.string.yes_button_text)) { _, _ ->
            viewModel.deleteGoal(currentData)

            Toast.makeText(
                mContext,
                mContext.getString(R.string.goal_deleted_text),
                Toast.LENGTH_SHORT
            ).show()
        }
        alertDialog.setNegativeButton(mContext.getString(R.string.no_button_text)) { _, _ ->
            Toast.makeText(
                mContext,
                mContext.getString(R.string.goal_isnt_deleted_text),
                Toast.LENGTH_SHORT
            ).show()
        }
        alertDialog.show()
    }

    private fun updateGoal(position: Int) {
        val data = data[position]
        val mDialogView =
            LayoutInflater.from(this.mContext).inflate(R.layout.goal_input_form, null)
        val nameText = mDialogView.findViewById<EditText>(R.id.goal_input_name_edit_text)
        val dateText = mDialogView.findViewById<EditText>(R.id.goal_input_date_edit_text)

        nameText.setText(data.dataName)
        dateText.setText(dateFormatter.format(data.dataDate))

        dateText.setOnClickListener {
            val calendar = Calendar.getInstance()

            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(
                mContext,
                { view, year, month, dayOfMonth ->
                    dateText.setText("$dayOfMonth/${month + 1}/$year")
                },
                year,
                month,
                dayOfMonth
            )

            datePickerDialog.show()
        }

        val mBuilder =
            AlertDialog.Builder(this.mContext).setView(mDialogView)
                .setTitle(mContext.getString(R.string.add_goal_text))
                .setPositiveButton(mContext.getString(R.string.save_button_text)) { dialogInterface, i ->

                    val name = nameText.text.toString()
                    val date = dateText.text.toString()
                    val profileId = data.profilesId

                    if (viewModel.isEntryValid(name, date)) {
                        viewModel.updateGoal(
                            Goals(data.id, name, Date(dateFormatter.parse(date).time), profileId)
                        )
                    } else {
                        showSnackbarForInputError(position)
                    }
                }.setNegativeButton(mContext.getString(R.string.exit_button_text)) { _, _ -> }

        mBuilder.setCancelable(false)
        mBuilder.show()
    }

    private fun showSnackbarForInputError(position: Int) {
        Snackbar.make(
            requiredView,
            mContext.getString(R.string.input_error_text),
            Snackbar.LENGTH_LONG
        ).setAction(mContext.getString(R.string.try_again_button_text)) { updateGoal(position) }
            .show()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setDataset(data: List<Goals>) {
        this.data = data
    }
}