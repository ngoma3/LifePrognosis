#!/bin/bash

# Define file paths
ANALYTICS_CSV="data/analytics.csv"

# Extract the summary arguments
totalMales=$1
totalFemales=$2
topCountries=$3

# Shift arguments to access country-specific data
shift 3

# Create the CSV file with the appropriate headers
echo "metric,value" > "$ANALYTICS_CSV"
echo "Total Males,$totalMales" >> "$ANALYTICS_CSV"
echo "Total Females,$totalFemales" >> "$ANALYTICS_CSV"
echo "Top 5 Countries with Most Patients,$topCountries" >> "$ANALYTICS_CSV"
echo "========================" >> "$ANALYTICS_CSV"

# Loop through the remaining arguments to process country-specific data
while [ "$#" -gt 0 ]; do
    country=$1
    averageLifespan=$2
    medianLifespan=$3
    percentile90=$4
    totalPatients=$5
    totalMales=$6
    totalFemales=$7
    ageGroups=$8

    # Append country-specific statistics
    echo "Country: $country" >> "$ANALYTICS_CSV"
    echo "Average Lifespan,$averageLifespan" >> "$ANALYTICS_CSV"
    echo "Median Lifespan,$medianLifespan" >> "$ANALYTICS_CSV"
    echo "90th Percentile Lifespan,$percentile90" >> "$ANALYTICS_CSV"
    echo "Total Patients,$totalPatients" >> "$ANALYTICS_CSV"
    echo "Total Males,$totalMales" >> "$ANALYTICS_CSV"
    echo "Total Females,$totalFemales" >> "$ANALYTICS_CSV"
    echo "Age Group Distribution,$ageGroups" >> "$ANALYTICS_CSV"
    echo "--------------------------" >> "$ANALYTICS_CSV"

    # Shift to the next set of arguments
    shift 8
done

echo "Data extraction and analytics completed."
