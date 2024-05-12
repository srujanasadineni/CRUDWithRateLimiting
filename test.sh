#!/bin/bash

# Function to send curl request and display output
send_request() {
    local response=$(curl -s -o response.txt -w "%{http_code}" -X POST -H "Content-Type: application/json" -d '{"name":"Device'$1'","price":10.99}' http://localhost:8080/devices)
    if [ "$response" == "429" ]; then
        echo "Rate limit exceeded."
    else
        if [ -s "response.txt" ]; then
            cat response.txt
        else
            echo "No response received."
        fi
    fi
    rm -f response.txt  # Remove the response.txt file after reading
}

echo "Send multiple requests to create devices, exceeding the rate limit"
for i in {1..100}; do
    send_request $i
done
# Expected output: RuntimeException indicating rate limit exceeded

# Wait for the rate limit interval to elapse
sleep 30  # Adjust the sleep time according to your rate limit interval

echo "Send request to UPDATE the device"
curl --location --request PUT 'http://localhost:8080/devices/2' \
--header 'Content-Type: application/json' \
--data '{"name":"UpdatedProduct","price":15.99}'
# Expected output: Device 2 will be updated.

echo "Send request to DELETE"
curl --location --request DELETE 'http://localhost:8080/devices/1'
# Expected output: Device 1 will be deleted.

echo "Send requests within the rate limit to list all the devices"
curl http://localhost:8080/devices
# Expected output: Get the list of devices

echo "Send request to get a single device"
curl --location --request GET 'http://localhost:8080/devices/3'
# Expected output: Get a single device

echo "Send requests exceeding the rate limit again"
for i in {1..100}; do
    send_request $i
done