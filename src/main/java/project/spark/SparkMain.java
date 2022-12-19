package project.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import project.Page;
import project.utils.FileUtils;
import project.utils.FolderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class is the main class of the project.
 * It is used to launch the Spark job.
 *
 * @author Nathanaël Bayle
 */
public class SparkMain {

    private static String dataDir = "";

    public static void main(String[] args) {

        dataDir = args[1];
        FolderUtils.createFolder(dataDir);

        // configure spark
        SparkConf sparkConf = new SparkConf()
                .setAppName("XML to DataFrame")
                .set("spark.executor.memory", "6g");

        // start a spark context
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        sc.setLogLevel("ERROR");

        // get spark session
        SparkSession spark = SparkSession.builder()
                .config(sparkConf)
                .getOrCreate();

        // read xml file
        try {
            // reading of the XML file as plain text
            System.out.println("Loading data...");
            Dataset<Row> df = spark.read()
                    .format("xml")
                    .option("rowTag", "page")
                    .load(args[0]);
            System.out.println("Data loaded");

            System.out.println("Parsing dataset...");
            Dataset<Row> parsedDf = df.select("title", "revision.text._VALUE");
            parse(parsedDf);
            System.out.println("Dataset parsed");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + " " + e.getCause());
            e.printStackTrace();
        }
        System.out.println("Work done!");
        sc.close();
    }


    /**
     * This method parses the dataset.
     *
     * @param df the dataset to parse
     */
    private static void parse(Dataset<Row> df) {
        // for each row in the dataset, parse the text and save it to the database
        df.foreach(row -> {
            Page p = new Page();
            String title = removeSpecialCharacters(row.getString(0));

            Pattern notCategoryPattern = Pattern.compile("(?s)(category:)");
            Matcher notCategoryMatcher = notCategoryPattern.matcher(title);
            if (notCategoryMatcher.find()) {
                return;
            }
            String text = row.getString(1);
            String category = parseText(text);
            if (!Objects.equals(category, "")) {
                p.setTitle(title);
                p.addIngredient(getIngredientList(text));
                p.addCategory(category.replace("\n", " | "));
                processPage(p);
            }
        });
    }

    /**
     * This method is called for each page parsed
     *
     * @param page the page to process
     */
    public static void processPage(Page page) {
        if (page.getCategory() != null && page.getTitle() != null) {
            String fileName = dataDir + "/" + page.getTitle() + ".txt";
            FileUtils.createFile(fileName);
            FileUtils.writeToFile(fileName, page);
        }
    }


    /**
     * This method parses the text of a page to get the category.
     *
     * @param text the text to parse
     * @return the category
     */
    private static List<String> getIngredientList(String text) {
        List<String> ingredients = new ArrayList<>();
        Pattern ingredientPattern = Pattern.compile("(\\s)\\[\\[(.*?)\\]\\]");
        Matcher ingredientMatcher = ingredientPattern.matcher(text);
        while (ingredientMatcher.find()) {
            ingredients.add(ingredientMatcher.group(2));
        }
        return ingredients;
    }

    /**
     * This method parses the text of a page to get the category.
     *
     * @param text the text to parse
     * @return the category
     */
    private static String parseText(String text) {
        if (text == null) {
            return "";
        }
        Pattern categoryPattern = Pattern.compile("\\[\\[Category:(.*?)(cuisine)\\]\\]");
        Matcher categoryMatcher = categoryPattern.matcher(text);

        String category = "";
        while (categoryMatcher.find()) {
            category += categoryMatcher.group(1) + categoryMatcher.group(2) + "\n";
        }
        return category;
    }

    /**
     * This method is called for each page parsed to remove special characters
     *
     * @param text the text to process
     * @return the text without special characters
     */
    public static String removeSpecialCharacters(String text) {
        while (text.contains("&amp;") ||
                text.contains("&nbsp;") ||
                text.contains("&quot;") ||
                text.contains("&lt;") ||
                text.contains("&gt;") ||
                text.contains("&apos;") ||
                text.contains("?") ||
                text.contains("'")) {
            text = text.replaceAll("&amp;", "&"); // Replaces entity name for the character '&'
            text = text.replaceAll("&nbsp;", "_");  // Replaces entity name for the character '-'
            text = text.replaceAll("&quot;", "\""); // Replaces entity name for the character '"'
            text = text.replaceAll("&lt;", "<");
            text = text.replaceAll("&gt;", ">");
            text = text.replaceAll("&apos;", "'");
            text = text.replaceAll("\\{\\{", "");
            text = text.replaceAll("\\}\\}", "");
            text = text.replaceAll("\\[\\[", "");
            text = text.replaceAll("\\]\\]", "");
            text = text.replaceAll("\\|", "");
            text = text.replaceAll("'", "");
            text = text.replaceAll("'", "");
            text = text.replaceAll("''", "");
            text = text.replaceAll("\\?", "_");
        }
        return text.toLowerCase();
    }
}