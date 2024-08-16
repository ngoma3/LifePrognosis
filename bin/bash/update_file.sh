#!/bin/bash

# Get the file path and lines from arguments
FILE_PATH=$1
shift  # Remove the first argument (file path) from the list

# Ensure the directory exists and has the right permissions
DIRECTORY=$(dirname "$FILE_PATH")
if [ ! -d "$DIRECTORY" ]; then
    echo "Directory $DIRECTORY does not exist."
    exit 1
fi

# Ensure the file has the correct permissions (read and write)
if [ ! -w "$FILE_PATH" ]; then
    echo "File $FILE_PATH is not writable. Attempting to set permissions..."
    chmod u+w "$FILE_PATH"
    if [ $? -ne 0 ]; then
        echo "Failed to set write permissions on $FILE_PATH."
        exit 1
    fi
fi

# Truncate the file (clears the content)
> "$FILE_PATH"

# Write each line to the file
for LINE in "$@"; do
    echo "$LINE" >> "$FILE_PATH"
done

echo "File updated successfully.
