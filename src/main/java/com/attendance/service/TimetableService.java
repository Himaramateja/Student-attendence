package com.attendance.service;

import com.attendance.dao.SubjectDAO;
import com.attendance.model.Subject;
import java.sql.SQLException;
import java.util.*;

public class TimetableService {
    private SubjectDAO subjectDAO = new SubjectDAO();

    public Map<String, List<String>> generateTimetable(int section, int semester) throws SQLException {
        List<Subject> allSubjects = subjectDAO.getAllSubjects();
        if (allSubjects.isEmpty()) {
            return Collections.emptyMap();
        }

        String[] dayNames = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        Map<String, List<String>> timetable = new LinkedHashMap<>();

        int subjectCount = allSubjects.size();

        for (int dayIndex = 0; dayIndex < dayNames.length; dayIndex++) {
            List<String> dailyPeriods = new ArrayList<>();
            for (int slotIndex = 0; slotIndex < 7; slotIndex++) {
                // 1. Calculate a unique Period ID for this time slot (1-7)
                // The offset moves by day, section, and semester to ensure no collisions
                int periodID = (slotIndex + dayIndex * 2 + section * 3 + semester * 5) % 7 + 1;

                // 2. Map this Period ID to a Subject
                // We use another class-specific offset so subjects are also unique per slot
                int subjectIndex = (periodID + section * 11 + semester * 17) % subjectCount;
                String subjectName = allSubjects.get(subjectIndex).getSubjectName();

                // Format as: "Subject Name (P1)"
                dailyPeriods.add(subjectName + " (P" + periodID + ")");
            }
            timetable.put(dayNames[dayIndex], dailyPeriods);
        }

        return timetable;
    }

    public List<String> getTimings() {
        return Arrays.asList(
                "09:30 - 10:20",
                "10:20 - 11:10",
                "11:10 - 12:00",
                "12:00 - 12:50",
                "LUNCH (12:50 - 01:50)",
                "01:50 - 02:40",
                "02:40 - 03:30",
                "03:30 - 04:20");
    }
}
