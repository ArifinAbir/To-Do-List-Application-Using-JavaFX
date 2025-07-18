package com.rfn.to_do_list;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XE";
    private static final String USER = "system";
    private static final String PASS = "admin";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void insertTask(String title, String description, LocalDateTime taskTimestamp) throws SQLException {
        String sql = "INSERT INTO TO_DO_LIST (TASK_ID, TITLE, DESCRIPTION, TIME_AND_DATE_TO_BE_DONE) VALUES (task_id_sequence.NEXTVAL, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setTimestamp(3, Timestamp.valueOf(taskTimestamp));
            pstmt.executeUpdate();
        }
    }

    public static void moveTaskToCompleted(Task task) throws SQLException {
        String insertSql = "INSERT INTO COMPLETED_TASK (TASK_ID, TITLE, DESCRIPTION, COMPLETED_TIME_AND_DATE, TIME_AND_DATE_TO_BE_DONE) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                insertPstmt.setInt(1, task.getTaskId());
                insertPstmt.setString(2, task.getTitle());
                insertPstmt.setString(3, task.getDescription());
                insertPstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                insertPstmt.setTimestamp(5, Timestamp.valueOf(task.getTaskTimestamp()));
                insertPstmt.executeUpdate();
                deleteTaskFromMainList(task.getTaskId(), conn);
            }
        }
    }

    public static void moveTasksToDeleted(List<Task> tasks) throws SQLException {
        String insertSql = "INSERT INTO DELETED_TASK (TASK_ID, TITLE, DESCRIPTION, DELETED_TIME_AND_DATE, TIME_AND_DATE_TO_BE_DONE) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                for (Task task : tasks) {
                    insertPstmt.setInt(1, task.getTaskId());
                    insertPstmt.setString(2, task.getTitle());
                    insertPstmt.setString(3, task.getDescription());
                    insertPstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                    insertPstmt.setTimestamp(5, Timestamp.valueOf(task.getTaskTimestamp()));
                    insertPstmt.addBatch();
                }
                insertPstmt.executeBatch();
                deleteMultipleTasksFromMainList(tasks, conn);
            }
        }
    }

    public static List<Task> getAllTasks() {
        String sql = "SELECT * FROM TO_DO_LIST ORDER BY TIME_AND_DATE_TO_BE_DONE ASC";
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("TASK_ID"),
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        rs.getTimestamp("TIME_AND_DATE_TO_BE_DONE").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static List<Task> getCompletedTasks() {
        String sql = "SELECT * FROM COMPLETED_TASK ORDER BY COMPLETED_TIME_AND_DATE DESC";
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("TASK_ID"),
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        rs.getTimestamp("TIME_AND_DATE_TO_BE_DONE").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static List<Task> getDeletedTasks() {
        String sql = "SELECT * FROM DELETED_TASK ORDER BY DELETED_TIME_AND_DATE DESC";
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("TASK_ID"),
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        rs.getTimestamp("TIME_AND_DATE_TO_BE_DONE").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static void restoreTasks(List<Task> tasks) throws SQLException {
        String insertSql = "INSERT INTO TO_DO_LIST (TASK_ID, TITLE, DESCRIPTION, TIME_AND_DATE_TO_BE_DONE) VALUES (?, ?, ?, ?)";
        String deleteSql = "DELETE FROM DELETED_TASK WHERE TASK_ID = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql); PreparedStatement deletePstmt = conn.prepareStatement(deleteSql)) {
                for (Task task : tasks) {
                    insertPstmt.setInt(1, task.getTaskId());
                    insertPstmt.setString(2, task.getTitle());
                    insertPstmt.setString(3, task.getDescription());
                    insertPstmt.setTimestamp(4, Timestamp.valueOf(task.getTaskTimestamp()));
                    insertPstmt.addBatch();
                    deletePstmt.setInt(1, task.getTaskId());
                    deletePstmt.addBatch();
                }
                insertPstmt.executeBatch();
                deletePstmt.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static void permanentlyDeleteTasks(List<Task> tasks) throws SQLException {
        String sql = "DELETE FROM DELETED_TASK WHERE TASK_ID = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Task task : tasks) {
                pstmt.setInt(1, task.getTaskId());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    private static void deleteTaskFromMainList(int taskId, Connection conn) throws SQLException {
        String sql = "DELETE FROM TO_DO_LIST WHERE TASK_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        }
    }

    private static void deleteMultipleTasksFromMainList(List<Task> tasks, Connection conn) throws SQLException {
        String sql = "DELETE FROM TO_DO_LIST WHERE TASK_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Task task : tasks) {
                pstmt.setInt(1, task.getTaskId());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }


    public static void updateTask(int taskId, String title, String description, LocalDateTime taskTimestamp) throws SQLException {
        String sql = "UPDATE TO_DO_LIST SET TITLE = ?, DESCRIPTION = ?, TIME_AND_DATE_TO_BE_DONE = ? WHERE TASK_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setTimestamp(3, Timestamp.valueOf(taskTimestamp));
            pstmt.setInt(4, taskId);

            pstmt.executeUpdate();
        }
    }


    public static void permanentlyDeleteCompletedTasks(List<Task> tasks) throws SQLException {
        String sql = "DELETE FROM COMPLETED_TASK WHERE TASK_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Task task : tasks) {
                pstmt.setInt(1, task.getTaskId());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }


    public static List<Task> getTasksBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String sql = "SELECT * FROM TO_DO_LIST WHERE TIME_AND_DATE_TO_BE_DONE BETWEEN ? AND ? ORDER BY TIME_AND_DATE_TO_BE_DONE ASC";
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(startDateTime));
            pstmt.setTimestamp(2, Timestamp.valueOf(endDateTime));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("TASK_ID"),
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        rs.getTimestamp("TIME_AND_DATE_TO_BE_DONE").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}