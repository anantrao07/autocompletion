package com.cover.test.anant.cover.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.cover.test.anant.cover.R
import com.cover.test.anant.cover.model.fetchPredictedInsurerName
import com.cover.test.anant.cover.model.store
import com.cover.test.anant.cover.networking.requestInsurers
import com.cover.test.anant.cover.utils.ifUpdated
import com.cover.test.anant.cover.utils.isUpdated
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_insurers.*
import kotlinx.android.synthetic.main.content_insurers.*
import kotlinx.coroutines.experimental.async
import java.io.IOException

class InsurerActivity : AppCompatActivity() {

    val realmInstance: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurers)
        setSupportActionBar(toolbar)
        currentInsurerAutoComplete.threshold = 2

        if (!ifUpdated()) {
            try {
                val fileName = "carriers.json"
                val jsonString = application.assets.open(fileName).bufferedReader().use {
                    it.readText()
                }
                async {
                    store(requestInsurers(jsonString))
                }
                isUpdated(true)
            } catch (IOe: IOException) {
                IOe.printStackTrace()
                isUpdated(false)
            }
        }

        var insurerResultList = fetchPredictedInsurerName(realmInstance,currentInsurerAutoComplete.text.toString())


        currentInsurerAutoComplete.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val description = parent.getItemAtPosition(position) as String
            Toast.makeText(applicationContext, description, Toast.LENGTH_SHORT).show()
        }

            currentInsurerAutoComplete.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if (p3 >= 3) {

                        if (!TextUtils.isEmpty(currentInsurerAutoComplete.text)) {

                            val textEntered = currentInsurerAutoComplete.text.toString()
                            insurerResultList = fetchPredictedInsurerName(realmInstance, textEntered)
                            val adapter  = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, insurerResultList)
                            adapter.setNotifyOnChange(true)
                            currentInsurerAutoComplete.setAdapter(adapter)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }
            })

        button.setOnClickListener {
            if (!insurerResultList!!.contains(currentInsurerAutoComplete.text.toString())) {
                showAlertDialogue(this, this.getString(R.string.insurerDialogTitle),
                        this.getString(R.string.insurerDialogMsg))
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
