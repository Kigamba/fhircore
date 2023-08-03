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

package org.smartregister.fhircore.quest.ui.main

import android.os.Bundle
import androidx.compose.material.ExperimentalMaterialApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import org.smartregister.fhircore.engine.data.local.register.RegisterRepository
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalMaterialApi
open class TestAppMainActivity : AppMainActivity() {

  @Inject lateinit var registerRepository: RegisterRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    inject()
    runBlocking {
      registerRepository.configurationRegistry.loadConfigurations(
              "app/debug",
              this@TestAppMainActivity,
      )
    }

    super.onCreate(savedInstanceState)
  }
}
