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
function getData(url, myData, eventBus, successEventName, errorEventName, debug) {
    //if (app.offline) {
    //    eventBus.trigger(successEventName);
    //    return;
    //}
    var oReq = new XMLHttpRequest();
    oReq.onerror = function (oEvent) { 
        if(debug) { console.log(oEvent.toString()) };
    };
    oReq.onload = function (oEvent) {/*getdataCallback(elementId, statusId);*/
        if(debug) { console.log(oEvent.toString()) };
    };
    oReq.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            app.myData = JSON.parse(this.responseText);
            eventBus.trigger(successEventName);
        } else if (this.readyState == 4 && this.status == 0) {
            eventBus.trigger(errorEventName);
        }
    };

    oReq.open("get", url, true);
    oReq.send(null);
    return false;
}

function postFormData(oFormElement, url, eventBus, successEventName, errorEventName, debug) {
    if(debug) { console.log("postFormData") };
    var oReq = new XMLHttpRequest();
    oReq.onerror = function (oEvent) { 
        if(debug) { console.log(oEvent.toString()) };
        eventBus.trigger(errorEventName);
    };
    oReq.onload = function (oEvent) { 
        if(debug) { console.log(oEvent.toString()) };
    };
    oReq.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 201) {
            eventBus.trigger(successEventName);
        } else {
            if(debug) { console.log("state "+this.readyState+" status: "+this.status) };
        }
    };
    // method declared in the form is ignored
    oReq.open("post", url);
    oReq.send(new FormData(oFormElement));
    return false;
}

function deleteData(id, url, eventBus, successEventName, errorEventName, debug) {

    var oReq = new XMLHttpRequest();
    oReq.onerror = function (oEvent) { 
        if(debug) { console.log(oEvent.toString()) };
    };
    oReq.onload = function (oEvent) {/*getdataCallback(elementId, statusId);*/
        if(debug) { console.log(oEvent.toString()) };
    };
    oReq.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            eventBus.trigger(successEventName);
        } else if (this.readyState == 4 && this.status == 0) {
            eventBus.trigger(errorEventName);
        }
    };

    oReq.open("DELETE", url+"/"+id, true);
    oReq.send(null);
    return false;
}
