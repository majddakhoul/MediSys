# نموذج البيانات

[⬅ توثيق النسخة](README.ar.md) · [English](DOMAIN.md)

## الأطباء (`doctors/`)

| الصنف | الحقول الإضافية | المعنى |
|---|---|---|
| `Doctor` | — | النوع الأساسي: id، الاسم، الراتب، تاريخ الميلاد، العنوان. `getDoctorType()` ← `"Regular"`. |
| `ContractedDoctor` | `contractDate` | طبيب معيّن بعقد ثابت. `getDoctorType()` ← `"Contracted"`. |
| `TrainerDoctor` | `startDate`، `endDate` | طبيب ببرنامج تدريب بفترة زمنية محددة. `getDoctorType()` ← `"Trainer"`. |
| `InnerDoctor` | `numberOfDepartment` | طبيب معيّن دائماً لقسم داخلي. `getDoctorType()` ← `"Inner"`. |

أرقام تعريف الأطباء (id) بتنعطى من عدّاد واحد مشترك عبر الأنواع الفرعية
الأربعة كلها، فكل طبيب بالنظام عنده id فريد بغض النظر عن نوعه.

## المرضى (`patient/`)

| الصنف | الحقول الإضافية | المعنى |
|---|---|---|
| `Patient` | — | النوع الأساسي: id، الاسم، العنوان، تاريخ الميلاد، وقائمة `Treatment`. `getPatientType()` ← `"Regular"`. |
| `ExternalPatient` | `acceptance`، `acceptDate`، قائمة خاصة من `ExternalTreatment` | مريض بيتعالج بدون ما ينقبل (يُدخل المستشفى). `getPatientType()` ← `"External"`. |
| `InternalPatient` | `discharge`، `dischargeDate`، قوائم خاصة من `InternalTreatment` و`ExternalTreatment` | مريض مقبول (مُدخل). `getPatientType()` ← `"Internal"`. |

متل الأطباء، أرقام تعريف المرضى بتشارك عدّاد واحد عبر الأنواع الفرعية
الثلاثة.

## العلاجات (`treatment/`)

| الصنف | الحقول الإضافية | المعنى |
|---|---|---|
| `Treatment` | — | النوع الأساسي: id، التاريخ، التكلفة. `getTreatmentType()` ← `"General"`. مستخدم للمرضى يلي مش داخليين ولا خارجيين. |
| `InternalTreatment` | `depID`، قائمة `Doctor` | علاج بينقدّم جوا قسم بالمستشفى. `getTreatmentType()` ← `"Internal"`. صالح بس لـ`InternalPatient`. |
| `ExternalTreatment` | `doctor`، `cliID` | علاج بيقدّمه طبيب محدد بعيادة محددة. `getTreatmentType()` ← `"External"`. صالح بس لـ`ExternalPatient`. |

`Hospital.addTreatmentToPatient()` بتفرض الإقران الصح: `InternalTreatment`
فيها تنربط بس بـ`InternalPatient`، و`ExternalTreatment` بس بـ
`ExternalPatient`؛ `Treatment` عادية بتنربط بـ`Patient` عادي. أي إقران
غلط بينرفض برسالة بدل ما يُقبل بصمت.

## العيادات (`hospital/Clinic.java`)

سجل بسيط `id` / `name` / `type`. `ExternalTreatment.cliID` بيشير لـid تبع
`Clinic` — `Main` بتتحقق إنه العيادة موجودة قبل ما تنشئ العلاج.

## المستشفى (`hospital/Hospital.java`)

جذر التجميع (aggregate root): بيحمل قوائم الأطباء، المرضى، والعيادات،
وبيملك كل عملية CRUD (`add*`، `delete*`، `get*ById`، `getAll*`،
`show*`)، زائد قواعد ربط العلاج الموصوفة فوق.

## الحفظ الدائم

`Hospital` وكل صنف بتشير له بيطبّقوا `Serializable`. عناصر قائمة **File →
Save / Save As / Open** بـ`MainFrame` بتسلسل وتفكّ تسلسل كامل شجرة كائن
`Hospital` عبر `gui/DataManager.java`، باستخدام تسلسل كائنات جافا القياسي
لملف `.dat` — بدون مكتبات خارجية أو قاعدة بيانات مطلوبة.

## إضافات خاصة بالواجهة الرسومية

- كل صنف أساسي بيعرض دالة `get*Type()` (`getDoctorType()`،
  `getPatientType()`، `getTreatmentType()`) مستخدمة مباشرة كعمود "Type"
  بكل جدول وكتسميات `JComboBox` بنوافذ الإضافة.
- تعديل طبيب أو مريض أبداً ما بيغيّر نوعه الفرعي الفعلي (منتقي النوع
  بينعطّل بمجرد ما تعدّل سجل موجود) — بس قيم الحقول بتتحدّث، مطابقةً لكيف
  نظام الأنواع بجافا بيشتغل فعلياً (ما فيك تحوّل `TrainerDoctor` لـ
  `InnerDoctor` بمكانه).
