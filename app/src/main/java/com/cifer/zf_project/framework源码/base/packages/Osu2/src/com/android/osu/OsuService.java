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

package com.cifer.zf_project.framework源码.base.packages.Osu2.src.com.android.osu;

/**
 * Abstraction for services that can be performed by the OSU app, such as provisioning,
 * subscription remediation, and etc.
 */
public interface OsuService {
    /**
     * Start the service.
     */
    public void start();

    /**
     * Stop the service.
     */
    public void stop();
}
