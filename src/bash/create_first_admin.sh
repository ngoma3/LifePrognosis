#!/bin/bash

# File paths
USER_STORE="data/user-store.txt"

# Admin details
FIRST_NAME="$1"
LAST_NAME="$2"
EMAIL="$3"
ROLE="ADMIN"
UUID="$6"
HASHED_PASSWORD="$4"
SALT="$5"

# Hash the password with the salt


# Check if the first admin already exists
if grep -q "$EMAIL" "$USER_STORE"; then
  echo "First admin already exists."
else
  # Append the first admin to the user-store file  
  echo "$UUID,$FIRST_NAME,$LAST_NAME,$EMAIL,$HASHED_PASSWORD,$SALT,$ROLE" >> "$USER_STORE"
  echo "First admin created successfully."
fi
