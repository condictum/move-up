package be.condictum.move_up.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import be.condictum.move_up.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var c = Runnable {
            val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment()
            view.findNavController().navigate(action)
        }

        var hand = Handler(Looper.getMainLooper())
        hand.postDelayed(c, 5000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}