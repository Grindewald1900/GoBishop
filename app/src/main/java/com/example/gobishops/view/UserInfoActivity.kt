package com.example.gobishops.view

import `in`.galaxyofandroid.awesometablayout.AwesomeTabBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gobishops.R
import com.example.gobishops.adapter.ScheduleCardAdapter
import com.example.gobishops.adapter.UserInfoPagerAdapter
import com.example.gobishops.contract.UserInfoContract
import com.example.gobishops.entity.Event
import com.example.gobishops.entity.Schedule
import com.example.gobishops.presenter.UserInfoPresenter
import com.example.gobishops.utils.TypeUtil
import kotlinx.android.synthetic.main.activity_dish.*
import kotlinx.android.synthetic.main.activity_user_info.*

class UserInfoActivity : AppCompatActivity(), UserInfoContract.View{
    private var presenter: UserInfoContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        presenter = UserInfoPresenter(this)
    }

    /**
     * View Initialization, including onClickListener()
     */
    override fun initView() {
        val data: ArrayList<Schedule> = ArrayList()
        Glide.with(this).asBitmap().load(R.drawable.img_portrait).into(btn_activity_user_info_portrait)

        for(i in 0..10){
            val event = Event()
            data.add(TypeUtil.eventToSchedule(event, 1, 1))
            // If different month, set the month layout
            if(i == 0){
                data.add(0, TypeUtil.eventToSchedule(event, 1, 2))
            }else{
                if (data[i].month - data[i-1].month >= 1){
                    data.add(i, TypeUtil.eventToSchedule(event, 1, 2))
                }
                if(data[i].year - data[i-1].year >=1){
                    data.add(i, TypeUtil.eventToSchedule(event, 1, 3))
                }
            }

        }

        viewpager_activity_user_info.adapter = UserInfoPagerAdapter(supportFragmentManager)
        tab_bar_activity_user_info.setupWithViewPager(viewpager_activity_user_info)
        btn_activity_user_info_back.setOnClickListener {
            onBackPressed()
            finish()
        }
        // Github style list
//        rv_activity_user_info_home.itemAnimator = DefaultItemAnimator()
//        rv_activity_user_info_home.layoutManager = LinearLayoutManager(this)
//        rv_activity_user_info_home.adapter = ScheduleCardAdapter(data)
    }

    /**
     * Show a short toast
     */
    override fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}