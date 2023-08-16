package com.faithdeveloper.believersheritagechurch.data.programmes

data class Programme(
    val day:String,
    val description:String,
    val frequency:String,
    val image_link:String,
    val name:String,
    val time:String
){
    constructor():this("","","","","","")
}
