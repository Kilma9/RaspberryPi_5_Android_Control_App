package com.kilma.raspberrypi.ui.minecraft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kilma.raspberrypi.databinding.FragmentMinecraftBinding

class MinecraftFragment : Fragment() {

    private var _binding: FragmentMinecraftBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val minecraftViewModel =
            ViewModelProvider(this).get(MinecraftViewModel::class.java)

        _binding = FragmentMinecraftBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMinecraft
        minecraftViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
