

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Creator {

    Creator() {
        htmlDir = "html/";
        templateDir = "html-template/";
    }

    public Creator(String htmlDir, String templateDir) {
        this.htmlDir = htmlDir;
        this.templateDir = templateDir;
    }

    private final String htmlDir;
    private final String templateDir;

    public void writeHtml(String name, HtmlItem rootItem) throws IOException {

        String htmlPath = htmlDir + name + ".html";

        String htmlString = "";
        htmlString = rootItem.buildHtml(this, name);


        PrintWriter writer = new PrintWriter(htmlPath, StandardCharsets.UTF_8);
        writer.print(htmlString);
        writer.close();

    }

    public String getHtmlDir() {
        return htmlDir;
    }

    public String getTemplateDir() {
        return templateDir;
    }
}
