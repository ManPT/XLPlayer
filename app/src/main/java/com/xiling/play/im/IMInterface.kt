package com.xiling.play.im

interface IMInterface {
    fun init()
fun login(userId:String,userSig:String,callback:LoginCallBack)
fun logout(callback:LoginCallBack)
fun sendMessage(message:String,id:String,callback:IMValueCallBack<String>)
fun createGroup(type:String,callback:IMValueCallBack<String>)
fun joinGroup(groupId:String,callback:IMValueCallBack<String>)
//退出群
fun quitGroup(groupId:String,callback:IMValueCallBack<String>)
//解散群
fun dismissGroup(groupId:String,callback:IMValueCallBack<String>)
}