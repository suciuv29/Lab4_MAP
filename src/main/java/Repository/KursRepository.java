package Repository;

import Modele.Kurs;
import Modele.Student;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import Exception.DasElementExistiertException;
import Exception.ListIsEmptyException;
import java.util.*;

public class KursRepository extends InMemoryRepository<Kurs> implements FileRepository<Kurs>{

    String file;

    public KursRepository(String file) {
        super();
        this.file = file;
    }

    @Override
    public String toString() {
        return "KursRepository{" +
                "repoList=" + repoList +
                '}';
    }

    /**
     * liest aus dem kurs.json File die Kurse
     * @return die Liste von Kursen
     * @throws IOException, fur Lesen im File
     */
    @Override
    public List<Kurs> readFromFile() throws IOException {

        Reader reader = new BufferedReader(new FileReader(file));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for (JsonNode pm : parser) {

            long ID = pm.path("id").asLong();

            String name = pm.path("name").asText();

            int lehrer = pm.path("lehrer").asInt();

            int maximaleAnzahlStudenten = pm.path("maximaleAnzahlStudenten").asInt();

            List<Long> listeStudenten = new ArrayList<>();
            for (JsonNode v : pm.path("listeStudenten"))
            {
                listeStudenten.add(v.asLong());
            }

            int ECTS = pm.path("ects").asInt();

            Kurs kurs = new Kurs(ID,name,lehrer,maximaleAnzahlStudenten,listeStudenten,ECTS);
            repoList.add(kurs);
        }

        return repoList;

    }

    /**
     * schreibt neue Kurse in kurse.json
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public void writeToFile() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());

        writer.writeValue(new File(file), repoList);

    }

    /**
     * sucht in dem kurs.json File einen Kurs nach seinem Id
     * @param idKurs, des Kurses
     * @return der Kurs, wenn man ihn findet, anderenfalls null
     * @throws IOException, fur Lesen im File
     */
    @Override
    public Kurs findOne(Long idKurs) throws IOException
    {
        Reader reader = new BufferedReader(new FileReader(file));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for (JsonNode pm : parser)
        {

            Long ID = pm.path("id").asLong();
            if(Objects.equals(idKurs, ID))
            {
                String name = pm.path("name").asText();

                int lehrer = pm.path("lehrer").asInt();

                int maximaleAnzahlStudenten = pm.path("maximaleAnzahlStudenten").asInt();

                List<Long> listeStudenten = new ArrayList<>();
                for (JsonNode v : pm.path("listeStudenten"))
                {
                    listeStudenten.add(v.asLong());
                }

                int ECTS = pm.path("ects").asInt();

                return new Kurs(idKurs,name,lehrer,maximaleAnzahlStudenten,listeStudenten,ECTS);
            }
        }

        return null;
    }

    /**
     * add eines Kurses in der RepoListe, wenn er nicht dort ist
     * @param obj, der Kurs der man hinlegen will
     * @return den hingelegten Kurs
     * @throws IOException, fur Schreiben im File
     * @throws DasElementExistiertException, das Element ist in der Liste
     */
    @Override
    public Kurs create(Kurs obj) throws IOException, DasElementExistiertException {
        for(Kurs kurs : repoList)
        {
            if(kurs.getID() == obj.getID())
                throw new DasElementExistiertException("Das Kurs ist in der Liste.");
        }

        this.repoList.add(obj);
        writeToFile();
        return obj;
    }


    /**
     * ändert die Attribute eines Kurses, wenn die Liste leer ist oder der Kurs nicht in der Liste ist → Exception
     * @param obj, das Objekt mit dem switch erledigt
     * @return der neue Kurs
     * @throws IOException, fur Schreiben im File
     * @throws ListIsEmptyException, die Liste ist leer
     */
    @Override
    public Kurs update(Kurs obj) throws IOException, ListIsEmptyException {
        if(repoList.isEmpty())
            throw  new ListIsEmptyException("Die Liste ist leer");

        Kurs kursToUpdate = this.repoList.stream()
                .filter(kurs -> Objects.equals(kurs.getID(), obj.getID()))
                .findFirst()
                .orElseThrow();

        kursToUpdate.setName(obj.getName());
        kursToUpdate.setLehrer(obj.getLehrer());
        kursToUpdate.setMaximaleAnzahlStudenten(obj.getMaximaleAnzahlStudenten());
        kursToUpdate.setEcts(obj.getEcts());
        kursToUpdate.setListeStudenten(obj.getListeStudenten());

        writeToFile();
        return kursToUpdate;
    }

    /**
     * löscht einen Kurs nach seinem Id
     * @param idKurs, des Kurse
     * @return true, wenn den Kurs löscht, anderenfalls false
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public boolean delete(Long idKurs) throws IOException
    {
        if(repoList.isEmpty())
            throw new IndexOutOfBoundsException("Die Liste ist leer");

        for(Kurs kurs : repoList)
        {
            if(kurs.getID() == idKurs)
            {
                repoList.remove(kurs);
                writeToFile();
                return true;
            }

        }

        return false;

    }

    /**
     * filtert die Liste nach die Anzahl von ECTS(die Kurse die > 5 ECTS haben)
     * @return die gefilterte Liste
     */
    public List<Kurs> filterList()
    {
        return repoList.stream()
                .filter(kurs->kurs.getEcts() > 5).toList();
    }

    /**
     * sortiert die Liste in steigender Reihenfolge nach Anzahl der freien PLatzen
     */
    public void sortList()
    {
        repoList.sort(Kurs::compareTo);
    }


    /**
     * überprüft, welche Kurse noch frei sind
     * @return eine Liste mit freien Kursen
     */
    public List<Kurs> getKurseFreiePlatzen()
    {
        List<Kurs> freieKurse = new ArrayList<>();
        for(Kurs kurs : repoList)
        {
            if(kurs.istFrei())
            {
                freieKurse.add(kurs);
            }
        }
        return freieKurse;
    }

    /**
     * untersucht, welche Kurse noch freien Platzen haben
     * @return ein Map der als Key den Kurs und als Value die Anzahl dem freien Platze hat
     */
    public Map<Long, Integer> kurseFreiePlatzenUndAnzahl()
    {
        Map<Long, Integer> mapFreieKurse = new HashMap<>();
        for(Kurs kurs : repoList)
        {
            if(kurs.istFrei())
            {
                mapFreieKurse.put(kurs.getID(),kurs.getAnzahlFreienPlatze());
            }
        }
        return mapFreieKurse;
    }

    /**
     * ändert die Anzahl der ECTS eines Kurses
     * @param ECTS, neue ECTS
     * @param idKurs, des Kurses
     * @return die alte ECTS, wenn der Kurs existiert, anderenfalls -1
     * @throws IOException, fur Schreiben im File
     * @throws ListIsEmptyException, die Liste ist leer
     */
    public int andernECTS(int ECTS,Long idKurs) throws IOException, ListIsEmptyException {
        for(Kurs kurs : repoList)
        {
            if (kurs.getID() == idKurs)
            {
                int alteECTS = kurs.getEcts();
                kurs.setEcts(ECTS);
                this.update(kurs);
                writeToFile();
                return alteECTS;
            }
        }
        return -1;
    }

    /**
     * untersucht, ob ein Kurs freie Platzen hat
     * @param idKurs, des Kurses
     * @return true, wenn der Kurs freie Platzen hat, anderenfalls false
     */
    public boolean validationFreiePlatzen(Long idKurs)
    {
        boolean valid = false;
        for(Kurs kurs : repoList)
        {
            if (kurs.getID() == idKurs)
                if(kurs.istFrei())
                    valid = true;

        }

        return valid;
    }

    /**
     * untersucht, ob ein Student bei einem Kurs teilnimmt
     * @param idKurs, des Kurses
     * @param idStudent, des Students
     * @return true, wenn der Student in der Liste von angemeldeten Studenten des Kurses sich befindet,
     * anderenfalls false
     */
    public boolean containsKursStudent(Long idKurs, Long idStudent)
    {
        boolean valid = false;
        for(Kurs kurs : repoList)
        {
            if (kurs.getID() == idKurs)
                if(!kurs.getListeStudenten().contains(idStudent))
                    valid = true;

        }

        return valid;
    }

    /**
     * man legt einen neuen Student in der Liste von Studenten eines Kurses
     * @param student, den man legt
     * @param idKurs, des Kurses
     * @throws IOException, fur Schreiben im File
     * @throws ListIsEmptyException, die Liste ist leer
     */
    public void addStudent(Student student, Long idKurs) throws IOException, ListIsEmptyException {
        if(validationFreiePlatzen(idKurs) && containsKursStudent(idKurs, student.getStudentID()))
        {
            for(Kurs kurs : repoList)
            {
                if(idKurs == kurs.getID())
                {
                    kurs.addStudent(student);
                    this.update(kurs);
                    writeToFile();
                }
            }

        }
    }

    /**
     * untersucht, ob ein Kurs existiert
     * @param id, des Kurses
     * @return true, wenn der Kurs in der RepoListe ist, anderenfalls false
     */
    public boolean containsID(Long id)
    {
        for(Kurs kurs : repoList)
        {
            if(kurs.getID() == id)
                return true;
        }
        return false;
    }
}
