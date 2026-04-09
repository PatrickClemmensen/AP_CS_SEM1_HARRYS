# Harry's Salon — Booking System

A console-based appointment management system for a small hair salon. Built in Java with no external dependencies, using CSV files for persistence.

Developed as part of the 1st semester cross-disciplinary project at EK.

## Group

| Name              | GitHub               |
|-------------------|----------------------|
| [Isak Pais]       | [@ispa070]           |
| [Nicklas Viftrup] | [@NickichanDK]       |
| [Patrick]         | [@PatrickClemmensen] |

---

## About the Project

Harry C. Cotter runs a hair salon with a growing customer base. This system replaces the physical Mayland calendar with a console-based single-user booking system. It allows Harry and his daughter Harriet to manage appointments, and gives the salon's accountant (Revisor) access to financial records by date.

## Roles

The system has three roles, selected from the main menu on startup.

### Salon Owner (Harry)
- Create and delete bookings
- View all upcoming appointments sorted by date and time
- Register payment for a past appointment (cash or credit)
- Settle an outstanding credit payment
- Register a closed business day
- Access financial records (password protected)

### Assistant (Harriet)
- Create and delete bookings
- View all upcoming appointments sorted by date and time

### Accountant (Revisor)
- Look up appointments on a past date (password protected)
- View customer name, payment amount, and payment type (cash/credit)
- Sort results by customer name or payment amount (descending)



## Features

- **Slot availability** — bookings are limited to hourly slots between 10:00–17:00, Mon–Fri
- **Alternative dates** — if a requested date is fully booked, the next 5 available working days are suggested
- **Closed days** — the owner can register days the salon is closed, blocking bookings on those dates
- **Credit payments** — credit deals are tracked separately and must be explicitly settled
- **File persistence** — all appointments and closed days are saved to CSV on every change

---

## Project Structure

```
src/
├── app/
│   └── Main.java                        — entry point, role selection
├── model/
│   ├── appointments/
│   │   ├── Appointment.java
│   │   ├── AppointmentStatus.java       — BOOKED, COMPLETED
│   │   ├── ClosedDays.java
│   │   └── TimeSlot.java
│   ├── payments/
│   │   ├── Payment.java                 — abstract base
│   │   ├── CashPayment.java
│   │   ├── CreditPayment.java
│   │   └── PaymentStatus.java           — CASH, CREDIT
│   ├── products/
│   │   ├── Product.java                 — enum of services and retail products
│   │   └── Category.java               — SERVICE, RETAIL
│   └── roles/
│       ├── User.java                    — abstract base
│       ├── Owner.java
│       ├── Assistant.java
│       └── Accountant.java
├── service/
│   ├── AppointmentRepository.java       — in-memory state and business queries
│   └── FileStorage.java                 — CSV read/write
├── ui/
│   ├── Menu.java                        — abstract base with shared booking logic
│   ├── OwnerMenu.java
│   ├── AssistantMenu.java
│   ├── AccountantMenu.java
│   └── PaymentMenu.java
└── util/
    ├── colors/
    │   └── Colors.java                  — ANSI color codes
    ├── exceptions/
    │   ├── InvalidInputException.java
    │   ├── InvalidDateException.java
    │   └── SlotUnavailableException.java
    ├── inputvalidation/
    │   ├── DateValidator.java            — future/past date rules
    │   ├── CustomerValidator.java        — name and phone rules
    │   ├── PasswordValidator.java        — salon password check
    │   └── MenuChoiceValidator.java      — integer range validation
    ├── menuhelper/
    │   ├── MenuInput.java               — validated input collection
    │   ├── MenuDisplay.java             — formatted console output
    │   └── MenuSelection.java           — domain-level selection flows
    ├── sorting/
    │   ├── SortByDate.java
    │   ├── SortByName.java
    │   └── SortByAmount.java
    └── AppConstants.java                — shared constants (paths, password, config)
 
data/
├── appointments.csv
└── closeddays.csv
```

---

## Deliverables

| Deliverable | Location | Status |
|---|---|---|
| SYS rapport | `SYS_rapport.pdf` |  Done |
| ITB presentation | `ITB_presentation.pdf` |  Done |
| Domain model | `_sysdev/diagrams/domain-model.png` |  Done |
| Activity diagram | `_sysdev/diagrams/activity-diagram.png` |  Done |
| Class diagram | `_sysdev/diagrams/class-diagram.png` |  Done |

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
