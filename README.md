# Stackhat

## BuiltWith

No longer in use, use StackhatServer folder / project.

## StackhatServer

Maven based jersey.github.io JAX-RS REST API implementation.
Provides endpoints to interact with BuiltWith classes and provide output back to users.

To build / run tests

`mvn clean test`

To run local server

`mvn exec:java`

To build WAR file for deployment

`mvn clean package`

File will be created in ./target folder ready for deployment.

## Application Hosting

The application is currently hosted on a Bitnami Tomcat Ubuntu image on AWS.

https://stackhat.pensoagency.com/

Configuring via the Bitnami help tool:

`sudo /opt/bitnami/bnhelper-tool`