package com.acromine.data.util

import com.acromine.data.model.AcromineModel

sealed class ResponseState {
    //loading
    object Loading : ResponseState()

    //success
    data class Success(val result: AcromineModel) :
        ResponseState() //ideally we should make it generic

    //failure
    data class Fail(val error: String) : ResponseState()
}