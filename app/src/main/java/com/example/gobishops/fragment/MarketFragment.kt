package com.example.gobishops.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gobishops.R
import com.example.gobishops.adapter.ShoppingCartAdapter
import com.example.gobishops.contract.BaseContract
import com.example.gobishops.entity.Item
import com.example.gobishops.entity.NormalCard
import com.example.gobishops.entity.OrderItem
import com.example.gobishops.utils.*
import com.example.gobishops.view.MainActivity
import com.example.gobishops.view.ResultActivity
import com.google.android.gms.maps.model.LatLng
import com.royrodriguez.transitionbutton.TransitionButton
import kotlinx.android.synthetic.main.activity_dish.*
import kotlinx.android.synthetic.main.activity_login_v2.*
import kotlinx.android.synthetic.main.fragment_market.*
import kotlin.random.Random

/**
 * Created by Yee on 2020/12/26.
 * Github: Grindewald1900
 * Email: grindewald1504@gmail.com
 */
class MarketFragment : Fragment(), BaseContract.OnAdapterCHanged {
    companion object{
        fun newInstance(): MarketFragment{
            return MarketFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_market, container, false)
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    /**
     * Update the recyclerview when data changed
     * @param index: -1: do not remove the item view, positive integer: remove the view at index
     */
    override fun updateAdapter(index: Int) {
        var tPrice = 0f
        val orders: ArrayList<OrderItem> = SharedPreferencesUtil.getOrder(ConstantUtil.CLASS_ORDER_ITEM)
        orders.forEach {
            tPrice += it.item!!.price * it.count
        }

        tv_shopping_cart_price.text = TextUtil.getItemPrice(tPrice)
        tv_shopping_cart_gst.text = TextUtil.getItemPrice(tPrice * 0.15f)
        tv_shopping_cart_total_price.text = TextUtil.getItemPrice(tPrice * 1.15f)
        if(index >= 0){
//            rv_activity_cart.removeViewAt(index)
            initView()

        }
    }

    /**
     * View Initialization, including onClickListener()
     */
    private fun initView(){
        val data: ArrayList<OrderItem>?
        val handler: Handler = Handler()

        // Prepare data for event list
        if(!SharedPreferencesUtil.isSharedKeyEmpty(ConstantUtil.CLASS_ORDER_ITEM)){
            data = SharedPreferencesUtil.getOrder(ConstantUtil.CLASS_ORDER_ITEM)
            rv_activity_cart.layoutManager = LinearLayoutManager(context)
            rv_activity_cart.itemAnimator = DefaultItemAnimator()
            rv_activity_cart.adapter = ShoppingCartAdapter(data, this)
        }

        btn_activity_cart_checkout.setOnClickListener {
            if(null == LoginStateUtil.getUser()){
                val intent = Intent(context, ResultActivity::class.java)
                intent.putExtra(ConstantUtil.STRING_RESULT_ACTIVITY, ConstantUtil.RESULT_INCORRECT)
                Toast.makeText(context, R.string.hint_no_login, Toast.LENGTH_SHORT).show()
                startActivity(intent)
                return@setOnClickListener
            }
            btn_activity_cart_checkout.startAnimation()
            // TODO: to be fixed - upload the whole order list, rather than one by one
            Thread{
                val orders = SharedPreferencesUtil.getOrder(ConstantUtil.CLASS_ORDER_ITEM)
                val orderId = (1..200000).random()
                if(orders.size > 0){
                    for(i in 0 until orders.size){
                        HttpJavaUtil.OrderByPost(i, orderId, LoginStateUtil.getUser()!!.id, orders[i].item!!.restaurantId, orders[i].item!!.id, orders[i].count, 1)
                    }
                }
                SharedPreferencesUtil.releaseOrder()
                handler.postDelayed(Runnable {
                    btn_activity_cart_checkout.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND) {
                        //Start Activity here
                        val intent = Intent(context, ResultActivity::class.java)
                        if(orders.size > 0){
                            intent.putExtra(ConstantUtil.STRING_RESULT_ACTIVITY, ConstantUtil.RESULT_CORRECT)
                        }else{
                            intent.putExtra(ConstantUtil.STRING_RESULT_ACTIVITY, ConstantUtil.RESULT_INCORRECT)
                        }
                        startActivity(intent)
                    }
                }, 1000)
            }.start()



//            handler.postDelayed(Runnable {
//                var isSuccessful: Boolean = true
//                if(isSuccessful){
//                    btn_activity_cart_checkout.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, TransitionButton.OnAnimationStopEndListener {
//                        //Start Activity here
//                        Toast.makeText(context, getText(R.string.checkout_successful), Toast.LENGTH_SHORT).show()
//                    })
//                }
//            }, 1000)
        }
    }

}