#!/bin/bash

# File paths
USER_STORE="data/user-store.txt"

# Admin details
FIRST_NAME="Super"
LAST_NAME="Admin"
EMAIL="ngoma@gmail.com"
PASSWORD="123"  # You should change this to a secure password
ROLE="ADMIN"
UUID=$(uuidgen)
SALT=$(openssl rand -base64 16)
HASHED_PASSWORD=$(echo -n "$PASSWORD" | openssl dgst -sha256 -binary -salt "$SALT" | base64)

# Check if the first admin already exists
if grep -q "$EMAIL" "$USER_STORE"; then
  echo "First admin already exists."
else
  # Append the first admin to the user-store file
  echo "$FIRST_NAME,$LAST_NAME,$EMAIL,$UUID,$ROLE,$HASHED_PASSWORD,$SALT" >> "$USER_STORE"
  echo "First admin created successfully."
fi
