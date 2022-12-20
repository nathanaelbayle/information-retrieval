package project.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to index the files
 *
 * @author Nathanaël Bayle
 */
public class Indexer {

    private final IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {
        //this directory will contain the indexes
        Directory indexDirectory =
                FSDirectory.open(new File(indexDirectoryPath).toPath());

        //create the indexer
        writer = new IndexWriter(indexDirectory,
                new IndexWriterConfig(new StandardAnalyzer()).setOpenMode(IndexWriterConfig.OpenMode.CREATE));
    }

    public void close() throws IOException {
        writer.close();
    }

    private Document getDocument(File file) throws IOException {
        Document document = new Document();
        FileReader fileReader = new FileReader(file);

        int i;
        String fileContent = "";
        while((i = fileReader.read()) != -1){
            fileContent += (char) i;
        }

        Pattern page = Pattern.compile("(.*)\\t\\t(.*)\\t\\t(.*)");
        Matcher pageMatcher = page.matcher(fileContent);

        if(pageMatcher.find()){
            document.add(new TextField("title", pageMatcher.group(1), Field.Store.YES));
            document.add(new TextField("country", pageMatcher.group(2), Field.Store.YES));
            document.add(new TextField("ingredients", pageMatcher.group(3), Field.Store.YES));
        }

        document.add(new TextField("contents", fileContent, Field.Store.YES));
        return document;

    }

    private void indexFile(File file) throws IOException {
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);
    }

    public int createIndex(String dataDirPath, FileFilter filter)
            throws IOException {
        //get all files in the data directory
        File[] files = new File(dataDirPath).listFiles();

        for (File file : files) {
            if (!file.isDirectory()
                    && !file.isHidden()
                    && file.exists()
                    && file.canRead()
                    && filter.accept(file)
            ) {
                indexFile(file);
            }
        }
        return writer.numDocs();
    }
}