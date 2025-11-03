package utils;

public class Metrics {
    public long dfsVisits = 0;
    public long edgeChecks = 0;
    public long relaxations = 0;
    public long startTime, endTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public double getTimeMs() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public long getTotalOps() {
        return dfsVisits + edgeChecks + relaxations;
    }

    @Override
    public String toString() {
        return String.format("{time_ms=%.3f, dfs=%d, edges=%d, relax=%d, total=%d}",
                getTimeMs(), dfsVisits, edgeChecks, relaxations, getTotalOps());
    }
}
