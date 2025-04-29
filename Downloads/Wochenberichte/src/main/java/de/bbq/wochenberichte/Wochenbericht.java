package de.bbq.wochenberichte;

import java.time.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Wochenbericht {
    private String name;
    private String umschulung;
    private long datum;
    private String[] eingabeFelder;
    private String[] stundenFelder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUmschulung() {
        return umschulung;
    }

    public void setUmschulung(String umschulung) {
        this.umschulung = umschulung;
    }

    public LocalDate getDatum() {
        return LocalDate.ofEpochDay(datum);
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum.toEpochDay();
    }

    public String[] getEingabeFelder() {
        return eingabeFelder;
    }

    public void setEingabeFelder(String[] eingabeFelder) {
        this.eingabeFelder = eingabeFelder;
    }

    public String[] getStundenFelder() {
        return stundenFelder;
    }

    public void setStundenFelder(String[] stundenFelder) {
        this.stundenFelder = stundenFelder;
    }
}
