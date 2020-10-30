package com.cloudflare.entities;

import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.time.LocalDate;


import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

/**
 *  This class stores url_stats information in database
 */
@Table("url_stats")
public class UrlStats {
    @PrimaryKeyColumn(name = "accessed_date", type = PARTITIONED)
    private LocalDate accessedDate;

    @PrimaryKeyColumn(name = "urlid", ordinal =0)
    private String urlid;

    @PrimaryKeyColumn(name = "accessed_timestamp", ordinal =0)
    private Date accessedTimestamp;

    public UrlStats(LocalDate accessedDate, String urlid, Date accessedTimestamp) {
        this.accessedDate = accessedDate;
        this.urlid = urlid;
        this.accessedTimestamp = accessedTimestamp;
    }

    public LocalDate getAccessedDate() {
        return accessedDate;
    }

    public void setAccessedDate(LocalDate accessedDate) {
        this.accessedDate = accessedDate;
    }

    public String getUrlid() {
        return urlid;
    }

    public void setUrlid(String urlid) {
        this.urlid = urlid;
    }

    public Date getAccessedTimestamp() {
        return accessedTimestamp;
    }

    public void setAccessedTimestamp(Date accessedTimestamp) {
        this.accessedTimestamp = accessedTimestamp;
    }

    @Override
    public String toString() {
        return "UrlStats{" +
                "accessedDate=" + accessedDate +
                ", urlid='" + urlid + '\'' +
                ", accessedTimestamp=" + accessedTimestamp +
                '}';
    }
}
