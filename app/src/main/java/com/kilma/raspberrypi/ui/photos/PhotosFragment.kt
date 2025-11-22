package com.kilma.raspberrypi.ui.photos

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kilma.raspberrypi.databinding.FragmentPhotosBinding

class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PhotosViewModel

    private val selectPhotosLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val clipData = data.clipData
                val selectedUris = mutableListOf<Uri>()

                if (clipData != null) {
                    // Multiple photos selected
                    for (i in 0 until clipData.itemCount) {
                        selectedUris.add(clipData.getItemAt(i).uri)
                    }
                } else {
                    // Single photo selected
                    data.data?.let { uri -> selectedUris.add(uri) }
                }

                viewModel.setSelectedPhotos(selectedUris)
                Toast.makeText(context, "${selectedUris.size} photo(s) selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[PhotosViewModel::class.java]

        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe status updates
        viewModel.statusText.observe(viewLifecycleOwner) { status ->
            binding.tvStatus.text = status
        }

        viewModel.uploadProgress.observe(viewLifecycleOwner) { progress ->
            if (progress != null) {
                binding.progressUpload.visibility = View.VISIBLE
                binding.tvUploadProgress.visibility = View.VISIBLE
                binding.progressUpload.progress = progress.first
                binding.progressUpload.max = progress.second
                binding.tvUploadProgress.text = "${progress.first} / ${progress.second} photos uploaded"
            } else {
                binding.progressUpload.visibility = View.GONE
                binding.tvUploadProgress.visibility = View.GONE
            }
        }

        viewModel.uploadButtonEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.btnUploadSelected.isEnabled = enabled
        }

        // Connect button
        binding.btnConnect.setOnClickListener {
            val serverUrl = binding.etServerUrl.text.toString()
            val apiKey = binding.etApiKey.text.toString()

            if (serverUrl.isNotEmpty() && apiKey.isNotEmpty()) {
                viewModel.connect(serverUrl, apiKey)
                Toast.makeText(context, "Connecting to Immich...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please enter server URL and API key", Toast.LENGTH_SHORT).show()
            }
        }

        // Select photos button
        binding.btnSelectPhotos.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            selectPhotosLauncher.launch(intent)
        }

        // Upload button
        binding.btnUploadSelected.setOnClickListener {
            viewModel.uploadPhotos(requireContext())
            Toast.makeText(context, "Starting upload...", Toast.LENGTH_SHORT).show()
        }

        // Auto backup switch
        binding.switchAutoBackup.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAutoBackup(isChecked)
            if (isChecked) {
                Toast.makeText(context, "Auto backup enabled (WiFi only)", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Auto backup disabled", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
