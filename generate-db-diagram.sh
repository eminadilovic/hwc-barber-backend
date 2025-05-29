#!/bin/bash

# Download SchemaSpy
wget https://github.com/schemaspy/schemaspy/releases/download/v6.1.0/schemaspy-6.1.0.jar

# Download PostgreSQL JDBC driver
wget https://jdbc.postgresql.org/download/postgresql-42.7.2.jar

# Create output directory
mkdir -p db-diagram

# Run SchemaSpy
java -jar schemaspy-6.1.0.jar \
  -t pgsql \
  -dp postgresql-42.7.2.jar \
  -host localhost \
  -port 5432 \
  -db barber_shop \
  -u postgres \
  -p postgres \
  -o db-diagram \
  -vizjs \
  -all

echo "Database diagram generated in db-diagram directory" 