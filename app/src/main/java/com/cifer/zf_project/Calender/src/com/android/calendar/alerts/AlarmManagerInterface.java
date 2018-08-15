/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.cifer.zf_project.Calender.src.com.android.calendar.alerts;

import android.app.PendingIntent;

/**
 * AlarmManager abstracted to an interface for testability.
 */
public interface AlarmManagerInterface {
    public void set(int type, long triggerAtMillis, PendingIntent operation);
}
