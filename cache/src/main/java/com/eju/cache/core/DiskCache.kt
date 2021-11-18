package com.eju.cache.core

import android.content.Context
import com.eju.cache.md5String
import com.jakewharton.disklrucache.DiskLruCache
import java.io.*

@Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
class DiskCache<T:Serializable>(private val context: Context,
                                private val max_size :Long =5*1024*1024L,
                                private val appVersion:Int=1):
    Cache<String, T> {


    private val diskLruCache:DiskLruCache by lazy {
        val cacheDir=File(context.cacheDir,"lruCache")
        if(!cacheDir.exists()){
            cacheDir.mkdirs()
        }
        DiskLruCache.open(cacheDir,appVersion,1, max_size)
    }


    override fun size(): Long {
        return diskLruCache.size()
    }

    override fun maxSize(): Long {
        return diskLruCache.maxSize
    }

    override fun remove(key: String) {
        runCatching {
            diskLruCache.remove(md5String(key))
        }
    }

    override fun put(key: String, value: T) {
        val editor=getEditor(key)
        editor?.let { editor->
            runCatching {
                ObjectOutputStream(editor.newOutputStream(0)).use {objStream->
                    objStream.writeObject(value)
                }
                editor.commit()
            }.onFailure {
                editor.abort()
            }
        }
    }

    private fun getEditor(key:String):DiskLruCache.Editor?{
        val md5Key= md5String(key)
        return md5Key?.let {md5Key->
            try {
                diskLruCache.edit(md5Key)
            }catch (e: IOException){
                val nothing = null
                nothing
            }
        }
    }


    override fun get(key: String): T? {
        val snapshot=getSnapShort(key)
        return snapshot?.let { snapshot->
            runCatching {
                ObjectInputStream(snapshot.getInputStream(0)).use {objStream->
                    objStream.readObject() as? T?
                }
            }.getOrNull()
        }
    }


    private fun getSnapShort(key:String):DiskLruCache.Snapshot?{
        val md5Key= md5String(key)
        return md5Key?.let {md5Key->
            try {
                diskLruCache.get(md5Key)
            }catch (e: IOException){
                null
            }
        }
    }


    override fun clear() {
        runCatching {
            diskLruCache.delete()
        }
    }


}