package be.condictum.move_up.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Profiles
import be.condictum.move_up.fragment.MainFragment
import be.condictum.move_up.fragment.MainFragmentDirections

class ProfileMainRecyclerAdapter(
    private val mContext: Context,
    private var dataSet: List<Profiles>
) :
    RecyclerView.Adapter<ProfileMainRecyclerAdapter.ProfileMainViewHolder>() {

    class ProfileMainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileNameText: TextView = view.findViewById(R.id.row_item_profile_name_text)
        val profileSurnameText: TextView = view.findViewById(R.id.row_item_profile_surname_text)
        val profileAgeText: TextView = view.findViewById(R.id.row_item_profile_age_text)
        val profilesCardView: CardView = view.findViewById(R.id.goals_list_card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileMainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profiles_list_row_item, parent, false)
        return ProfileMainViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileMainViewHolder, position: Int) {
        var name = dataSet[position].name
        var surname = dataSet[position].surname
        var age = dataSet[position].age.toString()

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
            val id = dataSet[position].id

            val sharedPreferences =
                mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(MainFragment.SHARED_PREFERENCES_KEY_PROFILE_ID, id)
                .apply()

            val action = MainFragmentDirections.actionMainFragmentToGoalScreenFragment()
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun setDataset(data: List<Profiles>) {
        dataSet = data
    }
}

