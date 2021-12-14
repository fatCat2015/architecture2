package com.eju.appbase.router

import android.net.Uri
import com.eju.tools.cacheDirPath
import com.eju.tools.print
import java.io.File
import java.lang.annotation.*
import java.lang.annotation.Retention
import java.lang.annotation.Target
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.memberProperties

object PagePath {

    @OptIn(ExperimentalStdlibApi::class)
    fun exportPath(){
        File(cacheDirPath,"androidPagePath.txt").print {
            PagePath::class.nestedClasses.forEach { clz->
                clz.memberProperties?.forEach { property->
                    property.findAnnotations(PathDescription::class)?.firstOrNull()?.let { path->
                        if(!path.isFragment){
                            println("${path.name} = ${property.call()}")
                        }
                    }
                }
            }
        }

    }

    fun createPathUri(path:String):Uri{
        return Uri.parse("eju://mobile.app.yilou${path}")
    }

    fun createPathUri1(path:String):Uri{
        return Uri.parse("http://www.jiandanhome.com${path}")
    }



    object Other{
        @PathDescription("降级页面")
        const val NotFound="/Other/NotFound"
    }


    object Start {
        @PathDescription("启动页")
        const val Splash="/Start/Splash"
        @PathDescription("引导页")
        const val Guide="/Start/Guide"
        @PathDescription("登录页")
        const val Login="/Start/Login"
    }

    object Main{
        @PathDescription("首页")
        const val Home="/Main/Home"
    }

    object DemoModule{
        @PathDescription("测试页面")
        const val Demo="/DemoModule/Demo"
        @PathDescription("测试大图页面")
        const val ImageDetail="/DemoModule/ImageDetial"
        @PathDescription("Web页面展示")
        const val WebPage="/DemoModule/WebPage"
        @PathDescription("arouter使用")
        const val ARouterDemo="/DemoModule/ARouterDemo"
        @PathDescription("room使用")
        const val RoomDemo="/DemoModule/RoomDemo"

        @PathDescription("demo例子",isFragment = true)
        const val DemoFragment="/DemoModule/DemoFragment"
        @PathDescription("coil的使用",isFragment = true)
        const val CoilFragment="/DemoModule/CoilFragment"
        @PathDescription("分页展示",isFragment = true)
        const val UserList="/DemoModule/UserList"
        @PathDescription("自定义分页逻辑",isFragment = true)
        const val MomentList="/DemoModule/MomentList"


    }

}

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD, ElementType.METHOD)
annotation class PathDescription(val name:String, val isFragment:Boolean = false)




