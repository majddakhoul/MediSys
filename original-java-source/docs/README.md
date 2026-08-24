# MediSys — Original Console Edition (Fixed)

[⬅ Main README](../../README.md) · [العربية](README.ar.md)

The original Java console engine for MediSys: a hospital management system
handling doctors, patients, clinics, and treatments through full CRUD
operations, driven by a text menu.

This is the same domain model the Java Swing GUI Edition is built on top of,
cleaned up and corrected so both editions share identical, correct rules.

## What was fixed here

- **`InnerDoctor` was unreachable**: the class existed (a doctor assigned to
  a department) but the "Add Doctor" menu only offered Regular / Contracted
  / Trainer. Option `4 — Inner` was added so every doctor subtype the model
  supports is actually usable.
- **`ExternalTreatment.cliID` was never set**: an external treatment has a
  `setCliID()` but nothing in the original `Main` ever called it, so every
  external treatment silently pointed at clinic `0`. The constructor now
  takes the clinic id directly (`new ExternalTreatment(date, cost, doctor,
  clinicId)`), and `Main` asks for and validates a real `Clinic ID` when
  creating one.
- **Any bad input crashed the whole program**: `Integer.parseInt(...)` and
  `LocalDate.parse(...)` were called directly on raw input with no
  try/catch, so typing "abc" for an ID or a malformed date threw an
  uncaught exception and killed the app. All input now goes through
  `readInt()` / `readDate()` / `readBoolean()` helpers that re-prompt on
  invalid input instead of crashing.
- **All data was lost on exit**: `Hospital` was never persisted anywhere.
  Every domain class (`Hospital`, `Doctor` and subclasses, `Patient` and
  subclasses, `Treatment` and subclasses, `Clinic`) now implements
  `Serializable`, and the menu gained **Save Data to File** / **Load Data
  from File** options (Java serialization to `medisys-data.dat`).
- **Added `getDoctorType()` / `getPatientType()` / `getTreatmentType()`**
  polymorphic methods to the three base classes, so code (and the GUI
  edition) can display a clean type label instead of chains of
  `instanceof` checks — the project already advertises polymorphism as a
  design goal, this makes it real at the display layer too.
- **"Full CRUD" wasn't actually full**: every field already had a setter,
  but the original menu never called any of them — there was no way to
  edit a doctor, patient, or clinic once created, and `InternalPatient`'s
  `discharge` / `dischargeDate` fields were completely unreachable. Added
  **Edit Clinic / Edit Doctor / Edit Patient** menu options that update
  every field, including the subtype-specific ones (contract date,
  training dates, department number, acceptance, discharge).

## Requirements

- **JDK 17 or later** (the code uses switch expressions and
  pattern-matching `instanceof`). No external dependencies.

## Building and running

```
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out Main
```

The menu is entirely interactive — no command-line arguments are needed.
Data is saved to / loaded from `medisys-data.dat` in the current working
directory.

## Project structure

```
original-java-source/
├── src/
│   ├── Main.java                Entry point: CLI menu, input handling, persistence
│   └── (see below)
│       ├── doctors/              Doctor, ContractedDoctor, InnerDoctor, TrainerDoctor
│       ├── patient/                Patient, ExternalPatient, InternalPatient
│       ├── treatment/                Treatment, InternalTreatment, ExternalTreatment
│       └── hospital/                   Hospital (CRUD + business rules), Clinic
├── docs/
│   ├── README.md          This file
│   └── DOMAIN.md         The entity model and CRUD rules in detail
└── LICENSE
```

## Menu reference

```
1.  Add Clinic
2.  Show Clinics
3.  Edit Clinic
4.  Delete Clinic
5.  Add Doctor
6.  Show Doctors
7.  Edit Doctor
8.  Delete Doctor
9.  Add Patient
10. Show Patients
11. Edit Patient
12. Delete Patient
13. Add Treatment to Patient
14. Delete Treatment from Patient
15. Show Treatments of Patient
16. Save Data to File
17. Load Data from File
18. Exit
```

## See also

- [`docs/DOMAIN.md`](DOMAIN.md) — the entity model and CRUD rules in detail.
- [Diagrams](../../diagrams/index.html) — ERD and class diagrams for the whole
  MediSys domain model (shared by both editions).
