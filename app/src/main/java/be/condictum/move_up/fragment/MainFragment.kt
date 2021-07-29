package be.condictum.move_up.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import be.condictum.move_up.adapter.GoalRecyclerAdapter
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.database.data.Lessons
import be.condictum.move_up.databinding.FragmentMainBinding
import be.condictum.move_up.viewmodel.AppDatabaseViewModel
import be.condictum.move_up.viewmodel.AppDatabaseViewModelFactory
import java.sql.Date


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    lateinit var goals: Goals
    lateinit var lessons: Lessons

    private val viewModel: AppDatabaseViewModel by activityViewModels {
        AppDatabaseViewModelFactory(
            (activity?.application as DatabaseApplication).database.goalsDao(),
            (activity?.application as DatabaseApplication).database.lessonsDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainFragmentRecyclerView.adapter = GoalRecyclerAdapter(
            view.context, arrayListOf(
                Goals(1, "YKS", Date(System.currentTimeMillis())),
                Goals(2, "KPSS", Date(System.currentTimeMillis())),
                Goals(3, "TUS", Date(System.currentTimeMillis())),
            )
        )

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.mainFragmentRecyclerView.layoutManager = linearLayoutManager

    }

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