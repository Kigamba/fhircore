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

package org.smartregister.fhircore.quest.ui.profile

import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.fhir.logicalId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.ResourceType
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.smartregister.fhircore.engine.configuration.ConfigurationRegistry
import org.smartregister.fhircore.engine.configuration.profile.ManagingEntityConfig
import org.smartregister.fhircore.engine.data.local.register.RegisterRepository
import org.smartregister.fhircore.engine.domain.model.RepositoryResourceData
import org.smartregister.fhircore.engine.domain.model.ResourceData
import org.smartregister.fhircore.engine.rulesengine.ResourceDataRulesExecutor
import org.smartregister.fhircore.engine.util.DefaultDispatcherProvider
import org.smartregister.fhircore.engine.util.fhirpath.FhirPathDataExtractor
import org.smartregister.fhircore.quest.app.fakes.Faker
import org.smartregister.fhircore.quest.robolectric.RobolectricTest
import org.smartregister.fhircore.quest.ui.profile.model.EligibleManagingEntity

@HiltAndroidTest
class ProfileViewModelTest : RobolectricTest() {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @Inject lateinit var fhirPathDataExtractor: FhirPathDataExtractor

  @Inject lateinit var resourceDataRulesExecutor: ResourceDataRulesExecutor
  private val configurationRegistry: ConfigurationRegistry = Faker.buildTestConfigurationRegistry()
  private lateinit var profileViewModel: ProfileViewModel
  private lateinit var resourceData: ResourceData
  private lateinit var expectedBaseResource: Patient
  private lateinit var registerRepository: RegisterRepository

  @Before
  @kotlinx.coroutines.ExperimentalCoroutinesApi
  fun setUp() {
    hiltRule.inject()
    expectedBaseResource = Faker.buildPatient()
    resourceData =
      ResourceData(
        baseResourceId = expectedBaseResource.logicalId,
        baseResourceType = expectedBaseResource.resourceType,
        computedValuesMap = emptyMap(),
      )
    registerRepository =
      spyk(
        RegisterRepository(
          fhirEngine = mockk(),
          dispatcherProvider = DefaultDispatcherProvider(),
          sharedPreferencesHelper = mockk(),
          configurationRegistry = configurationRegistry,
          configService = mockk(),
          configRulesExecutor = mockk(),
        ),
      )
    coEvery { registerRepository.loadProfileData(any(), any(), paramsList = emptyArray()) } returns
      RepositoryResourceData(resource = Faker.buildPatient())

    runBlocking {
      configurationRegistry.loadConfigurations(
        context = InstrumentationRegistry.getInstrumentation().targetContext,
        appId = APP_DEBUG,
      ) {}
    }

    profileViewModel =
      ProfileViewModel(
        registerRepository = registerRepository,
        configurationRegistry = configurationRegistry,
        dispatcherProvider = coroutineTestRule.testDispatcherProvider,
        fhirPathDataExtractor = fhirPathDataExtractor,
        resourceDataRulesExecutor = resourceDataRulesExecutor,
      )
  }

  @Test
  @kotlinx.coroutines.ExperimentalCoroutinesApi
  fun testRetrieveProfileUiState() {
    runBlocking {
      profileViewModel.retrieveProfileUiState(
        "householdProfile",
        "sampleId",
        paramsList = emptyArray(),
      )
    }

    assertNotNull(profileViewModel.profileUiState.value)
    val theResourceData = profileViewModel.profileUiState.value.resourceData
    assertNotNull(theResourceData)
    assertEquals(expectedBaseResource.logicalId, theResourceData.baseResourceId)
    assertEquals(expectedBaseResource.resourceType, theResourceData.baseResourceType)

    val profileConfiguration = profileViewModel.profileUiState.value.profileConfiguration
    assertEquals("app", profileConfiguration?.appId)
    assertEquals("profile", profileConfiguration?.configType)
    assertEquals("householdProfile", profileConfiguration?.id)
  }

  @Test
  fun testProfileEventOnChangeManagingEntity() {
    profileViewModel.onEvent(
      ProfileEvent.OnChangeManagingEntity(
        ApplicationProvider.getApplicationContext(),
        eligibleManagingEntity =
          EligibleManagingEntity("groupId", "newId", memberInfo = "James Doe"),
        managingEntityConfig =
          ManagingEntityConfig(
            eligibilityCriteriaFhirPathExpression = "Patient.active",
            resourceType = ResourceType.Patient,
            nameFhirPathExpression = "Patient.name.given",
          ),
      ),
    )
    coVerify { registerRepository.changeManagingEntity(any(), any(), any()) }
  }
}
