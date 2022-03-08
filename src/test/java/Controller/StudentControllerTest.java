package Controller;

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

import static org.junit.jupiter.api.Assertions.*;

class StudentControllerTest {

    StudentRepository studentRepository ;
    StudentController studentController;

    void input() throws IOException {

        studentRepository = new StudentRepository("C:\\Users\\Vlad\\IdeaProjects\\Hausaufgabe4-master\\src\\test\\java\\Controller\\originalStudent.json");
        studentRepository.readFromFile();
        studentController = new StudentController(studentRepository);
    }

    @Test
    @Description("Ein neuer Student wird in der Liste hingelegt")
    void create() throws IOException {
        input();
        Student student = new Student("Ana","Monica",100L);
        assertEquals(studentController.create(new Student("Ana","Monica",100L)),student);

    }

    @Test
    @Description("Ein Student, der in der Liste ist, wird nicht in der Liste hingelegt")
    void createExistingStudent() throws IOException {
        input();
        assertThrows(IllegalArgumentException.class, ()->studentController.create(new Student("","",423L)));
    }

    @Test
    @Description("gibt die Liste von Studenten")
    void getAll() throws IOException {
        input();
        List<Student> studenten = this.studentController.getAll();
        assertEquals(2, studenten.size());
    }

    @Test
    @Description("es ändert, wenn das gegebene Element sich in der Liste befindet")
    void update() throws IOException {
        input();
        Student student = new Student("Daria","Radu",423);
        assertEquals(student,this.studentController.update(student));

        Student student2 = new Student("Dariana","Beckham",423);
        assertEquals(student,this.studentController.update(student2));
    }

    @Test
    @Description("Löscht einen Student aus der Liste")
    void delete() throws IOException {
        input();
        assertTrue(studentController.delete(100L));

    }

    @Test
    @Description("kann einen Student der nicht in der Liste ist nicht löschen")
    void deleteNicht() throws IOException {
        input();
        assertFalse(studentController.delete(500L));

    }

    @Test
    @Description("Sucht einen Student nach Id in dem File")
    void findOne() throws IOException {
        input();
        Student student = new Student("Dariana","Beckham",423L);
        assertEquals(student,this.studentController.findOne(423L));
    }

    @Test
    @Description("Sucht einen Student nach Id nicht in dem File")
    void findOneNicht() throws IOException {
        input();
        assertNull(this.studentController.findOne(1000L));
    }

    @Test
    @Description("Filtert die Liste von Studenten, werden nur die Studenten mit 30 ECTS angenommen sein.")
    void filter() throws IOException {
        input();
        List<Student> filterList = this.studentController.filter();
        int ct = 0;
        int wahr = 0;
        if(filterList.get(ct).getStudentID() == 1)
                wahr++;

        assertEquals(1,wahr);

    }

    @Test
    @Description("Sortiert die Liste von Studenten im steigender Reihenfolge nach Anzahl von Kursen")
    void sort() throws IOException {
        input();
        this.studentController.sort();
        int ct = 0;
        int wahr = 0;
        List<Student> list;
        list = this.studentController.getAll();
        while(ct != 2)
        {
            if(ct==0 && list.get(ct).getStudentID() == 423)
                wahr++;

            if(ct==1 && list.get(ct).getStudentID() == 1)
                wahr++;

            ct++;
        }
        assertEquals(2,wahr);
    }

    @Test
    @Description("Gibt eine Liste von Studenten die bei einem Kurs angemeldet sind")
    void getStudentenAngemeldetBestimmtenKurs() throws IOException {
        input();
        assertEquals(1,this.studentController.getStudentenRepo().studentenAngemeldetBestimmtenKurs(800L).size());
    }

    @Test
    @Description("Sucht in der RepoListe einen Student nach einen gegebenen ID")
    void containsID() throws IOException {
        input();
        assertTrue(this.studentController.containsID(423L));
    }

    @Test
    @Description("Sucht in der RepoListe einen Student nach einen gegebenen ID und findet ihn nicht, weil er nicht existiert")
    void containsIDNicht() throws IOException {
        input();
        assertFalse(this.studentController.containsID(2L));
    }

    @Test
    @Description("Überprüft, ob ein Student einen Kurs untersucht")
    void containsKurs() throws IOException {
        input();
        List<Long> kurse = new ArrayList<>();
        kurse.add(1L);
        assertTrue(this.studentController.containsKurs(new Student("Anabella","Pop",1L,0,kurse),1L));
    }

    @Test
    @Description("Überprüft, ob ein Student einen Kurs untersucht")
    void containsKursNicht() throws IOException {
        input();
        List<Long> kurse = new ArrayList<>();
        kurse.add(1L);
        assertFalse(this.studentController.containsKurs(new Student("Anabella","Pop",1L,0,kurse),100L));
    }
}