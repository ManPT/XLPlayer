package com.xiling.play.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tencent.imsdk.v2.*
import com.xiling.play.adapter.ChatListAdapter
import com.xiling.play.im.IMInterface
import com.xiling.play.im.IMTencentImpl
import com.xiling.play.im.IMValueCallBack
import com.xiling.play.R
import kotlinx.android.synthetic.main.activity_i_m_group_test.*

class IMGroupTestActivity : AppCompatActivity() {
    lateinit var groupId: String
    lateinit var imInterface: IMInterface;

    var chatList = ArrayList<String>()
    lateinit var chatAdapter: ChatListAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_i_m_group_test)
        imInterface = IMTencentImpl
        if (intent != null) {
            groupId = intent.getStringExtra("groupId")
        }
        chatAdapter = ChatListAdapter()
        chatAdapter.setNewData(chatList)
        chat_recycler.layoutManager  = LinearLayoutManager(this)
        chat_recycler.adapter = chatAdapter

        send_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                var message = chat_message_input.text.toString().trim()
                if (!TextUtils.isEmpty(message)) {
                    imInterface.sendMessage(message, groupId, object : IMValueCallBack<String> {
                        override fun success(value: String) {
                            Log.d("pangtao", "send success value = " + value)
                            chat_message_input.setText("")

                            chatList.add(message)
                            chatAdapter.notifyDataSetChanged()
                            chat_recycler.scrollToPosition(chatList.size - 1)
                        }

                        override fun error(code: Int, desc: String) {
                            Log.d("pangtao", "send error desc = " + desc)
                        }

                    })
                }
            }

        })

        V2TIMManager.getGroupManager().getGroupMemberList(
            groupId,
            V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_FILTER_ALL,
            0,
            object : V2TIMValueCallback<V2TIMGroupMemberInfoResult> {
                override fun onSuccess(p0: V2TIMGroupMemberInfoResult?) {
                    Log.d("pangtao", "getGroupMemberList =" + p0!!.memberInfoList.size + "")
                    Log.d("pangtao", "getGroupMemberList =" + p0!!.memberInfoList.get(0).nickName)
                }

                override fun onError(p0: Int, p1: String?) {
                    Log.d("pangtao", "getGroupMemberList onError" + p1)
                }

            })

        V2TIMManager.getInstance().addSimpleMsgListener(object : V2TIMSimpleMsgListener() {
            override fun onRecvGroupTextMessage(
                msgID: String?,
                groupID: String?,
                sender: V2TIMGroupMemberInfo?,
                text: String?
            ) {
                super.onRecvGroupTextMessage(msgID, groupID, sender, text)
                Log.d("pangtao", "onRecvGroupTextMessage  text = " + text)


            }

            override fun onRecvC2CTextMessage(
                msgID: String?,
                sender: V2TIMUserInfo?,
                text: String?
            ) {
                super.onRecvC2CTextMessage(msgID, sender, text)
                Log.d("pangtao", "onRecvC2CTextMessage  text = " + text)
            }

            override fun onRecvC2CCustomMessage(
                msgID: String?,
                sender: V2TIMUserInfo?,
                customData: ByteArray?
            ) {
                super.onRecvC2CCustomMessage(msgID, sender, customData)
                Log.d("pangtao", "onRecvC2CCustomMessage  text = ")
            }

            override fun onRecvGroupCustomMessage(
                msgID: String?,
                groupID: String?,
                sender: V2TIMGroupMemberInfo?,
                customData: ByteArray?
            ) {
                super.onRecvGroupCustomMessage(msgID, groupID, sender, customData)
                Log.d("pangtao", "onRecvGroupCustomMessage  text = ")
            }

        })


    }

    override fun onDestroy() {
        super.onDestroy()
        imInterface.quitGroup(groupId,object:IMValueCallBack<String>{
            override fun success(value: String) {
                TODO("Not yet implemented")
            }

            override fun error(code: Int, desc: String) {
                TODO("Not yet implemented")
            }

        })
    }

}
