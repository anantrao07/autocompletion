package com.cover.test.anant.cover.response

import android.util.Log
import com.cover.test.anant.cover.model.InsurerModel
import com.cover.test.anant.cover.model.PredictedPlacesModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody

/**
 * Created by anant on 2018-12-21.
 */
class ParseException(val reason: String): RuntimeException(reason)



private const val TAG = "RESPONSEPARSING"

@Suppress("UNUSED_PARAMETER")
fun parseEmpty(responseBody: ResponseBody) {
}

var placesNames: ArrayList<PredictedPlacesModel>? = null
fun parsePredictedPlaces(responseBody: ResponseBody): ArrayList<PredictedPlacesModel> {

    val responseString = responseBody.string()

    val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    try {
        val predictedPlacesListData = gson.fromJson(responseString, PredictedPlacesList::class.java)
        Log.d("DESCRIPTION", predictedPlacesListData.data[0].description)
        placesNames = predictedPlacesListData.data
        return predictedPlacesListData.data
    } catch (e: Exception) {
        Log.d(TAG, e.localizedMessage)
    }
    throw ParseException("Couldn't parse places items list ")
}

fun parseInsurers(insurersString: String): InsurerModel {


    val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    try {
        val insurerListData = gson.fromJson(insurersString, InsurerModel::class.java)

        Log.d("insurer", insurerListData.insurance_carriers?.get(40))

        return insurerListData

    } catch (e: Exception) {
        Log.d(TAG, e.localizedMessage)
    }
    throw ParseException("Couldn't parse insurer items list ")
}

data class PredictedPlacesList
(
        @SerializedName("predictions") val data: ArrayList<PredictedPlacesModel>
)
