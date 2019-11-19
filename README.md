# Stackhat

## BuiltWith

BuiltWith API connector implementation that compiles data into Excel sheets.

To build / install

`mvn clean install`

With full deps

`mvn clean compile assembly:single`

## StackhatServer

Maven based jersey.github.io JAX-RS REST API implementation.
Provides endpoints to interact with BuiltWith classes and provide output back to users.

To build / run tests

`mvn clean test`

To run local server

`mvn exec:java`

## Application Hosting

The application is currently hosted on a Bitnami Tomcat Ubuntu image on AWS.

https://stackhat.pensoagency.com/

Configuring via the Bitnami help tool:

`sudo /opt/bitnami/bnhelper-tool`