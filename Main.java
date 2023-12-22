public class Main {
    public static void main(String[] args) {
        ScheduleManager manager = new ScheduleManager(1000000, 180);
        manager.runScheduleGenerationAndEvaluation();

        manager.printOriginalSchedule();
        manager.printStatistics();
        manager.printTopSchedules(3);
    }
}



