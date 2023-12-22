public class Watchdog {
    private final int timeoutSeconds;
    final long startTime;

    public Watchdog(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
        this.startTime = System.currentTimeMillis();
    }

    public boolean checkTimeout() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return elapsedTime >= timeoutSeconds * 1000;
    }

    public long getStartTime() {
        return startTime;
    }
}
