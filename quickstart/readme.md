#Apache Geode Quickstart

1. To begin, first we need to install Apache Geode.

On a mac with homebrew installed, simply run:
```
brew tap pivotal/tap && brew install geode
```
For other systems, see [How To Install Geode](http://geode.docs.pivotal.io/docs/getting_started/installation/install_standalone.html).

2. Once Geode is installed, we can start a locator for our first Geode cluster:
```
$ gfsh
gfsh> start locator --name=locator1 --port=10334
```

3. With a locator running, we can start spinning up our Geode servers.  For this example, we'll just start two server processes running on our local machine.
```
gfsh> start server --name=server1 --server-port=40411
gfsh> start server --name=server2 --server-port=40412 
```

4. We can verify that everything so far has started up as expected:
```
gfsh>list members
  Name   | Id
-------- | -------------------------------------------------
locator1 | 192.168.1.19(locator1:24390:locator)<ec><v0>:1024
server1  | 192.168.1.19(server1:24393)<ec><v1>:1025
server2  | 192.168.1.19(server2:24396)<ec><v2>:1026

```

5. Everything is now in place to create our first Region. You can think of a Region as a distributed java.util.HashMap.  There are some choices to be made about how exactly to distribute the entries in our Map.  We could let each server maintain a full copy of the Map (this would be a *replicated* region), or if we had a very large map we  might want to choose a *partitioned* region where each server only stores a subset of the entires. 
  Full details about [Regions](http://geode.docs.pivotal.io/docs/basic_config/data_regions/chapter_overview.html#data_regions) and [Region Shortcuts](http://geode.docs.pivotal.io/docs/reference/topics/region_shortcuts_reference.html) are available in the Geode documentation.  For now, we'll create a replicated region:
  ```
  gfsh>create region --name=regionA --type=REPLICATE
  ```
  
  6. Let's start the simulation:
  ```
  $ ./gradlew :quickstart:run
  ```
  
  7. We can query our stock simulation data on Geode
  ```
  gfsh> query --region=regionA --query="select key, value from /regionA.entrySet"
  ```
  
  8. Press enter to end the simulation, and then shut down Geode:
  ```
  gfsh> shutdown
  ```