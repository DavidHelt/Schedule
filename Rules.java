import java.util.HashMap;
import java.util.Map;

public class Rules {

     static int evaluateTimeSlotPreferences(Schedule schedule, int index) {
        int dayIndex = index / 10;
        int hourIndex = index % 10;
        ScheduleSlot slot = schedule.getScheduleSlot(dayIndex, hourIndex);

        // Example preferences:
        // Prefer having classes in the morning, penalize late classes
        if (hourIndex < 4) { // Prefer classes before 5th hour
            return (slot != null) ? 50 : -50;
        } else if (hourIndex > 6) { // Penalize classes after 7th hour
            return (slot != null) ? -50 : 50;
        }

        // Specific preferences: Friday's 9th hour

         return 0;
    }


    static int evaluateMultipleSessionsInDay(Schedule schedule, int index) {
        // Only evaluate once per day (at the start of each day)
        if (index % 10 != 0) {
            return 0;
        }

        int dayIndex = index / 10;
        Map<String, Integer> subjectCount = new HashMap<>();

        // Iterate over all hours in a day
        for (int hourIndex = 0; hourIndex < 10; hourIndex++) {
            ScheduleSlot slot = schedule.getScheduleSlot(dayIndex, hourIndex);
            if (slot != null) {
                String subject = slot.getSubject();
                subjectCount.put(subject, subjectCount.getOrDefault(subject, 0) + 1);
            }
        }

        // Check for multiple sessions of the same subject in a day
        for (int count : subjectCount.values()) {
            if (count > 1) {
                return -100; // Penalize if the same subject occurs more than once in a day
            }
        }

        return 0;
    }

    static int evaluateLunchBreak(Schedule schedule, int index) {
        int dayIndex = index / 10;
        int hourIndex = index % 10;

        // Check for lunch break between hours 5 to 8
        if (hourIndex >= 4 && hourIndex <= 7) {
            return schedule.getScheduleSlot(dayIndex, hourIndex) == null ? 0 : -100; // Penalizing if there's no lunch break
        }

        return 0;
    }


    static int evaluateAfternoonSlots(Schedule schedule, int index) {
        int dayIndex = index / 10;
        int hourIndex = index % 10;

        // Check from 5th to 9th hour (index 4 to 8)
        if (hourIndex >= 4 && hourIndex < 9) {
            ScheduleSlot currentSlot = schedule.getScheduleSlot(dayIndex, hourIndex);
            ScheduleSlot nextSlot = schedule.getScheduleSlot(dayIndex, hourIndex + 1);

            // If both current and next slots are not null (i.e., both are occupied), penalize
            return (currentSlot != null && nextSlot != null) ? -100 : 0;
        }
        return 0;
    }

    static int evaluateDailyHours(Schedule schedule, int index) {
        if (index % 10 == 0) { // Check only at the start of each day
            int dayIndex = index / 10;
            int totalHours = 0;
            for (int hourIndex = 0; hourIndex < 10; hourIndex++) {
                if (schedule.getScheduleSlot(dayIndex, hourIndex) != null) {
                    totalHours++;
                }
            }
            if (totalHours < 4) {
                return -200; // Penalize if less than 4 hours
            } else if (totalHours > 9) {
                return -200; // Penalize if more than 9 hours
            }
        }
        return 0;
    }

    static int evaluateKeySubjectsScheduling(Schedule schedule, int index) {
        int dayIndex = index / 10;
        int hourIndex = index % 10;

        if (hourIndex == 0 || hourIndex == 4) { // First hour or first hour after lunch break
            ScheduleSlot slot = schedule.getScheduleSlot(dayIndex, hourIndex);
            if (slot != null && isKeySubject(slot.getSubject())) {
                // Penalize if Mathematics or PV is scheduled at these hours
                return -50;
            }
        }
        return 0;
    }

    static boolean isKeySubject(String subject) {
        // Identifying Mathematics (M) and PV as key subjects
        return "M".equals(subject) || "PV".equals(subject);
    }
}

