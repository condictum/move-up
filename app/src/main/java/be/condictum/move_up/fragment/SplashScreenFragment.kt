package be.condictum.move_up.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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

        val colorc = Runnable {
            binding.layoutId.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary_color
                )
            )
            binding.imageView2.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary_color
                )
            )
        }
        val coloralphac= Runnable {
            binding.layoutId.alpha= 0.25F
            binding.imageView2.alpha= 0.25F

        }
        val colorfinalc = Runnable {
            binding.layoutId.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.imageView2.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.layoutId.alpha= 1F
            binding.imageView2.alpha= 1F
        }

        val hand = Handler(Looper.getMainLooper())
        hand.postDelayed(c, 3000)
        hand.postDelayed(textc, 1800)
        hand.postDelayed(colorc, 1600)
        hand.postDelayed(coloralphac, 1600)
        hand.postDelayed(colorfinalc, 1800)

    }
}