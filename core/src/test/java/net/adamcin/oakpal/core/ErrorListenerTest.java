/*
 * Copyright 2019 Mark Adamcin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.adamcin.oakpal.core;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

public class ErrorListenerTest {

    @Test
    public void testDefaults() throws Exception {
        ErrorListener mock = new ErrorListener() {
            @Override
            public Collection<Violation> getReportedViolations() {
                return Collections.emptyList();
            }
        };

        mock.startedScan();
        mock.onForcedRootCreationError(null, null);
        mock.onImporterException(null, null, null);
        mock.onJcrNamespaceRegistrationError(null, null, null);
        mock.onJcrPrivilegeRegistrationError(null, null);
        mock.onListenerException(null, null, null);
        mock.onListenerPathException(null, null, null, null);
        mock.onNodeTypeRegistrationError(null, null);
        mock.onSubpackageException(null, null);
        mock.onInstallHookError(null, null);
        mock.onProhibitedInstallHookRegistration(null);
        mock.finishedScan();
    }
}
