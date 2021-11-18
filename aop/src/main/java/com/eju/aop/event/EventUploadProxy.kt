package com.eju.aop.event

import org.json.JSONObject

object EventUploadProxy : IEventUpload {

    private var eventUpload: IEventUpload? = null

    fun initEventUpload(eventUpload: IEventUpload) {
        this.eventUpload = eventUpload
    }

    override fun uploadEvent(evetnName:String,params:JSONObject) {
        eventUpload?.uploadEvent(evetnName, params)
    }
}