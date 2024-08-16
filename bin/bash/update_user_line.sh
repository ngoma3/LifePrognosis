#!/bin/bash

FILE=$1
UUID=$2
UPDATED_LINE=$3

if grep -q "^$UUID," "$FILE"; then
    # Use sed to find the line starting with the UUID and replace it without creating a backup
    sed -i "/^$UUID,/c\\$UPDATED_LINE,PATIENT" "$FILE"
    echo "success"
else
    echo "not found"
fi
