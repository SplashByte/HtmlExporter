import lombok.Value;

@Value
/**
 * A Configuration for a {@link HtmlCreator}
 */
public class HtmlCreatorConfig {

    /**
     * Paths should end with a "/"
     * Example: html-dir: "html/" template-dir: "templates/html/"
     * @param htmlDir The directory where the html-files should be stored
     * @param templateDir The directory where templates are stored
     */
    public HtmlCreatorConfig(String templateDir, String htmlDir) {
        if(!templateDir.endsWith("/") || !htmlDir.endsWith("/"))
            throw new IllegalArgumentException("Badly formatted ");
        this.templateDir = templateDir;
        this.htmlDir = htmlDir;
    }

    String templateDir;
    String htmlDir;
}
