package be.condictum.move_up.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Lessons

class LessonRecyclerViewAdapter(private val mContext: Context, private var data: List<Lessons>) :
    RecyclerView.Adapter<LessonRecyclerViewAdapter.LessonsViewHolder>() {

    class LessonsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lessonNameTextView =
            view.findViewById<TextView>(R.id.lessons_list_lesson_name_item_text)
        val lessonScoreTextView = view.findViewById<TextView>(R.id.lessons_list_score_item_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.lessons_list_row_item, parent, false)
        return LessonsViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        val lesson = data[position]

        holder.lessonNameTextView.text = lesson.lessonName
        holder.lessonScoreTextView.text = lesson.lessonScore.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setDataset(data: List<Lessons>) {
        this.data = data
    }
}