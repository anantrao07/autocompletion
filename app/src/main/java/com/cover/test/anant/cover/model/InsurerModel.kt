package com.cover.test.anant.cover.model

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by anant on 2018-12-21.
 */
open class InsurerModel
(
        var insurance_carriers: RealmList<String>? = null
): RealmObject()