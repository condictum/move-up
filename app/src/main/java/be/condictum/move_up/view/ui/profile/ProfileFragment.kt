package be.condictum.move_up.view.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import be.condictum.move_up.R
import be.condictum.move_up.data.local.model.Profiles
import be.condictum.move_up.databinding.FragmentProfileBinding
import be.condictum.move_up.view.ui.main.MainFragment
import be.condictum.move_up.viewmodel.ProfilesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfilesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileFragmentSaveButton.visibility = View.INVISIBLE

        binding.textInputLayoutName.setStartIconOnClickListener {
            binding.profileFragmentSaveButton.isEnabled = true
            binding.profileFragmentSaveButton.visibility = View.VISIBLE

            binding.profileFragmentSaveButton.text = getString(R.string.save_button_text)
            binding.profileFragmentNameEditText.isEnabled = true
            showKeyboard()
            binding.profileFragmentNameEditText.requestFocus()
        }

        binding.textInputLayoutSurname.setStartIconOnClickListener {
            binding.profileFragmentSaveButton.isEnabled = true
            binding.profileFragmentSaveButton.visibility = View.VISIBLE

            binding.profileFragmentSaveButton.text = getString(R.string.save_button_text)
            binding.profileFragmentSurnameEditText.isEnabled = true
            showKeyboard()
            binding.profileFragmentSurnameEditText.requestFocus()
        }

        binding.textInputLayoutAge.setStartIconOnClickListener {
            binding.profileFragmentSaveButton.isEnabled = true
            binding.profileFragmentSaveButton.visibility = View.VISIBLE

            binding.profileFragmentSaveButton.text = getString(R.string.save_button_text)
            binding.profileFragmentAgeEditText.isEnabled = true
            showKeyboard()
            binding.profileFragmentAgeEditText.requestFocus()
        }

        binding.profileFragmentSaveButton.setOnClickListener {
            binding.profileFragmentSaveButton.visibility = View.INVISIBLE
            binding.profileFragmentSaveButton.text = getString(R.string.edit_button_text)

            binding.profileFragmentNameEditText.isEnabled = false
            binding.profileFragmentNameEditText.clearFocus()

            binding.profileFragmentSurnameEditText.isEnabled = false
            binding.profileFragmentSurnameEditText.clearFocus()

            binding.profileFragmentAgeEditText.isEnabled = false
            binding.profileFragmentNameEditText.clearFocus()

            hideKeyboard()

            updateProfile()
        }

        setDataset()

        binding.fragmentProfileDeleteImage.setOnClickListener {
            showDeleteItemDialog()
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

    private fun updateProfile() {
        val profileId = getProfileIdFromSharedPreferences()
        val name = binding.profileFragmentNameEditText.text.toString()
        val surname = binding.profileFragmentSurnameEditText.text.toString()
        val age = binding.profileFragmentAgeEditText.text.toString()

        if (isEntryValid(name, surname, age)) {
            viewModel.updateProfile(
                Profiles(profileId, name, surname, age.toInt())
            )
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.input_error_text),
                Toast.LENGTH_SHORT
            ).show()

            setDataset()
        }
    }

    private fun isEntryValid(name: String, surname: String, age: String): Boolean {
        return viewModel.isEntryValid(name, surname, age)
    }

    private fun setDataset() {
        val profileFromDb = viewModel.getProfileById(getProfileIdFromSharedPreferences())

        profileFromDb.observe(viewLifecycleOwner, Observer {
            binding.profileFragmentNameEditText.setText(it.name)
            binding.profileFragmentSurnameEditText.setText(it.surname)
            binding.profileFragmentAgeEditText.setText(it.age.toString())
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

    private fun showDeleteItemDialog() {
        viewModel.getProfileById(getProfileIdFromSharedPreferences())
            .observe(viewLifecycleOwner, Observer {
                val mContext = requireContext()
                val alertDialog = MaterialAlertDialogBuilder(mContext)
                alertDialog.setCancelable(false)

                alertDialog.setTitle(mContext.getString(R.string.are_you_sure_text))
                alertDialog.setMessage(mContext.getString(R.string.profile_is_deleting_text))
                alertDialog.setPositiveButton(mContext.getString(R.string.yes_button_text)) { _, _ ->
                    viewModel.deleteProfile(
                        it
                    )

                    val sharedPreferences =
                        mContext.getSharedPreferences(
                            mContext.packageName,
                            Context.MODE_PRIVATE
                        )

                    sharedPreferences.edit()
                        .remove(MainFragment.SHARED_PREFERENCES_KEY_PROFILE_ID).apply()

                    val action =
                        ProfileFragmentDirections.actionProfileFragmentToMainFragment()
                    Navigation.findNavController(requireView()).navigate(action)

                    Toast.makeText(
                        mContext,
                        mContext.getString(R.string.profile_deleted_text),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                alertDialog.setNegativeButton(mContext.getString(R.string.no_button_text)) { _, _ ->
                    Toast.makeText(
                        mContext,
                        mContext.getString(R.string.profile_isnt_deleted_text),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                alertDialog.show()
            })
    }
}