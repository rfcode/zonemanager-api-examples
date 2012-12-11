#!/usr/bin/env python

###
# #%L
# ZoneManagerApiExamples
# %%
# Copyright (C) 2012 RF Code, Inc.
# %%
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# #L%
###

from script import Script

class TagPrint(Script):
    def add_arguments(self, parser):
        parser.add_argument('tagid', nargs='+',
                            help='ID of tag to print')

    def run(self, args):
        params = {}
        index = 0

        for tagid in args.tagid:
            params['tagid' + str(index)] = tagid
            index+=1

        self.http_get('api/tagprint', params=params)

if __name__ == '__main__':
    TagPrint().main()
