package com.acromine.ui.acromine

import AcromineViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acromine.main.MainViewModel
import com.acromine.R
import com.acromine.data.util.ResponseState
import com.acromine.data.model.AcromineModelItemModel
import com.acromine.databinding.AcromineViewBinding
import com.google.firebase.auth.FirebaseAuth

class AcromineFragment : Fragment() {

    private var _binding: AcromineViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    // Use ViewModel scoped to the fragment
    private val viewmodel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AcromineViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the ViewModel's responseState
        viewmodel.responseState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseState.Loading -> updateLoadingUI()
                is ResponseState.Success -> updateSuccessUI(response.result.get(0))
                is ResponseState.Fail -> updateFailUI(response.error)
            }
        }

        // Set up click listener for a button to fetch data
        binding.btnGetUser.setOnClickListener {
            val shortFormInput = binding.etShort.text.toString().trim()

            // Check if the input is empty
            if (shortFormInput.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a short form", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if the input length exceeds 3 characters
            if (shortFormInput.length > 3) {
                Toast.makeText(requireContext(), "Short form cannot exceed 3 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Input is valid; proceed to fetch data
            viewmodel.getAcromine(shortFormInput)
        }

        binding.btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun updateLoadingUI() {
        binding.apply {
            progressCircular.visibility = View.VISIBLE
            tvText.text = "Loading..."
        }
    }

    private fun updateFailUI(error: String) {
        binding.apply {
            progressCircular.visibility = View.GONE
            tvText.text = error
        }
        Toast.makeText(requireContext(), "Failed: $error", Toast.LENGTH_SHORT).show()
    }

    private fun updateSuccessUI(response: AcromineModelItemModel) {
        binding.apply {
            progressCircular.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            tvText.visibility = View.GONE

            val userList = response.lfs // Get the list of users from the response
            // Set the adapter for the RecyclerView
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = AcromineViewAdapter(userList) // Set the correct adapter
            }

        }
    }

    private fun logoutUser() {
        auth = FirebaseAuth.getInstance()
        auth.signOut() // Sign out the user
        findNavController().navigate(R.id.action_acromineFragment_to_loginFragment) // Navigate back to login
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
