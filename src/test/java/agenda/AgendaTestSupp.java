package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class AgendaTestSupp {
    @Test
    public void testFindByTitle() {
        Agenda agenda = new Agenda();
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        Duration duration = Duration.ofHours(1);

        Event e1 = new Event("Raclette", start, duration);
        Event e2 = new Event("Boxe", start.plusHours(2), duration);
        Event e3 = new Event("Raclette", start.plusDays(1), duration);

        agenda.addEvent(e1);
        agenda.addEvent(e2);
        agenda.addEvent(e3);

        // Test cas trouvé (2 événements "Raclette")
        List<Event> Raclettes = agenda.findByTitle("Raclette");
        assertEquals(2, Raclettes.size(), "On doit trouver 2 Raclette");
        
        // Test cas non trouvé
        List<Event> danse = agenda.findByTitle("Danse");
        assertTrue(danse.isEmpty(), "On ne doit rien trouver pour Danse");
    }

    @Test
    public void testIsFreeFor() {
        Agenda agenda = new Agenda();
        LocalDateTime start1 = LocalDateTime.of(2023, 1, 1, 10, 0);
        Duration duration = Duration.ofHours(1);
        Event e1 = new Event("Event 1", start1, duration); 
        agenda.addEvent(e1);

        // Cas 1 : Libre 
        Event e2 = new Event("Event 2", LocalDateTime.of(2023, 1, 1, 12, 0), duration);
        assertTrue(agenda.isFreeFor(e2), "L'agenda devrait être libre à 12h");

        // Cas 2 : Occupé
        Event e3 = new Event("Event 3", start1, duration);
        assertFalse(agenda.isFreeFor(e3), "L'agenda ne devrait pas être libre (chevauchement exact)");

        Event e4 = new Event("Event 4", LocalDateTime.of(2023, 1, 1, 10, 30), duration);
        assertFalse(agenda.isFreeFor(e4), "L'agenda ne devrait pas être libre (chevauchement partiel)");
    }

    

    @Test
    public void testEventGettersAndToString() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        Duration duration = Duration.ofHours(1);
        Event e = new Event("Test", start, duration);

        // Test des getters
        assertEquals("Test", e.getTitle());
        assertEquals(start, e.getStart());
        assertEquals(duration, e.getDuration());

        // Test de toString 
        String s = e.toString();
        assertNotNull(s);
        assertTrue(s.contains("Test"));
        assertTrue(s.contains("duration="));
    }

    @Test
    public void testAgendaLogic() {
        Agenda agenda = new Agenda();
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        Duration duration = Duration.ofHours(1);

        Event e1 = new Event("POO", start, duration);
        Event e2 = new Event("Boxe", start.plusHours(2), duration);
        Event e3 = new Event("POO", start.plusDays(1), duration);

        agenda.addEvent(e1);
        agenda.addEvent(e2);
        agenda.addEvent(e3);

       
        assertEquals(2, agenda.findByTitle("POO").size(), "On doit trouver 2 POO");
        
        assertTrue(agenda.findByTitle("Zumba").isEmpty(), "On ne doit rien trouver pour Zumba");

        Event collision = new Event("Collision", start, duration); 
        assertFalse(agenda.isFreeFor(collision), "L'agenda ne doit pas être libre");
      
        Event free = new Event("Free", start.plusHours(5), duration); 
        assertTrue(agenda.isFreeFor(free), "L'agenda doit être libre");
        
        
        Agenda emptyAgenda = new Agenda();
        assertTrue(emptyAgenda.isFreeFor(e1), "Un agenda vide est toujours libre");
    }

    

    @Test
    public void testMethodsOnNonRepetitiveEvent() {
       
        
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        Event simple = new Event("Simple", start, Duration.ofHours(1));


        simple.addException(LocalDate.now());
        simple.setTermination(LocalDate.now());
        simple.setTermination(5);


        assertEquals(1, simple.getNumberOfOccurrences());
        assertNull(simple.getTerminationDate());
        
    
        assertNotNull(simple.toString());
    }
    
    @Test
    public void testEventTerminationGettersOnInfiniteRepetition() {
      
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        Event infinite = new Event("Infini", start, Duration.ofHours(1));
        infinite.setRepetition(ChronoUnit.DAYS);
        
        assertEquals(1, infinite.getNumberOfOccurrences());
        assertNull(infinite.getTerminationDate());
    }

    @Test
    public void testRepetitionAndTerminationBehaviors() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        Event rep = new Event("Rep", start, Duration.ofHours(1));
        rep.setRepetition(ChronoUnit.DAYS);

     
        rep.setTermination(LocalDate.of(2023, 1, 5));
        assertEquals(5, rep.getNumberOfOccurrences());
        assertEquals(LocalDate.of(2023, 1, 5), rep.getTerminationDate());

       
        LocalDate ex = LocalDate.of(2023, 1, 3);
        rep.addException(ex);
        assertFalse(rep.isInDay(ex), "La répétition doit être exclue le jour d'exception");
        assertTrue(rep.isInDay(LocalDate.of(2023, 1, 2)), "La répétition doit avoir une occurrence le 02/01/2023");

   
        Event rep2 = new Event("Rep2", start, Duration.ofHours(1));
        rep2.setRepetition(ChronoUnit.DAYS);
        rep2.setTermination(3);
        assertEquals(3, rep2.getNumberOfOccurrences());
        assertEquals(LocalDate.of(2023,1,3), rep2.getTerminationDate());
    }

}