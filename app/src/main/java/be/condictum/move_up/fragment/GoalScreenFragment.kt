package be.condictum.move_up.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.databinding.FragmentGoalScreenBinding
import be.condictum.move_up.viewmodel.GoalsViewModel
import be.condictum.move_up.viewmodel.GoalsViewModelFactory

class GoalScreenFragment : Fragment() {
    private var _binding: FragmentGoalScreenBinding? = null
    private val binding get() = _binding!!

    lateinit var goals: Goals

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

/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainFragmentRecyclerView.adapter = GoalRecyclerAdapter(
            view.context,
            arrayListOf(
                Goals(1, "YKS", Date(System.currentTimeMillis())),
                Goals(2, "KPSS", Date(System.currentTimeMillis())),
                Goals(3, "TUS", Date(System.currentTimeMillis())),
            ),
        )
    }*/

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