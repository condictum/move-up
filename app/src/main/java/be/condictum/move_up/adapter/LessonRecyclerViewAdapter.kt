package be.condictum.move_up.adapter

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

class LessonRecyclerViewAdapter(
    private val mContext: Context,
    private var data: List<Lessons>,
    private val viewModel: LessonsViewModel
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
/*
    private fun updateLesson() {
        val builder = MaterialAlertDialogBuilder(mContext)
        builder.setTitle(mContext.getString(R.string.update_lesson_text))

        val view = ?.layoutInflater?.inflate(
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
 */
}