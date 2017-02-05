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
import org.cricketmsf.Kernel;
import org.cricketmsf.RequestObject;
import java.util.HashMap;
import org.cricketmsf.annotation.EventHook;
import org.cricketmsf.annotation.HttpAdapterHook;
import org.cricketmsf.annotation.InboundAdapterHook;
import org.cricketmsf.in.cli.CliIface;
import org.cricketmsf.in.file.WatchdogIface;
import org.cricketmsf.in.http.EchoHttpAdapterIface;
import org.cricketmsf.in.http.HtmlGenAdapterIface;
import org.cricketmsf.in.http.HttpAdapter;
import org.cricketmsf.in.http.HttpAdapterIface;
import org.cricketmsf.in.http.ParameterMapResult;
import org.cricketmsf.in.http.StandardResult;
import org.cricketmsf.in.scheduler.SchedulerIface;
import org.cricketmsf.out.db.H2EmbededIface;
import org.cricketmsf.out.db.KeyValueDBException;
import org.cricketmsf.out.db.KeyValueDBIface;
import org.cricketmsf.out.file.FileReaderAdapterIface;
import org.cricketmsf.out.log.LoggerAdapterIface;
import org.cricketmsf.out.script.ScriptingAdapterIface;

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
    KeyValueDBIface database = null;
    SchedulerIface scheduler = null;
    HtmlGenAdapterIface htmlAdapter = null;
    FileReaderAdapterIface fileReader = null;
    // optional
    H2EmbededIface embededDatabase = null;
    HttpAdapterIface scriptingService = null;
    ScriptingAdapterIface scriptingEngine = null;
    CliIface cli = null;
    WatchdogIface watchdog = null;
    WatchdogIface tail = null;

    @Override
    public void getAdapters() {
        // standard Cricket adapters
        logAdapter = (LoggerAdapterIface) getRegistered("logger");
        httpAdapter = (EchoHttpAdapterIface) getRegistered("echo");
        database = (KeyValueDBIface) getRegistered("database");
        scheduler = (SchedulerIface) getRegistered("scheduler");
        htmlAdapter = (HtmlGenAdapterIface) getRegistered("WwwService");
        fileReader = (FileReaderAdapterIface) getRegistered("FileReader");
        // optional
        embededDatabase = (H2EmbededIface) getRegistered("H2DB");
        scriptingService = (HttpAdapterIface) getRegistered("ScriptingService");
        scriptingEngine = (ScriptingAdapterIface) getRegistered("ScriptingEngine");
        cli = (CliIface) getRegistered("CLI");
        watchdog = (WatchdogIface) getRegistered("watchdog");
        tail = (WatchdogIface) getRegistered("tail");
        // adapters specific to this service goes here
        greeterAdapter = (HttpAdapterIface) getRegistered("greeter");
    }

    @Override
    public void runInitTasks() {
        try {
            database.addTable("webcache", 100, false);
        } catch (KeyValueDBException e) {
        }
        try {
            database.addTable("counters", 1, false);
        } catch (KeyValueDBException e) {
        }
    }

    @Override
    public void runFinalTasks() {
        // CLI adapter doesn't start automaticaly as other inbound adapters
        if (cli != null) {
            cli.start();
        }
    }

    @Override
    public void runOnce() {
        super.runOnce();
        handleEvent(Event.logInfo("BasicService.runOnce()", "executed"));
        handleEvent(Event.logInfo("BasicService.runOnce()", "database version " + embededDatabase.getVersion()));
    }

    @HttpAdapterHook(adapterName = "ScriptingService", requestMethod = "*")
    public Object doGetScript(Event requestEvent) {
        StandardResult r = scriptingEngine.processRequest(requestEvent.getRequest());
        return r;
    }

    /**
     * Handles HTTP request received by the greeter inbound adapter. The context
     * path and other parameters of this adapter should be configured in
     * cricket.json file.
     *
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
        handle(Event.logInfo("BasicService.getGreeting()", greetings.message + greetings.name));
        return result;
    }

    /**
     * Process requests from simple web server implementation given by
     * HtmlGenAdapter access web web resources
     *
     * @param event
     * @return ParameterMapResult with the file content as a byte array
     */
    @HttpAdapterHook(adapterName = "WwwService", requestMethod = "GET")
    public Object doGet(Event event) {

        RequestObject request = event.getRequest();
        System.out.println(request.uri);
        ParameterMapResult result
                = (ParameterMapResult) fileReader
                        .getFile(request, htmlAdapter.useCache() ? database : null, "webcache");

        // caching policy 
        result.setMaxAge(120);

        return result;
    }

    @InboundAdapterHook(adapterName = "watchdog", inputMethod = "*")
    public Object processWarchdogEvent(Event event) {
        handleEvent(Event.logInfo("processWatchdogEvent", (String) event.getPayload()));
        return null;
    }

    @InboundAdapterHook(adapterName = "tail", inputMethod = "*")
    public Object processTailEvent(Event event) {
        handleEvent(Event.logInfo("processTailEvent", (String) event.getPayload()));
        return null;
    }

    @HttpAdapterHook(adapterName = "echo", requestMethod = "*")
    public Object doGetEcho(Event requestEvent) {
        return sendEcho(requestEvent.getRequest());
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
            handleEvent(Event.logInfo("EchoService", event.getPayload().toString()));
        }
    }

    public Object sendEcho(RequestObject request) {
        StandardResult r = new StandardResult();
        r.setCode(HttpAdapter.SC_OK);
        try {
            if (!httpAdapter.isSilent()) {
                // with echo counter
                Long counter;
                counter = (Long) database.get("counter", "counters", new Long(0));
                counter++;
                database.put("counter", "counters", counter);
                HashMap<String, Object> data = new HashMap<>(request.parameters);
                data.put("service.uuid", getUuid().toString());
                data.put("request.method", request.method);
                data.put("request.pathExt", request.pathExt);
                data.put("echo.counter", database.get("counter", "counters"));
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
        } catch (KeyValueDBException e) {
            handle(Event.logSevere(this.getClass().getSimpleName(), e.getMessage()));
        }
        return r;
    }
}
