package com.xiling.play.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.lib.base.BaseActivity
import com.xiling.play.R
import com.xiling.play.activity.MainActivity
import com.xiling.play.http.RetrofitManager
import com.xiling.play.http.UserService
import com.xiling.play.im.IMInterface

class LoginActivity : BaseActivity() {


    lateinit var imInterface: IMInterface;
    lateinit var userService: UserService;

    override fun getContentViewId(): Int {
        return R.layout.activity_login
    }

    override fun getIntentData(intent: Intent?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ButterKnife.bind(this)
        var retrofit = RetrofitManager().getMyRetrofit()
        userService = retrofit!!.create(UserService::class.java)
    }


    @OnClick(R.id.test1)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.test1 -> {
                startActivity(Intent(mContext, MainActivity::class.java))
                /* APIManager.startRequest(userService.login(),object : RequestListener<Any>{
                     override fun onStart() {

                     }

                     override fun onSuccess(result: Any?) {
                         super.onSuccess(result)
                         startActivity(Intent(mContext, MainActivity::class.java))
                     }

                     override fun onError(e: Throwable?) {
                        ToastTool.showToast(e!!.message.toString())
                     }

                     override fun onComplete() {

                     }


                 })*/
            }
        }
    }


    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        imInterface = IMTencentImpl

        // 获取userSig函数
        val userSig: String =
            GenerateTestUserSig.genTestUserSig("aaa")
        test1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {


                imInterface!!.login("aaa", userSig, object : LoginCallBack {
                    override fun loginSuccess() {
                        Log.d("pangtao", "login success")
                        //创建直播群
                        *//* imInterface.createGroup("AVChatRoom",object : IMValueCallBack<String>{
                             override fun success(value: String) {
                                 Log.d("pangtao","createGroup success groupId = " + value)




                             }

                             override fun error(code: Int, desc: String) {
                                 Log.d("pangtao","createGroup error = " + desc)
                             }


                         })*//*
                        var value = "202006473563"
                        imInterface.joinGroup(value, object : IMValueCallBack<String> {
                            override fun success(xxx: String) {
                                Log.d("pangtao", "join group success id = " + value)
                                var intent = Intent(context, IMGroupTestActivity::class.java);
                                intent.putExtra("groupId", value)
                                startActivity(intent)
                            }

                            override fun error(code: Int, desc: String) {
                                Log.d("pangtao", "join group error desc = " + desc)
                            }

                        })

                    }

                    override fun loginError(code: Int, desc: String) {
                        Log.d("pangtao", "login error desc = " + desc)
                    }

                });
            }


        })

    }*/

    override fun requestData() {

    }
}
