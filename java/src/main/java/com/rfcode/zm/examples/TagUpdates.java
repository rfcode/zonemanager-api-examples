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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagUpdates extends Command {
    @Parameter(names = {"-c", "--count"}, description = "Maximum number of events to retrieve in each request")
    private int _count = 100;

    @Parameter(names = {"-t", "--objtype"}, description = "Object type for results. Default is all object types.")
    private String _objtype;

    @Override()
    protected void onRun(Http http) throws Exception {
        Map<String, String> query = new HashMap<String, String>();

        query.put("_init", "true");
        query.put("_maxcnt", Integer.toString(_count));

        if (_objtype != null) {
            query.put("_objtype", _objtype);
        }

        http.get("api/tagupdates", query);

        // These parameters are only needed on the initial (_init=true) request
        query.remove("_init");
        query.remove("_objtype");

        while (true) {
            http.get("api/tagupdates", query);
        }
    }
}
