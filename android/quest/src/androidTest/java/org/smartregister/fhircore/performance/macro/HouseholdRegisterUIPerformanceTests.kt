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

package org.smartregister.fhircore.performance.macro

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.benchmark.junit4.BenchmarkRule
import androidx.compose.material.ExperimentalMaterialApi
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.smartregister.fhircore.engine.data.local.register.RegisterRepository
import org.smartregister.fhircore.quest.ui.main.AppMainActivity

@HiltAndroidTest
class HouseholdRegisterUIPerformanceTests : BaseRegisterPerformanceTest() {

  @get:Rule(order = 1) val benchmarkRule = BenchmarkRule()

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @Inject lateinit var registerRepository: RegisterRepository

  @Before
  fun setUp() {
    hiltRule.inject()

    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

    // Initialize WorkManager for instrumentation tests.
    WorkManagerTestInitHelper.initializeTestWorkManager(context, config)

    beforeTestSetup(registerRepository, benchmarkRule, "householdRegister")
  }

  @OptIn(ExperimentalMaterialApi::class)
  @Test
  fun benchmarkPage0() {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val intent = Intent(context, AppMainActivity::class.java)
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
  }
}
