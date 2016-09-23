This shows an example that registers a class for PDX autoserialization.  
 If we have the region set up from the Quickstart example and execute ```./gradlew :basic:pdx_autoserialization:run``` then we can execute a query that uses partial deserialization:
 
 ```
 gfsh>query --query="select * from /regionA a where a.name='bob'"
 
 Result     : true
 startCount : 0
 endCount   : 20
 Rows       : 1
 
 weight | name
 ------ | ----
 100    | bob
 

 ```