package com.example.gobishops.fragment

import android.content.Intent
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gobishops.R
import com.example.gobishops.adapter.DishAdapter
import com.example.gobishops.adapter.NormalCardAdapter
import com.example.gobishops.entity.Item
import com.example.gobishops.entity.NormalCard
import com.example.gobishops.entity.Store
import com.example.gobishops.utils.*
import com.example.gobishops.view.LoginActivity
import com.example.gobishops.view.RegisterActivity
import com.example.gobishops.view.StoreDetailActivity
import com.example.gobishops.view.UserInfoActivity
import com.joooonho.SelectableRoundedImageView
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by Yee on 2020/12/26.
 * Github: Grindewald1900
 * Email: grindewald1504@gmail.com
 */
class HomeFragment : Fragment() {
    companion object{
        fun newInstance(): HomeFragment{
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        // can not be invoked in onCreateView, since the view has not be initialized.
        initView()
    }


    /**
     * View Initialization, including onClickListener()
     */
    private fun initView(){
//        val data: ArrayList<NormalCard> = ArrayList()

        ArrayAdapter.createFromResource(requireContext(), R.array.home_search_type, R.layout.spinner_item_simple).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_fragment_home_type.adapter = it
        }

        // Prepare data for event list
        Thread{
            val result = HttpJavaUtil.GetStoreByPost(0, 1)
            val bundle = Bundle()
            val message = Message()
            bundle.putString(ConstantUtil.SERVER_RESULT, result)
            message.data = bundle
            message.what = ConstantUtil.HANDLER_STORE
            handler.sendMessage(message)
        }.start()

        iv_fragment_home_profile.setOnClickListener {
            if (LoginStateUtil.getIsLogin()){
                startActivity(Intent(context, UserInfoActivity::class.java))
            }else{
                startActivity(Intent(context, LoginActivity::class.java))
            }
        }
//        Glide.with(context!!)
//            .asBitmap()
//            .load(R.drawable.img_portrait)
//            .into(ivPortrait)
//        tv_home_1.text = initText(getString(R.string.f_home_activity))
//        tv_home_2.text = initText(getString(R.string.f_home_activity))
//        tv_home_3.text = initText(getString(R.string.f_home_activity))
//        iv_home_portrait.setOnClickListener {
//            if(null == AuthUtil.getCurrentUser()){
//                startActivity(Intent(context, LoginActivity::class.java))
//            }
//        }

    }

    /**
     * Deprecated
     * @param text: string to be processed
     */
    @Deprecated("This method is deprecated, since home page no longer need this style")
    private fun initText(text: String): SpannableString{
        val spannable = SpannableString(text)
        val index: ArrayList<Int> = TextUtil.getCharIndex(text, "\n")
        spannable.setSpan(StyleSpan(BOLD), index[0], index[1], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(BOLD), index[1], index[2], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(BOLD), index[3], index[4], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(BOLD), index[5], index[6], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    private fun initRecyclerView(stores: ArrayList<Store>){
        rv_fragment_home.layoutManager = LinearLayoutManager(context)
        rv_fragment_home.itemAnimator = DefaultItemAnimator()
        rv_fragment_home.adapter = NormalCardAdapter(stores)
    }

    //TODO memory leak here
    private var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                ConstantUtil.HANDLER_STORE -> {
                    var bundle = msg.data
                    var stores: ArrayList<Store> = ArrayList()
                    val result = bundle.getString(ConstantUtil.SERVER_RESULT).toString()
                    try {
                        Log.e("JSONTAG", result)
                        if (!TextUtil.isEmpty(result)) {
                            stores = EntityUtil.jsonToStoreList(result)
                            initRecyclerView(stores)
                        } else {
                            // Login unsuccessfully

                        }
                    } catch (e: java.lang.NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}