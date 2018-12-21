package com.cover.test.anant.cover.model

import android.util.Log
import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.where

/**
 * Created by anant on 2018-12-21.
 */

@Suppress("UNUSED_PARAMETER")
fun storeEmpty(unit: Any) {}

fun store(entity: RealmObject) {
    val realm = Realm.getDefaultInstance()
    realm.executeTransaction {
        it.copyToRealm(entity)
    }
    realm.close()
}

fun fetchPredictedSavedPlace(realm: Realm, placeName: String): ArrayList<PredictedPlacesModel>? {
    val list = ArrayList<PredictedPlacesModel>()
    val realmResults = realm
            .where<PredictedPlacesModel>()
            .beginsWith("description", placeName)
            .findAll()
    list.addAll(realm.copyFromRealm(realmResults))

    return list
}

fun isPlaceSelected(realm: Realm, placeName: String): Boolean {
    val result = realm
            .where<PredictedPlacesModel>()
            .like("description", placeName)
            .findFirst()
    if (result == null) {
        return false
    }
    else {

        if (result.description == placeName) {
            return true
        }
    }
    return false
}

fun fetchPredictedInsurerName(realm: Realm, insurerName: String): ArrayList<String>? {
    val list = ArrayList<InsurerModel>()
    var insurerNamesList = ArrayList<String>()

    if (!insurerName.isNullOrEmpty()) {
        val realmResults = realm
                .where<InsurerModel>()
                .findAll()
        list.addAll(realm.copyFromRealm(realmResults))

        list.forEach {
            it.insurance_carriers?.forEach {
                if (it.startsWith(insurerName, true)) {
                    if (!insurerNamesList.contains(it)) {
                        Log.d("insurer", it)
                        insurerNamesList.add(it)
                    }
                }
            }
        }
        return insurerNamesList
    }

    return insurerNamesList
}

