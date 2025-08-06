package org.acme.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;

public class AlumniGroupsMembershipDto {
    private Integer id;
    private Integer facultyNumber;
    private int groupNumber;
    private double averageScore;

    public AlumniGroupsMembershipDto() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(Integer facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
}
