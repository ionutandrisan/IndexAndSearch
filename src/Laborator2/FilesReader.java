package Laborator2;

import Laborator1.ParseHtml;
import Laborator1.WordSplitter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.*;

public class FilesReader {

    private static HashMap<String, Integer> stopWords;
    private static HashMap<String, Integer> exceptions;

    public FilesReader() throws IOException {
        stopWords = populateHashMap("./stopwords_english.txt");
        exceptions = populateHashMap("./exceptions.txt");
    }

    public static HashMap<String, Integer> populateHashMap(String path) throws IOException {
        File file = new File(path);
        HashMap<String, Integer> hashMap = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (!hashMap.containsKey(line.toString()))
                hashMap.put(line.toString(), 1);
        }
        return hashMap;
    }

    public Queue<File> putFilesToQueue(String path) throws IOException {
        File folder = new File(path);
        Queue<File> files = new LinkedList<>();
        Queue<File> directories = new LinkedList();
        if(folder.isDirectory()) {
            directories.add(folder);
        } else {
            files.add(folder);
        }
        while (!directories.isEmpty()) {
            File child = directories.remove();
            if (child.isDirectory()) {
                Collections.addAll(directories, Objects.requireNonNull(child.listFiles()));
            } else if (child.isFile()) {
                files.add(child);
            }
        }
        return files;
    }

    public HashMap<String, Integer> readFile(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8", "");
        String text = doc.body().text();
        HashMap<String, Integer> hm = WordSplitter.splitWords(text, exceptions, stopWords);
        return hm;
    }

    public HashMap<String, Integer> getStopWords() {
        return stopWords;
    }

    public HashMap<String, Integer> getExceptions() {
        return exceptions;
    }

}
