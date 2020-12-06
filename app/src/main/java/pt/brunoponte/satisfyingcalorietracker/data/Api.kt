package pt.brunoponte.satisfyingcalorietracker.data

import android.net.Uri

class Api {

    companion object {
        private const val baseUrl =
            "https://api.edamam.com/api/food-database/v2/parser?ingr=%s&app_id=%s&app_key=%s"

        fun constructUrl(query: String, appId: String, appKey: String) : String {
            return String.format(baseUrl, Uri.encode(query), appId, appKey)
        }
    }

}