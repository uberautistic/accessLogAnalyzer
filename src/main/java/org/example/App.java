package org.example;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

    //TODO Для теста поменяйте path и outputPath, файл "access.log" для теста есть в репо
    private static final String path = "ПУТЬ ЛОГ ФАЙЛА";//
    private static final String outputPath = "ПУТЬ ДЛЯ JSON ФАЙЛА";//
    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
    private static final String dateTimeRegEx = ".+\\[([^\\]]{20}\\s\\+[0-9]{4})\\].+";
    private static final Pattern dateTimePattern = Pattern.compile(dateTimeRegEx);

    public static void main(String[] args) throws IOException {
        HashMap<Long, Integer> countPerSecond = new HashMap<>();
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        int reqCount = 0;
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (String line : lines) {
            Matcher matcher = dateTimePattern.matcher(line);
            if (!matcher.find())
                continue;
            String dateTime = matcher.group(1);
            long time = getTimestamp(dateTime);
            if (!countPerSecond.containsKey(time))
                countPerSecond.put(time, 0);
            countPerSecond.put(time, countPerSecond.get(time) + 1);
            minTime = Math.min(time, minTime);
            maxTime = Math.max(time, maxTime);
            reqCount++;
        }
        int maxRPS = Collections.max(countPerSecond.values());
        double averageRPS = (double) reqCount / (maxTime - minTime);
        Statisctics statisctics = new Statisctics(maxRPS, averageRPS);
        write(statisctics);
    }

    public static long getTimestamp(String dateTime) {
        LocalDateTime time = LocalDateTime.parse(dateTime, formatter);
        return time.toEpochSecond(ZoneOffset.UTC);
    }

    public static void write(Statisctics statisctics) throws IOException {
        String json = new Gson().toJson(statisctics);
        FileWriter writer = new FileWriter(outputPath + "report-" + LocalDate.now() + ".json");
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
