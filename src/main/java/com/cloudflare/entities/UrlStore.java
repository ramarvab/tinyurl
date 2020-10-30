package com.cloudflare.entities;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.util.Date;

/**
 * Url store is the entity which is similar to the url_store table created in cassandra.
 * This entity is used to store, retrieve and insert data in cassandra.
 */
@Table("url_store")
public class UrlStore {

    @PrimaryKey
    private String shortUrl;

    private String longUrl;

    private Date creationDate;

    public UrlStore(){}

    public UrlStore(String shortUrl, String longUrl, Date creationDate) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.creationDate = creationDate;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "UrlStore{" +
                "shortUrl='" + shortUrl + '\'' +
                ", longUrl='" + longUrl + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
