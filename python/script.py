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
import argparse
import json
import requests
import xml.dom.minidom

def log(msg):
    print(msg)

class Script:
    def main(self):
        name = self.__class__.__name__.lower()
        parser = argparse.ArgumentParser(description = name)
        parser.add_argument('--host',
                            help='[Host/IP Address]:port. Default port is localhost:6580',
                            default='localhost:6580')
        parser.add_argument('-f', '--format',
                            help='Format for results. Default is json.',
                            default='json',
                            choices=('json', 'txt', 'xml'))
        parser.add_argument('-u', '--user',
                            help='Specify the user name and password to use for authentication')
        self.add_arguments(parser)

        args = parser.parse_args()
        try:
            host, port = args.host.split(':')
        except ValueError:
            host = args.host
            port = "6580"

        self.host = host
        self.port = port
        self.baseurl = 'http://' + host + ':' + port + '/rfcode_zonemgr/zonemgr/'
        self.format = args.format
        self.auth = args.user and tuple(args.user.split(':'))

        self.run(args)

    def url(self, api):
        return self.baseurl + api + '.' + self.format

    def http_get(self, urlpath, *args, **kwargs):
        if len(args) == 0:
            session = requests
            kwargs['auth'] = self.auth
        else:
            session = args[0]

        req = session.get(self.url(urlpath), **kwargs)
        log('URL: ' + req.url)
        req.raise_for_status()

        if self.format == 'json':
            log(json.dumps(req.json, indent=True))
        elif self.format == 'xml':
            log(xml.dom.minidom.parseString(req.text).toprettyxml())
        else:
            log(req.text)

        return req

    def add_arguments(self, parser):
        pass

    def run(self, args):
        pass
