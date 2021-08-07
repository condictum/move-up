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
import be.condictum.move_up.adapter.ProfileMainRecyclerAdapter
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.databinding.FragmentMainBinding
import be.condictum.move_up.viewmodel.ProfilesViewModel
import be.condictum.move_up.viewmodel.ProfilesViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class MainFragment : Fragment() {
    companion object {
        const val SHARED_PREFERENCES_KEY_PROFILE_ID = "profileId"
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerProfileAdapter: ProfileMainRecyclerAdapter

    private val viewModel: ProfilesViewModel by activityViewModels {
        ProfilesViewModelFactory(
            (activity?.application as DatabaseApplication).database.profilesDao(),
            (activity?.application as DatabaseApplication).database.goalsDao(),
            (activity?.application as DatabaseApplication).database.lessonsDao(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        recyclerProfileAdapter = ProfileMainRecyclerAdapter(
            this,
            listOf(),
            viewModel,
        )

        binding.mainFragmentRecyclerView.adapter = recyclerProfileAdapter
        setDataset()

        binding.mainFragmentFab.setOnClickListener {
            addNewProfile()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    private fun isEntryValid(name: String, surname: String, age: String): Boolean {
        return viewModel.isEntryValid(name, surname, age)
    }

    private fun addNewProfile() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle(getString(R.string.create_profile_text))

        val view = activity?.layoutInflater?.inflate(
            R.layout.custom_create_profile_alert_dialog,
            null
        )

        val nameText =
            view?.findViewById<TextInputEditText>(R.id.profile_fragment_name_edit_text)
        val surnameText =
            view?.findViewById<TextInputEditText>(R.id.profile_fragment_surname_edit_text)
        val ageText =
            view?.findViewById<TextInputEditText>(R.id.profile_fragment_age_edit_text)

        builder.setView(view)

        builder.setPositiveButton(
            getString(R.string.save_text)
        ) { _, _ ->
            val name = nameText?.text.toString()
            val surname = surnameText?.text.toString()
            val age = ageText?.text.toString()

            if (isEntryValid(name, surname, age)) {
                viewModel.addNewProfile(
                    name, surname, age.toInt()
                )

                setDataset()
            } else {
                showSnackbarForInputError()
            }
        }

        builder.setNegativeButton(
            getString(R.string.cancel_text)
        ) { dialog, _ -> dialog.cancel() }

        builder.setCancelable(false)
        builder.show()
    }

    private fun setDataset() {
        viewModel.allProfiles.observe(viewLifecycleOwner, {
            recyclerProfileAdapter.setDataset(it)
            recyclerProfileAdapter.notifyDataSetChanged()
            controlViewItemsVisibility()
        })
    }

    private fun showSnackbarForInputError() {
        Snackbar.make(
            requireContext(),
            requireView(),
            getString(R.string.input_error_text),
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.try_again_button_text)) { addNewProfile() }
            .show()
    }

    private fun controlViewItemsVisibility() {
        viewModel.allProfiles.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                binding.mainFragmentInformationText.visibility = View.VISIBLE
                binding.mainFragmentRecyclerView.visibility = View.INVISIBLE
            } else {
                binding.mainFragmentInformationText.visibility = View.GONE
                binding.mainFragmentRecyclerView.visibility = View.VISIBLE
            }
        })
    }
}