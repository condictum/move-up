package be.condictum.move_up.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.condictum.move_up.R
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.database.data.Goals
import be.condictum.move_up.database.data.Settings
import be.condictum.move_up.databinding.FragmentGoalScreenBinding
import be.condictum.move_up.databinding.FragmentSettingsBinding
import be.condictum.move_up.viewmodel.ProfilesViewModel
import be.condictum.move_up.viewmodel.ProfilesViewModelFactory
import be.condictum.move_up.viewmodel.SettingsViewModel
import java.sql.Date


class SettingsFragment : Fragment() {



    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by activityViewModels {
        SettingsViewModel.SettingsViewModelFactory(
            (activity?.application as DatabaseApplication).database.settingsDao()
        )
    }
private lateinit var data :List<Settings>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.kronometreSesiSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

        }
        binding.kronometreTitresimSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

        }
    }

    companion object {

    }
}