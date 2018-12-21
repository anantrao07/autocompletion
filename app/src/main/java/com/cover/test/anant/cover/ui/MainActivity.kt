package com.cover.test.anant.cover.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView
import android.widget.Toast
import com.cover.test.anant.cover.R
import com.cover.test.anant.cover.model.fetchPredictedSavedPlace
import com.cover.test.anant.cover.model.isPlaceSelected
import com.cover.test.anant.cover.model.store
import com.cover.test.anant.cover.networking.NetworkManager.suspendRequestPredictiveText
import com.cover.test.anant.cover.response.placesNames
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class MainActivity : AppCompatActivity() {

    val realmInstance = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val predictionAdapter = AutoFillAdapter(this, android.R.layout.simple_dropdown_item_1line)

        autoCompleteTextView.threshold = 2
        autoCompleteTextView.setAdapter(predictionAdapter)
        predictionAdapter.setNotifyOnChange(true)


        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val description = parent.getItemAtPosition(position) as String
            Toast.makeText(applicationContext, description, Toast.LENGTH_SHORT).show()
            if (!isPlaceSelected(realmInstance, description)) {
                store(predictionAdapter.getRealmItem(position))
            }
        }
        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (!TextUtils.isEmpty(autoCompleteTextView.text)) {

                    val textEntered = autoCompleteTextView.text.toString()
                    val placesList = fetchPredictedSavedPlace(realmInstance, textEntered)
                    if (placesList?.isEmpty()!!) {
                        async(UI) {
                            try {
                                suspendRequestPredictiveText(autoCompleteTextView.text.toString())
                                predictionAdapter.resultList = placesNames!!
                                predictionAdapter.notifyDataSetChanged()
                            } catch (e: Exception) {
                                Log.d("Exception", e.localizedMessage)
                            }
                        }

                    } else {
                        predictionAdapter.resultList = placesList
                        predictionAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
        })
        nextButton.setOnClickListener {
            if(autoCompleteTextView.text.toString().isNullOrEmpty() || !isPlaceSelected(realmInstance,autoCompleteTextView.text.toString()) ){
                showAlertDialogue(this,this.getString(R.string.insurerDialogTitle), this.getString(R.string.insurerDialogMsg))
                    }
            else {
                val intent = Intent(this,InsurerActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realmInstance.close()
    }

    private fun showAlertDialogue(context: Context, title: String, message: String) {
        val alertDialog = AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.ok), { _, _ -> })
        alertDialog.show()
    }
}

