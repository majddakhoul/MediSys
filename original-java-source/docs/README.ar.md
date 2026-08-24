# MediSys — نسخة الكونسول الأصلية (مصلّحة)

[⬅ الرئيسية](../../README.ar.md) · [English](README.md)

محرك الكونسول الأصلي بجافا لـMediSys: نظام إدارة مستشفى بيتعامل مع
الأطباء، المرضى، العيادات، والعلاجات عبر عمليات CRUD كاملة، مقاد بقائمة
نصية.

هاد نفس نموذج البيانات يلي نسخة Java Swing الرسومية مبنية فوقه، منظّف
ومصلّح عشان النسختين الاثنتين يشاركوا نفس القواعد الصحيحة تماماً.

## شو انصلّح هون

- **`InnerDoctor` كانت غير قابلة للوصول**: الصنف كان موجود (طبيب معيّن
  لقسم) بس قائمة "Add Doctor" كانت بس بتعرض Regular / Contracted /
  Trainer. الخيار `4 — Inner` انضاف عشان كل نوع فرعي للطبيب يدعمه النموذج
  يصير فعلياً قابل للاستخدام.
- **`ExternalTreatment.cliID` ما كانت تنحط أبداً**: العلاج الخارجي عنده
  `setCliID()` بس ولا شي بـ`Main` الأصلي كان بينادي عليها، فكل علاج خارجي
  كان بصمت بيشير لعيادة رقم `0`. الـconstructor هلق بياخد رقم العيادة
  مباشرة (`new ExternalTreatment(date, cost, doctor, clinicId)`)،
  و`Main` بيسأل ويتحقق من `Clinic ID` حقيقي وقت إنشاء واحد.
- **أي إدخال غلط كان يكسّر البرنامج كامل**: `Integer.parseInt(...)` و
  `LocalDate.parse(...)` كانوا ينادوا مباشرة على الإدخال الخام بدون
  try/catch، فكتابة "abc" لرقم تعريف أو تاريخ مشوّه كانت ترمي استثناء غير
  معالج وتقتل التطبيق. كل الإدخال هلق بيمر عبر دوال مساعدة `readInt()` /
  `readDate()` / `readBoolean()` بتعيد الطلب عند إدخال غلط بدل ما تنهار.
- **كل البيانات كانت تضيع عند الخروج**: `Hospital` ما كانت تنحفظ أبداً
  بأي مكان. كل صنف بالنموذج (`Hospital`، `Doctor` وأصنافها الفرعية،
  `Patient` وأصنافها الفرعية، `Treatment` وأصنافها الفرعية، `Clinic`) هلق
  بيطبّق `Serializable`، والقائمة أخدت خيارات **Save Data to File** /
  **Load Data from File** (تسلسل جافا لملف `medisys-data.dat`).
- **إضافة دوال `getDoctorType()` / `getPatientType()` /
  `getTreatmentType()`** متعددة الأشكال (polymorphic) للأصناف الأساسية
  الثلاثة، عشان الكود (ونسخة الواجهة الرسومية) يقدروا يعرضوا تسمية نوع
  نظيفة بدل سلاسل فحوصات `instanceof` — المشروع أصلاً بيعلن تعدد الأشكال
  كهدف تصميم، هاد بيخلّيه حقيقي على مستوى العرض كمان.
- **"CRUD كامل" ما كان كامل فعلياً**: كل حقل كان عنده setter أصلاً، بس
  القائمة الأصلية ما كانت تنادي على ولا وحدة منهم — ما كانت في طريقة تعدّل
  طبيب، مريض، أو عيادة بعد إنشائهم، وحقول `discharge` / `dischargeDate`
  تبع `InternalPatient` كانت غير قابلة للوصول بالكامل. انضافت خيارات قائمة
  **Edit Clinic / Edit Doctor / Edit Patient** بتحدّث كل حقل، بما فيهم
  الحقول الخاصة بالنوع الفرعي (تاريخ العقد، تواريخ التدريب، رقم القسم،
  القبول، الخروج).

## المتطلبات

- **JDK 17 أو أحدث** (الكود بيستخدم switch expressions وpattern-matching
  `instanceof`). بدون اعتماديات خارجية.

## البناء والتشغيل

```
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out Main
```

القائمة تفاعلية بالكامل — بدون أي معاملات سطر أوامر مطلوبة. البيانات
بتنحفظ لـ / بتنحمّل من `medisys-data.dat` بمجلد العمل الحالي.

## هيكلية المشروع

```
original-java-source/
├── src/
│   ├── Main.java                نقطة الدخول: قائمة CLI، التعامل مع الإدخال، الحفظ الدائم
│   └── (شوف تحت)
│       ├── doctors/              Doctor, ContractedDoctor, InnerDoctor, TrainerDoctor
│       ├── patient/                Patient, ExternalPatient, InternalPatient
│       ├── treatment/                Treatment, InternalTreatment, ExternalTreatment
│       └── hospital/                   Hospital (CRUD + قواعد العمل), Clinic
├── docs/
│   ├── README.md          هاد الملف
│   └── DOMAIN.md         نموذج الكيانات وقواعد CRUD بالتفصيل
└── LICENSE
```

## مرجع القائمة

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

## شوف كمان

- [`docs/DOMAIN.ar.md`](DOMAIN.ar.md) — نموذج الكيانات وقواعد CRUD بالتفصيل.
- [المخططات](../../diagrams/index.html) — مخططات ERD وUML الصنفي لكامل
  نموذج بيانات MediSys (مشتركة بين النسختين).
