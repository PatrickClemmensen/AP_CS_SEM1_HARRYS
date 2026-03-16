# Harry's Salon — Booking System

Console-based booking system for Harry's Salon, developed as part of the 1st semester cross-disciplinary project at KEA (DAT-F26B).

## Group

| Name       | GitHub               |
|------------|----------------------|
| [Member A] | [@username]          |
| [Member B] | [@username]          |
| [Patrick]  | [@PatrickClemmensen] |

---

## About the Project

Harry C. Cotter runs a hair salon with a growing customer base. This system replaces the physical Mayland calendar with a console-based single-user booking system. It allows Harry and his daughter Harriet to manage appointments, and gives the salon's accountant (Revisor) access to financial records by date.

**Three user roles:**
- **Harry / Harriet** — create, view, and delete appointments, register payments
- **Revisor** — look up appointments and totals by date, sort by name or amount

---

## Project Structure

```
AP_CS_SEM1_HARRYS/
├── src/
│   ├── app/          ← entry point (Main.java)
│   ├── model/        ← domain classes ()
│   ├── service/      ← file persistence ()
│   ├── ui/           ← console menus ()
│   └── util/         ← static helpers ()
├── _sysdev/
│   ├── diagrams/     ← domain model, activity diagrams, class diagram (PNG)
│   └── design-patterns/ ← pattern documentation
├── _itb/             ← ITB presentation materials
├── SYS_rapport.pdf   ← system development rapport (added at submission)
├── ITB_presentation.pdf ← ITB presentation (added at submission)
└── README.md
```

---

## Deliverables

| Deliverable | Location | Status |
|---|---|---|
| SYS rapport | `SYS_rapport.pdf` |  In progress |
| ITB presentation | `ITB_presentation.pdf` |  In progress |
| Domain model | `_sysdev/diagrams/domain-model.png` |  In progress |
| Activity diagram | `_sysdev/diagrams/activity-diagram.png` |  In progress |
| Class diagram | `_sysdev/diagrams/class-diagram.png` |  In progress |

---

## Development Process

Built using **Scrum** with two one-week sprints.

| | Dates | Sprint Goal |
|---|---|---|
| Sprint 1 | 16/03–20/03 | Harry or Harriet can create, view, and delete bookings and register payments — with all data saved to file and restored on restart |
| Sprint 2 | 23/03–27/03 | The Revisor can look up any past date and see who was served and for how much, with results sortable by name or amount |

---

## Technical Requirements

- Java (OOP — encapsulation, inheritance, abstraction, polymorphism)
- Console-based, single-user
- File persistence — data saved on every change, reloaded on startup
- Packages: `model` / `service` / `ui` / `util`
- Sorting via `Comparable` and `Comparator`
- Input validation with exception handling
- Enums for appointment status
- Both `Array` and `ArrayList` used where appropriate

---

## Submission

**Deadline:** Friday 27/03/2026 kl. 15:45 — ItsLearning
**Presentation:** Wednesday 08/04/2026