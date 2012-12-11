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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagListByAttrib extends Command {
    @Parameter(required = true, description="Attribute=Value to query (eg. lowbattery=true)")
    private List<String> _parameters = new ArrayList<String>();

    @Override()
    protected void onRun(Http http) throws Exception {
        Map<String, String> query = new HashMap<String, String>();

        for (int i = 0; i < _parameters.size(); i++) {
            String[] parts = _parameters.get(i).split("=");

            query.put("attribid" + i, parts[0]);
            query.put("attribval" + i, parts[1]);
        }
        http.get("api/taglistbyattrib", query);
    }
}
