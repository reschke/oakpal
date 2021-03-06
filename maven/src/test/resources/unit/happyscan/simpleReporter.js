/*
 * Copyright 2018 Mark Adamcin
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

function importedPath(packageId, path, node) {
    oakpal.minorViolation(path + " [" + node.getPrimaryNodeType().getName() + "]", packageId);
}

function startedScan() {
    if (config) {
        print("foo: " + config.foo);
        if (config.bars) {
            print("bars[0]: " + config.bars[0]);
            print("bars[2]: " + config.bars[3]);
        }
        if (config.someMap) {
            print("someMap[cars]", config.someMap["cars"]);
            for (var car in config.someMap["cars"]) {
                if (config.someMap["cars"][car]) {
                    print("someMap[cars]["+car+"] yes");
                } else {
                    print("someMap[cars]["+car+"] no");
                }
            }
            print("someMap[bats]", config.someMap["bats"]);
        }
    }
}

