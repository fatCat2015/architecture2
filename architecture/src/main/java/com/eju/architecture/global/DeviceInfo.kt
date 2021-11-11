/*
 * Copyright (c) 2021. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eju.architecture.global

import android.os.Build

/**
 * @author Dylan Cai
 */

/**
 * 设备android版本
 */
val sdkVersionName: String get() = Build.VERSION.RELEASE

/**
 * 设备android版本对应的code
 */
val sdkVersionCode: Int get() = Build.VERSION.SDK_INT

/**
 * 设备名称(Xiaomi)
 */
val deviceManufacturer: String get() = Build.MANUFACTURER

/**
 * 设备型号(Mi 10 Pro)
 */
val deviceModel: String get() = Build.MODEL


val screenWidth = application.resources.displayMetrics.widthPixels

val screenHeight = application.resources.displayMetrics.heightPixels
