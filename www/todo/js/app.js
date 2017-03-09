/*
 * Copyright 2017 Grzegorz Skorupa <g.skorupa at gmail.com>.
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

var globalEvents = riot.observable();

var app = {
    "myData": {"todos": []},
    "offline": false,
    "resourcesUrl": "http://localhost:8080/api/todos",
    "currentPage": "main",
    "language": "en",
    "languages": ["en","pl"],
    "debug": false,
    "localUid": 0
}