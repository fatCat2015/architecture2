package com.eju.startup

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import java.lang.IllegalStateException

/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ /**
 * The [ContentProvider] which discovers [Initializer]s in an application and
 * initializes them before [Application.onCreate].
 */
@SuppressLint("StaticFieldLeak")
class InitializationProvider : ContentProvider() {
    companion object{
        lateinit var sContext:Context
            private set
    }

    override fun onCreate(): Boolean {
        val context = context
        if (context != null) {
            sContext = context
            AppInitializer.discoverAndInitialize()
        } else {
            throw StartupException("Context cannot be null")
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        throw IllegalStateException("Not allowed.")
    }

    override fun getType(uri: Uri): String? {
        throw IllegalStateException("Not allowed.")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw IllegalStateException("Not allowed.")
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw IllegalStateException("Not allowed.")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw IllegalStateException("Not allowed.")
    }
}