package be.condictum.move_up.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import be.condictum.move_up.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreenTexts()
    }

    private fun setScreenTexts() {
        binding.aboutFragmentOtherTexts.text = "MIT License\n" +
                "\n" +
                "Copyright (c) 2021 condictum"
                "\n" +
                "\n" +
                        "Third party licences:\n"+
                        "- MPAndroidChart Copyright 2020 Philipp Jahoda\n"
    }
}