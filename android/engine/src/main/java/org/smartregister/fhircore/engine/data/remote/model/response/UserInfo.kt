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

package org.smartregister.fhircore.engine.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
  @SerialName("questionnaire_publisher") var questionnairePublisher: String? = null,
  @SerialName("organization") var organization: String? = null,
  @SerialName("location") var location: String? = null,
  @SerialName("family_name") var familyName: String? = null,
  @SerialName("given_name") var givenName: String? = null,
  @SerialName("name") var name: String? = null,
  @SerialName("preferred_username") var preferredUsername: String? = null,
  @SerialName("sub") var keycloakUuid: String? = null,
  @SerialName("fhir_core_app_id") var appId: String? = null,
)
