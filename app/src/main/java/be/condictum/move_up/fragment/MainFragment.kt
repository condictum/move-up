package be.condictum.move_up.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.condictum.move_up.R
import be.condictum.move_up.adapter.ProfileMainRecyclerAdapter
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.database.data.Profiles
import be.condictum.move_up.databinding.FragmentMainBinding
import be.condictum.move_up.viewmodel.ProfilesViewModel
import be.condictum.move_up.viewmodel.ProfilesViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var profiles: List<Profiles>? = null

    private lateinit var recyclerProfileAdapter: ProfileMainRecyclerAdapter

    private val viewModel: ProfilesViewModel by activityViewModels {
        ProfilesViewModelFactory(
            (activity?.application as DatabaseApplication).database.profilesDao(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        profiles = viewModel.allProfiles.value
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
            requireContext(),
            listOf(),
        )

        binding.mainFragmentRecyclerView.adapter = recyclerProfileAdapter
        setDataset()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ac_add_item -> {
                addNewProfile()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun isEntryValid(name: String, surname: String, age: String): Boolean {
        return viewModel.isEntryValid(name, surname, age)
    }

    private fun addNewProfile() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Create profile")

        val view = activity?.layoutInflater?.inflate(
            R.layout.custom_create_profile_alert_dialog,
            null
        )

        val nameText =
            view?.findViewById<TextInputEditText>(R.id.main_alert_dialog_profile_name_edit_text)
        val surnameText =
            view?.findViewById<TextInputEditText>(R.id.main_alert_dialog_profile_surname_edit_text)
        val ageText =
            view?.findViewById<TextInputEditText>(R.id.main_alert_dialog_profile_age_edit_text)

        builder.setView(view)

        builder.setPositiveButton(
            "Save"
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
                Snackbar.make(
                    requireContext(),
                    requireView(),
                    "Error! Check the inputs.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.setCancelable(false)
        builder.show()
    }

    private fun setDataset() {
        viewModel.allProfiles.observe(viewLifecycleOwner, {
            recyclerProfileAdapter.dataSet = it
            recyclerProfileAdapter.notifyDataSetChanged()
        })
    }
}