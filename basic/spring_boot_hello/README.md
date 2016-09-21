# geode-springboot-hello
Geode Spring Boot example application

Apache Geode (incubating) provides a database-like consistency model, reliable transaction processing and a shared-nothing architecture to maintain very low latency performance with high concurrency processing. For more information about Geode, see http://geode.apache.org.

This project shows a Spring Boot HelloWorld REST application using Geode.  The application provides endpoints for put and get operations backed by a Geode cluster.

## Instructions

1. Download the Geode binary distribution from http://geode.apache.org/releases/.  Extract the archive and set `$GEODE_HOME` to the install location.

2. Start the Geode cluster with `./scripts/geode-ctl start`.

2. Build and run the geode-springboot-hello project using `./gradlew bootRun`.

3. Put values into the cache using `curl http://localhost:8080/put?key=hi\&value=mom`.

4. Get values from the cache using `curl http://localhost:8080/get?key=hi`.

5. Shutdown the Geode cluster with `./scripts/geode-ctl stop`.
