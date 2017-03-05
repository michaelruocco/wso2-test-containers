# WSO2 Test Containers

[![Build Status](https://travis-ci.org/michaelruocco/wso2-test-containers.svg?branch=master)](https://travis-ci.org/michaelruocco/wso2-test-containers)
[![Coverage Status](https://coveralls.io/repos/github/michaelruocco/wso2-test-containers/badge.svg?branch=master)](https://coveralls.io/github/michaelruocco/wso2-test-containers?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ecd24e8d99a44f98915b769114e025aa)](https://www.codacy.com/app/michaelruocco/wso2-test-containers?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=michaelruocco/wso2-test-containers&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.michaelruocco/wso2-test-containers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.michaelruocco/wso2-test-containers)

This library utility classes to help with integration testing when using [WSO2](https://wso2.com/) components. It makes
use of docker and this [test containers](https://github.com/testcontainers/testcontainers-java).

The test containers library itself provides the ability to spin up a docker container using a junit rule, this
library provides a class that will wait until a container outputs a specified log message to allow tests to block
until the component inside the docker container has started.

## Usage

To use the library from a program you will need to add a dependency to your project. In
gradle you would do this by adding the following to your build.gradle file:

```
dependencies {
    compile group: 'com.github.michaelruocco', name: 'wso2-test-containers', version: '1.0.0'
}
```

## Creating a test that waits for a WSO2 component to start

You can use the test containers library to create a generic container that runs a docker image
containing a [WSO2](https://wso2.com/) component (e.g. [WSO2 API Manager](http://wso2.com/api-management/)).
Next you can create an instance of a Wso2StartupCheckLogConsumer and attached it to your container, finally
in your @Before method in your unit test class you can call waitForStartupMessageInLog. This method
will block until a start message is shown in the log.

```
private static final String DOCKER_IMAGE = "michaelruocco/wso2am:1.9.1";

private final StartupCheckLogConsumer logConsumer = new Wso2StartupCheckLogConsumer();

@Rule
public final GenericContainer container = new GenericContainer(DOCKER_IMAGE)
        .withLogConsumer(logConsumer);

...

@Before
public void setUp() {
    logConsumer.waitForStartMessageInLog();
}
```

The waitForStartupMessageInLog method can also be overloaded to provide a timeout
and delay value. The default timeout value is 60000 milliseconds (1 minute) and 
default delay value is 1000 milliseconds (1 second).

The timeout value is the maximum amount of time that the method will wait for
the startup message to appear in the logs, if it does not appear within the
timeout it will throw a StartupTimeoutException.

The delay value is the time that the method will wait between polling to check
whether the startup message has appeared in the logs, the first time polls after
the startup message is shown the method will return a true value.

Overriding the timeout and delay values can be done as follows:

```
@Before
public void setUp() {
    int timeout = 30000;
    int delay = 500;
    logConsumer.waitForStartupMessageInLog(timeout, delay);
}
```

## Custom startup messages

The Wso2StartupCheckLogConsumer extends the StartupCheckLogConsumer class. This
class can be used to create other StartupCheckLogConsumers with custom startup
messages. A startup message is a string that the class will look for in the log
of the docker machine, when a line of log output appears the waitForStartupMessageInLog
will complete. To create a StartupCheckLogConsumer with a custom startup message
simply create an instance passing the startup message you want to check for as a
constructor argument:

```
String customStartupMessage = "startup completed";
StartupCheckLogConsumer logConsumer = new StartupCheckLogConsumer(customStartupMessage);
```

## Running the tests

To run just the unit tests you can run the command:

```
gradlew clean build
```

## Checking dependencies

You can check the current dependencies used by the project to see whether
or not they are currently up to date by running the following command:

```
gradlew dependencyUpdates
```