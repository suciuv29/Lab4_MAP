package Repository;

import Modele.Kurs;
import Modele.Student;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentRepository extends InMemoryRepository<Student> implements FileRepository<Student>
{
    String file;
    public StudentRepository(String file) {
        super();
        this.file = file;
    }

    @Override
    public String toString() {
        return "StudentRepository{" +
                "repoList=" + repoList +
                '}';
    }

    @Override
    public List<Student> readFromFile() throws IOException {
        Reader reader = new BufferedReader(new FileReader(file));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for (JsonNode pm : parser) {

            long ID = pm.path("studentID").asLong();

            String vorName = pm.path("vorname").asText();

            String nachName = pm.path("nachname").asText();

            int totalKredits = pm.path("totalKredits").asInt();

            List<Long> angeschriebeneKurse = new ArrayList<>();
            for (JsonNode v : pm.path("angeschriebeneKurse"))
            {
                angeschriebeneKurse.add(v.asLong());
            }


            Student student = new Student(vorName,nachName,ID,totalKredits,angeschriebeneKurse);
            repoList.add(student);
        }

        return repoList;
    }

    @Override
    public void writeToFile() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());

        writer.writeValue(new File(file), repoList);

    }

    @Override
    public Student findOne(Long id) throws IOException {
        Reader reader = new BufferedReader(new FileReader(this.file));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for (JsonNode pm : parser)
        {

            long ID = pm.path("studentID").asLong();
            if(Objects.equals(id, ID))
            {

                String vorName = pm.path("vorname").asText();

                String nachName = pm.path("nachname").asText();

                int totalKredits = pm.path("totalKredits").asInt();

                List<Long> angeschriebeneKurse = new ArrayList<>();
                for (JsonNode v : pm.path("angeschriebeneKurse"))
                {
                    angeschriebeneKurse.add(v.asLong());
                }

                return new Student(vorName,nachName,ID,totalKredits,angeschriebeneKurse);

            }
        }

        return null;
    }

    @Override
    public Student create(Student obj) throws IOException {
        for(Student student : repoList)
        {
            if(student.getStudentID() == obj.getStudentID())
                throw new IllegalArgumentException("Das Object ist in der Liste.");
        }

        this.repoList.add(obj);
        writeToFile();
        return obj;
    }

    @Override
    public Student update(Student obj) throws IOException {
        if(repoList.isEmpty())
            throw  new IndexOutOfBoundsException("Die Liste ist leer");

        Student studentToUpdate = this.repoList.stream()
                .filter(student -> student.getStudentID() == obj.getStudentID())
                .findFirst()
                .orElseThrow();

        studentToUpdate.setNachname(obj.getNachname());
        studentToUpdate.setVorname(obj.getVorname());
        studentToUpdate.setTotalKredits(obj.getTotalKredits());
        studentToUpdate.setAngeschriebeneKurse(obj.getAngeschriebeneKurse());

        writeToFile();
        return studentToUpdate;
    }

    @Override
    public boolean delete(Long objID) throws IOException {
        if(repoList.isEmpty())
            throw new IndexOutOfBoundsException("Die Liste ist leer");

        for(Student student : repoList)
        {
            if(student.getStudentID() == objID)
            {
                repoList.remove(student);
                writeToFile();
                return true;
            }

        }
        return false;
    }

    /**
     * Sortiert in steigender Reihenfolge die Liste von Studenten nach Anzahl der Kurse
     */
    public void sortList()
    {
        repoList.sort(Student::compareTo);
    }

    /**
     * filtert die Liste, nimmt nur die Studenten die 30 ECTS haben
     * @return die gefilterte Liste
     */
    public List<Student> filterList()
    {
        return repoList.stream()
                .filter(student->student.getTotalKredits() == 30).toList();
    }

    /**
     * sieht, welche Studenten zu einem Kurs angemeldet sind
     * @param kursID, fur welcher wir sehen, welche Studenten angemeldet sind
     * @return eine Liste von Studenten, die fur einen bestimmten Kurs angemeldet sind
     */
    public List<Student> studentenAngemeldetBestimmtenKurs(Long kursID)
    {
        List<Student> studentenAngemeldet = new ArrayList<>();
        for(Student student : repoList)
        {
            List<Long> kurseStudent = student.getAngeschriebeneKurse();
            for(Long kurs : kurseStudent)
            {
                if(Objects.equals(kurs, kursID))
                {
                    studentenAngemeldet.add(student);
                    break;
                }
            }
        }
        return studentenAngemeldet;
    }

    /**
     * löscht einen Kurs von allen Studenten, die ihn belegt
     * @param kurs, den man entfernt
     * @throws IOException, fur Schreiben im File
     */
    public void loschenKurs(Kurs kurs) throws IOException {
        for(Student student : repoList)
        {
            if(student.getAngeschriebeneKurse().contains(kurs.getID()))
            {
                student.loschenKurs(kurs);
                writeToFile();
            }
        }
    }


    /**
     * ändert die Anzahl der ECTS von allen Studenten, die bei einem Kurs teilnehmen, wessen ECTS geändert wurden
     * @param ECTS, neue ECTS des Kurses
     * @param idKurs, des Kurses
     * @param oldECTS, alte ECTS des Kurses
     * @throws IOException, fur Schreiben im File beim update Methode
     */
    public void andernECTS(int ECTS, Long idKurs, int oldECTS) throws IOException {
        for (Student student : repoList) {
            if (student.getAngeschriebeneKurse().contains(idKurs)) {
                int neueAnzahlKredits = student.getTotalKredits() + (ECTS - oldECTS);
                student.setTotalKredits(neueAnzahlKredits);
                this.update(student);
            }
        }
    }

    /**
     * berechnet ob ein Student zu einem Kurs teilnehmen kann
     * @param student, der Student
     * @param kurs, der Kurs
     * @return true ,wenn er teilnehmen kann, anderenfalls false
     */
    public boolean validationAddKurs(Student student, Kurs kurs)
    {
        if(!repoList.contains(student))
            return false;

        return student.getNotwendigeKredits() >= kurs.getEcts();

    }

    /**
     * ein Student nimmt bei einem Kurs teil → der Kurs wird in seiner Liste von Kurs hingelegt
     * @param student, der Student
     * @param kurs, der Kurs
     * @throws IOException, fur Schreiben im File
     */
    public void addKurs(Student student, Kurs kurs) throws IOException {
        if(validationAddKurs(student,kurs))
        {
            student.enrolled(kurs);
            this.update(student);
            writeToFile();
        }
    }

    /**
     * untersucht, ob ein Student existiert
     * @param id, des Student
     * @return true, wenn der Student in der RepoListe ist, anderenfalls false
     */
    public boolean containsID(Long id)
    {
        for(Student student : repoList)
        {
            if(student.getStudentID() == id)
                return true;
        }
        return false;
    }

    public boolean containsKurs(long idKurs, Student student)
    {
        return student.getAngeschriebeneKurse().contains(idKurs);

    }
}
