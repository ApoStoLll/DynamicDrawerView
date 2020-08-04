package com.example.testdrawermenu.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class Repository{

    fun connect(context: Context){
        try{
            val url = URL(" https://www.dropbox.com/s/fk3d5kg6cptkpr6/menu.json?dl=1")
            val bis = BufferedInputStream(url.openStream())
            val filename = File(context.filesDir, "menu_json")
            val fis = FileOutputStream(filename)
            val buffer = ByteArray(1024)
            var count = 0
            while (bis.read(buffer, 0, 1024).also { count = it } != -1)
                fis.write(buffer, 0, count)
            fis.close()
            bis.close()
        }catch (e : Exception){
            Log.e("EXCEPTION", e.printStackTrace().toString())
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getJsonDataFromAsset(context: Context): String? {
        var jsonString = ""
        try {
//            jsonString = context.filesDir.("menu_json").bufferedReader().use { it.readText() }
            val br = BufferedReader(InputStreamReader(context.openFileInput("menu_json")))
            for (line in br.lines()){
                jsonString += line
            }
            Log.e("JSON STR", jsonString)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun parseJson(context: Context) : Menu{
        val jsonFileString = getJsonDataFromAsset(context)
        //Log.i("data", jsonFileString!!)
        return Gson().fromJson(jsonFileString, Menu::class.java)
//        val listPersonType = object : TypeToken<List<Menu>>() {}.type
//
//        val menu: List<Menu> = gson.fromJson(jsonFileString, listPersonType)
//        menu.forEachIndexed { idx, item -> Log.i("data", "> Item $idx:\n$menu") }
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url
                .openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}