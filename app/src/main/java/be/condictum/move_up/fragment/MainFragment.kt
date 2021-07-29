package be.condictum.move_up.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import be.condictum.move_up.database.AppData
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.databinding.FragmentMainBinding
import be.condictum.move_up.viewmodel.AppDatabaseViewModel
import be.condictum.move_up.viewmodel.AppDatabaseViewModelFactory

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    lateinit var data: AppData

    private val viewModel: AppDatabaseViewModel by activityViewModels {
        AppDatabaseViewModelFactory(
            (activity?.application as DatabaseApplication).database.appDao()
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

        binding.button.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToGoalScreenFragment()
            view.findNavController().navigate(action)
        }
    }

/*
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            /*
                binding.dataName.text.toString(),
                binding.dataPrice.text.toString(),
                binding.dataCount.text.toString(),
             */
        )
    }

    private fun addNewData() {
        if (isEntryValid()) {
            viewModel.addNewData(
                /*
                    binding.dataName.text.toString(),
                    binding.dataPrice.text.toString(),
                    binding.dataCount.text.toString(),
                 */
            )
            val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // hide keyboard
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
 */
}