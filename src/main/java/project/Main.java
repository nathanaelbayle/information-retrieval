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
    String indexDir = "C:\\Users\\Nath\\Desktop\\IR\\information-retrival-project\\data\\index";
    String dataDir = "C:\\Users\\Nath\\Desktop\\IR\\information-retrival-project\\data\\data";
    Indexer indexer;

    public static void main(String[] args) throws Exception {
        // WikipediaParser.parse(new File("C:\\Users\\Nath\\Desktop\\IR\\information-retrival-project\\src\\main\\resources\\frwiki-latest-pages-articles.xml"));

        try {
            Main main = new Main();
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

        // create analyzer
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // create directory reader
        Directory directory = FSDirectory.open(Paths.get(indexDir));

        System.out.println("Searching for '" + query.replace(", ", " AND ") + "'...");
        // search for the query
        List<Document> docs = searchFiles("content", query, analyzer, directory);
        docs.forEach(doc -> {
            System.out.println(doc.get("title"));
        });
    }

    public List<Document> searchFiles(String inField, String queryString, StandardAnalyzer analyzer, Directory directory) {
        try {
            Query query = new QueryParser(inField, analyzer).parse(queryString);

            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(indexReader);

            TopDocs topDocs = searcher.search(query, 10);
            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }
            return documents;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}