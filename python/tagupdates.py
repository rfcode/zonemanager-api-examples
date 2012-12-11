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
import requests

class TagUpdates(Script):
    def add_arguments(self, parser):
        parser.add_argument('-c', '--count', type=int,
                            default=100,
                            help='Maximum number of events to retrieve in each request')
        parser.add_argument('-t', '--objtype',
                            help='Object type for results. Default is all object types.',
                            choices=('location', 'reader', 'tag'))

    def run(self, args):
        # tagupdates requires a session for transactionality. We need to
        # preserve the HTTP cookies sent by Zone Manager
        session = requests.session(auth=self.auth)

        params = dict(_init="true", _maxcnt=args.count)
        if args.objtype:
            params['_objtype'] = args.objtype

        # Set _init to true to obtain a transactional session for tag updates
        self.http_get('api/tagupdates', session, params=params)

        while True:
            self.http_get('api/tagupdates', session, params={ "_maxcnt" : args.count })

if __name__ == '__main__':
    TagUpdates().main()
