# To-Do-List-Application-Using-JavaFX

To-Do List Application using JavaFX and Oracle
A comprehensive and feature-rich desktop To-Do List application built with JavaFX for a clean user interface and backed by an Oracle Database for robust data persistence. This project goes beyond a simple task manager by incorporating a complete task lifecycle (pending, completed, deleted), advanced notifications, and powerful filtering capabilities.

## Key Features
📝 Task Management:

Full CRUD operations: Create, View, Edit, and Delete tasks.

Add tasks with detailed descriptions and specific due dates and times.

Mark tasks as "Done" to move them from the active list to a separate completion log.

Use a safe "Delete Mode" to select and move multiple tasks to a recycle bin instead of deleting them instantly.

🗂️ Task Lifecycle & History:

Completed Tasks View: Browse a complete history of all finished tasks with an option to permanently delete them from the log.

Deleted Tasks View (Recycle Bin): View all deleted tasks and choose to either restore them to the active list or delete them permanently.

🔔 Smart Alerts & Notifications:

Proactive Reminders: Get a desktop notification 1 minute before a task is due.

Looping Alarm: An alarm sounds continuously when a task's due time is reached and stops only when the user dismisses the alert pop-up.

🔍 Advanced Filtering:

A powerful search feature to filter and display all tasks that fall within a user-defined date and time interval.


## Technology Stack
Frontend: JavaFX
Backend/Logic: Java
Database: Oracle Database (with JDBC)
Build Tool: Apache Maven (Intelij Idea)



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
