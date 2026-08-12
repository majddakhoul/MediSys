# MediSys — Java Swing Edition

A native desktop GUI for MediSys, built directly on top of the same
(fixed) domain model as the console engine: full CRUD for clinics,
doctors, patients, and treatments, with a proper file-backed Save/Open
workflow instead of a text menu.

## Features

- **Tabbed interface**: Clinics, Doctors, Patients, Treatments — each with
  a sortable table, and Add / Edit / Delete toolbar buttons.
- **All four doctor types** (Regular, Contracted, Trainer, Inner) and
  **all three patient types** (Regular, External, Internal) are creatable
  and editable, with type-specific fields shown dynamically in the dialog
  (contract date, training dates, department number, acceptance /
  discharge status).
- **Treatments tab**: pick a patient from the dropdown, see their
  treatments in a table, add a new one (the dialog only asks for fields
  relevant to that patient's type — department id for Internal patients,
  doctor + clinic pickers for External patients), or delete one.
- **File menu**: New, Save, Save As, Open, Exit — powered by plain Java
  serialization (`.dat` files), so a hospital's full data set survives
  between sessions.
- **Validated input everywhere**: every dialog checks required fields,
  numeric fields, and dates (`YYYY-MM-DD`) before accepting, with an
  inline error message instead of a crash or a silently-wrong entry.
- Pure Java SE + Swing. No external dependencies, no build tool required.

## Project structure

```
Java-GUI-Edition/
├── src/
│   ├── Main.java                          Application entry point
│   ├── doctors/                           Doctor, ContractedDoctor, InnerDoctor, TrainerDoctor
│   ├── patient/                           Patient, ExternalPatient, InternalPatient
│   ├── treatment/                         Treatment, InternalTreatment, ExternalTreatment
│   ├── hospital/                          Hospital (CRUD + rules), Clinic
│   └── gui/
│       ├── MainFrame.java                 Top-level window, menu bar, tabs
│       ├── Theme.java                     Shared colors/fonts/styled components
│       ├── DataManager.java               Save/Load via Java serialization
│       ├── panels/                        ClinicsPanel, DoctorsPanel, PatientsPanel, TreatmentsPanel
│       └── dialogs/                       ClinicDialog, DoctorDialog, PatientDialog, TreatmentDialog
└── docs/
    ├── README.md          This file
    └── DOMAIN.md         The entity model and CRUD rules in detail
```

## Requirements

- **JDK 17 or later** (the code uses switch expressions and
  pattern-matching `instanceof`). No external libraries.

## Building and running

```
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out Main
```

On Windows (PowerShell):

```
mkdir out
javac -d out (Get-ChildItem -Recurse -Filter *.java src | % { $_.FullName })
java -cp out Main
```

Or open the `src` folder as a project in IntelliJ IDEA / Eclipse / VS Code
with the Java extension and run `Main.java` directly.

## Using the app

1. **Clinics** tab — add a clinic (name + type) before adding External
   treatments, since those need a clinic to point at.
2. **Doctors** tab — add doctors of any of the four types; External
   treatments also need at least one doctor.
3. **Patients** tab — add a Regular, External, or Internal patient.
4. **Treatments** tab — pick a patient from the dropdown, click
   **Add Treatment**. The dialog adapts to the patient's type
   automatically.
5. **File → Save** (or **Save As**) writes everything to a `.dat` file;
   **File → Open** loads it back, replacing the current in-memory data.

## Relationship to the console engine

This edition reuses the exact same `doctors`, `patient`, `treatment`, and
`hospital` classes as `original-java-source/` (with the same bug fixes —
see `original-java-source/docs/README.md`), adding only the `gui` package
on top. Any correctness fix made in one place applies identically in the
other, since it's the same engine underneath.

## See also

- [`docs/DOMAIN.md`](DOMAIN.md) — the entity model and CRUD rules in detail.
- [Diagrams](../../diagrams/index.html) — ERD and class diagrams for the whole
  MediSys domain model (shared by both editions).
