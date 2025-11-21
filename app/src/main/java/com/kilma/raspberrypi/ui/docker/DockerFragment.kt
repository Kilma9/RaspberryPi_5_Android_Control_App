package com.kilma.raspberrypi.ui.docker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kilma.raspberrypi.databinding.FragmentDockerBinding

class DockerFragment : Fragment() {

    private var _binding: FragmentDockerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dockerViewModel =
            ViewModelProvider(this).get(DockerViewModel::class.java)

        _binding = FragmentDockerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDocker
        dockerViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
