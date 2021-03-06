# Calculator [![Build Status](https://travis-ci.org/jonalx/calculator.svg?branch=master)](https://travis-ci.org/jonalx/calculator)
Test Project with a simple calculator and an HTTP API

## Modules
### model
This module contains the business logic of a simple calculator

### springboot-server
This module contains a springboot server with a synchronous HTTP interface to the calculator.

### vertx-server
This module contains a vertx server with a reactive HTTP interface to the calculator.

## Local setup
You can build the project with the following command:

    ./gradlew build test jar shadowJar

For running the springboot implementation:

    java -jar springboot-server/build/libs/springboot-server.jar

For running the vertx implementation:

    java -jar vertx-server/build/libs/vertx-server.jar

## Testing
The application can be locally tested by executing:

    curl http://localhost:8080/calculus?query=`echo "1 + 2" | base64`

Or you can target the live version as follow:

    curl https://calculator-jonalx.herokuapp.com/calculus?query=`echo "1 + 2" | base64`
