package be.condictum.move_up.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import be.condictum.move_up.R
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.database.data.Settings
import be.condictum.move_up.databinding.FragmentSettingsBinding
import be.condictum.move_up.viewmodel.SettingsViewModel


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val SHARED_PREFERENCES_KEY_NOTIFICATION_IS_OPEN = "notificationIsOpen"
    }

    private lateinit var data: List<Settings>

    private val viewModel: SettingsViewModel by activityViewModels {
        SettingsViewModel.SettingsViewModelFactory(
            (activity?.application as DatabaseApplication).database.settingsDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            AppCompatActivity.MODE_PRIVATE
        )
        val isSwitchAlreadyChecked = sharedPreferences.getBoolean(
            SHARED_PREFERENCES_KEY_NOTIFICATION_IS_OPEN,
            false
        )

        if (isSwitchAlreadyChecked) {
            binding.bildirimSwitch.isChecked = true
        }

        binding.bildirimSwitch.setOnCheckedChangeListener { _, b ->
            val sharedPreferences = requireActivity().getSharedPreferences(
                requireActivity().packageName,
                Context.MODE_PRIVATE
            )
            if (b) {
                sharedPreferences.edit()
                    .putBoolean(SHARED_PREFERENCES_KEY_NOTIFICATION_IS_OPEN, true).apply()
            } else {
                sharedPreferences.edit()
                    .putBoolean(SHARED_PREFERENCES_KEY_NOTIFICATION_IS_OPEN, false).apply()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.about_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ac_about -> {
                val action =
                    SettingsFragmentDirections.actionSettingsFragmentToAboutFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}