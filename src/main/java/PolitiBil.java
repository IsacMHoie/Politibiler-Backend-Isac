import com.fasterxml.jackson.annotation.JsonProperty;

public class PolitiBil {
    public int id;
    public String merke;
    public String modell;

    @JsonProperty("årsmodell")
    public int arsmodell;

    public String regNr;
    public String status;
    public String oppdrag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerke() {
        return merke;
    }

    public void setMerke(String merke) {
        this.merke = merke;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public int getArsmodell() {
        return arsmodell;
    }

    public void setArsmodell(int arsmodell) {
        this.arsmodell = arsmodell;
    }

    public String getRegNr() {
        return regNr;
    }

    public void setRegNr(String regNr) {
        this.regNr = regNr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOppdrag() {
        return oppdrag;
    }

    public void setOppdrag(String oppdrag) {
        this.oppdrag = oppdrag;
    }
}