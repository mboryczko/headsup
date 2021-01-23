package pl.michalboryczko.exercise.data.source.remote.model.response

import com.google.gson.annotations.SerializedName

data class NetworkQuestionResponse(
        val questions: List<NetworkQuestion>
)


data class NetworkQuestion(
        @SerializedName("id")
        val id: String?,
        @SerializedName("name")
        val name: String?
)