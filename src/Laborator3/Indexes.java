package Laborator3;

import Laborator2.FilesReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Indexes {

    private static HashMap<String, HashMap<String, Integer>> directIndex;
    private static HashMap<String, HashMap<String, Integer>> indirectIndex;
    private static FilesReader filesReader;

    public Indexes() throws IOException {

    }

    public void populateDirectIndex() throws IOException {
        filesReader = new FilesReader();
        Queue<File> files = filesReader.putFilesToQueue("./test-files");
        directIndex = new HashMap<>();
        while(!files.isEmpty()) {
            File file = files.remove();
            directIndex.put(file.getPath(), filesReader.readFile(file));
        }
    }

    private static void writeToMappingFile(String filePath, HashMap<String, HashMap<String, Integer>> hm)
            throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(filePath);
        for (Map.Entry<String, HashMap<String, Integer>> entry : hm.entrySet()) {
            String key = entry.getKey();
            HashMap<String, Integer> h = entry.getValue();
            writeToFileHashMap(key, fileWriter, h);
        }
        fileWriter.close();
    }

    public static void writeToFileHashMap(String filePath, FileWriter fileWriter, HashMap<String, Integer> hm)
            throws IOException {
        for (Map.Entry<String, Integer> entry : hm.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            fileWriter.write(filePath + " " + key + " " + value + '\n');
        }
    }

    public void writeIndirectIndexToMappingFile(String filePath) throws IOException {
        writeToMappingFile(filePath, indirectIndex);
    }

    public void writeDirectIndexToMappingFile(String filepath) throws IOException {
        writeToMappingFile(filepath, directIndex);
    }

    private static void writeIndexToFile(String path, HashMap<String, HashMap<String, Integer>> hm) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(path);
        for (Map.Entry<String, HashMap<String, Integer>> entry : hm.entrySet()) {
            String key = entry.getKey();
            HashMap<String, Integer> h = entry.getValue();
            fileWriter.write(key + ' ');
            fileWriter.write(h.toString() + "\n");
        }
        fileWriter.close();
    }

    public void writeDirectIndexToFile(String path) throws IOException {
        writeIndexToFile(path, directIndex);
    }

    public void writeIndirectIndexToFile(String path) throws IOException {
        writeIndexToFile(path, indirectIndex);
    }

    public void populateIndirectIndex() {
        indirectIndex = new HashMap<>();
        for (Map.Entry<String, HashMap<String, Integer>> entry : directIndex.entrySet()) {
            String key = entry.getKey();
            HashMap<String, Integer> h = entry.getValue();
            for (Map.Entry<String, Integer> etr : h.entrySet()) {
                String k = etr.getKey();
                Integer val = etr.getValue();
                HashMap<String, Integer> hashMap;
                if (!indirectIndex.containsKey(k)) {
                    hashMap = new HashMap();
                } else {
                    hashMap = indirectIndex.get(k);
                }
                hashMap.put(key, val);
                indirectIndex.put(k, hashMap);
            }
        }
    }

    public HashMap<String, HashMap<String, Integer>> getIndirectIndex() {
        return indirectIndex;
    }

    public HashMap<String, HashMap<String, Integer>> getDirectIndex() {
        return directIndex;
    }

}
