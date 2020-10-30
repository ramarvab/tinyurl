FROM cassandra:latest

ENV CASSANDRA_KEYSPACE cf_tinyurl
ENV TABLE url_store
ENV TABLE1 url_stats


COPY entrypoint-wrap.sh /entrypoint-wrap.sh
RUN ["chmod", "+x", "entrypoint-wrap.sh"]
ENTRYPOINT ["/entrypoint-wrap.sh"]
CMD ["cassandra", "-f"]
