package com.rfcode.zm.examples;

/*
 * #%L
 * ZoneManagerApiExamples
 * %%
 * Copyright (C) 2012 RF Code, Inc.
 * %%
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
 * #L%
 */

import com.beust.jcommander.Parameter;

import org.apache.http.protocol.HTTP;

public abstract class Command implements Runnable {

    @Parameter(names = {"-h", "--host"}, description="[Host/IP Address]:port")
    private String _host = "localhost:6580";

    @Parameter(names = {"-f", "--format"}, description="Format for results (json, txt, xml)")
    private ResponseFormat _format = ResponseFormat.JSON;

    @Parameter(names = {"-u", "--user"}, description="Specify the user name and password to use for authentication")
    private String _credentials;

    @Override()
    public void run() {
        String user = null;
        String password = null;
        String host = null;
        int port = 6580;

        // host:port
        String[] parts = _host.split(":");
        host = parts[0];
        if (parts.length == 2) {
            port = Integer.parseInt(parts[1]);
        }

        // user:password
        if (_credentials != null) {
            String[] split = _credentials.split(":");

            if (split.length == 2) {
                user = split[0];
                password = split[1];
            }
            else {
                throw new IllegalArgumentException("Invalid username:password format");
            }
        }

        try {
            Http http = new Http(host, port, _format, user, password);
            onRun(http);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void onRun(Http http) throws Exception;
}
