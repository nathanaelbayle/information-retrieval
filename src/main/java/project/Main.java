package project;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import project.lucene.Indexer;
import project.lucene.TextFileFilter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String indexDir;
    static String dataDir;
    static Indexer indexer;

    public static void main(String[] args) throws Exception {

        try {
            Main main = new Main();
            indexDir = args[0];
            dataDir = args[1];
            main.createIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createIndex() throws IOException, ParseException {
        // create a directory object
        indexer = new Indexer(indexDir);
        // start counting time
        long startTime = System.currentTimeMillis();
        // index all text files under data directory
        int numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        // end counting time
        long endTime = System.currentTimeMillis();
        // close the index
        indexer.close();
        // print the time it took to index all files
        System.out.println(numIndexed + " File indexed, time taken: " + (endTime - startTime) + " ms");

        System.out.print("========================================\n\t\tEnter a Query\n========================================\n> ");
        // Using Scanner for Getting Input from User
        Scanner in = new Scanner(System.in);
        String query = in.nextLine();

        // search for the query
        List<Document> docs = searchIndex(query);
        docs.forEach(doc -> {
            System.out.println("Recipe found: " + doc.get("title") + " at URL: " + "https://en.wikipedia.org/wiki/" + doc.get("title").replace(" ", "_"));
        });
    }

    public static List<Document> searchIndex(String searchString) throws IOException, ParseException {
        System.out.println("Searching for '" + searchString + "'");

        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IndexReader indexReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        StandardAnalyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser("contents", analyzer);

        Query query = queryParser.parse(searchString);

        TopDocs topDocs = indexSearcher.search(query, 10);
        List<Document> documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            documents.add(indexSearcher.doc(scoreDoc.doc));
        }
        return documents;
    }
}