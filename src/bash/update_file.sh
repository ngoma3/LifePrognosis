#!/bin/bash

# Get the file path and lines from arguments
FILE_PATH=$1
shift  # Remove the first argument (file path) from the list

# Truncate the file (clears the content)
> "$FILE_PATH"

# Write each line to the file
for LINE in "$@"; do
    echo "$LINE" >> "$FILE_PATH"
done
