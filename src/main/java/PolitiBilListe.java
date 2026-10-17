import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;


public class PolitiBilListe {
    public List<PolitiBil> biler;


    public static List<PolitiBil> sorterEtterId(List<PolitiBil> liste){
        liste.sort(Comparator.comparingInt(PolitiBil::getId));
        return liste;
    }


    public static List<PolitiBil> hentTilgjengeligeBiler(List<PolitiBil> liste) {
        List<PolitiBil> tilgjengeligePolitiBiler = new ArrayList<>();
        for (PolitiBil bil : liste) {
            if ("Tilgjengelig".equals(bil.getStatus())) {
                tilgjengeligePolitiBiler.add(bil);
            }
        }
        return tilgjengeligePolitiBiler;
    }

    public static PolitiBil hentBilInfoPaRegNr(List<PolitiBil> liste, String regNr) {
        PolitiBil valgtBil = new PolitiBil();
        for (PolitiBil bil : liste){
            if (bil.getRegNr().equals(regNr)){
                return bil;
            }
        }
        return null;
    }

    public static void endreBilStatusPaRegNr(List<PolitiBil> liste, String regNr, String nyttOppdrag) {
            for (PolitiBil bil : liste){
                if (bil.getRegNr().equals(regNr)){
                    if(!bil.getStatus().equals("Under vedlikehold")){
                        bil.setOppdrag(nyttOppdrag);
                        bil.setStatus("I oppdrag");
                    }



                }
            }
    }

    public static void PrintPolitiBilListe(List<PolitiBil> liste){
        for (PolitiBil bil : liste) {
            System.out.println(bil.id + " - " + bil.regNr + " - " + bil.merke + " " + bil.modell
                    + " (" + bil.arsmodell + ") - " + bil.status + " - " + bil.oppdrag);
        }
    }
    public static void PrintPolitiBil(PolitiBil bil){
            System.out.println(bil.id + " - " + bil.regNr + " - " + bil.merke + " " + bil.modell
                    + " (" + bil.arsmodell + ") - " + bil.status + " - " + bil.oppdrag);
    }


    public List<PolitiBil> hentBiler() {
        return biler;
    }
}