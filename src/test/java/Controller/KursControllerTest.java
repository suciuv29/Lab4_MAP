package Controller;
import Exception.DasElementExistiertException;
import Modele.Kurs;
import Modele.Lehrer;
import Modele.Student;
import Repository.KursRepository;
import Repository.LehrerRepository;
import Repository.StudentRepository;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import Exception.ListIsEmptyException;

import static org.junit.jupiter.api.Assertions.*;

class KursControllerTest {

    KursRepository kursRepository ;
    LehrerRepository lehrerRepository ;
    StudentRepository studentRepository ;
    KursController kursController;
    Lehrer pop;
    Lehrer dancu;
    Kurs dataBase;

    void input() throws IOException {
        pop = new Lehrer("Marcel", "Pop",1);
        dancu = new Lehrer("Ingrid","Dancu",2);


        kursRepository = new KursRepository("C:\\Users\\Vlad\\IdeaProjects\\Hausaufgabe4-master\\src\\test\\java\\Controller\\originalKurs.json");
        kursRepository.readFromFile();
        lehrerRepository = new LehrerRepository("C:\\Users\\Vlad\\IdeaProjects\\Hausaufgabe4-master\\src\\test\\java\\Controller\\originalLehrer.json");
        lehrerRepository.readFromFile();
        studentRepository = new StudentRepository("C:\\Users\\Vlad\\IdeaProjects\\Hausaufgabe4-master\\src\\test\\java\\Controller\\originalStudent.json");
        studentRepository.readFromFile();
        kursController = new KursController(kursRepository,studentRepository,lehrerRepository);
    }

    @Test
    @Description("wenn das Kurs in der Liste ist, dann wird nichts passieren")
    void createElementInFile() throws IOException{
        input();
        dataBase = new Kurs(1,"Baze de date", 1, 30, 26);
        assertThrows(DasElementExistiertException.class, ()->kursController.create(dataBase));
    }

    @Test
    @Description("wenn das Kurs nicht in der Liste ist, dann wird wird man das Kurs in der RepoListe getan")
    void create() throws IOException, DasElementExistiertException {
        input();
        dataBase = new Kurs(100,"Baze de date", 1, 30, 26);
        assertEquals(this.kursController.create(dataBase),dataBase);

    }

    @Test
    @Description("gibt mir alle Elementen aus der RepoList")
    void getAll() throws IOException {
        input();
        List<Kurs> list = this.kursController.getAll();
        assertEquals(list.size(), this.kursController.getAll().size());

    }

    @Test
    @Description("es ändert sich nichts, wenn das gegebene Element sich nicht in der Liste befindet")
    void updateElementNichtInDerListe() throws IOException {
        input();
        dataBase = new Kurs(100,"Baze de date", 1, 30, 26);
        assertThrows(NoSuchElementException.class, ()->kursController.update(dataBase));

    }

    @Test
    @Description("es ändert, wenn das gegebene Element sich in der Liste befindet")
    void update() throws IOException, ListIsEmptyException {
        input();
        dataBase = new Kurs(1,"DB", 1, 300, 26);
        kursController.update(dataBase);
        Kurs kurs = kursController.findOne(1L);
        System.out.println(kurs);
        boolean wahr;

        if(kurs.getID() == dataBase.getID() &&
                Objects.equals(kurs.getName(), dataBase.getName()) &&
        kurs.getLehrer() == dataBase.getLehrer() &&
        kurs.getMaximaleAnzahlStudenten() == dataBase.getMaximaleAnzahlStudenten() &&
        kurs.getEcts() == dataBase.getEcts())
            {
                wahr = true;
            }
        else
            {
                wahr = false;
            }

        assertTrue(wahr);
    }

    @Test
    @Description("Löscht ein Element aus der RepoListe")
    void delete() throws IOException {
        input();
        assertTrue(this.kursController.delete(100L));
    }

    @Test
    @Description("Findet einen Element in dem File nach Id")
    void findOne() throws IOException {
        input();
        boolean wahr;
        Kurs kurs = this.kursController.findOne(1L);
        Kurs kurs2 = new Kurs(1,"DB",1,300,new ArrayList<>(),26);
        wahr = kurs.getID() == kurs2.getID() &&
                Objects.equals(kurs.getName(), kurs2.getName()) &&
                kurs.getLehrer() == kurs2.getLehrer() &&
                kurs.getMaximaleAnzahlStudenten() == kurs2.getMaximaleAnzahlStudenten() &&
                kurs.getEcts() == kurs2.getEcts();

        assertTrue(wahr);
    }

    @Test
    @Description("Findet nicht das Element in der Liste->null")
    void findOneNot() throws IOException {
        input();
        assertNull(this.kursController.findOne(100L));
    }

    @Test
    @Description("filtert die Liste von Kurse, welche  ECTS > 5 haben")
    void filter() throws IOException {
        input();
        List<Kurs> filterList = this.kursController.filter();
        assertEquals(1, filterList.size());

    }

    @Test
    @Description("Sortiert die Liste nach die Anzahl von freien Platzen")
    void sort() throws IOException {
        input();
        this.kursController.sort();
        int ct = 0;
        int wahr = 0;
        List<Kurs> kopie2;
        kopie2 = this.kursController.getAll();
        while(ct != 3)
        {
            System.out.println(kopie2.get(ct).getID());
            if(ct==0 && kopie2.get(ct).getID() == 2)
                wahr++;

            if(ct==1 && kopie2.get(ct).getID() == 3)
                wahr++;

            if(ct==2 && kopie2.get(ct).getID() == 1)
                wahr++;

            ct++;
        }
        assertEquals(3,wahr);
    }

    @Test
    @Description("Gibt die Liste der Kurse mit freien Platzen")
    void getKurseFreiePlatzen() throws IOException {
        input();
        List<Kurs> freiePlatzen = this.kursController.getKurseFreiePlatzen();
        assertEquals(2, freiePlatzen.size());
    }

    @Test
    @Description("ändert die ECTS eines Kurses")
    void andernECTS() throws IOException, ListIsEmptyException {
        input();
        assertTrue(this.kursController.andernECTS(100,1L));
        this.kursController.andernECTS(26,1L);
    }

    @Test
    @Description("ändert die ECTS eines Kurses, der nicht existiert, nicht")
    void andernECTSNicht() throws IOException, ListIsEmptyException {
        input();
        assertFalse(this.kursController.andernECTS(100,100L));
    }

    @Test
    @Description("ein Student wir zu einem Kurs nicht hingelegt")
    void registerNicht() throws IOException, ListIsEmptyException {
        input();
        Student student = new Student("Anca","Maria",100L);
        Kurs kurs = this.kursController.findOne(1L);
        assertFalse(this.kursController.register(student,kurs));
    }

    @Test
    @Description("ein Student wir zu einem Kurs hingelegt")
    void register() throws IOException, ListIsEmptyException {
        input();
        Student student = this.studentRepository.findOne(423L);
        Kurs kurs = this.kursController.findOne(1L);
        assertTrue(this.kursController.register(student,kurs));
        this.kursController.getStudentenRepo().loschenKurs(kurs);
        this.kursController.update(kurs);

    }

    @Test
    @Description("Schaut ob wir in der Liste einem Kurs mit einem ID haben")
    void containsID() throws IOException {
        input();
        assertTrue(this.kursController.containsID(1L));
    }

    @Test
    @Description("Schaut ob wir in der Liste einem Kurs mit einem ID haben")
    void containsIDNicht() throws IOException {
        input();
        assertFalse(this.kursController.containsID(100L));
    }
}