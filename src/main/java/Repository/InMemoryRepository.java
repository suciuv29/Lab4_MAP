package Repository;

import java.util.ArrayList;
import java.util.List;

public abstract class InMemoryRepository<T> implements ICrudRepository<T> {

    protected List<T> repoList;

    /**
     * erstellt ein leeres ArrayList fur dem Repo
     */
    public InMemoryRepository() {
        this.repoList = new ArrayList<>();

    }

    /**
     * gibt alle Elementen aus dem RepoList des Typs T
     * @return die Liste
     */
    public List<T> getAll() {

        return this.repoList;
    }

}
