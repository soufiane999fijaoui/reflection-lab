package dao;

/**
 * Interface de persistance (couche DAO).
 * Définit le contrat que devra respecter toute source de données
 * (base SQL, cache Redis, API REST distante...).
 * Le code métier ne verra que cette abstraction (principe DIP).
 */
public interface IDao {

    /**
     * Récupère la valeur depuis la source de données.
     * @return La valeur double récupérée.
     */
    double getValue();
}
