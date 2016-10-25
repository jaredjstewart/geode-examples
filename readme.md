Apache Gedoe Examples
=====================

This project provides a number of examples to get you started using Apache Geode. These examples are designed to work with [Apache Geode] (http://geode.apache.org/) and are organized into the following sub projects:

# Quickstart

This is a basic demo that generates random stock prices and stores them in Geode to introduce the basic concepts of a Geode Region.

# Basic

* continuous_query - This example introduces Continuous Queries by extending the Quickstart demo to add a CQ that triggers whenever a stock price exceeds a certain threshold.
* spring_boot_hello - This project shows a Spring Boot HelloWorld REST application using Geode.
# Running The Examples

You can run the Quickstart example by executing:
```
$ ./gradlew :quickstart:run
```
You can run the Continuous Query example by executing:
```
$./gradlew :basic:continuous_query:run
```

Detailed instructions for each example may be found in its own README file.



	