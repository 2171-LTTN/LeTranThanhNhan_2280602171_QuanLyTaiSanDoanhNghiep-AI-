package com.lttn.quanlytaisan.config;

import com.lttn.quanlytaisan.model.*;
import com.lttn.quanlytaisan.repository.AssetHistoryRepository;
import com.lttn.quanlytaisan.repository.AssetRepository;
import com.lttn.quanlytaisan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DataSeeder - Seeds the database with production-like data.
 * Only runs when "dev" or "seed" profile is active.
 *
 * To run: Add @ActiveProfiles("seed") or run with --spring.profiles.active=seed
 * Or call directly via API endpoint.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeederConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            AssetRepository assetRepository,
            AssetHistoryRepository assetHistoryRepository
    ) {
        return args -> {
            // Only seed if database is empty
            if (userRepository.count() > 0) {
                log.info("Database already has data. Skipping seed.");
                return;
            }

            log.info("=== Starting Database Seeding ===");
            long startTime = System.currentTimeMillis();

            // ========================================
            // STEP 1: Create Users (5 users)
            // ========================================
            log.info("Creating users...");

            User admin = User.builder()
                    .id("user-admin-001")
                    .name("Nguyen Van Admin")
                    .email("admin@company.com")
                    .password(passwordEncoder.encode("password123"))
                    .role("ADMIN")
                    .department("IT")
                    .position("IT Manager")
                    .phone("0912-345-678")
                    .isActive(true)
                    .createdAt(LocalDateTime.of(2024, 1, 15, 8, 0))
                    .build();

            User emp1 = User.builder()
                    .id("user-emp-001")
                    .name("Tran Thi Mai")
                    .email("mai.tran@company.com")
                    .password(passwordEncoder.encode("password123"))
                    .role("USER")
                    .department("Marketing")
                    .position("Marketing Specialist")
                    .phone("0934-567-890")
                    .isActive(true)
                    .createdAt(LocalDateTime.of(2024, 2, 1, 9, 0))
                    .build();

            User emp2 = User.builder()
                    .id("user-emp-002")
                    .name("Le Van Hoang")
                    .email("hoang.le@company.com")
                    .password(passwordEncoder.encode("password123"))
                    .role("USER")
                    .department("Development")
                    .position("Senior Developer")
                    .phone("0945-678-901")
                    .isActive(true)
                    .createdAt(LocalDateTime.of(2024, 2, 15, 10, 0))
                    .build();

            User emp3 = User.builder()
                    .id("user-emp-003")
                    .name("Pham Thi Lan")
                    .email("lan.pham@company.com")
                    .password(passwordEncoder.encode("password123"))
                    .role("USER")
                    .department("Finance")
                    .position("Accountant")
                    .phone("0956-789-012")
                    .isActive(true)
                    .createdAt(LocalDateTime.of(2024, 3, 1, 8, 30))
                    .build();

            User emp4 = User.builder()
                    .id("user-emp-004")
                    .name("Hoang Van Duc")
                    .email("duc.hoang@company.com")
                    .password(passwordEncoder.encode("password123"))
                    .role("USER")
                    .department("HR")
                    .position("HR Officer")
                    .phone("0967-890-123")
                    .isActive(true)
                    .createdAt(LocalDateTime.of(2024, 3, 15, 11, 0))
                    .build();

            userRepository.save(admin);
            userRepository.save(emp1);
            userRepository.save(emp2);
            userRepository.save(emp3);
            userRepository.save(emp4);

            log.info("Created 5 users successfully.");

            // ========================================
            // STEP 2: Create Assets (10 assets)
            // ========================================
            log.info("Creating assets...");

            // Laptops
            Asset laptop1 = Asset.builder()
                    .id("asset-lap-001")
                    .name("MacBook Pro 14-inch M3")
                    .category("Laptop")
                    .status(AssetStatus.IN_USE)
                    .assignedTo("user-emp-002")
                    .purchaseDate(LocalDate.of(2024, 6, 15))
                    .purchasePrice(45000000.0)
                    .serialNumber("C02XLAPTOP001")
                    .brand("Apple")
                    .model("MacBook Pro 14")
                    .warrantyUntil(LocalDate.of(2027, 6, 15))
                    .location("Office Floor 3")
                    .createdAt(LocalDateTime.of(2024, 6, 15, 10, 0))
                    .updatedAt(LocalDateTime.of(2024, 9, 1, 14, 0))
                    .build();

            Asset laptop2 = Asset.builder()
                    .id("asset-lap-002")
                    .name("Dell XPS 15")
                    .category("Laptop")
                    .status(AssetStatus.AVAILABLE)
                    .assignedTo(null)
                    .purchaseDate(LocalDate.of(2024, 3, 20))
                    .purchasePrice(32000000.0)
                    .serialNumber("DELLXPS15002")
                    .brand("Dell")
                    .model("XPS 15 9530")
                    .warrantyUntil(LocalDate.of(2027, 3, 20))
                    .location("IT Storage Room")
                    .createdAt(LocalDateTime.of(2024, 3, 20, 9, 0))
                    .build();

            Asset laptop3 = Asset.builder()
                    .id("asset-lap-003")
                    .name("ThinkPad X1 Carbon Gen 11")
                    .category("Laptop")
                    .status(AssetStatus.IN_USE)
                    .assignedTo("user-emp-001")
                    .purchaseDate(LocalDate.of(2024, 5, 10))
                    .purchasePrice(28000000.0)
                    .serialNumber("LNVX1CARB003")
                    .brand("Lenovo")
                    .model("ThinkPad X1 Carbon")
                    .warrantyUntil(LocalDate.of(2027, 5, 10))
                    .location("Marketing Office")
                    .createdAt(LocalDateTime.of(2024, 5, 10, 11, 0))
                    .updatedAt(LocalDateTime.of(2024, 8, 1, 9, 0))
                    .build();

            Asset laptop4 = Asset.builder()
                    .id("asset-lap-004")
                    .name("HP EliteBook 840 G10")
                    .category("Laptop")
                    .status(AssetStatus.BROKEN)
                    .assignedTo(null)
                    .purchaseDate(LocalDate.of(2023, 12, 1))
                    .purchasePrice(25000000.0)
                    .serialNumber("HP840G10004")
                    .brand("HP")
                    .model("EliteBook 840 G10")
                    .warrantyUntil(LocalDate.of(2026, 12, 1))
                    .location("IT Repair Shop")
                    .note("Screen damaged - awaiting repair")
                    .createdAt(LocalDateTime.of(2023, 12, 1, 8, 0))
                    .updatedAt(LocalDateTime.of(2024, 11, 20, 16, 0))
                    .build();

            // Monitors
            Asset monitor1 = Asset.builder()
                    .id("asset-mon-001")
                    .name("Dell UltraSharp U2723QE 27\"")
                    .category("Monitor")
                    .status(AssetStatus.IN_USE)
                    .assignedTo("user-emp-002")
                    .purchaseDate(LocalDate.of(2024, 6, 15))
                    .purchasePrice(15000000.0)
                    .serialNumber("DELU2723QE01")
                    .brand("Dell")
                    .model("U2723QE")
                    .warrantyUntil(LocalDate.of(2027, 6, 15))
                    .location("Development Floor 3")
                    .createdAt(LocalDateTime.of(2024, 6, 15, 10, 30))
                    .updatedAt(LocalDateTime.of(2024, 9, 1, 14, 30))
                    .build();

            Asset monitor2 = Asset.builder()
                    .id("asset-mon-002")
                    .name("LG UltraFine 27UN880")
                    .category("Monitor")
                    .status(AssetStatus.AVAILABLE)
                    .assignedTo(null)
                    .purchaseDate(LocalDate.of(2024, 4, 1))
                    .purchasePrice(12000000.0)
                    .serialNumber("LG27UN88002")
                    .brand("LG")
                    .model("UltraFine 27UN880")
                    .warrantyUntil(LocalDate.of(2027, 4, 1))
                    .location("IT Storage Room")
                    .createdAt(LocalDateTime.of(2024, 4, 1, 9, 0))
                    .build();

            Asset monitor3 = Asset.builder()
                    .id("asset-mon-003")
                    .name("Samsung Odyssey G7 32\"")
                    .category("Monitor")
                    .status(AssetStatus.IN_USE)
                    .assignedTo("user-emp-003")
                    .purchaseDate(LocalDate.of(2024, 7, 20))
                    .purchasePrice(22000000.0)
                    .serialNumber("SAMG7LC003")
                    .brand("Samsung")
                    .model("Odyssey G7 LC32G75T")
                    .warrantyUntil(LocalDate.of(2027, 7, 20))
                    .location("Finance Office")
                    .createdAt(LocalDateTime.of(2024, 7, 20, 11, 0))
                    .updatedAt(LocalDateTime.of(2024, 10, 1, 10, 0))
                    .build();

            // Phones
            Asset phone1 = Asset.builder()
                    .id("asset-phn-001")
                    .name("iPhone 15 Pro")
                    .category("Phone")
                    .status(AssetStatus.IN_USE)
                    .assignedTo("user-emp-004")
                    .purchaseDate(LocalDate.of(2024, 9, 15))
                    .purchasePrice(35000000.0)
                    .serialNumber("IP15PRO001")
                    .brand("Apple")
                    .model("iPhone 15 Pro")
                    .warrantyUntil(LocalDate.of(2026, 9, 15))
                    .location("HR Office")
                    .createdAt(LocalDateTime.of(2024, 9, 15, 9, 0))
                    .updatedAt(LocalDateTime.of(2024, 9, 20, 14, 0))
                    .build();

            Asset phone2 = Asset.builder()
                    .id("asset-phn-002")
                    .name("Samsung Galaxy S24 Ultra")
                    .category("Phone")
                    .status(AssetStatus.AVAILABLE)
                    .assignedTo(null)
                    .purchaseDate(LocalDate.of(2024, 10, 1))
                    .purchasePrice(30000000.0)
                    .serialNumber("SAMGS24U002")
                    .brand("Samsung")
                    .model("Galaxy S24 Ultra")
                    .warrantyUntil(LocalDate.of(2026, 10, 1))
                    .location("IT Storage Room")
                    .createdAt(LocalDateTime.of(2024, 10, 1, 10, 0))
                    .build();

            // Accessory
            Asset accessory1 = Asset.builder()
                    .id("asset-acc-001")
                    .name("Logitech MX Master 3S Mouse")
                    .category("Accessory")
                    .status(AssetStatus.IN_USE)
                    .assignedTo("user-emp-001")
                    .purchaseDate(LocalDate.of(2024, 8, 10))
                    .purchasePrice(3500000.0)
                    .serialNumber("LOGMX3S001")
                    .brand("Logitech")
                    .model("MX Master 3S")
                    .warrantyUntil(LocalDate.of(2026, 8, 10))
                    .location("Marketing Office")
                    .createdAt(LocalDateTime.of(2024, 8, 10, 9, 0))
                    .updatedAt(LocalDateTime.of(2024, 8, 15, 11, 0))
                    .build();

            assetRepository.save(laptop1);
            assetRepository.save(laptop2);
            assetRepository.save(laptop3);
            assetRepository.save(laptop4);
            assetRepository.save(monitor1);
            assetRepository.save(monitor2);
            assetRepository.save(monitor3);
            assetRepository.save(phone1);
            assetRepository.save(phone2);
            assetRepository.save(accessory1);

            log.info("Created 10 assets successfully.");

            // ========================================
            // STEP 3: Create Asset Histories (19 entries)
            // ========================================
            log.info("Creating asset histories...");

            // MacBook Pro - Assigned to Hoang
            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-001")
                    .assetId("asset-lap-001")
                    .assetName("MacBook Pro 14-inch M3")
                    .performedBy("admin@company.com")
                    .action(AssetAction.CREATED)
                    .details("Asset 'MacBook Pro 14-inch M3' (Category: Laptop) created")
                    .timestamp(LocalDateTime.of(2024, 6, 15, 10, 0))
                    .build());

            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-002")
                    .assetId("asset-lap-001")
                    .assetName("MacBook Pro 14-inch M3")
                    .userId("user-emp-002")
                    .userName("Le Van Hoang")
                    .performedBy("admin@company.com")
                    .action(AssetAction.ASSIGNED)
                    .details("Asset assigned to Le Van Hoang")
                    .timestamp(LocalDateTime.of(2024, 9, 1, 14, 0))
                    .build());

            // Dell XPS - Available
            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-003")
                    .assetId("asset-lap-002")
                    .assetName("Dell XPS 15")
                    .performedBy("admin@company.com")
                    .action(AssetAction.CREATED)
                    .details("Asset 'Dell XPS 15' (Category: Laptop) created")
                    .timestamp(LocalDateTime.of(2024, 3, 20, 9, 0))
                    .build());

            // ThinkPad - Assigned to Mai
            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-004")
                    .assetId("asset-lap-003")
                    .assetName("ThinkPad X1 Carbon Gen 11")
                    .performedBy("admin@company.com")
                    .action(AssetAction.CREATED)
                    .details("Asset 'ThinkPad X1 Carbon Gen 11' (Category: Laptop) created")
                    .timestamp(LocalDateTime.of(2024, 5, 10, 11, 0))
                    .build());

            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-005")
                    .assetId("asset-lap-003")
                    .assetName("ThinkPad X1 Carbon Gen 11")
                    .userId("user-emp-001")
                    .userName("Tran Thi Mai")
                    .performedBy("admin@company.com")
                    .action(AssetAction.ASSIGNED)
                    .details("Asset assigned to Tran Thi Mai")
                    .timestamp(LocalDateTime.of(2024, 8, 1, 9, 0))
                    .build());

            // HP EliteBook - Was assigned, returned, now broken
            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-006")
                    .assetId("asset-lap-004")
                    .assetName("HP EliteBook 840 G10")
                    .performedBy("admin@company.com")
                    .action(AssetAction.CREATED)
                    .details("Asset 'HP EliteBook 840 G10' (Category: Laptop) created")
                    .timestamp(LocalDateTime.of(2023, 12, 1, 8, 0))
                    .build());

            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-007")
                    .assetId("asset-lap-004")
                    .assetName("HP EliteBook 840 G10")
                    .userId("user-emp-003")
                    .userName("Pham Thi Lan")
                    .performedBy("admin@company.com")
                    .action(AssetAction.ASSIGNED)
                    .details("Asset assigned to Pham Thi Lan")
                    .timestamp(LocalDateTime.of(2024, 2, 1, 10, 0))
                    .build());

            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-008")
                    .assetId("asset-lap-004")
                    .assetName("HP EliteBook 840 G10")
                    .userId("user-emp-003")
                    .userName("Pham Thi Lan")
                    .performedBy("admin@company.com")
                    .action(AssetAction.RETURNED)
                    .details("Asset returned by Pham Thi Lan")
                    .timestamp(LocalDateTime.of(2024, 11, 20, 16, 0))
                    .build());

            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-009")
                    .assetId("asset-lap-004")
                    .assetName("HP EliteBook 840 G10")
                    .performedBy("admin@company.com")
                    .action(AssetAction.UPDATED)
                    .details("Status: AVAILABLE -> BROKEN; Screen damaged - awaiting repair")
                    .timestamp(LocalDateTime.of(2024, 11, 20, 16, 30))
                    .build());

            // Dell Monitor - Assigned to Hoang
            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-010")
                    .assetId("asset-mon-001")
                    .assetName("Dell UltraSharp U2723QE 27\"")
                    .performedBy("admin@company.com")
                    .action(AssetAction.CREATED)
                    .details("Asset 'Dell UltraSharp U2723QE 27\"' (Category: Monitor) created")
                    .timestamp(LocalDateTime.of(2024, 6, 15, 10, 30))
                    .build());

            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-011")
                    .assetId("asset-mon-001")
                    .assetName("Dell UltraSharp U2723QE 27\"")
                    .userId("user-emp-002")
                    .userName("Le Van Hoang")
                    .performedBy("admin@company.com")
                    .action(AssetAction.ASSIGNED)
                    .details("Asset assigned to Le Van Hoang")
                    .timestamp(LocalDateTime.of(2024, 9, 1, 14, 30))
                    .build());

            // LG Monitor - Available
            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-012")
                    .assetId("asset-mon-002")
                    .assetName("LG UltraFine 27UN880")
                    .performedBy("admin@company.com")
                    .action(AssetAction.CREATED)
                    .details("Asset 'LG UltraFine 27UN880' (Category: Monitor) created")
                    .timestamp(LocalDateTime.of(2024, 4, 1, 9, 0))
                    .build());

            // Samsung Monitor - Assigned to Lan
            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-013")
                    .assetId("asset-mon-003")
                    .assetName("Samsung Odyssey G7 32\"")
                    .performedBy("admin@company.com")
                    .action(AssetAction.CREATED)
                    .details("Asset 'Samsung Odyssey G7 32\"' (Category: Monitor) created")
                    .timestamp(LocalDateTime.of(2024, 7, 20, 11, 0))
                    .build());

            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-014")
                    .assetId("asset-mon-003")
                    .assetName("Samsung Odyssey G7 32\"")
                    .userId("user-emp-003")
                    .userName("Pham Thi Lan")
                    .performedBy("admin@company.com")
                    .action(AssetAction.ASSIGNED)
                    .details("Asset assigned to Pham Thi Lan")
                    .timestamp(LocalDateTime.of(2024, 10, 1, 10, 0))
                    .build());

            // iPhone - Assigned to Duc
            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-015")
                    .assetId("asset-phn-001")
                    .assetName("iPhone 15 Pro")
                    .performedBy("admin@company.com")
                    .action(AssetAction.CREATED)
                    .details("Asset 'iPhone 15 Pro' (Category: Phone) created")
                    .timestamp(LocalDateTime.of(2024, 9, 15, 9, 0))
                    .build());

            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-016")
                    .assetId("asset-phn-001")
                    .assetName("iPhone 15 Pro")
                    .userId("user-emp-004")
                    .userName("Hoang Van Duc")
                    .performedBy("admin@company.com")
                    .action(AssetAction.ASSIGNED)
                    .details("Asset assigned to Hoang Van Duc")
                    .timestamp(LocalDateTime.of(2024, 9, 20, 14, 0))
                    .build());

            // Samsung Phone - Available
            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-017")
                    .assetId("asset-phn-002")
                    .assetName("Samsung Galaxy S24 Ultra")
                    .performedBy("admin@company.com")
                    .action(AssetAction.CREATED)
                    .details("Asset 'Samsung Galaxy S24 Ultra' (Category: Phone) created")
                    .timestamp(LocalDateTime.of(2024, 10, 1, 10, 0))
                    .build());

            // Mouse - Assigned to Mai
            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-018")
                    .assetId("asset-acc-001")
                    .assetName("Logitech MX Master 3S Mouse")
                    .performedBy("admin@company.com")
                    .action(AssetAction.CREATED)
                    .details("Asset 'Logitech MX Master 3S Mouse' (Category: Accessory) created")
                    .timestamp(LocalDateTime.of(2024, 8, 10, 9, 0))
                    .build());

            assetHistoryRepository.save(AssetHistory.builder()
                    .id("hist-019")
                    .assetId("asset-acc-001")
                    .assetName("Logitech MX Master 3S Mouse")
                    .userId("user-emp-001")
                    .userName("Tran Thi Mai")
                    .performedBy("admin@company.com")
                    .action(AssetAction.ASSIGNED)
                    .details("Asset assigned to Tran Thi Mai")
                    .timestamp(LocalDateTime.of(2024, 8, 15, 11, 0))
                    .build());

            log.info("Created 19 asset history entries successfully.");

            // ========================================
            // STEP 4: Summary
            // ========================================
            long endTime = System.currentTimeMillis();

            log.info("=== Database Seeding Complete ===");
            log.info("Total users: {}", userRepository.count());
            log.info("Total assets: {}", assetRepository.count());
            log.info("Total histories: {}", assetHistoryRepository.count());
            log.info("Time taken: {} ms", (endTime - startTime));
            log.info("");
            log.info("=== LOGIN CREDENTIALS ===");
            log.info("Admin: admin@company.com / password123");
            log.info("User:  mai.tran@company.com / password123");
            log.info("");
            log.info("=== ASSET STATUS SUMMARY ===");
            log.info("In Use: {}", assetRepository.findByStatus(AssetStatus.IN_USE,
                org.springframework.data.domain.Pageable.unpaged()).getTotalElements());
            log.info("Available: {}", assetRepository.findByStatus(AssetStatus.AVAILABLE,
                org.springframework.data.domain.Pageable.unpaged()).getTotalElements());
            log.info("Broken: {}", assetRepository.findByStatus(AssetStatus.BROKEN,
                org.springframework.data.domain.Pageable.unpaged()).getTotalElements());
        };
    }
}
