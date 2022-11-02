package com.example.beep.data.dto

import com.google.gson.annotations.SerializedName

data class RetrofitTestGetResponse(
    @SerializedName("data") val data: TestDataDto,
    @SerializedName("support") val support: TestSupportDto
) {

}
