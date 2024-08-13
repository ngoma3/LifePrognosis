#!/bin/bash
FILE=$1

if [ -f "$FILE" ]; then
    cat "$FILE"
fi
