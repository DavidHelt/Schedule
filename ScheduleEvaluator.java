import java.util.*;
import java.util.function.BiFunction;

public class ScheduleEvaluator {
    private List<BiFunction<Schedule, Integer, Integer>> rules;

    public ScheduleEvaluator() {
        rules = new ArrayList<>();
        initializeRules();
    }

    private void initializeRules() {
        // Rule 1: Example - if the subject is "M" and the teacher is "A", add 100
        rules.add(Rules::evaluateTimeSlotPreferences);

        // Rule 2: Custom Rule 1
        rules.add(Rules::evaluateMultipleSessionsInDay);

        // Rule 4: Lunch Break Rule
        rules.add(Rules::evaluateLunchBreak);

        // Rule 5: Each day should have > 4 and < 9 hours of classes
        rules.add(Rules::evaluateDailyHours);
        //Rule 7
        rules.add(Rules::evaluateKeySubjectsScheduling);
        // Rule 8: No back-to-back classes, meaning no two afternoon breaks
        rules.add(Rules::evaluateAfternoonSlots);


    }

    public int evaluateSchedule(Schedule schedule) {
        int score = 0;
        for (int index = 0; index < 50; index++) {
            for (BiFunction<Schedule, Integer, Integer> rule : rules) {
                score += rule.apply(schedule, index);
            }
        }
        return score;
    }

    public boolean meetsSpecificCriteria(Schedule schedule) {
        // Check for a lunch break between the 5th and 8th hours each day
        return meetsLunchBreakCondition(schedule);
    }

    private boolean meetsLunchBreakCondition(Schedule schedule) {
        for (int day = 0; day < 5; day++) {
            boolean hasBreak = false;
            for (int hour = 4; hour <= 7; hour++) { // Checking hours 5 to 8
                if (schedule.getScheduleSlot(day, hour) == null) {
                    hasBreak = true;
                    break;
                }
            }
            if (!hasBreak) return false; // No lunch break on this day
        }
        return true; // Lunch break every day
    }
}
