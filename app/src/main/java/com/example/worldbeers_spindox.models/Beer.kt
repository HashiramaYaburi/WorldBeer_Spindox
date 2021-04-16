package com.example.worldbeers_spindox.models


import java.util.ArrayList

class Beer(val name : String, val first_brewed : String,
           val description : String, val image_url : String,
           val abv : String,  val ibu : String,
           val food_pairing : ArrayList<String>, val brewers_tips : String) {}