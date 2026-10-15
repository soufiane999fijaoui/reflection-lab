package metier;

/**
 * Interface métier (couche de logique de business).
 * Séparation des responsabilités (SRP) : cette couche ne gère
 * que les règles de calcul, jamais l'accès aux données.
 */
public interface IMetier {

    /**
     * Effectue le calcul métier.
     * @return Le résultat du calcul.
     */
    double calcul();
}
