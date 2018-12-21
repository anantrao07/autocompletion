package com.cover.test.anant.cover.ui


import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.cover.test.anant.cover.model.PredictedPlacesModel
import java.util.*

/**
 * Created by anant on 2018-12-21.
 */

 internal class AutoFillAdapter(var mContext: Context, var stringResources: Int) : ArrayAdapter<String>(mContext, stringResources),
        Filterable {

      var resultList: ArrayList<PredictedPlacesModel> = ArrayList()


    override fun getCount(): Int {
        // Last item will be the footer
        return resultList.size
    }

    override fun getItem(position: Int): String {
        return resultList[position].description
    }

    fun getRealmItem(position: Int): PredictedPlacesModel {
        return resultList[position]
    }
    override fun getFilter(): Filter {

        return object : Filter() {

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if (resultList.size > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {

                    filterResults.values = resultList
                    filterResults.count = resultList.size
                }
                return filterResults
            }
        }
    }
}

