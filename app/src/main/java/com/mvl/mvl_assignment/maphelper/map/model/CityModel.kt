package com.mvl.mvl_assignment.maphelper.map.model

class CityModel internal constructor(id: String, name: String, state: String, stateId: String) {
    var id = ""
    var name = ""
    var state = ""
    var stateId = ""

    init {
        this.id = id
        this.name = name
        this.state = state
        this.stateId = stateId
    }
}
