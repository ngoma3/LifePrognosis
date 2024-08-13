#!/bin/bash

FILE=$1
EMAIL=$2

# Check if email exists in the file
if grep -q "$EMAIL" "$FILE"; then
    echo "exists"
else
    echo "$EMAIL" >> "$FILE"
    echo "appended"
fi
