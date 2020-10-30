package com.cloudflare.repository;

import com.cloudflare.entities.UrlStore;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

/**
 * Repository class which is useful for leveraging spring data cassandra crud operations.
 */
public interface URLRepository extends CassandraRepository<UrlStore,String> {

    @Query("Select * from url_store where shorturl=?0")
    UrlStore fetchBysurl(String shorturl);
}
