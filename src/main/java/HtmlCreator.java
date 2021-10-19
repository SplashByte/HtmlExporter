

import lombok.experimental.Delegate;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class HtmlCreator {

    /**
     * The templates should be stored in the subfolder "html-template"
     * The html-files will be stored in the subfolder "html"
     */
    public HtmlCreator() {
        this("html/","html-template/");
    }

    /**
     * Paths should end with a "/"
     * Example: html-dir: "html/" template-dir: "templates/html/"
     * @param htmlDir The directory where the html-files should be stored
     * @param templateDir The directory where templates are stored
     */
    public HtmlCreator(String htmlDir, String templateDir) {
        this(new HtmlCreatorConfig(templateDir,htmlDir));
    }

    /**
     * @param config Configuration with the directory where templates are stored and the html-files should be stored
     */
    public HtmlCreator(HtmlCreatorConfig config){
        this.config = config;
    }

    @Delegate
    private final HtmlCreatorConfig config;

    /**
     * Building the html
     * @param name The name of the html that should be created
     * @param rootItem The root-item of the html
     * @throws IOException If the file can't be written this Exception will be thrown
     */
    public void buildHtml(String name, HtmlItem rootItem) throws IOException {

        String htmlPath = config.getHtmlDir() + name + ".html";

        String htmlString = "";
        htmlString = rootItem.buildContent(config, name);


        PrintWriter writer = new PrintWriter(htmlPath, StandardCharsets.UTF_8);
        writer.print(htmlString);
        writer.close();

    }
}
