#!/bin/bash

# Define file paths
USER_STORE="data/user-store.txt"
HEALTH_DATA="data/health-data.txt"
OUTPUT_CSV="data/all_users.csv"
ANALYTICS_CSV="data/analytics.csv"

# Create the CSV file for all users with the appropriate headers
echo "uuid,firstName,lastName,email,role,birthDate,hasChronicDisease,chronicDiseaseStartDate,vaccinated,vaccinationDate,country" > "$OUTPUT_CSV"

# Create the CSV file for analytics with the appropriate headers
echo "metric,value" > "$ANALYTICS_CSV"

# Loop through each line in user-store.txt (excluding the header if it exists)
tail -n +2 "$USER_STORE" | while IFS=',' read -r uuid firstName lastName email password salt role; do
    # Find the corresponding health data for the user based on UUID
    healthData=$(grep "^$uuid," "$HEALTH_DATA")

    # Extract health data fields
    if [ -n "$healthData" ]; then
        IFS=',' read -r _ birthDate hasChronicDisease chronicDiseaseStartDate vaccinated vaccinationDate country <<< "$healthData"
    else
        birthDate=""
        hasChronicDisease=""
        chronicDiseaseStartDate=""
        vaccinated=""
        vaccinationDate=""
        country=""
    fi

    # Append the combined user data to the CSV file
    echo "$uuid,$firstName,$lastName,$email,$role,$birthDate,$hasChronicDisease,$chronicDiseaseStartDate,$vaccinated,$vaccinationDate,$country" >> "$OUTPUT_CSV"
done

# Example analytics: Count total users
totalUsers=$(tail -n +2 "$USER_STORE" | wc -l)
echo "Total Users,$totalUsers" >> "$ANALYTICS_CSV"

# Example analytics: Count users with chronic disease
usersWithChronicDisease=$(grep ",true," "$HEALTH_DATA" | wc -l)
echo "Users with Chronic Disease,$usersWithChronicDisease" >> "$ANALYTICS_CSV"

# Add more analytics as needed

echo "Data extraction and analytics completed."
