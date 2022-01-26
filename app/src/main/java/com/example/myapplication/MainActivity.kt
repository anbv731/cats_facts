package com.example.myapplication

import android.graphics.Bitmap
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cat_item.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val url: String = "https://cat-fact.herokuapp.com/facts"
    private val imageUrl: String = "https://aws.random.cat/meow "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRealm()
        showListFromDB()


        val queue = Volley.newRequestQueue(this)
        getFactsFromServer(queue)


    }

    private fun getFactsFromServer(queue: RequestQueue) {
        val stringRequest = StringRequest(
            0,
            url,
            { response ->

                val catList = parseResponse(response)
                saveIntoDB(catList)
                showListFromDB()


            },
            {
                println(it.message)
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()

            }

        )
        queue.add(stringRequest)

    }

//    private fun getImageFromServer(queue: RequestQueue, catList: List<Cat>) {
//        for (item in catList) {
//            val imageRequest = StringRequest(
//                0,
//                imageUrl,
//                { response ->
//                    item.image = parseImageResponse(response)
//                    if (catList.last().image != "") {
//                        setList(catList)
//                    }
//
//                },
//                {
//                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
//                }
//            )
//
//            queue.add(imageRequest)
//        }
//
//    }

    private fun parseImageResponse(responseText: String): String {
        val jsonObject = JSONObject(responseText)
        val catImage = jsonObject.getString("file")
        return catImage//возвращаемое значение функции
    }

    private fun parseResponse(responseText: String): List<Cat> {
        //создаем пустой список объектов класса Cat
        val catList: MutableList<Cat> = mutableListOf()
        //преобразуем текст ответа сервера в JSON массива
        val jsonArray = JSONArray(responseText)
        //в цикле по количеству элементов массива JSON объектов
        for (index in 0 until jsonArray.length()) {
            // получаем каждый элемент в виде JSON объекта
            val jsonObject = jsonArray.getJSONObject(index)
            //получаем значение параметра text
            val catText = jsonObject.getString("text")
            //получаем значение параметра image
//            val catImage = jsonObject.getString("image")
            //создаем объект класса Cat с вышеполученными параметрами
            val cat = Cat()
            cat.text = catText
//            cat.image = catImage
            catList.add(cat) //добавляем в список
        }
        return catList //возвращаемое значение функции
    }

    private fun setList(cats: List<Cat>) {
//        for(item in cats){
//            item.image= getImageFromServer().getString("file")
//        }
        val adapter = CatAdapter(cats)
        recyclerViewId.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        recyclerViewId.layoutManager = layoutManager
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
    }
    fun saveIntoDB (cats:List<Cat>){
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val catsDB=realm.where(Cat::class.java).findAll()
        realm.copyToRealmOrUpdate(cats)
        realm.commitTransaction()
    }

    fun loadFromDB ():List<Cat>{
        val realm=Realm.getDefaultInstance()
        return realm.where(Cat::class.java).findAll()
    }

    fun showListFromDB (){
        val cats=loadFromDB()
        setList(cats)
    }



}

