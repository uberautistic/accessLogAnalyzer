package org.example;

public class Statisctics {
    private final int maxRPS;//макс запросов в секунду
    private final double averageRPS;//среднее запросов в секунду

    public Statisctics(int maxRPS, double averageRPS) {
        this.maxRPS = maxRPS;
        this.averageRPS = averageRPS;
    }

    public int getMaxRPS() {
        return maxRPS;
    }

    public double getAverageRPS() {
        return averageRPS;
    }
}
