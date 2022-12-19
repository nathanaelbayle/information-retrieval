package project.utils;

import project.Page;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.io.FileUtils.readLines;

/**
 * This class contains utility methods for file handling
 *
 * @author Nathanaël Bayle
 */
public final class FileUtils {

    /**
     * Creates the given file if it doesn't exist
     * @param fileName the name of the file to create
     */
    public static void createFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        } else {
            try {
                file.createNewFile();
            } catch (Exception e) {
               // System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void writeToFile(String fileName, Page page) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(page.toString());
            writer.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Read from the given file and returns the result
     * @param fileName the name of the file to read from
     * @return the content of the file
     */
    public static String readFromFile(String fileName, String countryName, String[] ingredientNames) {
        String result = "";

        String regex = "";
        for (String ingredientName : ingredientNames) {
            regex += "(?<![\\w\\d])" + ingredientName + "(?![\\w\\d])(.*)";
        }

        Pattern pattern = Pattern.compile("(.*)(.*?)\t(.*?)\t(.*)");
        try {
            File file = new File(fileName);

            for (String line : readLines(file, "UTF-8")) {
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {

                    if (countryName.length() > 3 && ingredientNames.length > 0) {
                        Pattern countryPattern = Pattern.compile(countryName);
                        Matcher countryMatcher = countryPattern.matcher(matcher.group(1));

                        if (countryMatcher.find()) {
                            Pattern ingredientPattern = Pattern.compile(regex);
                            Matcher ingredientMatcher = ingredientPattern.matcher(matcher.group(4));

                            if (ingredientMatcher.find()) {
                                result += line + "\n";
                            }
                        }
                    }

                    else if (countryName.length() > 3) {
                        Pattern countryPattern = Pattern.compile(countryName);
                        Matcher countryMatcher = countryPattern.matcher(matcher.group(1));

                        if (countryMatcher.find()) {
                            result += line + "\n";
                        }
                    }

                    else if (ingredientNames.length > 0) {
                        Pattern ingredientPattern = Pattern.compile(regex);
                        Matcher ingredientMatcher = ingredientPattern.matcher(matcher.group(4));

                        if (ingredientMatcher.find()) {
                            result += line + "\n";
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        return result;
    }
}
