package be.condictum.move_up.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.databinding.FragmentGoalResultBinding
import be.condictum.move_up.viewmodel.LessonsViewModel
import be.condictum.move_up.viewmodel.LessonsViewModelFactory

class GoalResultFragment : Fragment() {
    private var _binding: FragmentGoalResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LessonsViewModel by activityViewModels {
        LessonsViewModelFactory(
            (activity?.application as DatabaseApplication).database.lessonsDao(),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalResultBinding.inflate(inflater, container, false)
        return binding.root
    }

}