package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {


    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    private final Map<DayOfWeek, List<TrainingSession>> daySessions = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap =
                timetable.computeIfAbsent(day, d -> new TreeMap<>());

        dayMap
                .computeIfAbsent(time, t -> new ArrayList<>())
                .add(trainingSession);

        List<TrainingSession> sessionsForDay =
                daySessions.computeIfAbsent(day, d -> new ArrayList<>());

        int index = 0;
        while (index < sessionsForDay.size()
                && sessionsForDay.get(index).getTimeOfDay().compareTo(time) <= 0) {
            index++;
        }
        sessionsForDay.add(index, trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return daySessions.getOrDefault(dayOfWeek, List.of());
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(
            DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap == null) {
            return List.of();
        }
        return dayMap.getOrDefault(timeOfDay, List.of());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> counters = new HashMap<>();

        for (List<TrainingSession> sessions : daySessions.values()) {
            for (TrainingSession session : sessions) {
                Coach coach = session.getCoach();
                counters.put(coach, counters.getOrDefault(coach, 0) + 1);
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : counters.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
        return result;
    }
}
