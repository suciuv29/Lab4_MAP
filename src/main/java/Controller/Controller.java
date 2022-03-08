package Controller;

import java.io.IOException;
import java.util.List;
import Exception.DasElementExistiertException;
import Exception.ListIsEmptyException;
public interface Controller<T> {

    /**
     * legt ein Objekt der Typ T in der RepoListe
     * @param obj, das Objekt, das man hinlegt
     * @return das hingelegte Element
     * @throws IOException, fur Schreiben im File
     * @throws DasElementExistiertException, das Element ist in der Liste
     */
    T create(T obj) throws IOException, DasElementExistiertException;

    /**
     * gibt alle Elementen aus der RepoListe
     * @return eine Liste mit Elementen der Typ T
     */
    List<T> getAll();


    /**
     * Verändert einige Attribute eines Objektes
     * @param obj, das Objekt mit dem switch erledigt
     * @return das alte Objet mit den neuen Attributen
     * @throws IOException, fur Schreiben im File
     * @throws ListIsEmptyException, die Liste ist leer
     */
    T update(T obj) throws IOException, ListIsEmptyException;

    /**
     * aus der RepoListe ein Objekt löschen
     * @param objID, das Element das man löschen will
     * @return true, wenn man das Element löscht, false wenn man nichts tut
     * @throws IllegalAccessException, die Liste ist leer
     * @throws IOException, fur Schreiben im File
     */
    boolean delete(Long objID) throws IllegalAccessException, IOException;

    /**
     * liest alle Attribute eines Objekts von einem File
     * @return die Liste von Objekten des Typs T
     * @throws IOException, fur Schreiben im File
     */
    List<T> readFromFile() throws IOException;


    /**
     * schreit neue Objekte in dem File
     * @throws IOException, fur Schreiben im File
     */
    void writeToFile() throws IOException;

    /**
     * sucht in dem File, ob ein Objekt existiert mithilfe des IDs
     * @return das Objekt,wenn man es findet, anders null
     * @throws IOException das Objekt existiert nicht
     */
    T findOne(Long id) throws IOException;
}
