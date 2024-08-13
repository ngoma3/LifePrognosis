#!/bin/bash

# Define file paths
USER_STORE="data/user-store.txt"
HEALTH_DATA="data/health-data.txt"

ANALYTICS_CSV="data/analytics.csv"


# Create the CSV file for analytics with the appropriate headers
echo "metric,value" > "$ANALYTICS_CSV"



# Example analytics: Count total users
totalUsers=$(tail -n +2 "$USER_STORE" | wc -l)
echo "Total Users,$totalUsers" >> "$ANALYTICS_CSV"

# Example analytics: Count users with chronic disease
usersWithChronicDisease=$(grep ",true," "$HEALTH_DATA" | wc -l)
echo "Users with Chronic Disease,$usersWithChronicDisease" >> "$ANALYTICS_CSV"

# Add more analytics as needed

echo "Data extraction and analytics completed."
