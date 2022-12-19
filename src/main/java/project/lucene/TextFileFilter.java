package project.lucene;

import java.io.File;
import java.io.FileFilter;

/**
 * Text files filter.
 *
 * @author Nathanaël Bayle
 */
public class TextFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".txt");
    }
}
