package com.eju.demomodule.repository

import com.eju.architecture.core.BaseRepository
import com.eju.demomodule.entity.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactRepository @Inject constructor():BaseRepository() {

    suspend fun getContactList():List<Contact>{
        return withContext(Dispatchers.IO){
            listOf(
                Contact("sck","15801958364"),
                Contact("阿凡达","15801958364"),
                Contact("1245","15801958364"),
                Contact("李大江","15801958364"),
                Contact("#@","15801958364"),
                Contact("慕容复","15801958364"),
                Contact("八酒杯","15801958364"),
                Contact("大馒头","15801958364"),
                Contact("安达曼","15801958364"),
                Contact("达达","15801958364"),
                Contact("八宝粥","15801958364"),
                Contact("李飞刀","15801958364"),
                Contact("大疆无人机","15801958364"),
                Contact("把酒言欢","15801958364"),
                Contact("邵大江","15801958364"),
                Contact("撒花","15801958364"),
                Contact("dc","15801958364"),
                Contact("dd","15801958364"),
                Contact("da","15801958364"),
                Contact("乐居android-Bo-Tree","15801958364"),
                Contact("杨黎明","15801958364"),
                Contact("易居 施刘梅","15801958364"),
                Contact("易居-张晨","15801958364"),
                Contact("易居佳芳","15801958364"),
                Contact("1515","15801958364"),
                Contact("175674","15801958364"),
                Contact("1dadawd","15801958364"),
                Contact("18674t","15801958364"),
                Contact("1生的伟大","15801958364"),
                Contact("113414235","15801958364"),
                Contact("1454525","15801958364"),
                Contact("16565","15801958364"),
                Contact("156456","15801958364"),
                Contact("198989","15801958364"),
            )
        }
    }
}