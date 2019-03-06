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

package net.adamcin.oakpal.interactive;

import java.util.List;

import net.adamcin.oakpal.core.Checklist;

/**
 * Service interface for retrieving bundle-provided checklists.
 */
public interface ChecklistTracker {

    /**
     * Get a list of oakpal checklists provided by active bundles.
     *
     * @return a list of oakpal checklists provided by active bundles
     */
    List<Checklist> getBundleChecklists();
}
