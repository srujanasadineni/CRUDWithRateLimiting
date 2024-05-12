package com.example.demo.controller;

import com.example.demo.entities.Device;
import com.example.demo.service.DeviceService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final Bucket bucket;

    public DeviceController() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(1, Duration.ofSeconds(10)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        if (bucket.tryConsume(1)) {
            Optional<Device> device = deviceService.getDeviceById(id);
            return device.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(deviceService.createDevice(device));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device device) {
        if (bucket.tryConsume(1)) {
            Optional<Device> existingDevice = deviceService.getDeviceById(id);
            if (existingDevice.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(deviceService.updateDevice(id, device));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        if (bucket.tryConsume(1)) {
            Optional<Device> existingDevice = deviceService.getDeviceById(id);
            if (existingDevice.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            deviceService.deleteDevice(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}

