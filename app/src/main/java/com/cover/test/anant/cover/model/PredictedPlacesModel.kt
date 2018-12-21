package com.cover.test.anant.cover.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
/**
 * Created by anant on 2018-12-21.
 */
open class PredictedPlacesModel
(
    var description: String = ""
) : RealmObject()