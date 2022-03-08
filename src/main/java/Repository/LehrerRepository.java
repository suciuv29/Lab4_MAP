package Repository;
import Modele.Lehrer;
import Modele.Kurs;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LehrerRepository extends InMemoryRepository<Lehrer> implements FileRepository<Lehrer>{

    String file;

    public LehrerRepository(String file) {
        super();
        this.file = file;
    }

    @Override
    public String toString() {
        return "LehrerRepository{" +
                "repoList=" + repoList +
                '}';
    }

    /**
     * ändert die Attribute eines Lehrers, wenn die Liste leer ist oder der Lehrer nicht in der Liste ist → Exception
     * @param obj, der Lehrer, den man andern will
     * @return der neue Lehrer
     * @throws IndexOutOfBoundsException, fur Schreiben im File
     */
    @Override
    public Lehrer update(Lehrer obj) throws IOException {

        if(repoList.isEmpty())
            throw  new IndexOutOfBoundsException("Die Liste ist leer");

        Lehrer lehrerToUpdate = this.repoList.stream()
                .filter(lehrer -> lehrer.getLehrerID() == obj.getLehrerID())
                .findFirst()
                .orElseThrow();

        lehrerToUpdate.setNachname(obj.getNachname());
        lehrerToUpdate.setVorname(obj.getVorname());
        lehrerToUpdate.setKurse(obj.getKurse());

        writeToFile();

        return lehrerToUpdate;
    }


    /**
     * liest aus dem lehrer.json File die Professoren
     * @return die Liste von Professoren
     * @throws IOException, fur Lesen vomm File
     */
    @Override
    public List<Lehrer> readFromFile() throws IOException {
        Reader reader = new BufferedReader(new FileReader(file));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for (JsonNode pm : parser) {

            int id = pm.path("lehrerID").asInt();

            String vorName = pm.path("vorname").asText();

            String nachName = pm.path("nachname").asText();

            List<Long> kurse = new ArrayList<>();
            for (JsonNode v : pm.path("kurse"))
            {
                kurse.add(v.asLong());
            }


            Lehrer lehrer = new Lehrer(vorName,nachName,kurse,id);
            repoList.add(lehrer);
        }

        return repoList;
    }

    /**
     * schreibt neue Kurse in lehrer.json
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public void writeToFile() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());

        writer.writeValue(new File(file), repoList);

    }

    /**
     * sucht in dem lehrer.json File einen Professor nach seinem Id
     * @param id, des Lehrers
     * @return der Lehrer, wenn man ihn findet, anderenfalls null
     * @throws IOException, fur Lesen vom File
     */
    @Override
    public Lehrer findOne(Long id) throws IOException {
        Reader reader = new BufferedReader(new FileReader(file));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for (JsonNode pm : parser)
        {

            long idLehrer = pm.path("lehrerID").asLong();
            if(id == idLehrer)
            {
                String vorName = pm.path("vorname").asText();

                String nachName = pm.path("nachname").asText();

                List<Long> kurse = new ArrayList<>();
                for (JsonNode v : pm.path("kurse"))
                {
                    kurse.add(v.asLong());
                }


            return new Lehrer(vorName,nachName,kurse,idLehrer);


            }
        }

        return null;
    }

    /**
     * löscht einen Lehrer nach seinem Id
     * @param idLehrer, des Lehrers
     * @return true, wenn den Lehrer löscht, anderenfalls false
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public boolean delete(Long idLehrer) throws IOException {

        if(repoList.isEmpty())
            throw new IndexOutOfBoundsException("Die Liste ist leer");

        for(Lehrer lehrer : repoList)
        {
            if(lehrer.getLehrerID() == idLehrer)
            {
                repoList.remove(lehrer);
                writeToFile();
                return true;
            }

        }

        return false;
    }

    /**
     * add einen Lehrer in der RepoListe, wenn er nicht dort ist
     * @param obj, der Lehrer der man hinlegen will
     * @return den hingelegten Lehrer
     * @throws IOException, fur Schreiben im File
     */
    @Override
    public Lehrer create(Lehrer obj) throws IOException {

        for(Lehrer lehrer : repoList)
        {
            if(lehrer.getLehrerID() == obj.getLehrerID())
                throw new IllegalArgumentException("Das Object ist in der Liste.");
        }

        this.repoList.add(obj);
        writeToFile();
        return obj;
    }

    /**
     * löscht ein Kurs von einem Professor
     * @param lehrer, von dem man den Kurs löscht
     * @param kurs, den man löscht
     * @return true, wenn man den Kurs löscht, anderenfalls false
     * @throws IOException, fur Schreiben im File
     */
    public boolean loschenKurs(Lehrer lehrer,Kurs kurs) throws IOException {
        if(!lehrer.getKurse().contains(kurs.getID()))
            return false;

        lehrer.loschenKurs(kurs);
        writeToFile();
        return true;
    }

    /**
     * untersucht, ob ein Lehrer existiert
     * @param id, des Lehrers
     * @return true, wenn der Lehrer in der RepoListe ist, anderenfalls false
     */
    public boolean containsID(Long id)
    {
        for(Lehrer lehrer : repoList)
        {
            if(lehrer.getLehrerID() == id)
                return true;
        }
        return false;
    }

    /**
     *  untersucht, ob ein Lehrer einen Kurs unterrichtet
     * @param lehrer, der Lhrer
     * @param id, des Kurses
     * @return true, wenn der Lehrer den Kurs unterrichtet, anderenfalls false
     */
    public boolean containsKurs(Lehrer lehrer, Long id)
    {
        return lehrer.containsKurs(id);
    }

    /**
     * löscht einen Kurs von allen Professoren aus dem RepoList
     * @param kurs, den man löscht
     */
    public void deleteKursFromAll(Kurs kurs) throws IOException {
        for(Lehrer lehrer : repoList)
        {
            lehrer.loschenKurs(kurs);
        }
        writeToFile();

    }

    public void addKurs(long id, Kurs kurs) throws IOException {

        Lehrer lehrer = null;
        for(Lehrer lehrer2 : repoList)
        {
            if(id == lehrer2.getLehrerID())
                lehrer = lehrer2;
        }
        assert lehrer != null;
        lehrer.addKurs(kurs);
        writeToFile();
    }
}