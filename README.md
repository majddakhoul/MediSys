<div align="center">

<br/>

<h1>Simple Medical System</h1>

<p><strong>A hospital management system, implemented twice over</strong><br/>
Doctors, patients, clinics, and treatments with full CRUD, polymorphic entity hierarchies,
and file-backed persistence — driven from a terminal menu or a native desktop GUI.</p>

<p>
  <img src="https://img.shields.io/badge/editions-2-blue?style=flat-square" alt="Editions"/>
  <img src="https://img.shields.io/badge/desktop-Java%20Swing-orange?style=flat-square&logo=openjdk&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/persistence-Java%20Serialization-6f42c1?style=flat-square" alt="Persistence"/>
  <img src="https://img.shields.io/badge/license-MIT-orange?style=flat-square" alt="License"/>
</p>

<br/>

</div>

---

## Table of Contents

- [Editions](#editions)
- [Shared Feature Set](#shared-feature-set)
- [Quick Start](#quick-start)
- [Domain Model](#domain-model)
- [Diagrams](#diagrams)
- [What Was Fixed](#what-was-fixed)
- [Project Structure](#project-structure)
- [Documentation Index](#documentation-index)
- [License](#license)

---

## Editions

| Edition | Stack | Run it | Docs |
|---|---|---|---|
| **[Java GUI Edition](Java-GUI-Edition/)** | Java Swing, JDK 17+ | `javac` + `java Main` — see docs | [`Java-GUI-Edition/docs/`](Java-GUI-Edition/docs/README.md) |
| **[Original Console Engine](original-java-source/)** | Plain Java, JDK 17+ | `java Main` (interactive menu) | [`original-java-source/docs/`](original-java-source/docs/README.md) |

Both editions share the exact same domain model (`doctors`, `patient`, `treatment`, `hospital`
packages) and the exact same bug fixes — pick the interactive desktop app for day-to-day data
entry, or the console engine for a minimal, dependency-free reference implementation.

---

## Shared Feature Set

- **Full CRUD** for clinics, doctors, patients, and treatments — Create, Read, **Update**, and
  Delete, for every entity (the original console engine only had Create/Read/Delete; Update was
  missing entirely — see [What Was Fixed](#what-was-fixed)).
- **Four doctor subtypes** (Regular, Contracted, Trainer, Inner) and **three patient subtypes**
  (Regular, External, Internal), each with its own extra fields, fully reachable from both
  editions.
- **Type-aware treatments**: Internal patients get `InternalTreatment` (department id), External
  patients get `ExternalTreatment` (doctor + clinic), plain patients get a base `Treatment` — the
  domain model rejects any mismatched pairing.
- **File-backed persistence** via plain Java serialization — no database, no external
  dependencies. The GUI has a File menu (New / Save / Save As / Open); the console has
  Save/Load menu items.
- **Validated input everywhere** — numbers, dates (`YYYY-MM-DD`), and required fields are checked
  before being accepted, in both editions, so bad input never crashes the app or corrupts data.

---

## Quick Start

```bash
# Java GUI Edition
cd Java-GUI-Edition
mkdir out && javac -d out $(find src -name "*.java")
java -cp out Main

# Original console engine
cd original-java-source
mkdir out && javac -d out $(find src -name "*.java")
java -cp out Main
```

---

## Domain Model

| Package | Base class | Subtypes |
|---|---|---|
| `doctors` | `Doctor` | `ContractedDoctor`, `TrainerDoctor`, `InnerDoctor` |
| `patient` | `Patient` | `ExternalPatient`, `InternalPatient` |
| `treatment` | `Treatment` | `InternalTreatment`, `ExternalTreatment` |
| `hospital` | `Hospital` (aggregate root), `Clinic` | — |

Full field-by-field detail is in each edition's `docs/DOMAIN.md`
([Java GUI](Java-GUI-Edition/docs/DOMAIN.md) · [console](original-java-source/docs/DOMAIN.md)).

---

## Diagrams

Static HTML/CSS reference diagrams for the whole domain model, shared by both editions:

- **[Diagrams Home](diagrams/index.html)**
- **[Entity-Relationship Diagram](diagrams/erd.html)** — entities, keys, and ISA hierarchies.
- **[UML Class Diagram](diagrams/class-diagram.html)** — the same hierarchy as real Java classes,
  with fields, key methods, and inheritance/association arrows.

---

## What Was Fixed

The original engine had several real gaps between what it advertised and what it actually did.
Both editions now share the fixes (full details in
[`original-java-source/docs/README.md`](original-java-source/docs/README.md)):

- **`InnerDoctor` was unreachable** — the class existed but no menu option ever created one.
- **`ExternalTreatment.cliID` was never set** — every external treatment silently pointed at
  clinic `0`. The clinic is now a required, validated part of creating one.
- **"Full CRUD" wasn't full** — every field had a setter, but nothing ever called them; there was
  no way to edit a doctor, patient, or clinic once created, and `InternalPatient`'s discharge
  fields were completely unreachable. Both editions now expose real Edit functionality.
- **Bad input crashed the program** — unhandled `NumberFormatException` / `DateTimeParseException`
  on any malformed input. Both editions now validate and re-prompt / show an inline error instead.
- **All data was lost on exit** — nothing was ever persisted. Both editions now support
  Save/Load via Java serialization.

---

## Project Structure

```text
MediSys-Complete/
├── Java-GUI-Edition/
│   ├── src/
│   │   ├── Main.java
│   │   ├── doctors/ patient/ treatment/ hospital/
│   │   └── gui/
│   │       ├── MainFrame.java  Theme.java  DataManager.java
│   │       ├── panels/         ClinicsPanel, DoctorsPanel, PatientsPanel, TreatmentsPanel
│   │       └── dialogs/        ClinicDialog, DoctorDialog, PatientDialog, TreatmentDialog
│   └── docs/
│       ├── README.md
│       └── DOMAIN.md
├── original-java-source/
│   ├── src/
│   │   ├── Main.java
│   │   └── doctors/ patient/ treatment/ hospital/
│   └── docs/
│       ├── README.md
│       └── DOMAIN.md
├── diagrams/
│   ├── index.html
│   ├── erd.html
│   ├── class-diagram.html
│   └── css/diagrams.css
├── LICENSE
└── README.md            <- you are here
```

---

## Documentation Index

| Edition | Overview & build/run | Domain model |
|---|---|---|
| Java GUI | [README](Java-GUI-Edition/docs/README.md) | [DOMAIN](Java-GUI-Edition/docs/DOMAIN.md) |
| Console engine | [README](original-java-source/docs/README.md) | [DOMAIN](original-java-source/docs/DOMAIN.md) |

| Diagram | Link |
|---|---|
| Diagrams home | [diagrams/index.html](diagrams/index.html) |
| Entity-Relationship Diagram | [diagrams/erd.html](diagrams/erd.html) |
| UML Class Diagram | [diagrams/class-diagram.html](diagrams/class-diagram.html) |

---

## License

This project is released under the MIT License.

See the [`LICENSE`](LICENSE) file for full licensing details.
