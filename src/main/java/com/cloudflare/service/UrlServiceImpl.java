package com.cloudflare.service;

import com.cloudflare.entities.StatsInfo;
import com.cloudflare.entities.UrlStats;
import com.cloudflare.entities.UrlStore;
import com.cloudflare.repository.URLRepository;
import com.cloudflare.repository.UrlStatsRepository;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


/**
 * This class creates short url and fetches respective long url for the given short url.
 */
@Service
public class UrlServiceImpl implements UrlService {
    private Logger logger = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Autowired
    URLRepository urlRepository;

    @Autowired
    UrlStatsRepository urlStatsRepository;

    @Autowired
    public GenerateRandomKey generateRandomKey;

    /**
     * Creates a short url
     * //TODO : Instead of throwing error for duplicate key, should keep on generating random key until valid key is generated
     * @param longUrl
     * @return Url store object  {shorturl, longurl, timestamp}
     * @throws Exception In case of duplicate key the application throws key already exists exception
     */

    public UrlStore createShortUrl(String longUrl) throws Exception{

       try {
           Timestamp timestamp = new Timestamp(System.currentTimeMillis());
           logger.info("generating key for the url" + longUrl);
           String key = generateRandomKey.generateKey();

           //checks the given exists or not in DB
           boolean keyExists = urlRepository.findById(key).isPresent();
           if (keyExists) {
               logger.error("duplicate key found" + key);
               throw new Exception("Key already exists, please try again");

           } else {
               UrlStore urlStore = new UrlStore(key, longUrl, timestamp);
               //inserts the record in DB
               urlRepository.insert(urlStore);
               urlStore.setShortUrl("http://localhost:8082/" + key);
               return urlStore;
           }
       }catch (Exception ex){
           logger.error("Unknown issue occured in conveersion to Shorturl"+ ex.getLocalizedMessage());
           throw new Exception("Unknown issue occured in conveersion to Shorturl"+ ex.getLocalizedMessage());
       }

    }

    /**
     * fetches the longurl for the given short url. In case if the given url is not found, throws an exception
     * @param shortUrl
     * @return
     * @throws Exception
     */
    public String getLongUrl(String shortUrl) throws Exception{
        try {
            //fetches the longurl from the database
            logger.info("fetching longurl for " + shortUrl);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            UrlStore fetchLongUrl = urlRepository.findById(shortUrl).orElseThrow(() -> new Exception("long url not found"));
            urlStatsRepository.insert(new UrlStats(java.time.LocalDate.now(), shortUrl, timestamp));
            return fetchLongUrl.getLongUrl();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new Exception("Error in fetching long url" +e.getMessage());
        }
    }

    /**
     * class to fetch stats by day information.
     * @param shortUrl
     * @param days
     * @return StatsInfo (how many times the url accessed in last N days )
     * @throws Exception
     */
    public StatsInfo getStats(String shortUrl, int days) throws Exception{

        try{
            logger.info("retrieve stats for the url"+shortUrl);
            //if days is zero , then it will fetch all the records for the url
            if(days == 0){
                ResultSet resultSet = urlStatsRepository.fetchStatsByUrl(shortUrl);
                return new StatsInfo(shortUrl,resultSet.all().size(),"for all time");
            }
            else {
                Instant instant = Instant.now().truncatedTo(ChronoUnit.DAYS).minus(days, ChronoUnit.DAYS);
                ResultSet resultSet = urlStatsRepository.fetchStatsByDay(instant, shortUrl);
                return new StatsInfo(shortUrl,resultSet.all().size(),Integer.toString(days));
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new Exception("Error in fetching stats for the id "+shortUrl+ " " +e.getMessage());
        }
    }
}
