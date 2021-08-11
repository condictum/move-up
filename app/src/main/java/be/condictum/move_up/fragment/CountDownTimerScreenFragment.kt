package be.condictum.move_up.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import be.condictum.move_up.databinding.FragmentCountDownTimerScreenBinding

class CountDownTimerScreenFragment : Fragment() {
    companion object {
        const val SHARED_PREFERENCES_KEY_HOUR_OF_WORKING = "hourOfWorking"
    }

    private var _binding: FragmentCountDownTimerScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountDownTimerScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

}