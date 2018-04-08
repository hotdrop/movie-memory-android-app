package jp.hotdrop.moviememory.data.remote.response

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class MovieEntity(
        // 一旦これで
        val page: Int,

        @Json(name = "start_date")
        val startDate: String,

        @Json(name = "end_date")
        val endDate: String,

        @Json(name = "total_pages")
        val totalPages: Int,

        @Json(name = "total_results")
        val totalResults: Int,

        val results: List<MovieResult>
)
