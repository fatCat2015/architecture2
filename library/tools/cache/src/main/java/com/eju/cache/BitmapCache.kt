package com.eju.cache

import android.graphics.Bitmap
import com.eju.cache.core.MemoryCache

class BitmapCache<K>(maxSize:Int): MemoryCache<K, Bitmap>(
    objSizeCalculator={_,value ->(value.allocationByteCount)},
    maxSize=maxSize,
    onEntryRemoved = {evicted,_,oldValue,_->
        if(evicted){
            oldValue?.recycle()
        }
    }
)