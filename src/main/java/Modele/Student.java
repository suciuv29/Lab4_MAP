package Modele;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person implements Comparable<Student>{

    private long studentID;
    private int totalKredits;
    private List<Long> angeschriebeneKurse;

    public Student(String vorname, String nachname, long studentID, int totalKredits, List<Long> angeschriebeneKurse) {
        super(vorname, nachname);
        this.studentID = studentID;
        this.totalKredits = totalKredits;
        this.angeschriebeneKurse = angeschriebeneKurse;
    }

    public Student(String vorname, String nachname, long studentID) {
        super(vorname, nachname);
        this.studentID = studentID;
        this.totalKredits = 0;
        this.angeschriebeneKurse = new ArrayList<>();
    }

    public long getStudentID() {
        return studentID;
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public int getTotalKredits() {
        return totalKredits;
    }

    public void setTotalKredits(int totalKredits) {
        this.totalKredits = totalKredits;
    }

    public List<Long> getAngeschriebeneKurse() {
        return angeschriebeneKurse;
    }

    public void setAngeschriebeneKurse(List<Long> angeschriebeneKurse) {
        this.angeschriebeneKurse = angeschriebeneKurse;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentID=" + studentID +
                ", totalKredits=" + totalKredits +
                ", angeschriebeneKurse=" + angeschriebeneKurse +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentID == student.studentID && totalKredits == student.totalKredits && angeschriebeneKurse.equals(student.angeschriebeneKurse);
    }

    /**
     * löscht einen Kurs aus der Liste eines Students und subtrahiere die Anzahl des Kurses ECTS von dem Anzahl derKredite des Students
     * @param kurs, den man löscht
     */
    public void loschenKurs(Kurs kurs)
    {
        this.angeschriebeneKurse.remove(kurs.getID());
        this.totalKredits -= kurs.getEcts();
    }

    /**
     * berechnet wv. Kredite ein Student noch braucht bis er insgesamt 30 hat
     * @return Anzahl notwendigen Krediten
     */
    public int getNotwendigeKredits()
    {
        return (30 - this.getTotalKredits());
    }

    /**
     * Wenn ein Student sich fur einen Kurs anmeldet, dann fugt man den Kurs in seiner Liste und inkrementiere seine ECTS mit der Anzahl des Kurses ECTS
     * @param kurs, bei dem sich der Student anmelden will
     */
    public void enrolled(Kurs kurs)
    {
        this.angeschriebeneKurse.add(kurs.getID());
        this.totalKredits += kurs.getEcts();
    }

    /**
     * gibt beim wv. Kursen der Student angemeldet ist
     * @return Anzahl der Kurse des Studenten
     */
    public int getAnzahlKurse()
    {
        return this.angeschriebeneKurse.size();
    }

    /**
     * vergleicht der Anzahl der Kurse von 2 Studenten
     * @param student2, der andere Student
     * @return welcher eine Kleinere Anzahl von Kurse hat
     */
    @Override
    public int compareTo(Student student2) {
        return Integer.compare(this.getAnzahlKurse(), student2.getAnzahlKurse());

    }


}
