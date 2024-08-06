#!/bin/bash

# Create an empty CSV file for all users
touch data/all_users.csv
echo "firstName,lastName,email,role" > data/all_users.csv

# Create an empty CSV file for analytics
touch data/analytics.csv
echo "metric,value" > data/analytics.csv
