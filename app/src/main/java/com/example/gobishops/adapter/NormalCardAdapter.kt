package com.example.gobishops.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gobishops.R
import com.example.gobishops.entity.Store
import com.example.gobishops.utils.HttpJavaUtil
import com.example.gobishops.utils.LoginStateUtil
import com.example.gobishops.utils.TextUtil
import com.example.gobishops.view.LoginActivity
import com.example.gobishops.view.StoreDetailActivity
import com.joooonho.SelectableRoundedImageView


/**
 * Created by Yee on 2021/2/3.
 * Github: Grindewald1900
 * Email: grindewald1504@gmail.com
 */

/**
 * Implemented for listview in store list
 */
class NormalCardAdapter(var date: ArrayList<Store>): RecyclerView.Adapter<NormalCardAdapter.NormalCardHolder>() {
    private lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalCardHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_normal_card, parent, false)
        mContext = parent.context
        return NormalCardHolder(itemView)
    }

    override fun onBindViewHolder(holder: NormalCardHolder, position: Int) {
        val cardData = date[position]
        holder.title.text = cardData.name
        holder.location.text = cardData.address
        holder.time.text = TextUtil.getItemPrice(cardData.averagePrice)
        // when heart clicked, upload event to server and change the shape of heart
        holder.heart.setOnClickListener {
            if (!LoginStateUtil.getIsLogin()){
                mContext.startActivity(Intent(mContext, LoginActivity::class.java))
                return@setOnClickListener
            }
            if(cardData.isCollect){
                holder.heart.background = mContext.getDrawable(R.drawable.ic_md_favorite_border_color1)
                date[position].isCollect = false
                Toast.makeText(mContext, R.string.hint_collect_cancel, Toast.LENGTH_SHORT).show()
                Thread{
                    HttpJavaUtil.EventByPost((1..100000).random(), LoginStateUtil.getUserId(), cardData.id, "STORE", 2)
                }.start()
            }else{
                holder.heart.background = mContext.getDrawable(R.drawable.ic_antfill_heart)
                date[position].isCollect = true
                Toast.makeText(mContext, R.string.hint_collect, Toast.LENGTH_SHORT).show()
                Thread{
                    HttpJavaUtil.EventByPost((1..100000).random(), LoginStateUtil.getUserId(), cardData.id, "STORE", 1)
                }.start()
            }

        }
        Glide.with(holder.itemView)
            .asBitmap()
            .load(R.drawable.img_portrait1)
            .into(holder.portrait)
        holder.layout.setOnClickListener {
            val intent = Intent(mContext, StoreDetailActivity::class.java)
            intent.putExtra("Location", cardData.address)
            intent.putExtra("Price", TextUtil.getItemPrice(cardData.averagePrice))
            intent.putExtra("id", cardData.id)
            intent.putExtra("lat", cardData.lat)
            intent.putExtra("long", cardData.long)
            mContext.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return date.size
    }
    class NormalCardHolder(view: View): RecyclerView.ViewHolder(view){
        var layout: View = view.findViewById(R.id.view_normal_card_background)
        var title: TextView = view.findViewById(R.id.tv_normal_card_title)
        var location: TextView = view.findViewById(R.id.tv_normal_card_location)
        val time: TextView = view.findViewById(R.id.tv_normal_card_time)
        var portrait: SelectableRoundedImageView = view.findViewById(R.id.iv_normal_card_portrait)
        val background: ImageView = view.findViewById(R.id.iv_normal_card_image)
        val heart: ImageButton = view.findViewById(R.id.ib_normal_card_collect)
    }
}