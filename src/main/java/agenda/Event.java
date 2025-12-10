package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Event {

    /**
     * The myTitle of this event
     */
    private String myTitle;
    
    /**
     * The starting time of the event
     */
    private LocalDateTime myStart;

    /**
     * The durarion of the event 
     */
    private Duration myDuration;
    private Repetition repetition;


    /**
     * Constructs an event
     *
     * @param title the title of this event
     * @param start the start time of this event
     * @param duration the duration of this event
     */
    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    public void setRepetition(ChronoUnit frequency) {
        this.repetition = new Repetition(frequency);
    }

    public void addException(LocalDate date) {
        if (repetition != null) {
            this.repetition.addException(date);
        } 
    }

    public void setTermination(LocalDate terminationInclusive) {
       if ( repetition != null) {
            Termination term = new Termination(myStart.toLocalDate(), repetition.getFrequency(), terminationInclusive);
              repetition.setTermination(term);
       }
    }

    public void setTermination(long numberOfOccurrences) {
        if (repetition != null) {
            Termination term = new Termination(myStart.toLocalDate(), repetition.getFrequency(), numberOfOccurrences);
            repetition.setTermination(term);
    }
}

    public int getNumberOfOccurrences() {
        if (repetition != null && repetition.getTermination() != null) {
            return (int) repetition.getTermination().numberOfOccurrences();
        }
        return 1; 
    }

    public LocalDate getTerminationDate() {
        if (repetition != null && repetition.getTermination() != null) {
            return repetition.getTermination().terminationDateInclusive();
        }
        return null;
    }

    /**
     * Tests if an event occurs on a given day
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */
    public boolean isInDay(LocalDate aDay) {
        LocalDateTime dayStart = aDay.atStartOfDay();
        LocalDateTime dayEnd = aDay.plusDays(1).atStartOfDay();

        //cas 1 
        if (repetition == null) {
            LocalDateTime eventEnd = myStart.plus(myDuration);
        
            return myStart.isBefore(dayEnd) && eventEnd.isAfter(dayStart);
        }

        //cas 2
        if (repetition.getExceptions().contains(aDay)) {
            return false; 
        }
        long durationInDays = myDuration.toDays(); 
        
        for (long i = 0; i <= durationInDays + 1; i++) {
            LocalDate candidateDate = aDay.minusDays(i);
           
            long units = repetition.getFrequency().between(myStart.toLocalDate(), candidateDate);
            LocalDate theoreticalDate = myStart.toLocalDate().plus(units, repetition.getFrequency());

            if (theoreticalDate.equals(candidateDate) && units >= 0) {
        
                if (repetition.getExceptions().contains(candidateDate)) {
                    continue; 
                }
    
                if (repetition.getTermination() != null && candidateDate.isAfter(repetition.getTermination().terminationDateInclusive())) {
                    continue; 
                }
                
                LocalDateTime occStart = LocalDateTime.of(candidateDate, myStart.toLocalTime());
                LocalDateTime occEnd = occStart.plus(myDuration);
                
                if (occStart.isBefore(dayEnd) && occEnd.isAfter(dayStart)) {
                    return true;
                }
            }
        }
        
        return false;
    }
   
    /**
     * @return the myTitle
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * @return the myStart
     */
    public LocalDateTime getStart() {
        return myStart;
    }


    /**
     * @return the myDuration
     */
    public Duration getDuration() {
        return myDuration;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}
