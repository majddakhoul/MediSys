# Domain Model

## Doctors (`doctors/`)

| Class | Extra fields | Meaning |
|---|---|---|
| `Doctor` | — | Base type: id, name, salary, birth date, address. `getDoctorType()` → `"Regular"`. |
| `ContractedDoctor` | `contractDate` | A doctor hired on a fixed contract. `getDoctorType()` → `"Contracted"`. |
| `TrainerDoctor` | `startDate`, `endDate` | A doctor in a training program with a defined date range. `getDoctorType()` → `"Trainer"`. |
| `InnerDoctor` | `numberOfDepartment` | A doctor permanently assigned to an internal department. `getDoctorType()` → `"Inner"`. |

Doctor ids are assigned from a single shared counter across all four
subtypes, so every doctor in the system has a unique id regardless of type.

## Patients (`patient/`)

| Class | Extra fields | Meaning |
|---|---|---|
| `Patient` | — | Base type: id, name, address, birth date, and a list of `Treatment`. `getPatientType()` → `"Regular"`. |
| `ExternalPatient` | `acceptance`, `acceptDate`, own list of `ExternalTreatment` | A patient treated without being admitted. `getPatientType()` → `"External"`. |
| `InternalPatient` | `discharge`, `dischargeDate`, own lists of `InternalTreatment` and `ExternalTreatment` | An admitted patient. `getPatientType()` → `"Internal"`. |

Like doctors, patient ids share one counter across all three subtypes.

## Treatments (`treatment/`)

| Class | Extra fields | Meaning |
|---|---|---|
| `Treatment` | — | Base type: id, date, cost. `getTreatmentType()` → `"General"`. Used for `Patient`s that are neither internal nor external. |
| `InternalTreatment` | `depID`, list of `Doctor` | A treatment delivered within a hospital department. `getTreatmentType()` → `"Internal"`. Only valid for `InternalPatient`. |
| `ExternalTreatment` | `doctor`, `cliID` | A treatment delivered by a specific doctor at a specific clinic. `getTreatmentType()` → `"External"`. Only valid for `ExternalPatient`. |

`Hospital.addTreatmentToPatient()` enforces the pairing: an
`InternalTreatment` can only be attached to an `InternalPatient`, an
`ExternalTreatment` only to an `ExternalPatient`; a plain `Treatment`
attaches to a plain `Patient`. Mismatches are rejected with a message
instead of being silently accepted.

## Clinics (`hospital/Clinic.java`)

A simple `id` / `name` / `type` record. `ExternalTreatment.cliID` refers to
a `Clinic`'s id — `Main` validates that the clinic exists before creating
the treatment.

## Hospital (`hospital/Hospital.java`)

The aggregate root: holds the lists of doctors, patients, and clinics, and
owns every CRUD operation (`add*`, `delete*`, `get*ById`, `getAll*`,
`show*`), plus the treatment-attachment rules described above.

## Persistence

`Hospital` and every class it references implement `Serializable`. The
**File → Save / Save As / Open** menu items in `MainFrame` serialize and
deserialize the whole `Hospital` object graph via `gui/DataManager.java`,
using standard Java object serialization to a `.dat` file — no external
libraries or database required.

## GUI-specific additions

- Every base class exposes a `get*Type()` method (`getDoctorType()`,
  `getPatientType()`, `getTreatmentType()`) used directly as the "Type"
  column in each table and as the `JComboBox` labels in the Add dialogs.
- Editing a doctor or patient never changes its concrete subtype (the type
  selector is disabled once editing an existing record) — only field
  values are updated, matching how Java's type system actually works
  (you cannot turn a `TrainerDoctor` into an `InnerDoctor` in place).
