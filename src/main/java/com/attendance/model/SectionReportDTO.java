package com.attendance.model;

import java.sql.Date;

public class SectionReportDTO {
    private Date date;
    private int section;
    private int semester;
    private int total;
    private int present;

    public SectionReportDTO(Date date, int section, int semester, int total, int present) {
        this.date = date;
        this.section = section;
        this.semester = semester;
        this.total = total;
        this.present = present;
    }

    public Date getDate() {
        return date;
    }

    public int getSection() {
        return section;
    }

    public int getSemester() {
        return semester;
    }

    public int getTotal() {
        return total;
    }

    public int getPresent() {
        return present;
    }

    public double getPercentage() {
        return total == 0 ? 0 : (double) present / total * 100;
    }
}
