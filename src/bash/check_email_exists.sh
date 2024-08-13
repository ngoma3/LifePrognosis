#!/bin/bash

FILE=$1
EMAIL=$2

if grep -q "$EMAIL" "$FILE"; then
    echo "exists"
else
    echo "not_exists"
fi
