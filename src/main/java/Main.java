import io.javalin.Javalin;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        PolitiBilListe liste = Client.hentPolitibiler();
        List<PolitiBil> biler = liste.hentBiler();
        PolitiBilListe.sorterEtterId(biler);
        PolitiBilListe.PrintPolitiBilListe(biler);
        System.out.println("\n");
        List<PolitiBil> tilgjengeligeBiler = PolitiBilListe.hentTilgjengeligeBiler(biler);


        Javalin app = Javalin.create().start(8080);
        app.get("/politibiler/tilgjengelige", ctx ->
                ctx.json(PolitiBilListe.hentTilgjengeligeBiler(biler)));

        app.get("/politibiler", ctx -> {
            String regNr = ctx.queryParam("regNr");
            ctx.json(PolitiBilListe.hentBilInfoPaRegNr(biler, regNr));
        });

        app.post("/politibiler/oppdrag", ctx -> {
            String regNr = ctx.queryParam("regNr");
            String oppdrag = ctx.queryParam("oppdrag");
            PolitiBilListe.endreBilStatusPaRegNr(biler, regNr, oppdrag);
            ctx.result("Oppdrag tildelt");
        });
    }
}