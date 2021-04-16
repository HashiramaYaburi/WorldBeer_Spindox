package com.example.worldbeers_spindox.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldbeers_spindox.models.Beer
import com.example.worldbeers_spindox.R
import com.example.worldbeers_spindox.activities.BeerActivity
import java.util.*


class Adapter(val arrayList: ArrayList<Beer>, val context: Context) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

  open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      //setting text of TextViews
      fun bindItems(beer: Beer) {
          itemView.findViewById<TextView>(R.id.beer_name).text = beer.name
          itemView.findViewById<TextView>(R.id.beer_desc).text = beer.description
          var abv = beer.abv
          if (abv == null){
              itemView.findViewById<TextView>(R.id.beer_alcol).text = "ABV: N/A"
          } else {
              itemView.findViewById<TextView>(R.id.beer_alcol).text = "ABV: $abv %"
          }
          var ibu = beer.ibu
          if (ibu == null){
              itemView.findViewById<TextView>(R.id.beer_ibu).text = "IBU: N/A"
          } else {
              itemView.findViewById<TextView>(R.id.beer_ibu).text = "IBU $ibu"
          }
      }
  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType ==  VIEW_TYPE_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return ItemViewHolder(v) as ViewHolder
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            return LoadingViewHolder(v) as ViewHolder
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (holder is ItemViewHolder) {
            //fill TextViews with pre-loaded data
            holder.bindItems(arrayList[position])
            Glide.with(context)
                    .load(arrayList[position].image_url)
                    .into(holder.itemView.findViewById<ImageView>(R.id.iv_image))

        } else if (holder is LoadingViewHolder) {
            showLoadingView((holder as LoadingViewHolder?)!!, position)
        }
        //preparing new Activity with values
        holder.itemView.setOnClickListener{
            val beer = arrayList.get(position)

            var gTitle : String = beer.name
            var gFirstBrewed : String = beer.first_brewed
            var gFoodPairing : ArrayList<String> = beer.food_pairing
            var gBrewerTips : String = beer.brewers_tips
            var gImage : String = beer.image_url

            val intent = Intent(context, BeerActivity::class.java)

            intent.putExtra("iTitle", gTitle)
            intent.putExtra("iFirstBrewed", gFirstBrewed)
            intent.putExtra("iFoodPairing", gFoodPairing)
            intent.putExtra("iBrewerTips", gBrewerTips)
            intent.putExtra("iImage", gImage)

            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    //The following method decides the type of ViewHolder to display in the RecyclerView
    override fun getItemViewType(position: Int): Int {
            return if (position == arrayList.size-1) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

        private class ItemViewHolder : ViewHolder {
            constructor (@NonNull itemView: View) : super(itemView) {
                itemView.findViewById<TextView>(R.id.beer_name)
                itemView.findViewById<TextView>(R.id.beer_name)
                itemView.findViewById<TextView>(R.id.beer_desc)
                itemView.findViewById<TextView>(R.id.beer_alcol)
                itemView.findViewById<TextView>(R.id.beer_ibu)
            }
        }

        private class LoadingViewHolder: ViewHolder {
            constructor(@NonNull itemView: View) : super(itemView) {
                var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar);
            }
        }

        private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
            //ProgressBar would be displayed
        }

}