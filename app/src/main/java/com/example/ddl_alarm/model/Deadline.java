package com.example.ddl_alarm.model;

import java.util.Date;

public class Deadline {
    private int deadlineId;
    private int majorId;
    private String title;
    private String description;
    private Date dueDate;
    private Date createdAt;
    
    public Deadline(int deadlineId, int majorId, String title, 
                   String description, Date dueDate, Date createdAt) {
        this.deadlineId = deadlineId;
        this.majorId = majorId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }
    
    // Getters
    public int getDeadlineId() { return deadlineId; }
    public int getMajorId() { return majorId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getDueDate() { return dueDate; }
    public Date getCreatedAt() { return createdAt; }
    
    // Setters
    public void setDeadlineId(int deadlineId) { this.deadlineId = deadlineId; }
    public void setMajorId(int majorId) { this.majorId = majorId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
