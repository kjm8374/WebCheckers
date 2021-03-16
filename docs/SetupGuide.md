# WebCheckers Development Environment Setup Guide

This document describes how to set up your development environment to work on the Webcheckers project.

## Prerequisites

* You must have Java 11 or later installed.
* You must have Maven installed.
* You must have IntelliJ installed.
    * For compilation, you must have the Maven plugin installed.
    * For unit testing, you must have the JUnit plugin installed.

## Importing the project

1. Open IntelliJ.
2. If you already have a project open, select `File > New > Project from existing sources...`. If you do not have a
 project open, select `Import Project`.
3. Navigate to the `pom.xml` file in the project directory.
4. Click "OK" to import the project into the IDE.

## Compiling

Create a run configuration of type "Maven" and set "Command line" to `compile`. Title the run configuration something
 meaningful, like "Compile".

To compile the project, select the "Compile" run configuration and click the `Run` button.

## Starting the server

Create a run configuration of type "Maven" and set "Command line" to `compile exec:java`. Title the run configuration
 something meaningful, like "Run Server".

To start the server, select the "Run Server" run configuration and click the `Run` button.

## Connecting to the server

After starting the server (see "Starting the server" above), open a web browser to `http://localhost:4567/` to begin
 playing.

For additional sessions, open browsers to `http://127.0.0.1:4567/`, `http://127.0.0.2:4567/`, `http://127.0.0.3:4567/`,
etc.

> Note: Testing multiple sessions using `127.0.0.*:4567` addresses requires your system to be configured to have the
> entire `127.0.0.*` block set up as loopback addresses.
>
> Windows systems are set up like this by default. Macs are not, so this will not work on a Mac.
>
> As a workaround, you may test one additional session using each browser's "incognito mode" function and any further
> sessions using different web browsers.

## Stopping the server

While the server is running, select the "Run Server" run configuration and click the `Stop` button to stop the server.

## Running unit tests

Create a run configuration of type "JUnit". Title the run configuration something meaningful, like "Run Unit Tests". Set
 `Test kind` to `All in package`. Leave the `Package` field blank. Set `Search for tests` to `In whole project`.

To run the unit tests, select the "Run Unit Tests" run configuration and click the `Run` button. The test results
 will appear at the bottom of the screen.
 
### Running unit tests with coverage

Open the `Edit configuations` menu and select the "Run Unit Tests" run configuration. Navigate to the `Code Coverage`
 tab. Set `Choose test runner` to `IntelliJ IDEA`. Select `Tracing` and check `Track per test coverage`.

To run the unit tests with coverage, select the "Run Unit Tests" run configuration and click the `Run with Coverage`
 button. The test results will appear at the bottom of the screen. Once the tests complete, coverage data will appear
  directly in the editor.

### Generating test coverage reports

Create a run configuration of type "Maven" and set "Command line" to `exec:exec@tests-and-coverage`. Title the run
 configuration something meaningful, like "Generate Coverage Reports".

To generate the test coverage reports, select the "Generate Coverage Reports" run configuration and click the `Run`
 button. Progress will be displayed at the bottom of the screen.

Finished reports will be located at `target/site/jacoco/[tier]/index.html`, where `[tier]` is `model`, `appl`, or `ui`.