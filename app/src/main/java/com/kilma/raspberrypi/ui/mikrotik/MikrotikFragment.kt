package com.kilma.raspberrypi.ui.mikrotik

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kilma.raspberrypi.databinding.FragmentMikrotikBinding

class MikrotikFragment : Fragment() {

    private var _binding: FragmentMikrotikBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MikrotikViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[MikrotikViewModel::class.java]

        _binding = FragmentMikrotikBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe terminal output
        viewModel.terminalOutput.observe(viewLifecycleOwner) { output ->
            binding.tvTerminalOutput.text = output
        }

        // Quick command buttons
        binding.btnSystemResource.setOnClickListener {
            viewModel.getSystemResource()
        }

        binding.btnSystemIdentity.setOnClickListener {
            viewModel.getSystemIdentity()
        }

        binding.btnInterfaces.setOnClickListener {
            viewModel.getInterfaces()
        }

        binding.btnIpAddress.setOnClickListener {
            viewModel.getIpAddresses()
        }

        binding.btnRoutes.setOnClickListener {
            viewModel.getRoutes()
        }

        binding.btnFirewall.setOnClickListener {
            viewModel.getFirewallRules()
        }

        binding.btnDhcpLeases.setOnClickListener {
            viewModel.getDhcpLeases()
        }

        binding.btnWireless.setOnClickListener {
            viewModel.getWireless()
        }

        binding.btnWirelessClients.setOnClickListener {
            viewModel.getWirelessClients()
        }

        binding.btnLogs.setOnClickListener {
            viewModel.getSystemLogs()
        }

        // Connect button
        binding.btnConnect.setOnClickListener {
            val ip = binding.etIpAddress.text.toString()
            val port = binding.etPort.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val useHttps = binding.switchHttps.isChecked

            if (ip.isNotEmpty() && port.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.connect(ip, port, username, password, useHttps)
                Toast.makeText(context, "Connecting to MikroTik...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Send command button
        binding.btnSendCommand.setOnClickListener {
            val command = binding.etCommand.text.toString()
            if (command.isNotEmpty()) {
                viewModel.sendApiRequest(command, "GET")
                binding.etCommand.text?.clear()
            } else {
                Toast.makeText(context, "Please enter a command", Toast.LENGTH_SHORT).show()
            }
        }

        // Disconnect button
        binding.btnDisconnect.setOnClickListener {
            viewModel.disconnect()
            Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
