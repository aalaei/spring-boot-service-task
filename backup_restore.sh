#!/bin/bash

# check $1 is backup or restore
if [ "$1" != "backup" ] && [ "$1" != "restore" ]; then
    echo "Usage: $0 backup|restore"
    exit 1
fi
mongp_url="mongodb://root:letmein@localhost:27017/serviceDB?authSource=admin"
dump_prefix="dump"

mkdir -p $dump_prefix
if [ "$1" == "backup" ]; then
    echo "Backup redis and mongo"
    redis-cli save
    redis-cli --rdb  ${dump_prefix}/dump.rdb
    mongodump --uri=${mongp_url} --gzip --archive=${dump_prefix}/mongo.dump 
else
    echo "Restore redis and mongo"
    cat $dump_prefix/redis.txt | redis-cli
    docker cp ${dump_prefix}/dump.rdb task3_redis:/data/dump.rdb
    # redis-cli shutdown && redis-server --dbfilename dump.rdb --dir /data
    mongorestore --uri="$mongp_url" --gzip --archive=${dump_prefix}/mongo.dump --drop
fi
