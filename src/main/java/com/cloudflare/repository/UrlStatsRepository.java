package com.cloudflare.repository;

import com.cloudflare.entities.UrlStats;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import java.time.Instant;


public interface UrlStatsRepository extends CassandraRepository<UrlStats,String> {

    @Query("select * from url_stats where urlid=?0 and accessed_timestamp >=?1 and accessed_timestamp <=?2 ALLOW FILTERING")
    ResultSet fetchStatsByHour(String shorturl, Instant currentDate, Instant minusHour);

    //query to fetch stats by day and url
    @Query("select * from url_stats where urlid = ?1 and accessed_timestamp >=?0 ALLOW FILTERING ")
    ResultSet fetchStatsByDay(Instant days,String shorturl);

    //query to fetch stats by url;
    @Query("select * from url_stats where urlid=?0 ALLOW FILTERING")
    ResultSet fetchStatsByUrl(String shorturl);

}
