package com.example.gymlogapplication.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gymlogapplication.database.GymLogDatabase;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = GymLogDatabase.GYM_LOG_TABLE)
public class GymLog {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String exercise;
    private double weight;
    private int reps;
    private LocalDateTime localDate;

    public GymLog(String exercise, double weight, int reps) {
        this.exercise = exercise;
        this.weight = weight;
        this.reps = reps;
        localDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "GymLog{" +
                "id=" + id +
                ", exercise='" + exercise + '\'' +
                ", weight=" + weight +
                ", reps=" + reps +
                ", localDate=" + localDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GymLog gymLog = (GymLog) o;
        return id == gymLog.id && Double.compare(weight, gymLog.weight) == 0 && reps == gymLog.reps && Objects.equals(exercise, gymLog.exercise) && Objects.equals(localDate, gymLog.localDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exercise, weight, reps, localDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public LocalDateTime getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDateTime localDate) {
        this.localDate = localDate;
    }
}
