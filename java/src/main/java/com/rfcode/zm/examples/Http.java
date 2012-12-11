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

import java.net.URI;

import java.util.Map;

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

public class Http {
    private final DefaultHttpClient _client;
    private final HttpContext _context;
    private final ResponseFormat _format;
    private final String _host;
    private final int _port;

    public Http(String host, int port, ResponseFormat format, String user, String password) {
        _host = host;
        _port = port;
        _format = format;
        _client = new DefaultHttpClient();

        if (user != null && password != null) {
            _client.getCredentialsProvider().setCredentials(
                new AuthScope(host, port),
                new UsernamePasswordCredentials(user, password));

            AuthCache authCache = new BasicAuthCache();
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(new HttpHost(host, port, "http"), basicAuth);

            _context = new BasicHttpContext();
            _context.setAttribute(ClientContext.AUTH_CACHE, authCache);
        }
        else {
            _context = null;
        }
    }

    public void get(String path) throws Exception {
        get(path, null);
    }

    public void get(String path, Map<String, String> params) throws Exception {
        URI uri = toURI(path, params);

        log("URL: " + uri);

        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = _client.execute(httpget, _context);
        int status = response.getStatusLine().getStatusCode();

        if (status == 200) {
            log(EntityUtils.toString(response.getEntity()));
        }
        else {
            log("401 Unauthorized");
        }
    }

    private URI toURI(String path, Map<String, String> params) throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(_host).setPort(_port);
        builder.setPath("/rfcode_zonemgr/zonemgr/" + path + "." + _format.toString().toLowerCase());
        if (params != null) {
            for (Map.Entry<String, String> me : params.entrySet()) {
                builder.addParameter(me.getKey(), me.getValue());
            }
        }
        return builder.build();
    }

    private void log(String msg) {
        System.out.println(msg);
    }
}
