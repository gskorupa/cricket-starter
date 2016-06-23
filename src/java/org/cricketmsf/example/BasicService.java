/*
 * Copyright 2015 Grzegorz Skorupa <g.skorupa at gmail.com>.
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
package org.cricketmsf.example;

import org.cricketmsf.Event;
import org.cricketmsf.EventHook;
import org.cricketmsf.HttpAdapterHook;
import org.cricketmsf.Kernel;
import org.cricketmsf.RequestObject;
import java.util.HashMap;
import org.cricketmsf.in.http.EchoHttpAdapterIface;
import org.cricketmsf.in.http.HtmlGenAdapterIface;
import org.cricketmsf.in.http.HttpAdapter;
import org.cricketmsf.in.http.HttpAdapterIface;
import org.cricketmsf.in.http.StandardResult;
import org.cricketmsf.in.scheduler.SchedulerIface;
import org.cricketmsf.out.db.H2EmbededIface;
import org.cricketmsf.out.db.KeyValueCacheAdapterIface;
import org.cricketmsf.out.html.HtmlReaderAdapterIface;
import org.cricketmsf.out.log.LoggerAdapterIface;

/**
 * EchoService
 *
 * @author greg
 */
public class BasicService extends Kernel {

    // adapterClasses
    HttpAdapterIface greeterAdapter = null;
    LoggerAdapterIface logAdapter = null;
    EchoHttpAdapterIface httpAdapter = null;
    KeyValueCacheAdapterIface cache = null;
    SchedulerIface scheduler = null;
    HtmlGenAdapterIface htmlAdapter = null;
    HtmlReaderAdapterIface htmlReaderAdapter = null;
    H2EmbededIface embededDatabase = null;

    @Override
    public void getAdapters() {
        // standard Cricket adapters
        logAdapter = (LoggerAdapterIface) getRegistered("LoggerAdapterIface");
        httpAdapter = (EchoHttpAdapterIface) getRegistered("EchoHttpAdapterIface");
        cache = (KeyValueCacheAdapterIface) getRegistered("KeyValueCacheAdapterIface");
        scheduler = (SchedulerIface) getRegistered("SchedulerIface");
        htmlAdapter = (HtmlGenAdapterIface) getRegistered("HtmlGenAdapterIface");
        htmlReaderAdapter = (HtmlReaderAdapterIface) getRegistered("HtmlReaderAdapterIface");
        embededDatabase = (H2EmbededIface) getRegistered("H2EmbededIface");
        
        // adapters specific to this service goes here
        greeterAdapter = (HttpAdapterIface) getRegistered("greeter");
    }

    @Override
    public void runOnce() {
        super.runOnce();
        handle(Event.logInfo("BasicService.runOnce()", "executed"));
        System.out.println("Hello from BasicService.runOnce()");
        System.out.println(embededDatabase.getVersion());
    }
    
    /**
     * Handles HTTP request received by the greeter inbound adapter. The context path and other parameters of this
     * adapter should be configured in cricket.json file.
     * @param event encapsulates HTTP request object
     * @return result object (implementing Result interface)
     */
    @HttpAdapterHook(adapterName = "greeter", requestMethod = "GET")
    public Object getGreeting(Event event) {
        StandardResult result = new StandardResult();
        result.setCode(HttpAdapter.SC_OK);
        GreetingVO greetings = new GreetingVO();
        greetings.message = "Hello ";
        greetings.name = event.getRequestParameter("name");
        result.setData(greetings);
        handle(Event.logInfo("BasicService.getGreeting()", greetings.message+greetings.name));
        return result;
    }

    @HttpAdapterHook(adapterName = "HtmlGenAdapterIface", requestMethod = "GET")
    public Object doGet(Event event) {
        RequestObject request = (RequestObject) event.getPayload();
        return htmlReaderAdapter.getFile(request);
    }

    @HttpAdapterHook(adapterName = "EchoHttpAdapterIface", requestMethod = "*")
    public Object doGetEcho(Event requestEvent) {
        return sendEcho((RequestObject) requestEvent.getPayload());
    }

    @EventHook(eventCategory = Event.CATEGORY_LOG)
    public void logEvent(Event event) {
        logAdapter.log(event);
    }
    
    @EventHook(eventCategory = Event.CATEGORY_HTTP_LOG)
    public void logHttpEvent(Event event) {
        logAdapter.log(event);
    }

    @EventHook(eventCategory = "*")
    public void processEvent(Event event) {
        if (event.getTimePoint() != null) {
            scheduler.handleEvent(event);
        } else {
            handle(Event.logInfo("EchoService", event.getPayload().toString()));
        }
    }

    public Object sendEcho(RequestObject request) {
        StandardResult r = new StandardResult();
        r.setCode(HttpAdapter.SC_OK);
        if (!httpAdapter.isSilent()) {
            // with echo counter
            Long counter;
            counter = (Long) cache.get("counter", new Long(0));
            counter++;
            cache.put("counter", counter);
            HashMap<String, Object> data = new HashMap<String, Object>(request.parameters);
            data.put("service.uuid", getUuid().toString());
            data.put("request.method", request.method);
            data.put("request.pathExt", request.pathExt);
            data.put("echo.counter", cache.get("counter"));
            if (data.containsKey("error")) {
                int errCode = HttpAdapter.SC_INTERNAL_SERVER_ERROR;
                try {
                    errCode = Integer.parseInt((String) data.get("error"));
                } catch (Exception e) {
                }
                r.setCode(errCode);
                data.put("error", "error forced by request");
            }
            r.setData(data);
        }
        return r;
    }

}
