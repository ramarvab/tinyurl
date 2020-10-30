package com.cloudflare.controller;
import com.cloudflare.service.UrlServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;


/**
 * This class is the main entry point for the api.
 */

@RestController
@Validated
public class URLController {
    private Logger logger = LoggerFactory.getLogger(URLController.class);

    @Autowired
    UrlServiceImpl urlService;

    /**
     * endpoint if users enters domain name
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/")
    public ResponseEntity<String> ping() throws Exception{
        return ResponseEntity.ok("welcome to tinyUrl service - ");
    }

    /**
     * Post api call creates short url for the given long url. eg: http://localhost:8082/shorten
     * @param longUrl
     * @return returns an Urlstore object {tinyrul, longurl, createdtime}
     * @throws Exception if tiny url can't be created
     */

    @PostMapping(value="/shorten")
    public ResponseEntity<Object> generateUrl(@RequestBody String longUrl) throws Exception{
        try{
            logger.info("Creating tinyurl for "+ longUrl);
            return ResponseEntity.ok(urlService.createShortUrl(longUrl));
        }catch (Exception e){
            logger.error("error in creating tinyurl"+ e.getMessage());
            return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get api call getting number of times the Url access in last 24 hours, last week or for all time;
     * eg:curl --location --request GET 'http://localhost:8082/stats/Xpnc0fym?days=10'
     * @param url  Path variable
     * @param days query param
     * @return StatsInfo of the url e.g: StatsInfo{url='Xpnc0fym', accessed=6, days='10'}
     */

    @GetMapping(value="/stats/{url}")

    public ResponseEntity<Object> getStats(@PathVariable String url, @RequestParam(name ="days",defaultValue = "0",required = false) String days){
        try{
            if(Integer.parseInt(days)<0){
                throw new IllegalArgumentException("days must be greater than or equal to zero");
            }
           return ResponseEntity.ok(urlService.getStats(url,Integer.parseInt(days)).toString());
        }catch(IllegalArgumentException iae){
            logger.error("number must be greater than zero" + iae.getMessage());
            return new ResponseEntity<>(iae.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            logger.error("error in creating tinyurl" + e.getMessage());
            return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * get api call eg:https://localhost:8082/xyk812ty. Redirects to the respective longurl
     * @param url
     * @return redirects to respective longurl
     */
    @GetMapping(value="/{url}")
    public ResponseEntity<Object> redirect(@PathVariable String url){
        try {
            logger.info("fetching the url for ",url);
            String longUrl = urlService.getLongUrl(url);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(longUrl));
            logger.info("redirecting to the url",longUrl);
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }catch (Exception e){
           logger.error("no url found for the given id",url,e.getLocalizedMessage());
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
