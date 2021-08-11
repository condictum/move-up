package be.condictum.move_up.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import be.condictum.move_up.R
import be.condictum.move_up.databinding.FragmentPreparationScreenBinding
import com.github.mikephil.charting.charts.LineChart

class PreparationScreenFragment : Fragment() {
    private var _binding: FragmentPreparationScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var lessonTimeChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPreparationScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lessonTimeChart = binding.preparationsFragmentLineChart
        lessonTimeChart.setNoDataText(requireContext().getString(R.string.there_is_no_time_data_for_lessons_chart_text))
        lessonTimeChart.setNoDataTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.secondary_dark
            )
        )

        binding.preparationsFragmentFab.setOnClickListener {
            addNewLesson()
        }

        // binding.textView.text = getTotalWorkingTime("135:23:54", "24:54:32")
        controlViewItemsVisibility()
    }

    private fun addNewLesson() {
        val action =
            PreparationScreenFragmentDirections.actionPreparationScreenFragment2ToCountDownTimerScreenFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun controlViewItemsVisibility() {
        binding.preparationsFragmentNoLessonText.visibility = View.VISIBLE
        binding.preparationsFragmentRecyclerView.visibility = View.INVISIBLE
        binding.preparationsFragmentLineChart.visibility = View.INVISIBLE
    }

    private fun getTotalWorkingTime(oldTime: String, newTime: String): String {
        val oldSplitted = oldTime.split(":")
        val newSplitted = newTime.split(":")

        val oldHour = oldSplitted[0].toInt()
        val oldMinute = oldSplitted[1].toInt()
        val oldSecond = oldSplitted[2].toInt()

        val newHour = newSplitted[0].toInt()
        val newMinute = newSplitted[1].toInt()
        val newSecond = newSplitted[2].toInt()

        val totalSecond = (oldSecond + newSecond) % 60
        val minutesFromSeconds = (oldSecond + newSecond) / 60
        val totalMinute = ((oldMinute + newMinute) + minutesFromSeconds) % 60
        val hoursFromMinutes = ((oldMinute + newMinute) + minutesFromSeconds) / 60
        val totalHour = oldHour + newHour + hoursFromMinutes

        return "$totalHour:$totalMinute:$totalSecond"
    }
}