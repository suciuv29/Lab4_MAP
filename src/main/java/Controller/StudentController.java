package Controller;

import Modele.Student;
import Repository.StudentRepository;

import java.io.IOException;
import java.util.List;

public class StudentController implements Controller<Student>{

    private StudentRepository studentenRepo;

    public StudentController(StudentRepository studentenRepo) {
        this.studentenRepo = studentenRepo;
    }

    public StudentRepository getStudentenRepo() {
        return studentenRepo;
    }

    public void setStudentenRepo(StudentRepository studentenRepo) {
        this.studentenRepo = studentenRepo;
    }

    /**
     * legt einen Student in der RepoListe, indem man die Methode aus dem Repo aufruft
     * @param obj, das Objekt, das man hinlegt
     * @return das hingelegte Element
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public Student create(Student obj) throws IOException {
        return studentenRepo.create(obj);
    }

    /**
     * gibt alle Elementen aus der RepoListe, indem man die Methode aus dem Repo aufruft
     * @return eine Liste mit Studenten
     */
    @Override
    public List<Student> getAll() {
        return studentenRepo.getAll();
    }


    /**
     * Verändert einige Attribute eines Objektes, indem man die Methode aus dem Repo aufruft
     * @param obj, das Objekt mit dem switch erledigt
     * @return das alte Objet mit den neuen Attributen
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public Student update(Student obj) throws IOException {
        return studentenRepo.update(obj);
    }

    /**
     * aus der RepoListe ein Objekt löschen, indem man die Methode aus dem Repo aufruft
     * @param objID, das Element das man löschen will
     * @return true, wenn man das Element löscht, false wenn man nichts tut
     */
    @Override
    public boolean delete(Long objID) throws IOException {
        return studentenRepo.delete(objID);
    }


    /**
     * liest alle Attribute eines Objekts von einem File, indem man die Methode aus dem Repo aufruft
     * @return die Liste von Studenten
     * @throws IOException, fur Lesen aus dem File
     */
    @Override
    public List<Student> readFromFile() throws IOException {
        return studentenRepo.readFromFile();
    }

    /**
     * schreit neue Objekte in dem File, indem man die Methode aus dem Repo aufruft
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public void writeToFile() throws IOException {
            studentenRepo.writeToFile();
    }

    /**
     * sucht in dem File, ob ein Objekt existiert mithilfe der IDs, indem man die Methode aus dem Repo aufruft
     * @param id, Id des Objektes das man sucht, anders null
     * @return das Objekt,wenn man es findet
     * @throws IOException, fur Lesen aus dem File
     */
    @Override
    public Student findOne(Long id) throws IOException {
        return studentenRepo.findOne(id);
    }


    /**
     * Filtert die Liste, indem man die Methode aus dem Repo aufruft
     * @return die gefilterte Liste von Studenten
     */
    public List<Student> filter()
    {
        return studentenRepo.filterList();
    }


    /**
     * sortiert die Liste, indem man die Methode aus dem Repo aufruft
     */
    public void sort()
    {
        studentenRepo.sortList();
    }


    /**
     * gibt alle Studenten die bei einem Kurs angemeldet sind
     * @param kursId, des Kurses
     */
    public void getStudentenAngemeldetBestimmtenKurs(Long kursId)
    {
        System.out.println(studentenRepo.studentenAngemeldetBestimmtenKurs(kursId));
    }

    /**
     * untersucht, ob das gegebene Id eines Studenten in dem repoListe ist, indem man die Methode aus dem Repo aufruft
     * @param id des Studenten
     * @return true, wenn der Student in der RepoListe ist, anderenfalls false
     */
    public boolean containsID(long id)
    {
        return studentenRepo.containsID(id);
    }

    /**
     * überprüft, ob ein Student einen Kurs hat
     * @param student, Student
     * @param idKurs, Kurs
     * @return true, wenn wahr, wenn falsch false
     */
    public boolean containsKurs(Student student, long idKurs)
    {
        return studentenRepo.containsKurs(idKurs, student);
    }
}
