#!/bin/bash
# Check if redis-dump is installed
if ! [ -x "$(command -v redis-dump)" ]; then
    echo 'Error: redis-dump is not installed.' >&2
    echo 'Installing redis-dump' >&2
    sudo npm i redis-dump -g
fi

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
    redis-dump > $dump_prefix/redis.txt
    mongodump --uri=${mongp_url} --gzip --archive=${dump_prefix}/mongo.dump 
else
    echo "Restore redis and mongo"
    $redis_cli shutdown 
    cat $dump_prefix/redis.txt | redis-cli
    mongorestore --uri="$mongp_url" --gzip --archive=${dump_prefix}/mongo.dump --drop
fi
