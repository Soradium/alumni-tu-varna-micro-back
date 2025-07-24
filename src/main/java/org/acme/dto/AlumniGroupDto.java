package org.acme.dto;

public class AlumniGroupDto {
    private Long membershipId;
    private Long alumniId;
    private Long groupId;

    private int groupNumber;
    private int graduationYear;
    private Long specialityId;

    private double averageScore;

    public AlumniGroupDto() {}

    public AlumniGroupDto(Long membershipId, Long alumniId, Long groupId,
                          int groupNumber, int graduationYear, Long specialityId,
                          double averageScore) {
        this.membershipId = membershipId;
        this.alumniId = alumniId;
        this.groupId = groupId;
        this.groupNumber = groupNumber;
        this.graduationYear = graduationYear;
        this.specialityId = specialityId;
        this.averageScore = averageScore;
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public Long getAlumniId() {
        return alumniId;
    }

    public void setAlumniId(Long alumniId) {
        this.alumniId = alumniId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Long getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Long specialityId) {
        this.specialityId = specialityId;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
}
