
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class HtmlItem {

    private static final Class<?>[] expectedParameters = {HtmlCreatorConfig.class, String.class};

    /**
     * Building the content of the item based on the template and the valid methods
     * @param config Configuration of the HtmlCreator
     * @param name The name of the file that will be created
     * @return The content of the item as a html
     * @see #getValidMethods()
     * @see #getTemplate(String)
     */
    public final String buildContent(@NotNull HtmlCreatorConfig config, String name){

        String template = getTemplate(config.getTemplateDir());

        Map<String, String> replacement = getReplacements(config,name);

        return replaceMap(template,replacement);
    }


    /**
     * @return A {@link Stream<Method>} of all methods of the class
     */
    private @NotNull Stream<Method> getMethods(){
        Class<? extends HtmlItem> objectClass = this.getClass();
        return Arrays.stream(objectClass.getMethods());
    }

    /**
     * Validating a given {@link Method}.
     * A method is valid if it has the {@link HtmlContent} annotation,
     * parameters that match {@link HtmlCreatorConfig} and {@link String}
     * and returns a {@link String}
     * @param method The {@link Method} that should be validated
     * @return If the {@link Method} matches the requirements
     */
    private boolean validateMethod(@NotNull Method method){
        return (method.isAnnotationPresent(HtmlContent.class) &&
                Arrays.equals(method.getParameterTypes(), expectedParameters) &&
                method.getReturnType() == String.class&&
                !Modifier.isStatic(method.getModifiers())
        );
    }

    /**
     * Validates and streams the {@link Method} of the object
     * @return A {@link Stream<Method>} of all {@link Method} that are valid
     * @see #validateMethod(Method)
     */
    private Stream<Method> getValidMethods(){
        return getMethods().filter(this::validateMethod);
    }

    /**
     * Generate and collect all replacements that need to be done.
     * Every key defined by a {@link HtmlContent} will be mapped to the output of the corresponding {@link Method}
     * @param config The config of the {@link HtmlCreator}
     * @param name The name of the file that will be created
     * @return A {@link Map} of all replacements
     */
    private Map<String, String> getReplacements(HtmlCreatorConfig config, String name){
        return getValidMethods().collect(Collectors.toMap(
                        method -> method.getAnnotation(HtmlContent.class).key(),
                        method -> {
                            try {
                                return method.invoke(this, config, name).toString();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return "Exception:" + e.getMessage();
                            }
                        }
                )
        );
    }

    /**
     * Replacing every key with each corresponding values
     * @param s The starting-point for the replacement
     * @param replacementEntry The key (or more specifically the String "$key$") of the entry will get replaced with the value
     * @return The input with the replacement made
     */
    private @NotNull String replaceEntry(@NotNull String s, Map.@NotNull Entry<String,String> replacementEntry){
        return s.replace("$" + replacementEntry.getKey() + "$", replacementEntry.getValue());
    }

    /**
     * Replaces all keys with the corresponding values in a String
     * @param s The starting-point for the replacements
     * @param map All keys in this map will be replaced
     * @return The input with all replacements made
     */
    private String replaceMap(String s, @NotNull Map<String, String> map){
        for (Map.Entry<String, String> entry : map.entrySet())
            s = replaceEntry(s,entry);
        return s;
    }

    /**
     * This Method will be used to receive the name of the template to load it from the file-system
     * Override this Method to set the name of the template for your custom item
     * @return The name of the template.
     */
    public abstract String templateName();

    /**
     * Getting the template from the file-system
     * @param directory The directory in wich the template-file is stored
     * @return The content of the template-file. An IOException will result in an empty String
     */
    private String getTemplate(String directory) {
        try {
            return Files.readString(Paths.get(directory + templateName() + ".html"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
