package com.cloudflare.service;

import com.cloudflare.entities.UrlStore;

/**
 * Interface for UrlService implementation class
 */
public interface UrlService {

    UrlStore createShortUrl(String longUrl) throws Exception;

    String getLongUrl (String shortUrl) throws Exception;


}
