package be.condictum.move_up.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Profiles
import be.condictum.move_up.fragment.MainFragment
import be.condictum.move_up.fragment.MainFragmentDirections
import be.condictum.move_up.viewmodel.ProfilesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class ProfileMainRecyclerAdapter(
    private val mContext: Context,
    private var dataSet: List<Profiles>,
    private val viewModel: ProfilesViewModel,
    private val activity: Activity?,
    private val requiredView: View
) :
    RecyclerView.Adapter<ProfileMainRecyclerAdapter.ProfileMainViewHolder>() {

    class ProfileMainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileNameText: TextView = view.findViewById(R.id.row_item_goals_name_text)
        val profileSurnameText: TextView = view.findViewById(R.id.row_item_goals_date_text)
        val profileAgeText: TextView = view.findViewById(R.id.row_item_profile_age_text)
        val profilesCardView: CardView = view.findViewById(R.id.goals_list_card_view)
        val profileVerticalMenu: ImageButton =
            view.findViewById(R.id.row_item_goals_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileMainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profiles_list_row_item, parent, false)
        return ProfileMainViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileMainViewHolder, position: Int) {
        val currentData = dataSet[position]

        var name = currentData.name
        var surname = currentData.surname
        var age = currentData.age.toString()

        if (name.length > 16) {
            name = name.substring(0, 16) + "..."
        }

        if (surname.length > 16) {
            surname = surname.substring(0, 16) + "..."
        }

        if (age.length > 3) {
            age = age.substring(0, 3) + "..."
        }

        holder.profileNameText.text = name
        holder.profileSurnameText.text = surname
        holder.profileAgeText.text = age

        holder.profilesCardView.setOnClickListener {
            val id = currentData.id

            val sharedPreferences =
                mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(MainFragment.SHARED_PREFERENCES_KEY_PROFILE_ID, id)
                .apply()

            val action = MainFragmentDirections.actionMainFragmentToGoalScreenFragment()
            holder.itemView.findNavController().navigate(action)
        }

        holder.profileVerticalMenu.setOnClickListener {
           val popupMenu = PopupMenu(mContext, it)
            popupMenu.menuInflater.inflate(R.menu.main_profiles_edit_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.ac_main_edit_profile -> {
                        updateProfile(position)
                        true
                    }
                    R.id.ac_main_delete_profile -> {
                        showDeleteItemDialog(currentData)
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

    private fun showDeleteItemDialog(currentData: Profiles) {
        val alertDialog = MaterialAlertDialogBuilder(mContext)
        alertDialog.setCancelable(false)

        alertDialog.setTitle(mContext.getString(R.string.are_you_sure_text))
        alertDialog.setMessage(mContext.getString(R.string.profile_is_deleting_text))
        alertDialog.setPositiveButton(mContext.getString(R.string.yes_button_text)) { _, _ ->
            viewModel.deleteProfile(currentData)

            val sharedPreferences =
                mContext.getSharedPreferences(
                    mContext.packageName,
                    Context.MODE_PRIVATE
                )
            sharedPreferences.edit()
                .remove(MainFragment.SHARED_PREFERENCES_KEY_PROFILE_ID).apply()

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
    }

    override fun getItemCount(): Int = dataSet.size

    fun setDataset(data: List<Profiles>) {
        dataSet = data
    }

    private fun updateProfile(position: Int) {
        val data = dataSet[position]

        val builder = MaterialAlertDialogBuilder(mContext)
        builder.setTitle(mContext.getString(R.string.create_profile_text))

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

        nameText?.setText(data.name)
        surnameText?.setText(data.surname)
        ageText?.setText(data.age.toString())

        builder.setView(view)

        builder.setPositiveButton(
            mContext.getString(R.string.save_text)
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
            mContext.getString(R.string.cancel_text)
        ) { dialog, _ -> dialog.cancel() }

        builder.setCancelable(false)
        builder.show()
    }

    private fun isEntryValid(name: String, surname: String, age: String): Boolean {
        return viewModel.isEntryValid(name, surname, age)
    }

    private fun showSnackbarForInputError(position: Int) {
        Snackbar.make(
            mContext,
            requiredView,
            mContext.getString(R.string.input_error_text),
            Snackbar.LENGTH_LONG
        ).setAction(mContext.getString(R.string.try_again_button_text)) { updateProfile(position) }
            .show()
    }
}

