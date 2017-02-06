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

// get data from the service
function getData() {
    if (app.offline) {
        globalEvents.trigger("data:ready");
        return;
    }
    var oReq = new XMLHttpRequest();
    oReq.onerror = function (oEvent) { 
        if(app.debug) { console.log(oEvent.toString()) };
    };
    oReq.onload = function (oEvent) {/*getdataCallback(elementId, statusId);*/
        if(app.debug) { console.log(oEvent.toString()) };
    };
    oReq.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            app.myData = JSON.parse(this.responseText);
            app.offline = false;
            globalEvents.trigger("data:ready");
        } else if (this.readyState == 4 && this.status == 0) {
            app.offline = true;
            globalEvents.trigger("data:get-error");
        }
    };
    //hValue = 'Basic ';
    //hValue += key;
    //alert('sending with header '+ hValue);
    oReq.open("get", app.resourcesUrl, true);
    //oReq.withCredentials = true;
    //oReq.setRequestHeader("Authentication", hValue);
    oReq.send(null);
    return false;
}

function postFormData(oFormElement, url, successEventName) {
    if(app.debug) { console.log("postFormData") };
    var oReq = new XMLHttpRequest();
    oReq.onerror = function (oEvent) { 
        if(app.debug) { console.log(oEvent.toString()) };
    };
    oReq.onload = function (oEvent) { 
        if(app.debug) { console.log(oEvent.toString()) };
    };
    oReq.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 201) {
            globalEvents.trigger(successEventName);
        } else {
            if(app.debug) { console.log("state "+this.readyState+" status: "+this.status) };
        }
    };
    // method declared in the form is ignored
    oReq.open("post", url);
    oReq.send(new FormData(oFormElement));
    return false;
}

// submit application form data to the service
// not used
function AXAJSubmit(oFormElement, getdataCallback) {
    if (app.offline) {
        submitLocally(oFormElement);
        riot.update();
        return;
    }
    /*
     if (!oFormElement.action) {
     return;
     }*/
    var oReq = new XMLHttpRequest();
    oReq.onerror = function (oEvent) { };
    oReq.onload = function (oEvent) {/*getdataCallback(elementId, statusId);*/
    };
    oReq.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 201) {
            getdataCallback();
        } else if (this.readyState == 4 && this.status == 0) {
        }
    };
    if (oFormElement.method.toLowerCase() === "post") {

        oReq.open("post", app.resourcesUrl);
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
        oReq.open("get", app.resourcesUrl.replace(/(?:\?.*)?$/, sSearch.replace(/^&/, "?")), true);
        oReq.send(null);
    }
    oFormElement.reset();
    return false;
}

