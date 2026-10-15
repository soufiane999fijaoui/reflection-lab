package presentation;

import dao.IDao;
import metier.IMetier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Moteur de chargement dynamique (conteneur léger).
 * Simule un conteneur d'injection de dépendances (type Spring/CDI)
 * en utilisant uniquement l'API native de réflexion :
 * 1. lecture des noms de classes depuis config.txt
 * 2. chargement des classes (Class.forName)
 * 3. instanciation (Constructor.newInstance)
 * 4. injection de la dépendance DAO dans le Métier (Method.invoke)
 * 5. exécution du calcul métier
 */
public class Presentation2 {

    public static void main(String[] args) {
        try {
            // 1. Charger les noms de classes depuis le fichier de configuration
            String[] classNames = loadClassNames("config.txt");

            // 2. Instancier le DAO dynamiquement
            IDao dao = (IDao) instantiateClass(classNames[0]);
            System.out.println("[CONTAINER] DAO instancié : " + dao.getClass().getName());

            // 3. Instancier le Métier dynamiquement
            IMetier metier = (IMetier) instantiateClass(classNames[1]);
            System.out.println("[CONTAINER] Métier instancié : " + metier.getClass().getName());

            // 4. Injecter le DAO dans le Métier via la réflexion (setter setDao)
            injectDependency(metier, "setDao", dao);

            // 5. Exécuter le calcul via l'interface IMetier
            double resultat = metier.calcul();
            System.out.println("Résultats = " + resultat);

        } catch (Exception e) {
            // Les exceptions de réflexion sont peu explicites : on affiche la pile complète
            e.printStackTrace();
        }
    }

    /**
     * Lit les noms de classes (fully qualified) depuis un fichier de configuration.
     * Une classe par ligne, sans espaces ni lignes vides.
     * @param fileName chemin du fichier de configuration
     * @return un tableau de 2 noms de classes [dao, metier]
     * @throws IOException si le fichier est introuvable ou illisible
     */
    private static String[] loadClassNames(String fileName) throws IOException {
        String[] classNames = new String[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            for (int i = 0; i < 2; i++) {
                // readLine() retourne null si le fichier est trop court -> NullPointerException
                classNames[i] = reader.readLine().trim();
            }
        }
        return classNames;
    }

    /**
     * Instancie une classe à partir de son nom complet.
     * @param className nom fully qualified (ex: dao.DaoImpl)
     * @return la nouvelle instance de l'objet
     * @throws ClassNotFoundException si la classe n'existe pas dans le classpath
     * @throws InstantiationException si la classe est abstraite ou sans constructeur public
     */
    private static Object instantiateClass(String className) throws Exception {
        // Charge la classe dans la JVM à partir de son nom en chaîne
        Class<?> clazz = Class.forName(className);
        // Récupère le constructeur public par défaut
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        // Crée l'instance
        return constructor.newInstance();
    }

    /**
     * Injecte une dépendance dans un objet via une méthode setter,
     * en déterminant le type du paramètre à partir des interfaces
     * implémentées par la dépendance (ici IDao).
     * @param target l'objet cible à injecter (ex: MetierImpl)
     * @param setterName nom de la méthode setter (ex: setDao)
     * @param dependency l'instance à injecter (ex: DaoImpl)
     * @throws NoSuchMethodException si le setter n'existe pas
     * @throws IllegalAccessException si la méthode n'est pas publique
     */
    private static void injectDependency(Object target, String setterName, Object dependency) throws Exception {
        Class<?> targetClass = target.getClass();
        // On passe le type de l'interface (ex: IDao) correspondant à la signature du setter
        Method setterMethod = targetClass.getMethod(setterName, dependency.getClass().getInterfaces()[0]);
        // Appel effectif de la méthode sur la cible avec la dépendance
        setterMethod.invoke(target, dependency);
    }
}
