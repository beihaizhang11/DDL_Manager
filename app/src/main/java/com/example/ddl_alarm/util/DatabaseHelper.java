// app/src/main/java/com/example/ddl_alarm/util/DatabaseHelper.java
package com.example.ddl_alarm.util;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import com.mysql.jdbc.Driver;

import com.example.ddl_alarm.model.Major;
import com.example.ddl_alarm.model.Deadline;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://39.105.0.202:3306/ddlalarm";
    private static final String USER = "root";
    private static final String PASS = "OOD20241022";
    
    public static List<Major> getAllMajors() {
        List<Major> majors = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "SELECT major_id, major_name FROM majors";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("major_id");
                String name = rs.getString("major_name");
                majors.add(new Major(id, name));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting majors", e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error closing connection", e);
            }
        }
        return majors;
    }
    
    public static List<Deadline> getDeadlinesByMajor(int majorId) {
        List<Deadline> deadlines = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM deadlines WHERE major_id = " + majorId;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("deadline_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                Date dueDate = new Date(rs.getTimestamp("due_date").getTime());
                Date createdAt = new Date(rs.getTimestamp("created_at").getTime());
                deadlines.add(new Deadline(id, majorId, title, description, dueDate, createdAt));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting deadlines", e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error closing connection", e);
            }
        }
        return deadlines;
    }
}