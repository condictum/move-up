package be.condictum.move_up.view.ui.goalresult

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import be.condictum.move_up.R
import be.condictum.move_up.adapter.LessonRecyclerViewAdapter
import be.condictum.move_up.data.local.DatabaseApplication
import be.condictum.move_up.data.local.model.Lessons
import be.condictum.move_up.databinding.FragmentGoalResultBinding
import be.condictum.move_up.view.ui.goalscreen.GoalScreenFragment
import be.condictum.move_up.viewmodel.LessonsViewModel
import be.condictum.move_up.viewmodel.LessonsViewModelFactory
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class GoalResultFragment : Fragment() {
    private lateinit var lessonChart: CombinedChart
    private val barWidth: Float = 0.25f

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

        lessonChart = binding.goalResultCombinedChart
        lessonChart.setNoDataText(requireContext().getString(R.string.there_is_no_lesson_for_chart_text))
        lessonChart.setNoDataTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.secondary_dark
            )
        )

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
                controlViewItemsVisibility(it)
            })
    }

    private fun isEntryValid(name: String, score: String, totalScore: String): Boolean {
        return viewModel.isEntryValid(name, score, totalScore)
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
        val totalScoreText =
            view?.findViewById<TextInputEditText>(R.id.goal_result_lesson_total_score_edit_text)

        builder.setView(view)

        builder.setPositiveButton(
            getString(R.string.save_text)
        ) { _, _ ->
            val name = nameText?.text.toString()
            val score = scoreText?.text.toString()
            val totalScore = totalScoreText?.text.toString()

            if (isEntryValid(name, score, totalScore)) {
                viewModel.addNewLesson(
                    name, score, totalScore, getGoalIdFromSharedPreferences().toString()
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

    private fun controlViewItemsVisibility(lessons: List<Lessons>) {
        viewModel.getAllDataByGoalsId(getGoalIdFromSharedPreferences())
            .observe(viewLifecycleOwner, {
                if (it.isNullOrEmpty()) {
                    binding.goalResultNoProfileText.visibility = View.VISIBLE
                    binding.goalResultLessonRecyclerView.visibility = View.INVISIBLE
                    binding.goalResultCombinedChart.visibility = View.INVISIBLE
                } else {
                    binding.goalResultNoProfileText.visibility = View.GONE
                    binding.goalResultLessonRecyclerView.visibility = View.VISIBLE
                    binding.goalResultCombinedChart.visibility = View.VISIBLE
                    setChart(lessons)
                }
            })
    }

    private fun setChart(lessons: List<Lessons>) {
        lessonChart.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.windowBackgroundColorLight
            )
        )

        lessonChart.description.isEnabled = false
        lessonChart.setDrawGridBackground(false)
        lessonChart.setDrawBarShadow(false)
        lessonChart.isHighlightFullBarEnabled = false

        lessonChart.drawOrder = arrayOf(
            DrawOrder.BAR, DrawOrder.LINE
        )

        val rightAxis = lessonChart.axisRight
        rightAxis.axisMinimum = 0f
        rightAxis.setDrawGridLines(false)

        val leftAxis = lessonChart.axisLeft
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(false)

        val xAxis = lessonChart.xAxis
        xAxis.position = XAxisPosition.BOTH_SIDED
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f

        val chartData = CombinedData()

        chartData.setData(setLineChartData(lessons))
        chartData.setData(setBarChartData(lessons))

        lessonChart.data = chartData
        lessonChart.invalidate()
    }

    private fun setLineChartData(lessons: List<Lessons>): LineData? {
        val data = LineData()
        val entries: ArrayList<Entry> = ArrayList()
        for ((counter, index) in lessons.indices.withIndex()) entries.add(
            Entry(
                counter / 3f + barWidth / 15f,
                lessons[index].lessonScore.toFloat()
            )
        )

        val set = LineDataSet(entries, requireContext().getString(R.string.scores_text))

        set.color = ContextCompat.getColor(requireContext(), R.color.primary_light)
        set.lineWidth = 5f

        set.setCircleColor(ContextCompat.getColor(requireContext(), R.color.secondary_dark))
        set.circleRadius = 7f

        set.fillColor = ContextCompat.getColor(requireContext(), R.color.secondary_light)
        set.valueTextColor = ContextCompat.getColor(requireContext(), R.color.secondary_dark)

        set.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 15f

        data.addDataSet(set)

        return data
    }

    private fun setBarChartData(lessons: List<Lessons>): BarData? {
        val data: ArrayList<BarEntry> = ArrayList()
        for ((counter, index) in lessons.indices.withIndex()) {
            data.add(BarEntry(counter / 3f, lessons[index].lessonTotalScore.toFloat()))
        }

        val set = BarDataSet(data, requireContext().getString(R.string.total_scores_text))

        set.color = Color.rgb(60, 220, 78)
        set.valueTextSize = 0f

        val barWidth = barWidth
        val barData = BarData(set)
        barData.barWidth = barWidth

        return barData
    }
}