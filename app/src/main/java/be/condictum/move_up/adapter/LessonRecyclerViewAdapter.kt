package be.condictum.move_up.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Lessons
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LessonRecyclerViewAdapter(private val mContext: Context, private var data: List<Lessons>) :
    RecyclerView.Adapter<LessonRecyclerViewAdapter.LessonsViewHolder>() {

    class LessonsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lessonNameTextView =
            view.findViewById<TextView>(R.id.lessons_list_lesson_name_item_text)
        val lessonScoreTextView = view.findViewById<TextView>(R.id.lessons_list_score_item_text)
        val lessonCardView = view.findViewById<CardView>(R.id.lessons_list_card_view_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.lessons_list_row_item, parent, false)
        return LessonsViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        val lesson = data[position]

        var lessonName = lesson.lessonName
        var lessonScore = lesson.lessonScore.toString()

        if (lessonName.length > 8) {
            lessonName = lessonName.substring(0, 8) + "..."
        }

        if (lessonScore.length > 7) {
            lessonScore = lessonScore.substring(0, 7) + "..."
        }

        holder.lessonNameTextView.text =
            mContext.getString(R.string.formatted_lesson_text, lessonName)
        holder.lessonScoreTextView.text =
            mContext.getString(R.string.formatted_score_text, lessonScore)

        holder.lessonCardView.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(mContext)

            dialog.setTitle(mContext.getString(R.string.details_text))
            dialog.setMessage(
                "${
                    mContext.getString(
                        R.string.formatted_lesson_text,
                        lesson.lessonName
                    )
                }\n${
                    mContext.getString(
                        R.string.formatted_score_text,
                        lesson.lessonScore.toString()
                    )
                }"
            )
            dialog.setNegativeButton(mContext.getString(R.string.exit_button_text)) { _, _ -> }
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setDataset(data: List<Lessons>) {
        this.data = data
    }
}