#!/bin/bash
# Generate [Lat, Lng] pairs from RTD schedule data

# Get files
wget http://www.rtd-denver.com/GoogleFeeder/google_transit.zip
unzip -p google_transit.zip shapes.txt > shapes.txt
unzip -p google_transit.zip trips.txt > trips.txt

# Parse
ROUTE="STMP"
cat trips.txt| grep "$ROUTE," | awk -F',' '{print $7}' | sort | uniq | tr -d '\r' | while read TRIP_ID; do 
  echo "$TRIP_ID" 
  cat shapes.txt | grep "$TRIP_ID" | awk -F ',' 'BEGIN{printf "["} {if (NR != 1) { printf ","}; printf "[\"%s\",\"%s\"]", $2, $3} END {printf "]"}'
  echo -e "\n"
done


# Cleanup
rm google_transit.zip shapes.txt trips.txt
