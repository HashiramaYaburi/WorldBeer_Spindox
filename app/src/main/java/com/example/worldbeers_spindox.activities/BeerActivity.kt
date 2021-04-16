package com.example.worldbeers_spindox.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.example.worldbeers_spindox.R
import java.util.ArrayList

class BeerActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_event)

        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)


        //collecting intent parameters
        var intent = intent
        val aTitle = intent.getStringExtra("iTitle")
        val aFirstBrewed = intent.getStringExtra("iFirstBrewed")
        val aFoodPairing = intent.getStringArrayListExtra("iFoodPairing")
        val aBrewerTips = intent.getStringExtra("iBrewerTips")
        val aImage = intent.getStringExtra("iImage")

        actionBar.setTitle(aTitle)

        //show data into layout
        showData(aFirstBrewed,aFoodPairing,aBrewerTips,aImage)
    }

    private fun showData(aFirstBrewed : String, aFoodPairing : ArrayList<String>, aBrewerTips : String, aImage : String,){
        val firstBrewed : TextView = findViewById(R.id.a_first_brw)
        firstBrewed.text = "\n First Brewed: $aFirstBrewed \n"
        val FoodPairing : TextView = findViewById(R.id.a_food)
        FoodPairing.setText("Food Pairing : \n");
        aFoodPairing.forEach {
            FoodPairing.append("$it \n")
        }
        val BrewedTips : TextView = findViewById(R.id.a_brewer)
        BrewedTips.text= "Brewed Tips: $aBrewerTips"
        val image : ImageView = findViewById(R.id.ImageView)
        Glide.with(this)
                .load(aImage)
                .into(image)
    }
}