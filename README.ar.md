<div align="center">

<br/>

<h1>Simple Medical System</h1>

<p><strong>نظام إدارة مستشفى، منفّذ مرتين</strong><br/>
أطباء، مرضى، عيادات، وعلاجات مع CRUD كامل، تسلسلات هرمية متعددة الأشكال
للكيانات (polymorphic)، وحفظ بيانات دائم على ملف — تشغّله من قائمة تيرمنال
أو واجهة رسومية أصلية لسطح المكتب.</p>

<p>
  <img src="https://img.shields.io/badge/editions-2-blue?style=flat-square" alt="Editions"/>
  <img src="https://img.shields.io/badge/desktop-Java%20Swing-orange?style=flat-square&logo=openjdk&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/persistence-Java%20Serialization-6f42c1?style=flat-square" alt="Persistence"/>
  <img src="https://img.shields.io/badge/license-MIT-orange?style=flat-square" alt="License"/>
</p>

<p><a href="README.md">English</a></p>

<br/>

</div>

---

## المحتويات

- [النسختان](#النسختان)
- [المزايا المشتركة](#المزايا-المشتركة)
- [بداية سريعة](#بداية-سريعة)
- [نموذج البيانات](#نموذج-البيانات)
- [المخططات](#المخططات)
- [شو انصلح](#شو-انصلح)
- [هيكلية المشروع](#هيكلية-المشروع)
- [فهرس التوثيق](#فهرس-التوثيق)
- [الرخصة](#الرخصة)

---

## النسختان

| النسخة | التقنية | تشغيلها | التوثيق |
|---|---|---|---|
| **[نسخة Java GUI](Java-GUI-Edition/)** | Java Swing، JDK 17+ | `javac` + `java Main` — شوف التوثيق | [`README`](Java-GUI-Edition/docs/README.ar.md) · [`English`](Java-GUI-Edition/docs/README.md) · [`دليل الواجهات`](Java-GUI-Edition/docs/UI_GUIDE.ar.md) |
| **[محرك الكونسول الأصلي](original-java-source/)** | جافا عادية، JDK 17+ | `java Main` (قائمة تفاعلية) | [`README`](original-java-source/docs/README.ar.md) · [`English`](original-java-source/docs/README.md) |

النسختان الاثنتان بيشاركوا بالضبط نفس نموذج البيانات (حزم `doctors`،
`patient`، `treatment`، `hospital`) وبالضبط نفس تصليحات الـbugs — اختر
التطبيق التفاعلي لسطح المكتب لإدخال البيانات اليومي، أو محرك الكونسول
كتطبيق مرجعي بسيط وبدون أي اعتماديات.

---

## المزايا المشتركة

- **CRUD كامل** للعيادات، الأطباء، المرضى، والعلاجات — إنشاء، قراءة،
  **تعديل**، وحذف، لكل كيان (محرك الكونسول الأصلي كان بس فيه إنشاء/قراءة/حذف؛
  التعديل كان مفقود بالكامل — شوف [شو انصلح](#شو-انصلح)).
- **أربع أنواع فرعية للأطباء** (Regular، Contracted، Trainer، Inner) و**ثلاث
  أنواع فرعية للمرضى** (Regular، External، Internal)، كل وحدة منهم بحقولها
  الإضافية الخاصة، ووصولها كامل من النسختين الاثنتين.
- **علاجات واعية بالنوع**: المرضى الداخليين (Internal) بياخدوا
  `InternalTreatment` (رقم قسم)، المرضى الخارجيين (External) بياخدوا
  `ExternalTreatment` (طبيب + عيادة)، المرضى العاديين بياخدوا `Treatment`
  أساسية — نموذج البيانات بيرفض أي إقران غلط.
- **حفظ بيانات دائم على ملف** عبر تسلسل جافا (Java serialization) العادي —
  بدون قاعدة بيانات، بدون اعتماديات خارجية. الواجهة الرسومية فيها قائمة File
  (New / Save / Save As / Open)؛ الكونسول فيه عناصر قائمة Save/Load.
- **إدخال متحقق منه بكل مكان** — الأرقام، التواريخ (`YYYY-MM-DD`)، والحقول
  المطلوبة بتنفحص قبل ما تُقبل، بالنسختين الاثنتين، فالإدخال الغلط أبداً ما
  بيكسّر التطبيق أو يخرّب البيانات.

---

## بداية سريعة

```bash
# نسخة Java GUI
cd Java-GUI-Edition
mkdir out && javac -d out $(find src -name "*.java")
java -cp out Main

# محرك الكونسول الأصلي
cd original-java-source
mkdir out && javac -d out $(find src -name "*.java")
java -cp out Main
```

---

## نموذج البيانات

| الحزمة | الصنف الأساسي | الأنواع الفرعية |
|---|---|---|
| `doctors` | `Doctor` | `ContractedDoctor`، `TrainerDoctor`، `InnerDoctor` |
| `patient` | `Patient` | `ExternalPatient`، `InternalPatient` |
| `treatment` | `Treatment` | `InternalTreatment`، `ExternalTreatment` |
| `hospital` | `Hospital` (جذر التجميع)، `Clinic` | — |

التفاصيل الكاملة حقل حقل موجودة بـ `docs/DOMAIN.md` تبع كل نسخة
([Java GUI](Java-GUI-Edition/docs/DOMAIN.md) ·
[الكونسول](original-java-source/docs/DOMAIN.md))،
ومتوفرة كمان بالعربي
([Java GUI](Java-GUI-Edition/docs/DOMAIN.ar.md) ·
[الكونسول](original-java-source/docs/DOMAIN.ar.md)).

---

## المخططات

مخططات مرجعية ثابتة بـ HTML/CSS لكامل نموذج البيانات، مشتركة بين النسختين
الاثنتين:

- **[الصفحة الرئيسية للمخططات](diagrams/index.html)**
- **[مخطط العلاقات بين الكيانات (ERD)](diagrams/erd.html)** — الكيانات،
  المفاتيح، والتسلسلات الهرمية ISA.
- **[مخطط UML الصنفي](diagrams/class-diagram.html)** — نفس التسلسل الهرمي
  كأصناف جافا حقيقية، مع الحقول، الدوال الأساسية، وأسهم الوراثة/الارتباط.

---

## شو انصلح

المحرك الأصلي كان فيه شوية فجوات حقيقية بين شو كان معلن عنه وشو كان فعلياً
بيعمله. النسختان الاثنتان هلق بيشاركوا التصليحات (تفاصيل كاملة بـ
[`original-java-source/docs/README.md`](original-java-source/docs/README.md)):

- **`InnerDoctor` كانت غير قابلة للوصول** — الصنف كان موجود بس ولا خيار
  بالقائمة كان بينشئ وحدة منها.
- **`ExternalTreatment.cliID` ما كانت تنحط أبداً** — كل علاج خارجي كان
  بصمت بيشير لعيادة رقم `0`. العيادة هلق جزء مطلوب ومتحقق منه من إنشاء
  العلاج.
- **"CRUD كامل" ما كان كامل فعلياً** — كل حقل كان عنده setter، بس ولا شي
  كان بينادي عليهم؛ ما كانت في طريقة تعدّل طبيب، مريض، أو عيادة بعد إنشائهم،
  وحقول خروج (discharge) الـ `InternalPatient` كانت غير قابلة للوصول
  بالكامل. النسختان الاثنتان هلق بيعرضوا وظيفة تعديل حقيقية.
- **الإدخال الغلط كان يكسّر البرنامج** — `NumberFormatException` /
  `DateTimeParseException` غير معالجة على أي إدخال مشوّه. النسختان الاثنتان
  هلق بيتحققوا وبيعيدوا الطلب / بيوروا خطأ داخلي بدل هيك.
- **كل البيانات كانت تضيع عند الخروج** — ولا شي كان ينحفظ أبداً. النسختان
  الاثنتان هلق بيدعموا Save/Load عبر تسلسل جافا.

---

## هيكلية المشروع

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
└── README.md            <- إنت هون
```

---

## فهرس التوثيق

| النسخة | نظرة عامة والبناء/التشغيل | نموذج البيانات | دليل الواجهات |
|---|---|---|---|
| Java GUI | [README](Java-GUI-Edition/docs/README.ar.md) · [English](Java-GUI-Edition/docs/README.md) | [DOMAIN](Java-GUI-Edition/docs/DOMAIN.ar.md) · [English](Java-GUI-Edition/docs/DOMAIN.md) | [UI_GUIDE](Java-GUI-Edition/docs/UI_GUIDE.ar.md) · [English](Java-GUI-Edition/docs/UI_GUIDE.md) |
| محرك الكونسول | [README](original-java-source/docs/README.ar.md) · [English](original-java-source/docs/README.md) | [DOMAIN](original-java-source/docs/DOMAIN.ar.md) · [English](original-java-source/docs/DOMAIN.md) | — |

| ملف جذري | الرابط |
|---|---|
| هاد الملف نفسه، بالإنجليزي | [`README.md`](README.md) |

| المخطط | الرابط |
|---|---|
| الصفحة الرئيسية للمخططات | [diagrams/index.html](diagrams/index.html) |
| مخطط العلاقات بين الكيانات | [diagrams/erd.html](diagrams/erd.html) |
| مخطط UML الصنفي | [diagrams/class-diagram.html](diagrams/class-diagram.html) |

---

## الرخصة

هاد المشروع منشور تحت رخصة MIT.

شوف ملف [`LICENSE`](LICENSE) للتفاصيل الكاملة للترخيص.
