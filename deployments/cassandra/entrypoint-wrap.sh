#!/bin/bash

if [[ ! -z "$CASSANDRA_KEYSPACE" && $1 = 'cassandra' ]]; then
  # Create default keyspace for single node cluster
  CQL="CREATE KEYSPACE $CASSANDRA_KEYSPACE WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1}; USE $CASSANDRA_KEYSPACE;
  CREATE TABLE $TABLE(shortUrl Text PRIMARY KEY, longUrl Text, creationDate timestamp);
  create Table $TABLE1(urlid Text,accessed_date date, accessed_timestamp timestamp, primary key ((accessed_date),urlid,accessed_timestamp));"
  until echo $CQL | cqlsh; do
    echo "cqlsh: Cassandra is unavailable - retry later"
    sleep 10
  done &
fi

exec /docker-entrypoint.sh "$@"

