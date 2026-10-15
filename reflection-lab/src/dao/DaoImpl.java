package dao;

/**
 * Implémentation concrète de l'interface IDao.
 * Simule une récupération de données depuis une source externe.
 * Remarque : la classe possède un constructeur public par défaut,
 * exigence stricte pour l'instanciation via la réflexion
 * (Constructor.newInstance()), sinon InstantiationException.
 */
public class DaoImpl implements IDao {

    /**
     * Retourne la valeur simulant les données de la source.
     * @return la valeur 100.0
     */
    @Override
    public double getValue() {
        // Trace utile pour déboguer le chargement dynamique
        System.out.println("[DAO] Récupération de la valeur depuis la source de données...");
        return 100.0;
    }
}
