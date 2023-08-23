#!/usr/bin/env bash

# use the same database as the one from the environment variables
dbname="$POSTGRES_DB"

# include custom config from the main config
# cat /var/lib/postgresql/data/postgresql.conf
conf=/var/lib/postgresql/data/postgresql.conf
echo "" >> $conf
echo "# pg_cron extension" >> $conf
echo "shared_preload_libraries = 'pg_cron'" >> $conf
echo "cron.database_name = '$dbname'" >> $conf