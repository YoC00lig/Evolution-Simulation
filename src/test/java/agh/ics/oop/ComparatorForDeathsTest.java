package agh.ics.oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparatorForDeathsTest {
    @Test
    void comparatorTest() {
        AbstractWorldMap map = new AbstractWorldMap(5, 5, true,true,
                true,true, 3, 4, 5);

        InfoField i1 = new InfoField(new Vector2d(2,5));
        InfoField i2 = new InfoField(new Vector2d(2,5));
        InfoField i3 = new InfoField(new Vector2d(2,5));

        i1.setDeathStatus(8);
        i2.setDeathStatus(10);
        i3.setDeathStatus(2);

        map.informations.add(i1);
        map.informations.add(i2);
        map.informations.add(i3);

        ComparatorForDeaths deathsComparator = new ComparatorForDeaths();
        map.informations.sort(deathsComparator);

        // indeksy w map.informations tych elementów (i1,i2,i3) są większe bądź równe 5*5=25, ponieważ domyślnie
        // tworzona jest tablica  map.informations w konstruktorze AbstractWorldMap, a ja tutaj
        // dodaje 3 dodatkowe elementy do tych już istniejących (Te istniejące mają pole .death domyślnie równe 0,
        // dlatego jeśli komparator działa dobrze to one będą na początku,a te dodane przeze mnie na samym końcu
        // w odpowiedniej kolejności - najpierw najmniejszy, później większy itd.) - stąd indeksy 26,27,25 a nie 1,2,0
        assertEquals(i1, map.informations.get(26));
        assertEquals(i2, map.informations.get(27));
        assertEquals(i3, map.informations.get(25));
    }
}
