import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Schedule {
    private final List<ScheduleSlot> scheduleSlots;

    public Schedule() {
        this.scheduleSlots = new ArrayList<>(Collections.nCopies(50, null)); // Assuming 50 slots for a 5-day week with 10 slots per day
    }


    public void setScheduleSlot(int dayIndex, int hourIndex, ScheduleSlot slot) {
        int index = dayIndex * 10 + hourIndex;
        scheduleSlots.set(index, slot);
    }

    public List<ScheduleSlot> getScheduleSlots() {
        return new ArrayList<>(scheduleSlots);
    }

    public ScheduleSlot getScheduleSlot(int dayIndex, int hourIndex) {
        return scheduleSlots.get(dayIndex * 10 + hourIndex);
    }

    // Additional methods as needed...
}
