package be.condictum.move_up.adapter

/*
class ProfileRecyclerAdapter(private val context: Context, private val dataSet: ArrayList<Goals>) :
    RecyclerView.Adapter<ProfileRecyclerAdapter.GoalViewHolder>() {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    class ProfileViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val goalNameText: TextView = view.findViewById(R.id.row_item_profile_name_text)
        val goalDateText: TextView = view.findViewById(R.id.row_item_profile_surname_text)
        val goalsListCardView: CardView = view.findViewById(R.id.goals_list_card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profiles_list_row_item, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.goalNameText.text = dataSet[position].dataName
        holder.goalDateText.text = dateFormat.format(dataSet[position].dataDate)
        holder.goalsListCardView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToGoalScreenFragment()
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = dataSet.size
}
*/
