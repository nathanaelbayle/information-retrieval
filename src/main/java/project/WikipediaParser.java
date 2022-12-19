package project;

import project.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static project.constants.WikiParserConstants.*;
import static project.spark.SparkMain.removeSpecialCharacters;


/**
 * This class is used to parse a Wikipedia dump file
 * @author Nathanaël Bayle
 */
public abstract class WikipediaParser {

    private static String buffer = "";

    /**
     * Parses the given Wikipedia XML file
     * @param wikipediaDump the wikipedia dump to parse
     */
    public static void parse(File wikipediaDump) {
        FileUtils.createFile(dataBaseFile);
        try (BufferedReader br = Files.newBufferedReader(wikipediaDump.toPath())) {
            boolean EOF = false;
            int MAX = 0;
            while (!EOF && MAX < maxIterations) {
                List<String> batch = new ArrayList<>(batchSize);
                for (int i = 0; i < batchSize; i++) {
                    String line = br.readLine();
                    if (EOF = line == null) break;
                    batch.add(line);
                }
                processBatch(batch);
                MAX++;
            }
            FileUtils.writeToFile(dataBaseFile, buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Processes the given batch of lines
     * @param batch the batch of lines to process
     */
    private static void processBatch(List<String> batch) {
        Pattern pagePattern = Pattern.compile("(?s)(?<=<page>).*?(?=<\\/page>)");
        Matcher pageMatcher = pagePattern.matcher(String.join("", batch));

        while (pageMatcher.find()) {
            String page = pageMatcher.group();
            Page p = new Page();

            // If the page is a category, we skip it
            Pattern notCategoryPattern = Pattern.compile("(?s)(?<=<title>Catégorie:).*?(?=<\\/title>)");
            Matcher notCategoryMatcher = notCategoryPattern.matcher(page);
            if (notCategoryMatcher.find()) continue;

            Pattern categoryPattern = Pattern.compile("\\[\\[Catégorie:(Cuisine.*?)\\]\\]");
            Matcher categoryMatcher = categoryPattern.matcher(page);
            while (categoryMatcher.find()) {
                p.setCategory(categoryMatcher.group(1).split("\\|")[0]);
            }

            // Get list of ingredients
            Pattern ingredientPattern = Pattern.compile("(\\s)\\[\\[(.*?)\\]\\]");
            Matcher ingredientMatcher = ingredientPattern.matcher(page);
            while (ingredientMatcher.find()) {
                p.addIngredient(ingredientMatcher.group(2));
            }

            Pattern titlePattern = Pattern.compile("(?<=<title>).*?(?=<\\/title>)");
            Matcher titleMatcher = titlePattern.matcher(page);
            if (titleMatcher.find()) {
                p.setTitle(titleMatcher.group());
            }

            Pattern textPattern = Pattern.compile("<text (.*)>(.*?)<\\/text>");
            Matcher textMatcher = textPattern.matcher(page);
            if (textMatcher.find() && p.getCategory() != null && p.getTitle() != null && p.getText() != null) {
                String content = textMatcher.group(2);
                content = removeSpecialCharacters(content);
                content = content.toLowerCase();
                p.setText(content);
            }
            processPage(p);
        }
    }

    /**
     * This method is called for each page parsed
     * @param page the page to process
     */
    public static void processPage(Page page) {
        if (page.getCategory() != null && page.getTitle() != null) {
            String fileName = dataDir + "/" + page.getTitle() + ".txt";
            FileUtils.createFile(fileName);
            FileUtils.writeToFile(fileName, page);

            //buffer += page.getCategory() + "\t" + page.getTitle() + "\t" + page.getMainIngredients() + "\n";
        }
    }
}