# Spring Boot Rate Limiting Demo

This is a simple Spring Boot application demonstrating rate limiting and CRUD.

## Overview

The application provides endpoints for performing CRUD (Create, Read, Update, Delete) operations on devices, with rate limiting applied to control the number of requests allowed within a specified time interval.

## Requirements

- Java Development Kit (JDK) 22 or higher
- Apache Maven

## Build

1. Build the device using Maven:

   ```bash
   mvn clean package

## Usage

- Use curl commands or any REST client to interact with the endpoints.
- The application provides CRUD endpoints for managing devices:
- GET /devices: Retrieve all devices
- GET /devices/{id}: Retrieve a specific device by ID
- POST /devices: Create a new device
- PUT /devices/{id}: Update an existing device
- DELETE /devices/{id}: Delete a device

## Testing

- Please use the attached ```test.sh``` file to test the curd and rate limiter. 
