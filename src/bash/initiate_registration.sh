#!/bin/bash

FILE=$1
EMAIL=$2
UUID=$3

# Check if the email already exists in the file
if grep -q "$EMAIL" "$FILE"; then
    echo "User is already initiated."
    exit 1
else
    echo "$UUID,$EMAIL" >> "$FILE"
    echo "User registration initiated."
fi
