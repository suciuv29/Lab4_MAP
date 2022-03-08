package Controller;

import Modele.Kurs;
import Modele.Student;
import Repository.KursRepository;
import Repository.LehrerRepository;
import Repository.StudentRepository;
import Exception.DasElementExistiertException;
import Exception.ListIsEmptyException;
import java.io.IOException;
import java.util.List;


public class KursController implements Controller<Kurs> {

    private KursRepository kursRepo;
    private StudentRepository studentenRepo;
    private LehrerRepository lehrerRepo;

    public KursController(KursRepository kursRepo, StudentRepository studentenRepo, LehrerRepository lehrerRepo) {
        this.kursRepo = kursRepo;
        this.studentenRepo = studentenRepo;
        this.lehrerRepo = lehrerRepo;
    }

    public KursRepository getKursRepo() {
        return kursRepo;
    }

    public void setKursRepo(KursRepository kursRepo) {
        this.kursRepo = kursRepo;
    }

    public StudentRepository getStudentenRepo() {
        return studentenRepo;
    }

    public void setStudentenRepo(StudentRepository studentenRepo) {
        this.studentenRepo = studentenRepo;
    }

    public LehrerRepository getLehrerRepo() {
        return lehrerRepo;
    }

    public void setLehrerRepo(LehrerRepository lehrerRepo) {
        this.lehrerRepo = lehrerRepo;
    }

    /**
     * legt ein Kurs in der RepoListe, indem man die Methode aus dem Repo aufruft
     * @param obj, das Objekt, das man hinlegt
     * @return das hingelegte Element
     * @throws IOException, fur Schreiben im File
     * @throws DasElementExistiertException, das Element ist in der Liste
     */
    @Override
    public Kurs create(Kurs obj) throws IOException, DasElementExistiertException {

        lehrerRepo.addKurs(obj.getLehrer(), obj);
        return kursRepo.create(obj);
    }

    /**
     * gibt alle Elementen aus der RepoListe, indem man die Methode aus dem Repo aufruft
     * @return eine Liste mit Kursen
     */
    @Override
    public List<Kurs> getAll() {
        return kursRepo.getAll();
    }

    /**
     * Verändert einige Attribute eines Objektes, indem man die Methode aus dem Repo aufruft
     * @param obj, das Objekt mit dem switch erledigt
     * @return das alte Objet mit den neuen Attributen
     * @throws IOException, fur Schreiben im File
     * @throws ListIsEmptyException, die Liste ist leer
     */
    @Override
    public Kurs update(Kurs obj) throws IOException, ListIsEmptyException {
        return kursRepo.update(obj);
    }

    /**
     * aus der RepoListe ein Objekt löschen, indem man die Methode aus dem Repo aufruft
     * @param objID, das Element das man löschen will
     * @return true, wenn man das Element löscht, false wenn man nichts tut
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public boolean delete(Long objID) throws IOException {

        return kursRepo.delete(objID);
    }

    /**
     * liest alle Attribute eines Objekts von einem File, indem man die Methode aus dem Repo aufruft
     * @return die Liste von Kurse
     * @throws IOException, fur Lesen vom File
     */
    @Override
    public List<Kurs> readFromFile() throws IOException {
        return kursRepo.readFromFile();
    }

    /**
     * schreit neue Objekte in dem File, indem man die Methode aus dem Repo aufruft
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public void writeToFile() throws IOException {
        kursRepo.writeToFile();
    }

    /**
     * sucht in dem File, ob ein Objekt existiert mithilfe der IDs, indem man die Methode aus dem Repo aufruft
     * @param id, Id des Objektes das man sucht, anders null
     * @return das Objekt,wenn man es findet
     * @throws IOException, fur lesen aus dem File
     */
    @Override
    public Kurs findOne(Long id) throws IOException {
        return kursRepo.findOne(id);
    }

    /**
     * Filtert die Liste, indem man die Methode aus dem Repo aufruft
     * @return die gefilterte Liste
     */
    public List<Kurs> filter()
    {
        return kursRepo.filterList();
    }


    /**
     * sortiert die Liste, indem man die Methode aus dem Repo aufruft
     */
    public void sort()
    {
        kursRepo.sortList();
    }


    /**
     * gibt die Kurse mit freien Platzen, indem man die Methode aus dem Repo aufruft
     * @return Liste den Kursen
     */
    public List<Kurs> getKurseFreiePlatzen()
    {
        return kursRepo.getKurseFreiePlatzen();
    }


    /**
     * ein HashMap mit den Kursen mit freien Platzen und der Anzahl der Plätze, indem man die Methode aus dem Repo aufruft
     */
    public void kurseFreiePlatzenUndAnzahl()
    {
        kursRepo.kurseFreiePlatzenUndAnzahl();
    }


    /**
     * ander die ECTS von einem Kurs, indem man die Methode aus dem Repo aufruft
     * @param ECTS, neue ECTS
     * @param idKurs des Kurses
     * @return true, wenn das Kurs existiert und die ECTS geändert wurden, andernfalls false
     * @throws IOException, fur Schreiben im File
     * @throws ListIsEmptyException, die Liste ist leer
     */
    public boolean andernECTS(int ECTS, Long idKurs) throws IOException, ListIsEmptyException {
        int alteECTS = kursRepo.andernECTS(ECTS, idKurs);
        if(alteECTS != -1)
        {
            studentenRepo.andernECTS(ECTS, idKurs, alteECTS);
            return true;
        }
        return false;
    }

    /**
     * ein Student will bei einem Kurs teilnehmen
     * @param student, der sich zu dem Kurs anschreibt
     * @param kurs, der Kurs, wo der Student sich anschreiben will
     * @return true, wenn der Student zu dem Kurs enrolled ist, false anderenfalls
     * @throws IOException, fur Schreiben im File
     * @throws ListIsEmptyException, die Liste ist leer
     */
    public boolean register(Student student, Kurs kurs) throws IOException, ListIsEmptyException {
        if(kursRepo.validationFreiePlatzen(kurs.getID()) && studentenRepo.validationAddKurs(student, kurs))
        {
            if(!studentenRepo.containsKurs(kurs.getID(), student))
            {
                kursRepo.addStudent(student, kurs.getID());
                studentenRepo.addKurs(student, kurs);
                return true;
            }

        }
        return false;
    }

    /**
     * untersucht, ob das gegebene Id eines Kurses in dem repoListe ist, indem man die Methode aus dem Repo aufruft
     * @param id des Kurses
     * @return true, wenn der Kurs in der RepoListe ist, anderenfalls false
     */
    public boolean containsID(long id)
    {
        return kursRepo.containsID(id);
    }


}
