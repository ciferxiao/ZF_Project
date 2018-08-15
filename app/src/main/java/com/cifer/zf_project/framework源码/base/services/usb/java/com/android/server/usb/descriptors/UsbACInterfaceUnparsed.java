/*
 * Copyright (C) 2017 The Android Open Source Project
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
 */
package com.cifer.zf_project.framework源码.base.services.usb.java.com.android.server.usb.descriptors;

/**
 * @hide
 * A holder class for as yet unparsed audio-class interfaces.
 */
public final class UsbACInterfaceUnparsed extends UsbACInterface {
    private static final String TAG = "UsbACInterfaceUnparsed";

    public UsbACInterfaceUnparsed(int length, byte type, byte subtype, byte subClass) {
        super(length, type, subtype, subClass);
    }
}
