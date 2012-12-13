# Java Zone Manager API Examples

## Building

To build, use maven:

    $ mvn clean package

## Running

To get command line help:

    $ run.sh

To get help about a specific command:

    $ run.sh help <command>

## Example

To print out a tag at the host zm.example.com with username/password admin/admin.

    $ run.sh tagprint -h zm.example.com -u admin:admin HUMRCK00000005

