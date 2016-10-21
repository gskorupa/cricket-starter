/*
 * Copyright 2016 Grzegorz Skorupa <g.skorupa at gmail.com>.
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
var offline = true;
var myData = {"todos": []};
var resourcesUrl = "http://localhost:8080/api/todos";
        
// get data from the service
function getData(elementId, statusId) {
    w3Http(resourcesUrl, function () {
        if (this.readyState === 4 && this.status === 200) {
            myData = JSON.parse(this.responseText);
            offline = false;
        } else if (this.readyState === 4 && this.status === 0) {
            //service not available
            offline = true;
        }
        updatePage(myData, elementId, statusId, offline);
    });
}

// update application view
function updatePage(data, elementId, statusId, offlineStatus) {
    // elementId on the page shold be hidden when are no data available
    if (data.todos.length > 0) {
        w3DisplayData(elementId, data);
        document.getElementById(elementId).style.display = "";
    } else {
        document.getElementById(elementId).style.display = "none";
    }
    // display online/offline indicator
    if (offlineStatus) {
        document.getElementById(statusId).innerHTML = "offline";
    } else {
        document.getElementById(statusId).innerHTML = "online";
    }
}

// submit application form data to the service
function AJAXSubmit(oFormElement, elementId, statusId, getdataCallback) {
    if (offline) {
        submitLocally(oFormElement);
        getdataCallback(elementId, statusId);
        return;
    }
    /*
    if (!oFormElement.action) {
        return;
    }*/
    var oReq = new XMLHttpRequest();
    oReq.onerror = function (oEvent) { };
    oReq.onload = function (oEvent) {/*getdataCallback(elementId, statusId);*/};
    oReq.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 201) {
            getdataCallback(elementId, statusId);
        } else if (this.readyState == 4 && this.status == 0) {
        }
    };
    if (oFormElement.method.toLowerCase() === "post") {
        //oFormElement.action = resourcesUrl;
        //oReq.open("post", oFormElement.action);
        oReq.open("post", resourcesUrl);
        oReq.send(new FormData(oFormElement));
    } else {
        var oField, sFieldType, nFile, sSearch = "";
        for (var nItem = 0; nItem < oFormElement.elements.length; nItem++) {
            oField = oFormElement.elements[nItem];
            if (!oField.hasAttribute("name")) {
                continue;
            }
            sFieldType = oField.nodeName.toUpperCase() === "INPUT" ? oField.getAttribute("type").toUpperCase() : "TEXT";
            if (sFieldType === "FILE") {
                for (nFile = 0; nFile < oField.files.length; sSearch += "&" + escape(oField.name) + "=" + escape(oField.files[nFile++].name))
                    ;
            } else if ((sFieldType !== "RADIO" && sFieldType !== "CHECKBOX") || oField.checked) {
                sSearch += "&" + escape(oField.name) + "=" + escape(oField.value);
            }
        }
        //oReq.open("get", oFormElement.action.replace(/(?:\?.*)?$/, sSearch.replace(/^&/, "?")), true);
        oReq.open("get", resourceUrl.replace(/(?:\?.*)?$/, sSearch.replace(/^&/, "?")), true);
        oReq.send(null);
    }
    oFormElement.reset();
    return false;
}

// store application form data locally (if the service is not available)
function submitLocally(oFormElement) {
    var newToDo = {
        "name": oFormElement.elements["name"].value,
        "description": oFormElement.elements["description"].value
    };
    myData.todos.push(newToDo);
}

// not used, example
var exampleData = {"todos": [
        {"name": "my task 1", "description": "description 1"},
        {"name": "my task 2", "description": "description 2"}
    ]};