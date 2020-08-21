package com.xiling.play.bean

class TableLayoutBean {
    constructor(name: String, icon: Int?, isEnable: Boolean) {
        this.name = name
        this.icon = icon
        this.isEnable = isEnable
    }

    var name :String = ""
    var icon : Int? = null
    var isEnable : Boolean = false;
}