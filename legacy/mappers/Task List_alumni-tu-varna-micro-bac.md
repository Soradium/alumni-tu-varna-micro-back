# ✅ Task List: alumni-tu-varna-micro-back

## 🧩 DTO & Mapper Refactoring

- [ ] Refactor `AlumniMapper` to support new `AlumniDto`
- [ ] Update `DegreeMapper` for new `DegreeDto` structure
- [ ] Rewrite `AlumniGroupsMembershipMapper` to align with updated DTOs
- [ ] Ensure all mappers use constructor injection for better encapsulation
- [ ] Remove legacy DTO references from mappers

## 🛠️ Service Layer Updates

- [ ] Refactor `AlumniService` to use updated mappers and DTOs
- [ ] Update `FacultyService` to reflect new DTO contracts
- [ ] Review service interfaces for consistency with new DTOs
- [ ] Add unit tests for service methods using new DTOs

## 📡 Kafka Integration

- [ ] Create Kafka controller for alumni events using `AlumniService`
- [ ] Implement Kafka controller for degree updates via `DegreeService`
- [ ] Define Avro schemas for Kafka payloads (if applicable)
- [ ] Configure Kafka topics in `application.properties`
- [ ] Write integration tests for Kafka message flow

## 🧪 Testing & Validation

- [ ] Add unit tests for all rewritten mappers
- [ ] Add service-level tests for DTO transformation logic
- [ ] Write mock Kafka producer/consumer tests
- [ ] Validate schema compatibility with Avro (if used)

## 📦 Build & Documentation

- [ ] Verify Maven build after refactoring (`./mvnw clean package`)
- [ ] Run in dev mode (`./mvnw quarkus:dev`) to test live changes
- [ ] Document DTO changes and Kafka setup in `README.md`
- [ ] Update Mermaid diagrams to reflect new DTO and service relationships

---

🗓️ *Tip: Use `Ctrl+Shift+V` in VS Code to preview this file in Markdown.*
