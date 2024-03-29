package be.condictum.move_up.view.ui.goalscreen

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import be.condictum.move_up.R
import be.condictum.move_up.adapter.GoalScreenAdapter
import be.condictum.move_up.data.local.model.Goals
import be.condictum.move_up.databinding.FragmentGoalScreenBinding
import be.condictum.move_up.view.ui.main.MainFragment
import be.condictum.move_up.viewmodel.GoalsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class GoalScreenFragment : Fragment() {
    companion object {
        const val SHARED_PREFERENCES_KEY_GOAL_ID = "goalId"
    }

    private val viewModel: GoalsViewModel by viewModels()

    private var _binding: FragmentGoalScreenBinding? = null
    private val binding get() = _binding!!

    lateinit var goals: Goals

    private lateinit var adapter: GoalScreenAdapter
    private var dateFormatter: SimpleDateFormat =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goalScreenRecyclerView.visibility = View.GONE
        binding.goalScreenNoGoalText.visibility = View.VISIBLE

        adapter = GoalScreenAdapter(listOf(), viewModel)
        binding.goalScreenRecyclerView.adapter = adapter

        binding.goalScreenFab.setOnClickListener {
            addNewGoal()
        }

        setDataset()
    }

    private fun addNewGoal() {
        val mDialogView =
            LayoutInflater.from(this.context).inflate(R.layout.goal_input_form, null)
        val nameText = mDialogView.findViewById<EditText>(R.id.goal_input_name_edit_text)
        val dateText = mDialogView.findViewById<EditText>(R.id.goal_input_date_edit_text)

        dateText.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()

            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    dateText.setText(
                        String.format(
                            "%d/%d/%d", selectedDayOfMonth, (selectedMonth + 1), selectedYear
                        )
                    )
                },
                year,
                month,
                dayOfMonth,
            ).show()
        }

        val mBuilder =
            AlertDialog.Builder(this.context).setView(mDialogView)
                .setTitle(getString(R.string.add_goal_text))
                .setPositiveButton(getString(R.string.save_button_text)) { _, _ ->

                    val name = nameText.text.toString()
                    val date = dateText.text.toString()

                    if (viewModel.isEntryValid(name, date)) {
                        viewModel.addNewGoal(
                            name,
                            Date(dateFormatter.parse(date)!!.time),
                            getProfileIdFromSharedPreferences()
                        )

                        setDataset()
                    } else {
                        showSnackbarForInputError()
                    }
                }.setNegativeButton(getString(R.string.exit_button_text)) { _, _ ->
                }

        mBuilder.show()
    }

    private fun showSnackbarForInputError() {
        Snackbar.make(
            requireContext(),
            requireView(),
            getString(R.string.input_error_text),
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.try_again_button_text)) { addNewGoal() }
            .show()
    }

    private fun setDataset() {
        viewModel.getAllLiveDataByProfileId(getProfileIdFromSharedPreferences())
            .observe(viewLifecycleOwner, {
                if (it.isNullOrEmpty()) {
                    binding.goalScreenRecyclerView.visibility = View.GONE
                    binding.goalScreenNoGoalText.visibility = View.VISIBLE
                } else {
                    binding.goalScreenRecyclerView.visibility = View.VISIBLE
                    binding.goalScreenNoGoalText.visibility = View.GONE
                }

                adapter.setDataset(it)
                adapter.notifyDataSetChanged()
            })
    }

    private fun getProfileIdFromSharedPreferences(): Int {
        val mContext = requireContext()
        val sharedPreferences =
            mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)
        val profileId = sharedPreferences.getInt(MainFragment.SHARED_PREFERENCES_KEY_PROFILE_ID, 0)

        if (profileId == 0) {
            Log.e("Error", "Profile Id Must Not Be Zero!")
        }

        return profileId
    }
}