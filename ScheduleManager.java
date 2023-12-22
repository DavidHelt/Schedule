import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ScheduleManager {
    private Schedule originalSchedule;
    private int numVariants;
    private int timeoutSeconds;
    private ScheduleEvaluator evaluator;
    private UniqueScheduleGenerator generator;
    private Watchdog watchdog;

    private long generationTime;
    private long evaluationTime;
    private int generatedCount;
    private int evaluatedCount;
    private List<Schedule> topSchedules;

    public ScheduleManager(int numVariants, int timeoutSeconds) {
        this.numVariants = numVariants;
        this.timeoutSeconds = timeoutSeconds;
        initializeComponents();
    }

    private void initializeComponents() {
        this.originalSchedule = initializeOriginalSchedule();
        this.evaluator = new ScheduleEvaluator();
        this.generator = new UniqueScheduleGenerator(originalSchedule);
        this.watchdog = new Watchdog(timeoutSeconds);
    }

    /** In this method, we will be using the ExecutorService to run the schedule generation and evaluation in parallel. **/
    public void runScheduleGenerationAndEvaluation() {
        ExecutorService generatorExecutor = Executors.newFixedThreadPool(4);
        ExecutorService evaluatorExecutor = Executors.newFixedThreadPool(4);

        try {
            List<CompletableFuture<Schedule>> variantFutures = new ArrayList<>();
            for (int i = 0; i < numVariants; i++) {
                variantFutures.add(CompletableFuture.supplyAsync(generator::generateRandomVariant, generatorExecutor));
            }

            long startGenerationTime = System.currentTimeMillis();
            CompletableFuture<Void> allOf = CompletableFuture.allOf(variantFutures.toArray(new CompletableFuture[0]));
            allOf.get();
            long endGenerationTime = System.currentTimeMillis();
            this.generationTime = endGenerationTime - startGenerationTime;
            this.generatedCount = variantFutures.size();

            long startEvaluationTime = System.currentTimeMillis();
            this.topSchedules = variantFutures.stream()
                    .map(CompletableFuture::join)
                    .filter(evaluator::meetsSpecificCriteria)
                    .sorted(Comparator.comparingInt(evaluator::evaluateSchedule).reversed())
                    .limit(1000)
                    .collect(Collectors.toList());
            long endEvaluationTime = System.currentTimeMillis();
            this.evaluationTime = endEvaluationTime - startEvaluationTime;
            this.evaluatedCount = topSchedules.size();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            generatorExecutor.shutdown();
            evaluatorExecutor.shutdown();
        }
    }

    public void printOriginalSchedule() {
        System.out.println("Original Schedule:\n" + formatSchedule(originalSchedule));
    }

    public void printStatistics() {
        System.out.println("Total generated variants: " + generatedCount);
        System.out.println("Total evaluated variants: " + evaluatedCount);
        System.out.println("Generation time: " + generationTime + " ms");
        System.out.println("Evaluation time: " + evaluationTime + " ms");
    }

    public void printTopSchedules(int numberOfSchedules) {
        for (int i = 0; i < Math.min(numberOfSchedules, topSchedules.size()); i++) {
            System.out.println("Top Schedule " + (i + 1) + ":\n" + formatSchedule(topSchedules.get(i)));
        }
    }

    /** In this method we initiliaze original schedule with Teacher+NameOfTheSubject and same for Classroom **/
    private Schedule initializeOriginalSchedule() {
        Schedule schedule = new Schedule();
        List<String> subjects = Arrays.asList(
                "M", "DS", "DS", "PSS", "PSS", "A", null, "TV", null, null,
                "PIS", "M", "PIS", "PIS", "TP", "A", "CJ", null, null, null,
                "CIT", "CIT", "WA", "DS", "PV", null, "PSS", null, null, null,
                "AM", "M", "WA", "WA", null, "A", "C", "PIS", "TV", null,
                "C", "A", "M", "PV", "PV", "AM", null, null, null, null
        );

        for (int day = 0; day < 5; day++) {
            for (int hour = 0; hour < 10; hour++) {
                int index = day * 10 + hour;
                String subject = subjects.get(index);
                if (subject != null) {
                    ScheduleSlot slot = new ScheduleSlot(subject, "Teacher" + subject, "Classroom" + subject);
                    schedule.setScheduleSlot(day, hour, slot);
                } else {
                    schedule.setScheduleSlot(day, hour, null);
                }
            }
        }

        return schedule;
    }

    private String formatSchedule(Schedule schedule) {
        StringBuilder sb = new StringBuilder();
        for (int day = 0; day < 5; day++) {
            for (int hour = 0; hour < 10; hour++) {
                ScheduleSlot slot = schedule.getScheduleSlot(day, hour);
                if (slot != null) {
                    sb.append(slot.getSubject())
                            .append(" (")
                            .append(slot.getTeacher())
                            .append(", ")
                            .append(slot.getClassroom())
                            .append(") ");
                } else {
                    sb.append("Free ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
