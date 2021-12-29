package be.condictum.move_up.adapter

import android.app.DatePickerDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.data.local.model.Goals
import be.condictum.move_up.databinding.GoalsListRowItemBinding
import be.condictum.move_up.view.ui.goalscreen.GoalScreenFragment
import be.condictum.move_up.view.ui.goalscreen.GoalScreenFragmentDirections
import be.condictum.move_up.viewmodel.GoalsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

class GoalScreenAdapter(
    private var data: List<Goals>,
    private val viewModel: GoalsViewModel,
) : RecyclerView.Adapter<GoalScreenAdapter.GoalsViewHolder>() {

    private var dateFormatter: SimpleDateFormat =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    class GoalsViewHolder(val binding: GoalsListRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalsViewHolder {
        val binding = GoalsListRowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GoalsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalsViewHolder, position: Int) {
        val currentData = data[position]
        val binding = holder.binding
        val context = holder.itemView.context

        binding.rowItemGoalsNameText.text = currentData.dataName
        binding.rowItemGoalsDateText.text = dateFormatter.format(currentData.dataDate)

        binding.goalsListCardView.setOnClickListener {
            val sharedPreferences = context.getSharedPreferences(context.packageName, MODE_PRIVATE)
            sharedPreferences
                .edit()
                .putInt(GoalScreenFragment.SHARED_PREFERENCES_KEY_GOAL_ID, currentData.id)
                .apply()

            val action = GoalScreenFragmentDirections.actionGoalScreenFragmentToGoalResultFragment()
            holder.itemView.findNavController().navigate(action)
        }

        holder.binding.rowItemGoalsMenu.setOnClickListener {
            val popupMenu = PopupMenu(context, it)

            popupMenu.menuInflater.inflate(R.menu.goal_screen_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.ac_main_edit_goal -> {
                        updateGoal(holder.itemView, position, context)
                        true
                    }
                    R.id.ac_main_delete_goal -> {
                        showDeleteItemDialog(currentData, context)
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


    private fun showDeleteItemDialog(currentData: Goals, context: Context) {
        val alertDialog = MaterialAlertDialogBuilder(context)

        alertDialog.setCancelable(false)

        alertDialog.setTitle(context.getString(R.string.are_you_sure_text))
        alertDialog.setMessage(context.getString(R.string.goal_is_deleting_text))
        alertDialog.setPositiveButton(context.getString(R.string.yes_button_text)) { _, _ ->
            viewModel.deleteGoal(currentData)

            Toast.makeText(
                context,
                context.getString(R.string.goal_deleted_text),
                Toast.LENGTH_SHORT
            ).show()
        }

        alertDialog.setNegativeButton(context.getString(R.string.no_button_text)) { _, _ ->
            Toast.makeText(
                context,
                context.getString(R.string.goal_isnt_deleted_text),
                Toast.LENGTH_SHORT
            ).show()
        }

        alertDialog.show()
    }

    private fun updateGoal(view: View, position: Int, context: Context) {
        val data = data[position]
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.goal_input_form, null)
        val nameText: EditText = mDialogView.findViewById(R.id.goal_input_name_edit_text)
        val dateText: EditText = mDialogView.findViewById(R.id.goal_input_date_edit_text)

        nameText.setText(data.dataName)
        dateText.setText(dateFormatter.format(data.dataDate))

        dateText.setOnClickListener {
            val calendar = Calendar.getInstance()

            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    dateText.setText(
                        String.format(
                            "%d/%d/%d", selectedDayOfMonth, (selectedMonth + 1), selectedYear
                        )
                    )
                },
                year,
                month,
                dayOfMonth
            )

            datePickerDialog.show()
        }

        val alertDialog =
            MaterialAlertDialogBuilder(context).setView(mDialogView)
                .setTitle(context.getString(R.string.add_goal_text))
                .setPositiveButton(context.getString(R.string.save_button_text)) { _, _ ->
                    val name = nameText.text.toString()
                    val date = dateText.text.toString()
                    val profileId = data.profilesId

                    if (viewModel.isEntryValid(name, date)) {
                        viewModel.updateGoal(
                            Goals(data.id, name, Date(dateFormatter.parse(date)!!.time), profileId)
                        )
                    } else {
                        showSnackbarForInputError(view, position, context)
                    }
                }.setNegativeButton(context.getString(R.string.exit_button_text)) { _, _ -> }

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun showSnackbarForInputError(view: View, position: Int, context: Context) {
        Snackbar.make(
            view,
            context.getString(R.string.input_error_text),
            Snackbar.LENGTH_LONG
        ).setAction(context.getString(R.string.try_again_button_text)) {
            updateGoal(
                view,
                position,
                context
            )
        }.show()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setDataset(data: List<Goals>) {
        this.data = data
    }
}