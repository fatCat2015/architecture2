package com.eju.start.api.bean

import java.io.Serializable

data class User(
    var userId: String = "",
    var userToken: String = "",
    var address: String = "",
    var avatar: String = "",
    var category: Int = 0,
    var category_desc: String = "",
    var comments: Int = 0,
    var create_time: String = "",
    var feature: String = "",
    var gender: String = "",
    var is_recommend: Int = 0,
    var kind: Int = 0,
    var kind_desc: String = "",
    var kind_name: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var mobile: String = "",
    var modify_time: String = "",
    var pickup_time: String = "",
    var pics: String = "",
    var reason: String = "",
    var rescue_id: Int = 0,
    var share_counts: Int = 0,
    var title: String = "",
    var user_id: Int = 0,
    var user_name: String = "",
    var view_counts: Int = 0,
    var wx: String = ""
):Serializable{

}