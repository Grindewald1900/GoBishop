package com.example.gobishops.utils

import android.R.attr.data
import android.util.Log
import com.example.gobishops.entity.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Yee on 2021/3/4.
 * Github: Grindewald1900
 * Email: grindewald1504@gmail.com
 */
object EntityUtil {
    /**
     * Convert json to User
     */
    fun jsonToUser(string: String): User{
        val str = "{\"user_birthday\":\"3894-10-24\",\"user_password\":\"aaa\",\"user_mail\":\"aaa\",\"user_id\":\"1690\",\"user_tel\":\"8008008888\",\"user_name\":\"aaa\",\"user_gender\":\"M\",\"user_icon\":\"\\\\x\"}"
        val jsonArray = JSONArray(string)
        val jsonObject = jsonArray.getJSONObject(0)
        return User(
            jsonObject.getString("user_id").toInt(),
            jsonObject.getString("user_name"),
            jsonObject.getString("user_mail"),
            Date(1994, 9, 24), 'M',
            jsonObject.getString("user_tel"),
            "image",
        )
    }

    /**
     * Convert json to Item(Dish)
     */
    fun jsonToItem(string: String): Item{
        val gson = Gson()
        return gson.fromJson(string, Item::class.java)
    }

    /**
     * Convert json to Item(Dish)
     */
    fun jsonToItemList(string: String): ArrayList<Item>{
        val result: ArrayList<Item> = ArrayList()
        if(string.isNullOrEmpty()) return result
        val jsonArray = JSONArray(string)
        for (i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            result.add(Item(
                jsonObject.getString("item_id").toInt(),
                jsonObject.getString("store_id").toInt(),
                jsonObject.getString("item_name"),
                jsonObject.getString("item_price").toFloat(),
                "image",
                jsonObject.getString("item_taste").toFloat(),
                jsonObject.getString("item_discount").toFloat(),
                ))
        }
        return result
    }

    /**
     * Convert json to Store list
     */
    fun jsonToStoreList(string: String): ArrayList<Store>{
        val result: ArrayList<Store> = ArrayList()
        val jsonArray = JSONArray(string)
        for (i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            result.add(
                Store(
                jsonObject.getString("store_id").toInt(),
                jsonObject.getString("store_name"),
                jsonObject.getString("store_address"),
                jsonObject.getString("store_genre"),
                jsonObject.getString("store_average").toFloat(),
                jsonObject.getString("store_tel"),
                jsonObject.getString("store_lat").toFloat(),
                jsonObject.getString("store_long").toFloat()
                ))
        }
        return result
    }

    /**
     * Convert json to Store list
     */
    fun jsonToOrderList(string: String): ArrayList<Order>{
        val result: ArrayList<Order> = ArrayList()
        val jsonArray = JSONArray(string)
        for (i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            result.add(
                Order(
                    jsonObject.getString("order_id").toInt(),
                    jsonObject.getString("user_name"),
                    jsonObject.getString("user_id").toInt(),
                    jsonObject.getString("upload_time"),
                    jsonObject.getString("order_price").toFloat(),
                    jsonObject.getString("store_tel"),
                    jsonObject.getString("order_content"),
                    // fake score
                    3,3,3
                )
            )
        }
        return result
    }

    /**
     * Convert json to OrderItem
     */
    fun jsonToOrderItem(string: String): OrderItem {
        val gson = Gson()
        return gson.fromJson(string, OrderItem::class.java)
    }



}