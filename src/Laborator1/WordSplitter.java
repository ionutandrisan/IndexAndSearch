package Laborator1;

import PorterStemming.Stemmer;

import java.io.IOException;
import java.util.HashMap;

public class WordSplitter {

    public static HashMap<String, Integer> splitWords(String text, HashMap<String, Integer> exceptionsHM,
                                                      HashMap<String, Integer> stopWordsHM) {
        char[] letters = text.toCharArray();
        StringBuilder word = new StringBuilder();
        HashMap<String, Integer> hm = new HashMap();
        for (char letter : letters) {
            if (!Character.isLetter(letter) && !Character.isDigit(letter)) {
                if (!word.toString().equals("")) {
                    if (exceptionsHM.containsKey(word.toString())) {
                        if (!hm.containsKey(word.toString())) {
                            hm.put(word.toString(), 1);
                        } else {
                            hm.replace(word.toString(), hm.get(word.toString()) + 1);
                        }
                    } else {
                        if (!stopWordsHM.containsKey(word.toString())) {
                            Stemmer s = new Stemmer(word);
                            s.stem();
                            if (!hm.containsKey(s.toString())) {
                                hm.put(s.toString(), 1);
                            } else {
                                hm.replace(s.toString(), hm.get(s.toString()) + 1);
                            }
                        }
                    }
                }
                word = new StringBuilder();
            } else {
                letter = Character.toLowerCase(letter);
                word.append(letter);
            }
        }
        return hm;
    }

}
