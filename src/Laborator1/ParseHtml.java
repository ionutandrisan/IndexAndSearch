package Laborator1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ParseHtml {

    public void parseHtml(String path) throws IOException {
        File fileName = new File(path);
        Document document = Jsoup.parse(fileName, "UTF-8");

        if ((document).title() != null) {
            System.out.println("Title: " + document.title());
        }

        String metaKeywords = document.select("meta[name=keywords]").first().attr("content");
        System.out.println("Keywords: " + metaKeywords);

        String metaDescription = document.select("meta[name=description]").first().attr("content");
        System.out.println("Description: " + metaDescription);

        //String metaRobots = document.select("meta[name=robots]").first().attr("content");
        //System.out.println("Robots: " + metaRobots);


        Element link = document.select("a[abs:href]").first();
        System.out.println("Primul link: " + link.attr("abs:href"));

        Element body = document.body();
        System.out.println("Text: " + body.text() + "\n");
    }

    static public void parseToDocument(String path, String url) throws IOException {
        Document doc;
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(path);
        try {
            doc = (Document) Jsoup.connect(url).get();
            fileWriter.write(String.valueOf(doc));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

