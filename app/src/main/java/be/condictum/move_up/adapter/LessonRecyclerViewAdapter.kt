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
import androidx.recyclerview.widget.RecyclerView
import be.condictum.move_up.R
import be.condictum.move_up.database.data.Lessons
import be.condictum.move_up.viewmodel.LessonsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class LessonRecyclerViewAdapter(
    private val mContext: Context,
    private var data: List<Lessons>,
    private val viewModel: LessonsViewModel,
    private val activity: Activity,
    private val requiredView: View
) :
    RecyclerView.Adapter<LessonRecyclerViewAdapter.LessonsViewHolder>() {

    class LessonsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lessonNameTextView =
            view.findViewById<TextView>(R.id.lessons_list_lesson_name_item_text)
        val lessonScoreTextView = view.findViewById<TextView>(R.id.lessons_list_score_item_text)
        val lessonCardView = view.findViewById<CardView>(R.id.lessons_list_card_view_item)
        val popupMenuImageButton =
            view.findViewById<ImageButton>(R.id.lessons_list_vertical_menu_item)
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
        var lessonTotalScore = lesson.lessonTotalScore.toString()

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
                }\n${
                    mContext.getString(R.string.formatted_total_score_text, lessonTotalScore)
                }"
            )
            dialog.setNegativeButton(mContext.getString(R.string.exit_button_text)) { _, _ -> }
            dialog.show()
        }

        holder.lessonCardView.setOnLongClickListener {
            showPopupMenu(holder.lessonCardView, lesson)
            true
        }

        holder.popupMenuImageButton.setOnClickListener {
            showPopupMenu(holder.popupMenuImageButton, lesson)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setDataset(data: List<Lessons>) {
        this.data = data
    }

    private fun showPopupMenu(anchor: View, data: Lessons) {
        val menu = PopupMenu(mContext, anchor)

        menu.menuInflater.inflate(R.menu.main_profiles_edit_menu, menu.menu)

        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.ac_main_edit_profile -> {
                    updateLesson(data)
                    true
                }
                R.id.ac_main_delete_profile -> {
                    showDeleteAlertDialog(data)
                    true
                }
                else -> {
                    false
                }
            }
        }

        menu.show()
    }

    private fun showDeleteAlertDialog(data: Lessons) {
        val dialog = MaterialAlertDialogBuilder(mContext)

        dialog.setTitle(mContext.getString(R.string.are_you_sure_text))
        dialog.setMessage(mContext.getString(R.string.lesson_is_deleting_text))

        dialog.setPositiveButton(mContext.getString(R.string.yes_button_text)) { _, _ ->
            viewModel.deleteLesson(data)
            Toast.makeText(
                mContext,
                mContext.getString(R.string.lesson_deleted_text),
                Toast.LENGTH_SHORT
            ).show()
        }

        dialog.setNegativeButton(mContext.getString(R.string.no_button_text)) { _, _ ->
            Toast.makeText(
                mContext,
                mContext.getString(R.string.lesson_isnt_deleted_text),
                Toast.LENGTH_SHORT
            ).show()
        }

        dialog.show()
    }

    private fun updateLesson(lesson: Lessons) {
        val builder = MaterialAlertDialogBuilder(mContext)
        builder.setTitle(mContext.getString(R.string.update_lesson_text))

        val view = activity.layoutInflater.inflate(
            R.layout.custom_create_lesson_alert_dialog,
            null
        )

        val nameText =
            view?.findViewById<TextInputEditText>(R.id.goal_result_lesson_name_edit_text)
        val scoreText =
            view?.findViewById<TextInputEditText>(R.id.goal_result_lesson_score_edit_text)
        val totalScoreText =
            view?.findViewById<TextInputEditText>(R.id.goal_result_lesson_total_score_edit_text)

        nameText?.setText(lesson.lessonName)
        scoreText?.setText(lesson.lessonScore.toString())
        totalScoreText?.setText(lesson.lessonTotalScore.toString())

        builder.setView(view)

        builder.setPositiveButton(
            mContext.getString(R.string.save_text)
        ) { _, _ ->
            val name = nameText?.text.toString()
            val score = scoreText?.text.toString()
            val totalScore = totalScoreText?.text.toString()

            if (isEntryValid(name, score, totalScore)) {
                val updatedLesson = Lessons(
                    lesson.id,
                    name,
                    score.toDouble(),
                    totalScore.toDouble(),
                    lesson.goalsId
                )
                viewModel.updateLesson(updatedLesson)
            } else {
                showSnackbarForInputError(lesson)
            }
        }

        builder.setNegativeButton(
            mContext.getString(R.string.cancel_text)
        ) { dialog, _ -> dialog.cancel() }

        builder.setCancelable(false)
        builder.show()
    }

    private fun showSnackbarForInputError(lesson: Lessons) {
        Snackbar.make(
            mContext,
            requiredView,
            mContext.getString(R.string.input_error_text),
            Snackbar.LENGTH_LONG
        ).setAction(mContext.getString(R.string.try_again_button_text)) { updateLesson(lesson) }
            .show()
    }

    private fun isEntryValid(name: String, score: String, totalScore: String): Boolean {
        return viewModel.isEntryValid(name, score, totalScore)
    }
}