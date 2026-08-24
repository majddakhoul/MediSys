# UI Guide

[⬅ Edition README](README.md) · [العربية](UI_GUIDE.ar.md)

A walkthrough of the four **Add** dialogs — one per tab — with real screenshots. Every dialog follows the same shape: a form, an inline error line that only appears on invalid submission, and Cancel / Add buttons. See [`DOMAIN.md`](DOMAIN.md) for the full field-by-field rules behind each one.

## Clinics tab — Add Clinic

The simplest dialog: just a name and a free-text type. There's no dropdown here because clinics don't have subtypes — every clinic is the same shape, only the ordinary CRUD toolbar (Add / Edit / Delete) applies to it.

<img src="assets/ui/add-clinic.png" alt="Add Clinic dialog with Clinic Name and Clinic Type fields, over the Clinics table showing one existing clinic" width="800" />

Add a clinic **before** adding an External treatment later — the Treatments tab's clinic picker is built from whatever clinics already exist, so an empty clinic list means External patients can't get a treatment yet (`TreatmentDialog` reports *"Add a clinic first."* if you try).

## Doctors tab — Add Doctor

**Doctor Type** drives which extra fields appear beneath the shared Name / Salary / Birth Date / Address fields — the dialog swaps a card in place instead of showing every field for every type at once:

<img src="assets/ui/add-doctor.png" alt="Add Doctor dialog with Doctor Type dropdown set to Regular, and Name/Salary/Birth Date/Address fields" width="800" />

| Doctor Type | Extra field(s) shown |
|---|---|
| Regular | *(none — just the shared fields)* |
| Contracted | Contract Date |
| Trainer | Start Date, End Date |
| Inner | Department Number |

All dates use `YYYY-MM-DD` and are validated on submit; an invalid date or a non-numeric salary shows an inline error instead of closing the dialog. When you reopen this same dialog to **edit** an existing doctor, **Doctor Type** is disabled — you can change every field's value, but not which subtype the doctor is, matching how Java's type system actually works (see [`DOMAIN.md`](DOMAIN.md#gui-specific-additions)).

## Patients tab — Add Patient

Same pattern as doctors: **Patient Type** picks which extra fields show up, this time with only three options instead of four:

<img src="assets/ui/add-patient.png" alt="Add Patient dialog with Patient Type dropdown set to Regular, and Name/Address/Birth Date fields" width="800" />

| Patient Type | Extra field(s) shown |
|---|---|
| Regular | *(none — just the shared fields)* |
| External | Acceptance, Accept Date |
| Internal | Discharge, Discharge Date |

A patient's type is what the Treatments tab uses to decide which kind of treatment dialog to show next — see below.

## Treatments tab — Add Treatment

This dialog is the one place the app reaches outside the current tab: it reads the **currently selected patient's type** from the Patients data and shows different fields accordingly, with no type dropdown of its own:

<img src="assets/ui/add-treatment.png" alt="Add Treatment dialog for patient #1 MAjd (Regular), with only Treatment Date and Cost fields — no department or doctor/clinic fields" width="800" />

The patient in this screenshot — **#1 MAjd (Regular)** — is a plain `Patient`, so the dialog shows only the two fields every treatment needs: **Treatment Date** and **Cost**. For the other two patient types, the dialog adds different fields instead of these two extra ones:

| Patient's type | Extra field(s) shown | Treatment class created |
|---|---|---|
| Regular | *(none)* | `Treatment` |
| Internal | Department ID | `InternalTreatment` |
| External | Doctor (dropdown), Clinic (dropdown) | `ExternalTreatment` |

The Doctor and Clinic dropdowns are populated from whatever doctors/clinics already exist in the hospital — if either list is empty, submitting for an External patient reports *"Add a doctor first."* or *"Add a clinic first."* instead of creating a broken treatment. This is also why doctors and clinics are worth adding before external treatments, as noted in the [edition README](README.md#using-the-app).

## Shared behavior across all four dialogs

- **Inline validation** — every required field, numeric field, and date field is checked before the dialog closes; the first problem found is shown as a red line above the buttons instead of a popup, and the dialog stays open so you can fix it.
- **Cancel vs. the window's close button (×)** — both discard the dialog without changing any data; nothing is written until you press **Add** (or **Save**, when editing) and validation passes.
- **Add vs. Edit is the same dialog class** — opening one of these dialogs on an existing row pre-fills every field from that record and changes the button label to **Save**; the Doctor/Patient Type dropdown is disabled in that case specifically (see the Doctors section above) since changing an entity's concrete subtype isn't something the domain model supports in place.
