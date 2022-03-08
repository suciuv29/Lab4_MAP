package Repository;

import java.io.IOException;
import java.util.List;

public interface FileRepository<T> extends ICrudRepository<T>{

    /**
     * liest alle Attribute eines Objekts von einem File
     */
    List<T> readFromFile() throws IOException;

    /**
     * schreit neue Objekte in dem File
     */
    void writeToFile() throws IOException;

    /**
     * sucht in dem File, ob ein Objekt existiert mit Hilfe eines IDs
     * @param id, des Elementes das man in dem RepoListe sucht
     * @return das Objekt,wenn man es findet
     * @throws IOException das Objekt existiert nicht
     */
    T findOne(Long id) throws IOException;
}
