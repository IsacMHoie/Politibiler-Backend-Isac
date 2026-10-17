import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class tester {


    private PolitiBil lagBil(int id, String regNr, String status) {
        PolitiBil bil = new PolitiBil();
        bil.setId(id);
        bil.setRegNr(regNr);
        bil.setStatus(status);
        bil.setOppdrag("");
        return bil;
    }


    private List<PolitiBil> lagTestListe() {
        List<PolitiBil> biler = new ArrayList<>();
        biler.add(lagBil(12, "AA 11111", "Tilgjengelig"));
        biler.add(lagBil(4, "BB 22222", "Under vedlikehold"));
        biler.add(lagBil(1, "CC 33333", "Tilgjengelig"));
        return biler;
    }

    @Test
    void sorterEtterId_sortererNumeriskOgIkkeSomTekst() {
        List<PolitiBil> biler = lagTestListe();

        PolitiBilListe.sorterEtterId(biler);

        assertEquals(1, biler.get(0).getId());
        assertEquals(4, biler.get(1).getId());
        assertEquals(12, biler.get(2).getId());
    }

    @Test
    void hentTilgjengeligeBiler_returnererBareTilgjengelige() {
        List<PolitiBil> biler = lagTestListe();

        List<PolitiBil> tilgjengelige = PolitiBilListe.hentTilgjengeligeBiler(biler);

        assertEquals(2, tilgjengelige.size());
        for (PolitiBil bil : tilgjengelige) {
            assertEquals("Tilgjengelig", bil.getStatus());
        }
    }

    @Test
    void hentTilgjengeligeBiler_erSortertEtterId() {
        List<PolitiBil> biler = lagTestListe();
        PolitiBilListe.sorterEtterId(biler);

        List<PolitiBil> tilgjengelige = PolitiBilListe.hentTilgjengeligeBiler(biler);

        assertEquals(1, tilgjengelige.get(0).getId());
        assertEquals(12, tilgjengelige.get(1).getId());
    }

    @Test
    void hentBilInfoPaRegNr_finnerRiktigBil() {
        List<PolitiBil> biler = lagTestListe();

        PolitiBil bil = PolitiBilListe.hentBilInfoPaRegNr(biler, "BB 22222");

        assertEquals(4, bil.getId());
    }

    @Test
    void endreBilStatusPaRegNr_setterOppdragOgStatus() {
        List<PolitiBil> biler = lagTestListe();

        PolitiBilListe.endreBilStatusPaRegNr(biler, "AA 11111", "Patruljering");

        PolitiBil bil = biler.get(0); // AA 11111 er første bil i testlisten
        assertEquals("Patruljering", bil.getOppdrag());
        assertEquals("I oppdrag", bil.getStatus());
    }
}
