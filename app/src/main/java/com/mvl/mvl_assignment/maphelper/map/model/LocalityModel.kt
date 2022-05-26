package com.mvl.mvl_assignment.maphelper.map.model

class LocalityModel internal constructor(id: String, name: String, pincode: String) {
    var id = ""
    var name = ""
    var pincode = ""

    init {
        this.id = id
        this.name = name
        this.pincode = pincode
    }
}