package be.condictum.move_up.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class GoalScreenFragment : Fragment() {
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

        binding.button.setOnClickListener {
            adapter = GoalScreenAdapter(requireContext(), listOf())

            binding.goalScreenRecyclerView.adapter = adapter
            setDataset()

            val profileId = getProfileIdFromSharedPreferences()

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.goal_input_form, null)

            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("form").show()

            mBuilder.btn_dialog_save.setOnClickListener {

                viewModel.addNewGoal(
                    "Test Goal Name",
                    Date(dateFormatter.parse("20/01/2020").time),
                    profileId
                )

            }
            mBuilder.dialogCancelBtn.setOnClickListener {
                mBuilder.dismiss()

            }

            binding.goalsScreenTextView.text = "$profileId"
        }


    }

    private fun setDataset() {
        viewModel.allGoals.observe(viewLifecycleOwner, {
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

/*
        binding.mainFragmentRecyclerView.adapter = GoalRecyclerAdapter(
            view.context,
            arrayListOf(
                Goals(1, "YKS", Date(System.currentTimeMillis())),
                Goals(2, "KPSS", Date(System.currentTimeMillis())),
                Goals(3, "TUS", Date(System.currentTimeMillis())),
            ),
        )
 */

    /*
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
                binding.dataName.text.toString(),
                binding.dataPrice.text.toString(),
                binding.dataCount.text.toString(),

        )
    }

    private fun addNewGoal() {
        if (isEntryValid()) {
            viewModel.addNewGoal(
                    binding.dataName.text.toString(),
                    binding.dataPrice.text.toString(),
                    binding.dataCount.text.toString(),
            )
            val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
 */
}