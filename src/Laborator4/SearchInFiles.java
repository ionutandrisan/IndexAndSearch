package Laborator4;

import Laborator2.FilesReader;
import PorterStemming.Stemmer;
import com.sun.xml.internal.stream.events.StartElementEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchInFiles {

    private static HashMap<String, Integer> stopWordsHM;
    private static HashMap<String, Integer> exceptions;
    private static HashMap<String, HashMap<String, Integer>> indirectIndex;
    private static ArrayList<Operators> operators = new ArrayList<>();
    private static ArrayList<String> operands = new ArrayList<>();

    public SearchInFiles(HashMap<String, HashMap<String, Integer>> index) throws IOException {
        FilesReader filesReader = new FilesReader();
        stopWordsHM = filesReader.getStopWords();
        exceptions = filesReader.getExceptions();
        indirectIndex = index;
    }

    public void readSearchLine(String text) {
        char[] letters = text.toCharArray();
        StringBuilder word = new StringBuilder();

        for (char c : letters) {
            if (!Character.isDigit(c) && !Character.isLetter(c)) {
                if (!word.equals("")) {
                    if(exceptions.containsKey(word.toString())) {
                        operands.add(word.toString());
                    } else {
                        if (!stopWordsHM.containsKey(word.toString())) {
                            Stemmer s = new Stemmer(word);
                            s.stem();
                            operands.add(s.toString());
                        } else {
                            operators.remove(operators.size() - 1);
                        }
                    }
                    word = new StringBuilder();
                }

                if (c == ' ') {
                    operators.add(Operators.SPACE);
                } else if (c == '+') {
                    operators.add(Operators.PLUS);
                } else if (c == '-') {
                    operators.add(Operators.MINUS);
                }
            } else {
                word.append(c);
            }
        }
        if (!word.equals("")) {
            if(!stopWordsHM.containsKey(word.toString().toLowerCase())) {
                Stemmer s = new Stemmer(word);
                s.stem();
                operands.add(s.toString());
            }
        }
        System.out.println(operands);
        System.out.println(operators);
    }

    public ArrayList<String> search() {
        ArrayList<String> result = new ArrayList<>();
        if (operands.size() == 1) {
            return getWordLocation(operands.get(0));
        } else {
            ArrayList<String> temp = getWordLocation(operands.get(0));
            for(int i = 1; i < operands.size(); i++) {
                if(operators.get(i - 1) == Operators.SPACE) {
                    result = orOperator(temp, getWordLocation(operands.get(i)));
                }
                if(operators.get(i - 1) == Operators.PLUS) {
                    result = andOperator(temp, getWordLocation(operands.get(i)));
                }
                if(operators.get(i - 1) == Operators.MINUS) {
                    result = notOperator(temp, getWordLocation(operands.get(i)));
                }
                temp = result;
            }
            System.out.println(result);
            return result;
        }
    }

    public static ArrayList<String> getWordLocation(String word) {
        ArrayList<String> result = new ArrayList<>();
        if(!indirectIndex.containsKey(word)) {
            return result;
        }
        HashMap<String, Integer> wordLocationHM = indirectIndex.get(word);
        for (Map.Entry<String, Integer> entry : wordLocationHM.entrySet()) {
            String k = entry.getKey();
            result.add(k);
        }
        return result;
    }

    public static ArrayList<String> orOperator(ArrayList<String> firstWord, ArrayList<String> secondWord) {
        ArrayList<String> result;
        if(firstWord.size() > secondWord.size()) {
            result = firstWord;
            for(String word : secondWord) {
                if(!result.contains(word))
                    result.add(word);
            }
        } else {
            result = secondWord;
            for (String word : firstWord) {
                if(!result.contains(word))
                    result.add(word);
            }
        }
        return  result;
    }

    public static ArrayList<String> andOperator(ArrayList<String> firstWord, ArrayList<String> secondWord) {
        ArrayList<String> result = new ArrayList<>();
        if(firstWord.size() < secondWord.size()) {
            for(String word : firstWord) {
                if(secondWord.contains(word)) {
                    result.add(word);
                }
            }
        } else {
            for(String word : secondWord) {
                if(firstWord.contains(word))
                    result.add(word);
            }
        }
        return  result;
    }

    public static ArrayList<String> notOperator(ArrayList<String> firstWord, ArrayList<String> secondWord) {
        for(String word : secondWord) {
            firstWord.remove(word);
        }
        return firstWord;
    }
}
