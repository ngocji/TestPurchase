package ji.common.flow

import com.google.gson.annotations.SerializedName

data class ErrorBody(
    @SerializedName("statusCode")
    val status: Int?,

    @SerializedName("error")
    val error: String?,

    @SerializedName("message")
    val errorMessage: String?
) : Throwable(errorMessage)