package com.mvl.mvl_assignment.helper


import androidx.appcompat.app.AppCompatActivity

class SharePrefStorage : AppCompatActivity() {
    var mapALocation: String? by stringPref(MAP_A_LOCATION)
    var mapBLocation: String? by stringPref(MAP_B_LOCATION)
    var mapQAValue: String? by stringPref(MAP_QA_VALUE)
    var mapQBValue: String? by stringPref(MAP_QB_VALUE)
    var latAValue: String? by stringPref(LAT_A_VALUE)
    var latBVAlue: String? by stringPref(LAT_B_VALUE)
    var lngBValue: String? by stringPref(LANG_B_VALUE)
    var lngAValue: String? by stringPref(LANG_A_VALUE)
    var dateValue: String? by stringPref(data)
    var userId: String? by stringPref(LANG_A_VALUE)
    var Loc_NickName_A: String? by stringPref(LOC_NICKNAME_A)
    var Loc_NickName_b: String? by stringPref(LOC_NICKNAME_B)

    companion object Key {
        const val MAP_A_LOCATION="map_a_location"
        const val MAP_B_LOCATION="map_b_location"
        const val MAP_QA_VALUE="map_qa_value"
        const val MAP_QB_VALUE="map_qb_value"
        const val LAT_A_VALUE="map_lata_value"
        const val LAT_B_VALUE="map_latb_value"
        const val LANG_A_VALUE="map_langa_value"
        const val LANG_B_VALUE="map_langb_value"
        const val LOC_NICKNAME_A="nick_name_a"
        const val LOC_NICKNAME_B="nick_name_b"
        const val data="date"
    }
}


