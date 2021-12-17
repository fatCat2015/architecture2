package com.eju.tools

import androidx.lifecycle.*

/**
 * 未读消息除了本身未读数量之外,还添加了一个forceNotify属性
 * forceNotify true:强提醒,需要展示未读消息数量;false:弱提箱,只需使用红点表示存在未读消息
 */
internal data class MessageCount(
    var count:Int, //消息数量
    var forceNotify:Boolean
)

/**
 * 用于未读消息的聚合
 * 分三种情况
 * 1. hasUnreadMessage == false,不需要展示未读消息
 * 2. forceNotify == true,需要展示未读消息数字forceNotifyCount
 * 3. forceNotify == false,只需使用红点表示存在未读消息
 */
data class UnreadMessage(
    var count:Int, //未读消息总数量
    var forceNotifyCount:Int //强提醒的未读消息数量
){
    val hasUnreadMessage :Boolean get() = count > 0  //是否有未读消息
    val forceNotify :Boolean get() = forceNotifyCount > 0 //是否强提醒未读消息
}

class UnreadMessageHandler{

    /**
     * key:未读消息类型
     * value:未读消息数量
     */
    private val messageCountMap :HashMap<Int,MessageCount> by lazy {
        hashMapOf()
    }

    /**
     * key:未读消息的聚合类型
     * value:未读消息的聚合类型对应的未读消息类型列表
     */
    private val unreadMessageMap :HashMap<Int,List<Int>> by lazy {
        hashMapOf()
    }

    /**
     * key:未读消息的聚合类型
     * value:未读消息的聚合类型对应的可观察的LiveData
     */
    private val unreadMessageLiveDataMap :HashMap<Int,MutableLiveData<UnreadMessage>> by lazy {
        hashMapOf()
    }


    @Synchronized
    fun addUnreadMessageCondition(messageType:Int,types:List<Int>){
        unreadMessageMap[messageType] = types
    }

    @Synchronized
    fun observe(messageType:Int,lifecycleOwner: LifecycleOwner,observer: Observer<UnreadMessage>){
        val liveData = unreadMessageLiveDataMap[messageType]?:MutableLiveData<UnreadMessage>().also{
            unreadMessageLiveDataMap[messageType] = it
        }
        liveData.observe(lifecycleOwner,observer)
    }

    /**
     * 更新type消息类型对应的未读消息数量
     */
    @Synchronized
    fun updateMessageCount(type:Int,count:Int,forceNotify:Boolean = true){
        messageCountMap[type]?.let {
            it.count = count
            it.forceNotify = forceNotify
        }?:MessageCount(count,forceNotify).also { messageCountMap[type] = it }
        updateUnreadMessage(type)
    }

    private fun updateUnreadMessage(type:Int){
        unreadMessageMap.filterValues { it.contains(type) }.forEach {
            val messageType = it.key
            val types = it.value
            val messageCounts = messageCountMap.filterKeys { types.contains(it) }.values
            val unreadMessage = UnreadMessage(
                messageCounts.fold(0){ count,item->
                    count+item.count
                },
                messageCounts.fold(0) { count, item ->
                    count + (if (item.forceNotify) {
                        item.count
                    } else {
                        0
                    })
                }
            )
            dispatchNotify(messageType,unreadMessage)
        }
    }

    private fun dispatchNotify(messageType:Int,unreadMessage:UnreadMessage){
        unreadMessageLiveDataMap[messageType]?.postValue(unreadMessage)
    }

}

internal object UseDemo{
    //step1.定义消息类型
    //单聊未读消息
    const val unread_c2c_msg_count = 0
    //群聊未读消息
    const val unread_group_msg_count = 1
    //客服未读消息
    const val unread_service_msg_count = 3

    //step2.定义消息聚合类型
    //页面某个地方需要展示未读的im单聊消息数量
    const val a = 0
    //页面某个地方需要展示未读的im消息数量
    const val b = 1
    //页面某个地方需要展示未读的im消息和客服消息数量
    const val c = 2


    private val unreadMessageHandler = UnreadMessageHandler()

    init {
        //step3.关联消息聚合的消息类型
        unreadMessageHandler.addUnreadMessageCondition(a,listOf(unread_c2c_msg_count))

        unreadMessageHandler.addUnreadMessageCondition(b,listOf(unread_c2c_msg_count,unread_group_msg_count))

        unreadMessageHandler.addUnreadMessageCondition(c,listOf(unread_c2c_msg_count,unread_group_msg_count,unread_service_msg_count))

    }

    //step4.需要展示未读消息的地方添加监听,messageType是消息聚合类型
    fun observe(messageType:Int,lifecycleOwner: LifecycleOwner,observer: Observer<UnreadMessage>){
        unreadMessageHandler.observe(messageType,lifecycleOwner,observer)
    }

    //step5.在页面的详情生命周期或者收到消息通知后,查询消息类型type对应的未读数量之后调用
    fun updateMessageCount(type:Int,count:Int,forceNotify:Boolean = true){
        unreadMessageHandler.updateMessageCount(type,count,forceNotify)
    }





}
