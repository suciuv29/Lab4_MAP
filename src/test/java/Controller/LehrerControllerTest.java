package Controller;

import Modele.Kurs;
import Modele.Lehrer;
import Repository.KursRepository;
import Repository.LehrerRepository;
import Repository.StudentRepository;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class LehrerControllerTest {

    KursRepository kursRepository ;
    LehrerRepository lehrerRepository ;
    StudentRepository studentRepository ;
    LehrerController lehrerController;
    Lehrer pop;
    Lehrer dancu;

    void input() throws IOException {
        pop = new Lehrer("Marcel", "Pop",100);
        dancu = new Lehrer("Ingrid","Dancu",200);

        kursRepository = new KursRepository("C:\\Users\\Vlad\\IdeaProjects\\Hausaufgabe4-master\\src\\test\\java\\Controller\\originalKurs.json");
        kursRepository.readFromFile();
        lehrerRepository = new LehrerRepository("C:\\Users\\Vlad\\IdeaProjects\\Hausaufgabe4-master\\src\\test\\java\\Controller\\originalLehrer.json");
        lehrerRepository.readFromFile();
        studentRepository = new StudentRepository("C:\\Users\\Vlad\\IdeaProjects\\Hausaufgabe4-master\\src\\test\\java\\Controller\\originalStudent.json");
        studentRepository.readFromFile();
        lehrerController = new LehrerController(lehrerRepository,studentRepository,kursRepository);
    }

    @Test
    @Description("wenn das Kurs in der Liste ist, dann wird nichts passieren")
    void createElementInFile() throws IOException{
        input();
        Lehrer lehrer = new Lehrer("Diana","Cristea",1);
        assertThrows(IllegalArgumentException.class, ()->this.lehrerController.create(lehrer));

    }

    @Test
    @Description("wenn das Kurs nicht in der Liste ist, dann wird wird man den Lehrer in der RepoListe getan")
    void create() throws IOException {
        input();
        this.lehrerController.create(pop);
        assertEquals(4,this.lehrerController.getAll().size());
    }

    @Test
    @Description("gibt mir alle Elementen aus der RepoList")
    void getAll() throws IOException {
        input();
        assertEquals(3,this.lehrerController.getAll().size());
    }

    @Test
    @Description("ändert die Attribute eines Lehrers")
    void update() throws IOException {
        input();
        List<Long> kurse = new ArrayList<>();
        kurse.add(1L);
        kurse.add(2L);
        kurse.add(3L);
        kurse.add(4L);
        boolean wahr;
        Lehrer lehrer = new Lehrer("Mihaela","Inge",kurse,3);
        lehrerController.update(lehrer);
        Lehrer lehrer2 = lehrerController.findOne(3L);
        wahr = lehrer.getLehrerID() == lehrer2.getLehrerID() && lehrer.getKurse().equals(lehrer2.getKurse()) &&
                Objects.equals(lehrer.getVorname(), lehrer2.getVorname()) &&
                Objects.equals(lehrer.getNachname(), lehrer2.getNachname());
        assertTrue(wahr);
        Lehrer lehrer3 = new Lehrer("Sommer","Inge",kurse,3);
        lehrerController.update(lehrer3);
    }

    @Test
    @Description("Löscht ein Element aus der RepoListe")
    void delete() throws IOException {
        input();
        this.lehrerController.delete(100L);
        assertEquals(3,this.lehrerController.getAll().size());
    }

    @Test
    @Description("Findet einen Element in dem File nach Id")
    void findOne() throws IOException {
        input();
        Lehrer lehrer = this.lehrerController.findOne(2L);
        List<Long> kurse = new ArrayList<>();
        kurse.add(3L);
        boolean wahr;
        this.lehrerController.getAll();
        Lehrer lehrer2 = new Lehrer("Catalin","Rusu",kurse,2L);
        wahr = lehrer.getLehrerID() == lehrer2.getLehrerID() && lehrer.getKurse().equals(lehrer2.getKurse()) &&
                Objects.equals(lehrer.getVorname(), lehrer2.getVorname()) &&
                Objects.equals(lehrer.getNachname(), lehrer2.getNachname());
        assertTrue(wahr);
    }

    @Test
    @Description("Findet nicht das Element in der Liste->null")
    void findOneNot() throws IOException {
        input();
        assertNull(this.lehrerController.findOne(100L));
    }

    @Test
    @Description("ein Lehrer löscht einen Kurs")
    void loschenKurs() throws IOException {
        input();
        Kurs kurs = new Kurs(5000,10);
        List<Long> kurse = new ArrayList<>();
        kurse.add(5000L);
        Lehrer lehrer = new Lehrer("Ana","Lob",kurse,3000);
        this.lehrerController.create(lehrer);
        this.lehrerController.loschenKurs(lehrer,kurs);
        assertEquals(0, lehrer.getKurse().size());
        this.lehrerController.getLehrerRepo().delete(3000L);

    }

    @Test
    @Description("Überprüft ob ein Id in der Liste existiert")
    void containsID() throws IOException {
        input();
        assertTrue(this.lehrerController.containsID(1L));

    }

    @Test
    @Description("Legt einem Kurs zum einem Lehrer hin")
    void addKurs() throws IOException {
        input();
        List<Long> kurse = new ArrayList<>();
        kurse.add(5000L);
        Lehrer lehrer = new Lehrer("Ana","Lob",kurse,3000);
        Kurs kurs2 = new Kurs(3000,100);
        this.lehrerController.create(lehrer);
        this.lehrerController.addKurs(lehrer.getLehrerID(),kurs2);
        assertEquals(2, lehrer.getKurse().size());
        this.lehrerController.getLehrerRepo().delete(3000L);
    }
}