package be.condictum.move_up.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import be.condictum.move_up.R
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

        ObjectAnimator.ofFloat(binding.textView2, "translationY", -100f).apply {
            duration = 2000
            start()
        }

        ObjectAnimator.ofFloat(binding.imageView2, "translationY", -100f).apply {
            duration = 2000
            start()
        }

        ObjectAnimator.ofFloat(binding.imageView2, "translationX", 100f).apply {
            duration = 2000
            start()
        }

        val c = Runnable {
            val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment()
            view.findNavController().navigate(action)
        }

        val textc = Runnable {
            binding.textView2.text = getString(R.string.by_condictum_text)
        }

        val hand = Handler(Looper.getMainLooper())
        hand.postDelayed(c, 3000)
        hand.postDelayed(textc, 1800)
    }
}