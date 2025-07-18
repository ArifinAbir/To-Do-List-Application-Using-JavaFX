# 📝 Smart To-Do List App

A feature-rich **To-Do List Desktop Application** built with **Java** and **JavaFX**, backed by an **Oracle Database**. This app is designed to help users stay organized, productive, and never miss important tasks. With an intuitive interface, time-based alerts, and powerful task management features, it’s more than just a simple to-do list—it’s your personal task assistant.

Whether you're a student managing assignments, a professional tracking meetings, or just someone looking to organize your day-to-day work, this app makes task management simple and efficient.

---

## 🌟 Key Features

### ✅ Task Management

- **Add Tasks**: Enter a task with a title, description, and assign a date & time.
- **Edit Tasks**: Update existing task details at any time.
- **Delete Tasks**: Soft delete to keep a record, or permanently delete when no longer needed.
- **Restore Tasks**: Accidentally deleted a task? Easily restore it from the deleted list.
- **Mark as Done**: Easily mark tasks as completed and view them in a separate list.

### 🔎 Filtering & Sorting

- **Filter by Date**: View tasks scheduled for a specific date.
- **Filter by Time**: Sort and display tasks based on their scheduled time.
- **Homepage Overview**: Instantly see all your active tasks when the app launches.

### 🔔 Smart Notifications & Alarms

- **Pre-Task Notification**: Get notified **2 minutes before** a task is due.
- **Task Alarm**: A **ringing alarm** will trigger exactly at the task’s scheduled time.

---

## 🛠 Technology Stack

- **Java** – Core logic and backend implementation
- **JavaFX** – Rich GUI framework for building responsive desktop UI
- **Oracle Database** – Robust backend to store all task data
- **JDBC (Java Database Connectivity)** – Seamless connection between Java and Oracle DB
- **Timers / Threads** – For scheduling notifications and alarms

---

## 📦 How to Run

1. Make sure you have the following installed:
   - Java JDK (17 or above)
   - JavaFX SDK
   - Oracle Database (any local or remote instance)
2. Clone the repository:
   ```bash
   git clone https://github.com/ArifinAbir/To-Do-List-Application-Using-JavaFX.git


SQL Queries=>

CREATE TABLE TO_DO_LIST (
    TASK_ID NUMBER PRIMARY KEY,
    TITLE VARCHAR2(255),
    DESCRIPTION CLOB,
    TIME_AND_DATE_TO_BE_DONE TIMESTAMP(6)
);

CREATE TABLE COMPLETED_TASK (
    TASK_ID NUMBER PRIMARY KEY,
    TITLE VARCHAR2(255),
    DESCRIPTION CLOB,
    COMPLETED_TIME_AND_DATE TIMESTAMP(6),
    TIME_AND_DATE_TO_BE_DONE TIMESTAMP(6)
);

CREATE TABLE DELETED_TASK (
    TASK_ID NUMBER PRIMARY KEY,
    TITLE VARCHAR2(255),
    DESCRIPTION CLOB,
    DELETED_TIME_AND_DATE TIMESTAMP(6),
    TIME_AND_DATE_TO_BE_DONE TIMESTAMP(6)
);

CREATE SEQUENCE task_id_sequence
  START WITH 1
  INCREMENT BY 1
  NOCACHE
  NOCYCLE;
