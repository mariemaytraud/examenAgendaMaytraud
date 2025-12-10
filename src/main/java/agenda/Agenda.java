package agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : An agenda that stores events
 */
public class Agenda {

    private final List<Event> events = new ArrayList<>();
    /**
     * Adds an event to this agenda
     *
     * @param e the event to add
     */
    public void addEvent(Event e) {
        this.events.add(e);
    }

    /**
     * Computes the events that occur on a given day
     *
     * @param day the day toi test
     * @return a list of events that occur on that day
     */
    public List<Event> eventsInDay(LocalDate day) {
        List<Event> result = new ArrayList<>();
        for (Event e : events) {
            if (e.isInDay(day))  {
                result.add(e);
            }
        }
        return result;
    }


//Bonus 

public List<Event> findByTitle(String title){
    return events.stream()
                 .filter(e -> e.getTitle().equals(title))
                 .toList();
}

public boolean isFreeFor(Event e){
    
        LocalDate startDay = e.getStart().toLocalDate();
        LocalDate endDay = e.getStart().plus(e.getDuration()).toLocalDate();
        
        LocalDate current = startDay;
        while (!current.isAfter(endDay)) {

            List<Event> eventsOnDay = eventsInDay(current);
            
            for (Event existing : eventsOnDay) {

                
                LocalDateTime existingStart = LocalDateTime.of(current, existing.getStart().toLocalTime());
                LocalDateTime existingEnd = existingStart.plus(existing.getDuration());
                
                LocalDateTime newStart = e.getStart(); 
                LocalDateTime newEnd = newStart.plus(e.getDuration());
                
                
                if (existingStart.isBefore(newEnd) && existingEnd.isAfter(newStart)) {
                    return false; 
                }
            }
            current = current.plusDays(1);
        }
        return true;
    }
}

