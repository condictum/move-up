package be.condictum.move_up.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.condictum.move_up.R
import be.condictum.move_up.adapter.LessonRecyclerViewAdapter
import be.condictum.move_up.database.DatabaseApplication
import be.condictum.move_up.databinding.FragmentGoalResultBinding
import be.condictum.move_up.viewmodel.LessonsViewModel
import be.condictum.move_up.viewmodel.LessonsViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class GoalResultFragment : Fragment() {
    private var _binding: FragmentGoalResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerLessonAdapter: LessonRecyclerViewAdapter

    private val viewModel: LessonsViewModel by activityViewModels {
        LessonsViewModelFactory(
            (activity?.application as DatabaseApplication).database.lessonsDao(),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerLessonAdapter = LessonRecyclerViewAdapter(
            requireContext(),
            listOf(),
            viewModel,
            requireActivity(),
            requireView()
        )

        binding.goalResultLessonRecyclerView.adapter = recyclerLessonAdapter
        setDataset()

        binding.goalResultFab.setOnClickListener {
            addNewLesson()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    private fun setDataset() {
        viewModel.getAllDataByGoalsId(getGoalIdFromSharedPreferences())
            .observe(viewLifecycleOwner, {
                recyclerLessonAdapter.setDataset(it)
                recyclerLessonAdapter.notifyDataSetChanged()
                controlViewItemsVisibility()
            })
    }

    private fun isEntryValid(name: String, score: String): Boolean {
        return viewModel.isEntryValid(name, score)
    }

    private fun addNewLesson() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle(getString(R.string.create_lesson_text))

        val view = activity?.layoutInflater?.inflate(
            R.layout.custom_create_lesson_alert_dialog,
            null
        )

        val nameText =
            view?.findViewById<TextInputEditText>(R.id.goal_result_lesson_name_edit_text)
        val scoreText =
            view?.findViewById<TextInputEditText>(R.id.goal_result_lesson_score_edit_text)

        builder.setView(view)

        builder.setPositiveButton(
            getString(R.string.save_text)
        ) { _, _ ->
            val name = nameText?.text.toString()
            val score = scoreText?.text.toString()

            if (isEntryValid(name, score)) {
                viewModel.addNewLesson(
                    name, score, getGoalIdFromSharedPreferences().toString()
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

    private fun showSnackbarForInputError() {
        Snackbar.make(
            requireContext(),
            requireView(),
            getString(R.string.input_error_text),
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.try_again_button_text)) { addNewLesson() }
            .show()
    }

    private fun getGoalIdFromSharedPreferences(): Int {
        val mContext = requireContext()
        val sharedPreferences =
            mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)
        val goalId = sharedPreferences.getInt(GoalScreenFragment.SHARED_PREFERENCES_KEY_GOAL_ID, 0)

        if (goalId == 0) {
            Log.e("Error", "Goal Id Must Not Be Zero!")
        }

        return goalId
    }

    private fun controlViewItemsVisibility() {
        viewModel.getAllDataByGoalsId(getGoalIdFromSharedPreferences())
            .observe(viewLifecycleOwner, {
                if (it.isNullOrEmpty()) {
                    binding.goalResultNoProfileText.visibility = View.VISIBLE
                    binding.goalResultLessonRecyclerView.visibility = View.INVISIBLE
                } else {
                    binding.goalResultNoProfileText.visibility = View.GONE
                    binding.goalResultLessonRecyclerView.visibility = View.VISIBLE
                }
            })
    }
}