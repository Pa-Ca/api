#!/bin/bash

# Parse command line arguments
while [[ $# -gt 0 ]]
do
    key="$1"
    
    case $key in
        -i|--init)
            INIT=true
            shift # past argument
        ;;
        *)    # unknown option
            POSITIONAL+=("$1") # save it in an array for later
            shift # past argument
        ;;
    esac
done
set -- "${POSITIONAL[@]}" # restore positional parameters

name=$1
timestamp=$(date +"%Y%m%d%H%M%S")

if [ "$INIT" = true ]; then
    prefix="R__"
else
    prefix="V"
fi

migration_name="${prefix}${timestamp}__${name}.sql"

echo $migration_name