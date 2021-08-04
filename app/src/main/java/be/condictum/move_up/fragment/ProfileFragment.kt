package be.condictum.move_up.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.condictum.move_up.R
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.databinding.FragmentProfileBinding
import be.condictum.move_up.viewmodel.ProfilesViewModel
import be.condictum.move_up.viewmodel.ProfilesViewModelFactory


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfilesViewModel by activityViewModels {
        ProfilesViewModelFactory(
            (activity?.application as DatabaseApplication).database.profilesDao(),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textInputLayoutName.setStartIconOnClickListener {
            binding.profileFragmentSaveButton.text = getString(R.string.save_button_text)
            binding.profileFragmentNameEditText.isEnabled = true
            showKeyboard()
            binding.profileFragmentNameEditText.requestFocus()
        }

        binding.textInputLayoutSurname.setStartIconOnClickListener {
            binding.profileFragmentSaveButton.text = getString(R.string.save_button_text)
            binding.profileFragmentSurnameEditText.isEnabled = true
            showKeyboard()
            binding.profileFragmentSurnameEditText.requestFocus()
        }

        binding.textInputLayoutAge.setStartIconOnClickListener {
            binding.profileFragmentSaveButton.text = getString(R.string.save_button_text)
            binding.profileFragmentAgeEditText.isEnabled = true
            showKeyboard()
            binding.profileFragmentAgeEditText.requestFocus()
        }

        binding.profileFragmentSaveButton.setOnClickListener {
            binding.profileFragmentSaveButton.text = getString(R.string.edit_button_text)

            binding.profileFragmentNameEditText.isEnabled = false
            binding.profileFragmentNameEditText.clearFocus()

            binding.profileFragmentSurnameEditText.isEnabled = false
            binding.profileFragmentSurnameEditText.clearFocus()

            binding.profileFragmentAgeEditText.isEnabled = false
            binding.profileFragmentNameEditText.clearFocus()

            hideKeyboard()
        }
    }

    private fun showKeyboard() {
        val keyboard: InputMethodManager? =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        keyboard?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun hideKeyboard() {
        val keyboard: InputMethodManager? =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        keyboard?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}