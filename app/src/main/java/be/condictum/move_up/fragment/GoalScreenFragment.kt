package be.condictum.move_up.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.condictum.move_up.R
import be.condictum.move_up.adapter.GoalScreenAdapter
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.databinding.FragmentGoalScreenBinding
import be.condictum.move_up.viewmodel.GoalsViewModel
import be.condictum.move_up.viewmodel.GoalsViewModelFactory
import java.sql.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class GoalScreenFragment : Fragment() {
    companion object {
        const val SHARED_PREFERENCES_KEY_GOAL_ID = "goalId"
    }

    private var _binding: FragmentGoalScreenBinding? = null
    private val binding get() = _binding!!

    lateinit var goals: Goals

    private lateinit var adapter: GoalScreenAdapter
    var dateFormatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")

    private val viewModel: GoalsViewModel by activityViewModels {
        GoalsViewModelFactory(
            (activity?.application as DatabaseApplication).database.goalsDao(),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGoalScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GoalScreenAdapter(requireContext(), listOf(), viewModel)
        binding.goalScreenRecyclerView.adapter = adapter
        setDataset()


        val profileId = getProfileIdFromSharedPreferences()

        binding.goalScreenFab.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(this.context).inflate(R.layout.goal_input_form, null)
            val nameText = mDialogView.findViewById<EditText>(R.id.goal_input_name_edit_text)
            val dateText = mDialogView.findViewById<EditText>(R.id.goal_input_date_edit_text)

            dateText.setOnClickListener {
                val calendar = Calendar.getInstance()

                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { view, year, month, dayOfMonth ->
                        dateText.setText("$dayOfMonth/$month/$year")
                    },
                    year,
                    month,
                    dayOfMonth
                )

                datePickerDialog.show()
            }

            val mBuilder =
                AlertDialog.Builder(this.context).setView(mDialogView).setTitle("Add Goals")
                    .setPositiveButton("Kaydet") { dialogInterface, i ->

                        val name = nameText.text.toString()
                        val date = dateText.text.toString()
                      if(isEntryValid(name, date)) {

                          viewModel.addNewGoal(
                              name,
                              Date(dateFormatter.parse(date).time),
                              profileId
                          )
                      }
                        else{
                          Toast.makeText(
                              requireContext(),
                              getString(R.string.input_error_text),
                              Toast.LENGTH_SHORT
                          ).show()
                            setDataset()
                        }
                    }.setNegativeButton("ÇIK") { _, _ -> }

            mBuilder.show()
        }


    }
    private fun isEntryValid(name: String, date: String): Boolean {
        return viewModel.isEntryValid(name, date)
    }

    private fun setDataset() {
        viewModel.getAllLiveDataByProfileId(getProfileIdFromSharedPreferences())
            .observe(viewLifecycleOwner, {
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