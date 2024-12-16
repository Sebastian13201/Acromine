package com.acromine.data.model


import com.google.gson.annotations.SerializedName

data class AcromineModelItemModel(
    @SerializedName("lfs")
    val lfs: List<LfModel?>? = listOf(),
    @SerializedName("sf")
    val sf: String? = ""
)