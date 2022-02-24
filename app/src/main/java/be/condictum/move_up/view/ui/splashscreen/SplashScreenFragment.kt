package be.condictum.move_up.view.ui.splashscreen

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import be.condictum.move_up.R
import be.condictum.move_up.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    companion object {
        private var isFirstOpening: Boolean = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (!isFirstOpening) {
            val action =
                SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment()
            view?.findNavController()?.navigate(action)

            isFirstOpening = false
        } else {
            playAnimation()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.textView2, "translationY", -100f).apply {
            duration = 2000
            start()
        }

        val c = Runnable {
            val action =
                SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment()
            view?.let { Navigation.findNavController(it).navigate(action) }
        }

        val textc = Runnable {
            binding.textView2.text = context?.getString(R.string.condictum_text)
        }

        val hand = Handler(Looper.getMainLooper())
        hand.postDelayed(c, 3000)
        hand.postDelayed(textc, 1800)
    }
}