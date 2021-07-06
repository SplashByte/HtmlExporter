import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class HtmlItem {

    abstract String templateName();

    public final String buildHtml(Creator creator, String dir) throws IOException {

        Class<? extends HtmlItem> objectClass = this.getClass();

        String result = getTemplate(creator);
        Map<String, String> replacement = Arrays.stream(objectClass.getDeclaredMethods())
                .filter(method ->
                        method.isAnnotationPresent(HtmlContent.class) &&
                        method.getParameterTypes().length == 0 &&
                        method.getReturnType() == String.class)
                .collect(Collectors.toMap(
                        method -> method.getAnnotation(HtmlContent.class).key(),
                        method -> {
                            try {
                                return method.invoke(this).toString();
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                                return "Exception:" + e.getMessage();
                            }
                        }
                        )
                );

        for (Map.Entry<String, String> entry :
                replacement.entrySet())
            result = result.replace("$"+entry.getKey()+"$", entry.getValue());

        return result;
    }

    private String getTemplate(Creator creator) {

        String result = "";
        try {
            result = Files.readString(Paths.get(creator.getTemplateDir() + templateName() + ".html"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }
}
