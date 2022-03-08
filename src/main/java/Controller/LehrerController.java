package Controller;

import Modele.Kurs;
import Modele.Lehrer;
import Repository.KursRepository;
import Repository.LehrerRepository;
import Repository.StudentRepository;

import java.io.IOException;
import java.util.List;

public class LehrerController implements Controller<Lehrer>{

    private LehrerRepository lehrerRepo;
    private StudentRepository studentenRepo;
    private KursRepository kursRepository;

    public LehrerController(LehrerRepository lehrerRepo, StudentRepository studentenRepo, KursRepository kursRepository) {
        this.lehrerRepo = lehrerRepo;
        this.studentenRepo = studentenRepo;
        this.kursRepository = kursRepository;
    }

    public LehrerRepository getLehrerRepo() {
        return lehrerRepo;
    }

    public void setLehrerRepo(LehrerRepository lehrerRepo) {
        this.lehrerRepo = lehrerRepo;
    }

    public StudentRepository getStudentenRepo() {
        return studentenRepo;
    }

    public void setStudentenRepo(StudentRepository studentenRepo) {
        this.studentenRepo = studentenRepo;
    }

    public KursRepository getKursRepository() {
        return kursRepository;
    }

    public void setKursRepository(KursRepository kursRepository) {
        this.kursRepository = kursRepository;
    }

    /**
     * legt einen Lehrer in der RepoListe, indem man die Methode aus dem Repo aufruft
     * @param obj, das Objekt, das man hinlegt
     * @return das hingelegte Element
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public Lehrer create(Lehrer obj) throws IOException {
        return lehrerRepo.create(obj);
    }

    /**
     * gibt alle Elementen aus der RepoListe, indem man die Methode aus dem Repo aufruft
     * @return eine Liste von Professoren
     */
    @Override
    public List<Lehrer> getAll() {
        return lehrerRepo.getAll();
    }


    /**
     * Verändert einige Attribute eines Objektes, indem man die Methode aus dem Repo aufruft
     * @param obj, das Objekt mit dem switch erledigt
     * @return das alte Objet mit den neuen Attributen
     */
    @Override
    public Lehrer update(Lehrer obj) throws IOException {
        return lehrerRepo.update(obj);
    }

    /**
     * aus der RepoListe ein Objekt löschen, indem man die Methode aus dem Repo aufruft
     * @param objID, das Element das man löschen will
     * @return true, wenn man das Element löscht, false wenn man nichts tut
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public boolean delete(Long objID) throws IOException {
        return lehrerRepo.delete(objID);
    }

    /**
     * liest alle Attribute eines Objekts von einem File, indem man die Methode aus dem Repo aufruft
     * @return die Liste von Professoren
     * @throws IOException, fur Lesen aus dem File
     */
    @Override
    public List<Lehrer> readFromFile() throws IOException {
        return lehrerRepo.readFromFile();
    }

    /**
     * schreit neue Objekte in dem File, indem man die Methode aus dem Repo aufruft
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public void writeToFile() throws IOException {
        lehrerRepo.writeToFile();
    }

    /**
     * sucht in dem File, ob ein Objekt existiert mithilfe der IDs, indem man die Methode aus dem Repo aufruft
     * @param id, Id des Objektes das man sucht, anders null
     * @return das Objekt,wenn man es findet
     * @throws IOException, fur Lesen im File
     */
    @Override
    public Lehrer findOne(Long id) throws IOException {
        return lehrerRepo.findOne(id);
    }


    /**
     *
     * @param lehrer, der den Kurs löscht
     * @param kurs, den man löschen will
     * @return true, wenn das Kurs existiert und der Lehrer den löschen kann, andernfalls false
     * @throws IOException, fur Schreiben im File
     */
    public boolean loschenKurs(Lehrer lehrer, Kurs kurs) throws IOException
    {
        if(lehrerRepo.loschenKurs(lehrer, kurs))
        {
            studentenRepo.loschenKurs(kurs);
            kursRepository.delete(kurs.getID());
            return true;
        }
        return false;
    }

    /**
     * untersucht, ob das gegebene Id eines Lehrers in dem repoListe ist, indem man die Methode aus dem Repo aufruft
     * @param id Lehrer
     * @return true, wenn der Lehrer in der RepoListe ist, anderenfalls false
     */
    public boolean containsID(long id)
    {
        return lehrerRepo.containsID(id);
    }

    /**
     * untersucht, ob ein Professor einen Kurs unterrichtet, indem man die Methode aus dem Repo aufruft
     * @param lehrer, der unterrichtet
     * @param id des Kurses
     * @return true, wenn der Lehrer den Kurs unterrichtet, anderenfalls false
     */
    public boolean containsKurs(Lehrer lehrer, Long id)
    {
        return lehrerRepo.containsKurs(lehrer, id);
    }

    /**
     * loscht einen gegebenen Kurs von allen Professoren, die den unterrichten
     * @param kurs, der man wegnimmt
     */
    public void deleteKursFromAll(Kurs kurs) throws IOException {
        lehrerRepo.deleteKursFromAll(kurs);
    }

    /**
     * legt zu einen Lehrer einen Kurs
     * @param lehrer, Lehrer
     * @param kurs, Kurs
     * @throws IOException, fur Schreiben im File
     */
    public void addKurs(long lehrer, Kurs kurs) throws IOException {
        lehrerRepo.addKurs(lehrer, kurs);
    }


}
