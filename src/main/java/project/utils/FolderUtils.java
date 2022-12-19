package project.utils;

import java.io.File;

import static project.constants.WikiParserConstants.dataDir;

/**
 * This class is used to manage the folders
 *
 * @author Nathanaël Bayle
 */
public final class FolderUtils {

    /**
     * Creates the initial directory if it doesn't exist
     */
    public static void createFolder() {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * Creates the given folder if it doesn't exist
     * @param folderName the name of the folder to create
     */
    public static void createFolder(String folderName) {
        File file = new File(dataDir + "/" + folderName);
        if (!file.exists()) {
            file.mkdir();
        }
    }
}
