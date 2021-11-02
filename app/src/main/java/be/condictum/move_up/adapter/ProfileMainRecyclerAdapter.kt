package be.condictum.move_up.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Profiles
import be.condictum.move_up.databinding.ProfilesListRowItemBinding
import be.condictum.move_up.view.fragment.MainFragment
import be.condictum.move_up.view.fragment.MainFragmentDirections
import be.condictum.move_up.viewmodel.ProfilesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class ProfileMainRecyclerAdapter(
    private val mFragment: Fragment,
    private var dataSet: List<Profiles>,
    private val viewModel: ProfilesViewModel
) : RecyclerView.Adapter<ProfileMainRecyclerAdapter.ProfileMainViewHolder>() {

    private val mContext = mFragment.requireContext()
    private val mActivity = mFragment.requireActivity()
    private val mRequiredView = mFragment.requireView()

    class ProfileMainViewHolder(val binding: ProfilesListRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileMainViewHolder {
        val itemBinding =
            ProfilesListRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileMainViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ProfileMainViewHolder, position: Int) {
        val currentData = dataSet[position]

        var name = currentData.name
        var surname = currentData.surname
        var age = currentData.age.toString()

        name = if (name.length > 16) name.substring(0, 16) + "..." else name
        surname = if (surname.length > 16) surname.substring(0, 16) + "..." else surname
        age = if (age.length > 3) age.substring(0, 3) + "..." else age

        holder.binding.profileListRowItemNameText.text = name
        holder.binding.profileListRowItemSurnameText.text = surname
        holder.binding.profileListRowItemProfileAgeText.text = age

        holder.binding.profileListRowItemCardView.setOnClickListener {
            putProfileIdKeyToSharedPreferences(currentData.id)

            val action = MainFragmentDirections.actionMainFragmentToGoalScreenFragment()
            holder.itemView.findNavController().navigate(action)
        }

        holder.binding.profileListRowItemCardView.setOnLongClickListener {
            showPopupMenu(holder.binding.profileListRowItemCardView, position)
            true
        }

        holder.binding.profileListRowItemVerticalMenu.setOnClickListener {
            showPopupMenu(holder.binding.profileListRowItemVerticalMenu, position)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    private fun putProfileIdKeyToSharedPreferences(id: Int) {
        val sharedPreferences =
            mContext.getSharedPreferences(
                mFragment.requireContext().packageName,
                Context.MODE_PRIVATE
            )
        sharedPreferences.edit().putInt(MainFragment.SHARED_PREFERENCES_KEY_PROFILE_ID, id)
            .apply()
    }

    private fun showDeleteItemDialog(currentData: Profiles) {
        val alertDialog = MaterialAlertDialogBuilder(mContext)
        alertDialog.setCancelable(false)

        alertDialog.setTitle(getStringFromResources(R.string.are_you_sure_text))
        alertDialog.setMessage(getStringFromResources(R.string.profile_is_deleting_text))

        alertDialog.setPositiveButton(getStringFromResources(R.string.yes_button_text)) { _, _ ->
            viewModel.deleteProfile(currentData)

            removeProfileIdKeyFromSharedPreferences()
            showToastMessage(R.string.profile_deleted_text)
        }

        alertDialog.setNegativeButton(getStringFromResources(R.string.no_button_text)) { _, _ ->
            showToastMessage(R.string.profile_isnt_deleted_text)
        }

        alertDialog.show()
    }

    private fun getStringFromResources(@StringRes stringId: Int): String {
        return mContext.getString(stringId)
    }

    private fun showToastMessage(@StringRes message: Int) {
        Toast.makeText(
            mContext,
            getStringFromResources(message),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun removeProfileIdKeyFromSharedPreferences() {
        val sharedPreferences =
            mContext.getSharedPreferences(
                mContext.packageName,
                Context.MODE_PRIVATE
            )
        sharedPreferences.edit()
            .remove(MainFragment.SHARED_PREFERENCES_KEY_PROFILE_ID).apply()
    }

    fun setDataset(data: List<Profiles>) {
        dataSet = data
    }

    private fun showAlertDialogForUpdateProfile(position: Int) {
        val data = dataSet[position]
        val builder = MaterialAlertDialogBuilder(mContext)

        builder.setTitle(getStringFromResources(R.string.create_profile_text))

        val view = mActivity.layoutInflater.inflate(
            R.layout.custom_create_profile_alert_dialog,
            null
        )

        val nameText =
            view?.findViewById<TextInputEditText>(R.id.custom_main_profile_fragment_name_edit_text)
        val surnameText =
            view?.findViewById<TextInputEditText>(R.id.custom_main_profile_surname_edit_text)
        val ageText =
            view?.findViewById<TextInputEditText>(R.id.custom_main_profile_age_edit_text)

        nameText?.setText(data.name)
        surnameText?.setText(data.surname)
        ageText?.setText(data.age.toString())

        builder.setView(view)

        builder.setPositiveButton(
            getStringFromResources(R.string.save_text)
        ) { _, _ ->
            val name = nameText?.text.toString()
            val surname = surnameText?.text.toString()
            val age = ageText?.text.toString()

            if (isEntryValid(name, surname, age)) {
                viewModel.updateProfile(
                    Profiles(dataSet[position].id, name, surname, age.toInt())
                )

            } else {
                showSnackbarForInputError(position)
            }
        }

        builder.setNegativeButton(
            getStringFromResources(R.string.cancel_text)
        ) { dialog, _ -> dialog.cancel() }

        builder.setCancelable(false)
        builder.show()
    }

    private fun isEntryValid(name: String, surname: String, age: String): Boolean {
        return viewModel.isEntryValid(name, surname, age)
    }

    private fun showSnackbarForInputError(position: Int) {
        val snackbar = Snackbar.make(
            mContext,
            mRequiredView,
            getStringFromResources(R.string.input_error_text),
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction(getStringFromResources(R.string.try_again_button_text)) {
            showAlertDialogForUpdateProfile(position)
        }

        snackbar.show()
    }

    private fun showPopupMenu(anchor: View, position: Int) {
        val popupMenu = PopupMenu(mContext, anchor)
        popupMenu.menuInflater.inflate(R.menu.main_profiles_edit_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.ac_main_edit_profile -> {
                    showAlertDialogForUpdateProfile(position)
                    true
                }
                R.id.ac_main_delete_profile -> {
                    showDeleteItemDialog(dataSet[position])
                    true
                }
                else -> {
                    false
                }
            }
        }

        popupMenu.show()
    }
}
