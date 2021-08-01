package be.condictum.move_up.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Profiles
import be.condictum.move_up.fragment.MainFragmentDirections

class ProfileMainRecyclerAdapter(
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
        holder.profileNameText.text = dataSet[position].name
        holder.profileSurnameText.text = dataSet[position].surname
        holder.profileAgeText.text = dataSet[position].age.toString()

        holder.profilesCardView.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToGoalScreenFragment(dataSet[position].id)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun setDataset(data: List<Profiles>) {
        dataSet = data
    }
}

