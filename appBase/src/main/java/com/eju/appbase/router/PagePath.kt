package com.eju.appbase.router

import android.net.Uri
import com.eju.tools.cacheDirPath
import com.eju.tools.print
import timber.log.Timber
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
                    property.findAnnotations(PathName::class)?.firstOrNull()?.let { pathName->
                        println("${pathName.name} = ${property.call()}")
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


    object Start {
        @PathName("启动页")
        const val Splash="/Start/Splash"
        @PathName("引导页")
        const val Guide="/Start/Guide"
        @PathName("登录页")
        const val Login="/Start/Login"
    }

    object Main{
        @PathName("首页")
        const val Home="/Main/Home"
    }

    object DemoModule{
        @PathName("测试页面")
        const val Demo="/DemoModule/Demo"
        @PathName("测试大图页面")
        const val ImageDetail="/DemoModule/ImageDetial"
    }

}

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD, ElementType.METHOD)
annotation class PathName( val name:String)


