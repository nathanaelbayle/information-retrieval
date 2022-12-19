package project.constants;

/**
 * This class stores the hard coded file paths.
 *
 * @author Nathanaël Bayle
 */
public final class WikiParserConstants {

    // Max number of iterations for the parser
    public static final int maxIterations = 1000;

    // Max number of line per batch to parse
    public static int batchSize = 10000;

    // Input files
    public static final String frWikiFile = "src/main/resources/frwiki-latest-pages-articles.xml";
    public static final String enWikiFile = "src/main/resources/enwiki-latest-pages-articles.xml";

    // Output Dir
    public static final String dataDir = "results/data";

    // Output files
    public static final String dataXlsxFile = "results/data.xlsx";

    // Output files
    public static final String dataBaseFile = "results/dataBase.txt";
}
