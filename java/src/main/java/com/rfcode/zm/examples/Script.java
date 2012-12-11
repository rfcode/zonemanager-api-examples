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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.net.URI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public abstract class Script {

    protected static enum Format { JSON, TXT, XML};

    @Parameter(names = {"-h", "--help"}, help = true)
    private boolean help;

    @Parameter(names = {"--host"}, description="[Host/IP Address]:port")
    private String host = "localhost:6580";

    @Parameter(names = {"-f", "--format"}, description="Format for results (json, txt, xml)")
    protected Format format = Format.JSON;

    @Parameter(names = {"-u", "--user"}, description="Specify the user name and password to use for authentication")
    private String credentials;

    private String domain;
    private int port;

    private DefaultHttpClient client;
    private HttpContext context;

    public void start(String[] args) throws Exception {
        JCommander cmd = new JCommander(this);

        try {
            cmd.parse(args);
        }
        catch (ParameterException e) {
            showUsageAndExit(cmd, e.getMessage());
        }

        if (this.help) {
            showUsageAndExit(cmd, null);
        }

        String[] parts = this.host.split(":");
        this.domain = parts[0];
        this.port = 6580;
        if (parts.length == 2) {
            this.port = Integer.parseInt(parts[1]);
        }

        this.client = new DefaultHttpClient();

        if (this.credentials != null) {
            String[] split = this.credentials.split(":");
            String user = null;
            String password = null;

            if (split.length == 2) {
                user = split[0];
                password = split[1];
            }
            else {
                showUsageAndExit(cmd, "Invalid username:password format");
            }
            this.client.getCredentialsProvider().setCredentials(
                                                          new AuthScope(domain, port),
                                                          new UsernamePasswordCredentials(user, password));

            AuthCache authCache = new BasicAuthCache();
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(new HttpHost(domain, port, "http"), basicAuth);

            this.context = new BasicHttpContext();
            this.context.setAttribute(ClientContext.AUTH_CACHE, authCache);
        }

        run();
    }

    public void httpGet(String path) throws Exception {
        httpGet(path, null);
    }

    public void httpGet(String path, Map<String, String> params) throws Exception {
        URI uri = toURI(path, params);

        log("URL: " + uri);

        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = this.client.execute(httpget, this.context);
        int status = response.getStatusLine().getStatusCode();

        if (status == 200) {
            log(EntityUtils.toString(response.getEntity()));
        }
        else {
            log("401 Unauthorized");
        }
    }

    public URI toURI(String path, Map<String, String> params) throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(this.host).setPort(this.port);
        builder.setPath("/rfcode_zonemgr/zonemgr/" + path + "." + this.format.toString().toLowerCase());
        if (params != null) {
            for (Map.Entry<String, String> me : params.entrySet()) {
                builder.addParameter(me.getKey(), me.getValue());
            }
        }
        return builder.build();
    }

    private void showUsageAndExit(JCommander cmd, String msg) {
        if (msg != null) {
            log(msg);
        }
        cmd.usage();
        System.exit(0);
    }

    protected void log(String msg) {
        System.out.println(msg);
    }

    public abstract void run() throws Exception;
}

