package com.cover.test.anant.cover.response

import com.cover.test.anant.cover.networking.API
import com.cover.test.anant.cover.networking.NetworkManager
import com.cover.test.anant.cover.networking.RequestParameters
import okhttp3.HttpUrl
import okhttp3.Request

/**
 * Created by anant on 2018-12-21.
 */

sealed class Route(val path: String) {

    open fun getUrl(): HttpUrl {
        return HttpUrl.get(NetworkManager.api.baseURL)!!.newBuilder()
                .addPathSegments(path)
                .build()
    }

    fun makeRequest(api: API, params: RequestParameters?): Request {
        val requestMethod = "GET"
        return makeRequest(api, params, requestMethod)
    }

    private fun makeRequest(api: API, params: RequestParameters?, requestMethod: String): Request {

        val requestBuilder = Request.Builder()
                .url(getUrl())
        return requestBuilder.build()
    }
}

class GetPredictedText(var text: String) : Route("") {
    override fun getUrl(): HttpUrl {
        val predictTextUrl = "${NetworkManager.api.baseURL}" +"?input=$text&types=address&key=AIzaSyBnMJjJXi3cyIVxzhdlYyaCG3PPQ4huF78"/*?input=137 Noe Street&types=address&key=AIzaSyBnMJjJXi3cyIVxzhdlYyaCG3PPQ4huF78"*//*+ "$text"*/
        return HttpUrl.parse(predictTextUrl)!!
    }
}