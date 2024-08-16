#!/bin/bash

FILE=$1
EMAIL=$2
UUID=$3

# Check if there is a line that contains both the UUID and EMAIL
FOUND=$(grep "^$UUID,$EMAIL" "$FILE")

if [[ "$FOUND" == "$UUID,$EMAIL"* ]]; then
    echo "exists"
    exit 0  # true
else
    echo "not exists"
    exit 1  # false
fi
