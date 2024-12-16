package com.acromine.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.acromine.MainViewModel
import com.acromine.data.ResponseState
import com.acromine.data.model.AcromineModelItemModel
import com.acromine.databinding.AcromineViewBinding

class AcromineFragment : Fragment() {

    private var _binding: AcromineViewBinding? = null
    private val binding get() = _binding!!

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
            viewmodel.getAcromine()
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

            val displayText = "${response.sf}, \n" + // Short form
                    "${response.lfs?.get(0)?.lf}, \n" + // First long form
                    "${response.lfs?.get(0)?.freq}, \n" + // Frequency of the first long form
                    "${response.lfs?.get(0)?.since}" // Since when the long form has been used

            etLong.setText(displayText)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
