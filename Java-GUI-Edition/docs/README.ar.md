# MediSys — نسخة Java Swing

[⬅ الرئيسية](../../README.ar.md) · [English](README.md)

واجهة رسومية أصلية لسطح المكتب لـMediSys، مبنية مباشرة فوق نفس نموذج
البيانات (المصلّح) يلي محرك الكونسول مبني عليه: CRUD كامل للعيادات،
الأطباء، المرضى، والعلاجات، مع سير عمل Save/Open حقيقي مدعوم بملف بدل
قائمة نصية.

## المزايا

- **واجهة بتابات**: Clinics، Doctors، Patients، Treatments — كل وحدة فيها
  جدول قابل للفرز، وأزرار شريط أدوات Add / Edit / Delete.
- **كل أنواع الأطباء الأربعة** (Regular، Contracted، Trainer، Inner) و**كل
  أنواع المرضى الثلاثة** (Regular، External، Internal) قابلة للإنشاء
  والتعديل، مع حقول خاصة بالنوع بتظهر ديناميكياً بالنافذة (تاريخ العقد،
  تواريخ التدريب، رقم القسم، حالة القبول / الخروج).
- **تاب Treatments**: اختر مريض من القائمة المنسدلة، شوف علاجاته بجدول،
  ضيف واحد جديد (النافذة بس بتسأل عن الحقول ذات العلاقة بنوع هاد المريض —
  رقم قسم للمرضى الداخليين، اختيار طبيب + عيادة للمرضى الخارجيين)، أو احذف
  واحد.
- **قائمة File**: New، Save، Save As، Open، Exit — مدعومة بتسلسل جافا
  العادي (ملفات `.dat`)، فمجموعة بيانات المستشفى الكاملة بتضل موجودة بين
  الجلسات.
- **إدخال متحقق منه بكل مكان**: كل نافذة بتفحص الحقول المطلوبة، الحقول
  الرقمية، والتواريخ (`YYYY-MM-DD`) قبل القبول، مع رسالة خطأ داخلية بدل
  انهيار أو إدخال غلط بصمت.
- Java SE + Swing خالصة. بدون اعتماديات خارجية، بدون أداة بناء مطلوبة.

## هيكلية المشروع

```
Java-GUI-Edition/
├── src/
│   ├── Main.java                          نقطة دخول التطبيق
│   ├── doctors/                           Doctor, ContractedDoctor, InnerDoctor, TrainerDoctor
│   ├── patient/                           Patient, ExternalPatient, InternalPatient
│   ├── treatment/                         Treatment, InternalTreatment, ExternalTreatment
│   ├── hospital/                          Hospital (CRUD + rules), Clinic
│   └── gui/
│       ├── MainFrame.java                 النافذة الرئيسية، شريط القوائم، التابات
│       ├── Theme.java                     ألوان/خطوط/مكوّنات مصممة مشتركة
│       ├── DataManager.java               Save/Load عبر تسلسل جافا
│       ├── panels/                        ClinicsPanel, DoctorsPanel, PatientsPanel, TreatmentsPanel
│       └── dialogs/                       ClinicDialog, DoctorDialog, PatientDialog, TreatmentDialog
└── docs/
    ├── README.md          هاد الملف
    └── DOMAIN.md         نموذج الكيانات وقواعد CRUD بالتفصيل
```

## المتطلبات

- **JDK 17 أو أحدث** (الكود بيستخدم switch expressions وpattern-matching
  `instanceof`). بدون مكتبات خارجية.

## البناء والتشغيل

```
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out Main
```

على ويندوز (PowerShell):

```
mkdir out
javac -d out (Get-ChildItem -Recurse -Filter *.java src | % { $_.FullName })
java -cp out Main
```

أو افتح مجلد `src` كمشروع بـ IntelliJ IDEA / Eclipse / VS Code مع إضافة
Java وشغّل `Main.java` مباشرة.

## استخدام التطبيق

كل نافذة Add موثّقة بلقطات شاشة حقيقية بـ [`docs/UI_GUIDE.ar.md`](UI_GUIDE.ar.md) — هاد القسم ملخّص سريع؛ هداك الدليل بيغطّي الحقول المعتمدة على النوع لكل نافذة وحدة وحدة.

1. تاب **Clinics** — ضيف عيادة (اسم + نوع) قبل ما تضيف علاجات External،
   لأنها محتاجة عيادة تشير إلها.
2. تاب **Doctors** — ضيف أطباء من أي نوع من الأربعة؛ علاجات External كمان
   محتاجة طبيب واحد ع الأقل.
3. تاب **Patients** — ضيف مريض Regular، External، أو Internal.
4. تاب **Treatments** — اختر مريض من القائمة المنسدلة، اضغط **Add
   Treatment**. النافذة بتتكيّف مع نوع المريض تلقائياً.
5. **File → Save** (أو **Save As**) بتكتب كل شي لملف `.dat`؛ **File →
   Open** بترجّعه، وبتستبدل البيانات الحالية بالذاكرة.

## العلاقة مع محرك الكونسول

هاي النسخة بتعيد استخدام بالضبط نفس أصناف `doctors`، `patient`،
`treatment`، و`hospital` متل `original-java-source/` (بنفس تصليحات
الـbugs — شوف `original-java-source/docs/README.md`)، وبس بتضيف حزمة `gui`
فوقها. أي تصليح صحة يصير بمكان واحد بينطبق بنفس الشكل بالمكان التاني، لأنه
نفس المحرك تحت الاثنين.

## شوف كمان

- [`docs/UI_GUIDE.ar.md`](UI_GUIDE.ar.md) — كل نافذة Add، بلقطات شاشة حقيقية.
- [`docs/DOMAIN.ar.md`](DOMAIN.ar.md) — نموذج الكيانات وقواعد CRUD بالتفصيل.
- [المخططات](../../diagrams/index.html) — مخططات ERD وUML الصنفي لكامل
  نموذج بيانات MediSys (مشتركة بين النسختين).
