### Tinyurl Service.

<h4> Quick Start </h5>

  **Dependencies**

  - Docker  

  **Technologies used**
  - Java  
  - SpringBoot  
  - Maven
  - Docker
  - Cassandra
  
  **Launch the development environment with Docker**
  - Clone the git repo
    ```
     https://github.com/ramarvab/tinyurl
    ```  
  - To start the app
    ```
     cd tinyurl
     docker-compose -f deployments/docker-compose.yml up
    ```
  - The first build takes time as maven needs to download the dependencies.
  
  **Accessing the service using rest**
  
  - To create short url for the long url
    ```
        curl --location --request POST 'localhost:8082/shorten' \
        --header 'Content-Type: text/plain' \
        --data-raw 'https://github.com/ramarvab/tinyurl'
        
         Output:
         {
            "shortUrl": "http://localhost:8082/7kERuwNf",
            "longUrl": "https://github.com/ramarvab/tinyurl",
            "creationDate": "2020-10-30T11:47:14.972+00:00"
          }  
    ```
  - To redirect the short url to long url
    ```
        curl --location --request GET 'http://localhost:8082/7kERuwNf' 
  
        or paste the url in the browser http://localhost:8082/7kERuwNf
    ```
-  To know how many times the short url was accessed in last N days
    
    ```
        last 24 hours
        curl --location --request GET 'http://localhost:8082/stats/7kERuwNf?days=1'
        
        last one week 
        curl --location --request GET 'http://localhost:8082/stats/7kERuwNf?days=7'
        
        All time
        curl --location --request GET 'http://localhost:8082/stats/7kERuwNf
        
        output:
        
        StatsInfo{url='wFQZYUnd', accessed=2, days='7'}
    ```
  

Tinyurl service converts the given longurl into short url which is of 8 characters.  
For example if we shorten the longurl  

`https://github.com/ramarvab/tinyurl`  

 we would get   
 
 `http://localhost:8082/LDJrwCbh`
 
This service will be read heavy as there are lot of redirection requests compared to new Url shortenings. 
#### Goals of the system
This service has 3 api functions  
1. Converts given long url to short url.
2. Redirects the given short url to long url
3. Get stats for the short url. How many times the short url has been accessed in last 24 hours, last week and all time.


#### Database Design

Created two tables for storing information about url mappings and one for url stats

```
    CREATE TABLE url_store(shortUrl Text PRIMARY KEY, longUrl Text, creationDate timestamp);
    
    create Table url_stats(urlid Text,
                           accessed_date date, 
                           accessed_timestamp timestamp,
                            primary key ((accessed_date),urlid,accessed_timestamp))
```
- For url stats table to use greater than or less than queries the table was clustered on urlid and timestamp
as primary key column doesn't support those type of queries in cassandra

- As we store billions of rows and we don't need to use relationship between two objects, I used
 NoSql database like cassandra for storing the information
- The data is persisted in local machine to avoid data loss when docker machine restarts.
 
 #### Creating ShortUrl
 
   - Used apache commons library for generating 8 bit random string for the given long url.
   - The key is generated using alphanumeric characters [A-Z, a-z, 0-9]. This would generate 62^8 combinations
     which would result in roughly 250 trillion possible keys which would suffice for our system.
  
 #### Improvement/Issues
    
   - In case of key collision, the application returns error. Instead of that we can keep generating keys 
   until there is no collision. This approach will increase the latency of the appliication as we need to 
   keep checking the key in the database until we found a valid key.
   - Another approach can be keep generating keys offline and use the pregenerated key for the url service.
   This will improve the speed and latency of the application.
   - Better exception handling and writing test cases.
 
 
#### scalability  
-  Use consistent hashing for partitioning the data as Range based or Hashbased partitioning can lead to 
skewness in the partitions because of hot url which generates most of the traffic.
- For caching, we can use Least Recently Used eviction policy because for our service 80% of the traffic
will be generated for top 20% of the urls.
     




