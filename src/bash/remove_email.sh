#!/bin/bash
FILE=$1
EMAIL=$2

if [ -f "$FILE" ]; then
    sed -i "/$EMAIL/d" "$FILE"
fi
