package Modele;

import java.util.ArrayList;
import java.util.List;

public class Kurs implements Comparable<Kurs>{

    private long ID;
    private String name;
    private long lehrer;
    private int maximaleAnzahlStudenten;
    private List<Long> listeStudenten;
    private int ects;

    public Kurs(long ID, String name, long lehrer, int maximaleAnzahlStudenten, List<Long> listeStudenten, int ECTS) {
        this.ID = ID;
        this.name = name;
        this.lehrer = lehrer;
        this.maximaleAnzahlStudenten = maximaleAnzahlStudenten;
        this.listeStudenten = listeStudenten;
        this.ects = ECTS;
    }

    public Kurs(long ID, String name, long lehrer, int maximaleAnzahlStudenten, int ECTS) {
        this.ID = ID;
        this.name = name;
        this.lehrer = lehrer;
        this.maximaleAnzahlStudenten = maximaleAnzahlStudenten;
        this.listeStudenten = new ArrayList<>();
        this.ects = ECTS;
    }

    public Kurs(long ID,int ECTS) {
        this.ID = ID;
        this.name = "";
        this.lehrer = 0L;
        this.maximaleAnzahlStudenten = 0;
        this.listeStudenten = new ArrayList<>();
        this.ects = ECTS;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLehrer() {
        return lehrer;
    }

    public void setLehrer(long lehrer) {
        this.lehrer = lehrer;
    }

    public int getMaximaleAnzahlStudenten() {
        return maximaleAnzahlStudenten;
    }

    public void setMaximaleAnzahlStudenten(int maximaleAnzahlStudenten) {
        this.maximaleAnzahlStudenten = maximaleAnzahlStudenten;
    }

    public List<Long> getListeStudenten() {
        return listeStudenten;
    }

    public void setListeStudenten(List<Long> listeStudenten) {
        this.listeStudenten = listeStudenten;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }


    @Override
    public String toString() {
        return "Kurs{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", lehrer=" + lehrer +
                ", maximaleAnzahlStudenten=" + maximaleAnzahlStudenten +
                ", listeStudenten=" + listeStudenten +
                ", ects=" + ects +
                '}';
    }

    /**
     * untersucht, ob ein Kurs freie Platzen hat
     * @return true, wenn es noch Platzen gibt/ false, wenn es voll ist
     */
    public boolean istFrei()
    {
        return this.getListeStudenten().size() < this.getMaximaleAnzahlStudenten();
    }


    /**
     * hinzufügt ein bestimmter Student in der Liste der angemeldeten Studenten
     */
    public void addStudent(Student student)
    {
        this.listeStudenten.add(student.getStudentID());
    }

    /**
     * berechnet der Anzahl dem freien Platzen fur dem Kurs
     * @return Anzahl von freien Platzen
     */
    public int getAnzahlFreienPlatze()
    {
        return (this.getMaximaleAnzahlStudenten() - this.getListeStudenten().size());
    }


    /**
     * vergleicht 2 Kurse nach der Anzahl das freie Platzen
     * @param o (Kurs2)
     * @return 1, wenn Kurs1 mehreren Platzen hat, -1, wenn Kurs1 weniger Platzen hat, 0, wenn Kurs1 = Kurs2 beim Anzahl freien Platzen
     */
    @Override
    public int compareTo(Kurs o) {
        if(this.getAnzahlFreienPlatze() < o.getAnzahlFreienPlatze())
            return -1;

        if(this.getAnzahlFreienPlatze() < o.getAnzahlFreienPlatze())
            return 0;

        return 1;
    }
}
