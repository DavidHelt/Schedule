import java.util.*;
import java.util.concurrent.*;

public class UniqueScheduleGenerator {
    private final Schedule originalSchedule;
    private final ConcurrentHashMap<String, Boolean> generatedSchedules;

    public UniqueScheduleGenerator(Schedule originalSchedule) {
        this.originalSchedule = originalSchedule;
        this.generatedSchedules = new ConcurrentHashMap<>();
    }

    public Schedule generateRandomVariant() {
        Random random = new Random();
        Schedule variant = new Schedule();

        for (int day = 0; day < 5; day++) {
            for (int hour = 0; hour < 10; hour++) {
                ScheduleSlot originalSlot = originalSchedule.getScheduleSlot(day, hour);

                if (originalSlot != null) {
                    // Example modification: Swap with another random slot
                    int swapDay = random.nextInt(5);
                    int swapHour = random.nextInt(10);

                    ScheduleSlot swapSlot = originalSchedule.getScheduleSlot(swapDay, swapHour);

                    variant.setScheduleSlot(day, hour, swapSlot);
                    variant.setScheduleSlot(swapDay, swapHour, originalSlot);
                }
            }
        }

        return variant;
    }

    private String createVariantKey(Schedule schedule) {
        StringBuilder keyBuilder = new StringBuilder();
        for (ScheduleSlot slot : schedule.getScheduleSlots()) {
            if (slot != null) {
                keyBuilder.append(slot.getSubject())
                        .append(slot.getTeacher())
                        .append(slot.getClassroom());
            } else {
                keyBuilder.append("FreeSlot;");
            }
        }
        return keyBuilder.toString();
    }

}
