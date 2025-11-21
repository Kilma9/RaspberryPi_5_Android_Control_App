package com.kilma.raspberrypi.ui.hypervisor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kilma.raspberrypi.databinding.FragmentHypervisorBinding

class HypervisorFragment : Fragment() {

    private var _binding: FragmentHypervisorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val hypervisorViewModel =
            ViewModelProvider(this).get(HypervisorViewModel::class.java)

        _binding = FragmentHypervisorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHypervisor
        hypervisorViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
