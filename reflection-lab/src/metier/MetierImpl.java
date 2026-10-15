package metier;

import dao.IDao;

/**
 * Implémentation concrète de l'interface IMetier.
 * Dépend d'une instance d'IDao injectée dynamiquement
 * par le conteneur (Presentation2) via le setter setDao.
 */
public class MetierImpl implements IMetier {

    /** Dépendance injectable : jamais instanciée directement ici. */
    private IDao dao;

    /**
     * Méthode d'injection de la dépendance.
     * Appelée dynamiquement par le conteneur via la réflexion.
     * @param dao L'instance de IDao à injecter.
     */
    public void setDao(IDao dao) {
        this.dao = dao;
    }

    /**
     * Logique métier : récupère la valeur via le DAO et la double.
     * @return le résultat du calcul
     */
    @Override
    public double calcul() {
        // Garde-fou contre une injection oubliée (évite une NPE tardive)
        if (dao == null) {
            throw new IllegalStateException("Le DAO n'a pas été injecté.");
        }

        // Étape 1 : récupération des données via l'abstraction IDao
        double valeurBase = dao.getValue();

        // Étape 2 : règle métier simple = doublement de la valeur
        double resultat = valeurBase * 2;

        // Trace du flux de données à travers les couches
        System.out.println("[METIER] Calcul effectué. Résultat : " + resultat);
        return resultat;
    }
}
