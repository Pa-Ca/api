#!/usr/bin/env bash

# use the same database as the one from the environment variables
dbname="$POSTGRES_DB"

# create custom config
customconf=/var/lib/postgresql/data/custom-conf.conf
echo "" > $customconf
echo "shared_preload_libraries = 'pg_cron'" >> $customconf
echo "cron.database_name = '$dbname'" >> $customconf
chown postgres $customconf
chgrp postgres $customconf

# include custom config from the main config
conf=/var/lib/postgresql/data/postgresql.conf
echo "include = '$customconf'" >> $conf