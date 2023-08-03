/*
 * Copyright 2021-2023 Ona Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.smartregister.opensrp.quest.macrobenchmark.macro

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HouseholdRegisterUIPerformanceTests  {

  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()


  @SuppressLint("RestrictedApi")
  @Before
  fun setUp() {
    /*
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

    // Initialize WorkManager for instrumentation tests.
    WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    */
  }

  @Test
  fun benchmarkPage0() {
    benchmarkRule.measureRepeated(
            packageName = "org.smartregister.opensrp",
            metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
            iterations = 5,
            startupMode = StartupMode.COLD
    ) {
      pressHome()
      val intent = Intent()
              .setComponent(ComponentName("org.smartregister.opensrp", "org.smartregister.fhircore.quest.ui.main.TestAppMainActivity"))
      startActivityAndWait(intent)
    }
  }
}
