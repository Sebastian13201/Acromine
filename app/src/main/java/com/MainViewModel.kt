package com.acromine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acromine.data.ResponseState
import com.acromine.data.model.AcromineModel
import com.acromine.data.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    // LiveData to hold the response state (success, fail, loading)
    private val _responseState = MutableLiveData<ResponseState>()
    val responseState: LiveData<ResponseState> = _responseState

    // Function to fetch the long forms and short form data
    fun getAcromine(acronym: String) {
        _responseState.value = ResponseState.Loading // Set the state to loading initially

        viewModelScope.launch {
            RetrofitClient.api.getLongForms(acronym).enqueue(object : Callback<AcromineModel> {
                override fun onResponse(call: Call<AcromineModel>, response: Response<AcromineModel>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            // Successfully fetched the data, set the state to Success
                            _responseState.value = ResponseState.Success(it)

                        } ?: run {
                            // No data returned, set the state to Fail
                            _responseState.value = ResponseState.Fail("No data found.")
                        }
                    } else {
                        // Failed API call
                        _responseState.value = ResponseState.Fail("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<AcromineModel>, t: Throwable) {
                    // Handle failure in network request
                    _responseState.value = ResponseState.Fail("Failed to load data: ${t.message}")
                }
            })
        }
    }
}