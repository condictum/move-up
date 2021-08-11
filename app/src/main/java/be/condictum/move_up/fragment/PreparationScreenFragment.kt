package be.condictum.move_up.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import be.condictum.move_up.databinding.FragmentPreparationScreenBinding

class PreparationScreenFragment : Fragment() {
    private var _binding: FragmentPreparationScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPreparationScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

}