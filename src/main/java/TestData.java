import java.io.IOException;

class TestData extends HtmlItem {

    @HtmlContent(key = "test")
    public String getContent(){
        return "Test-Content";
    }

    @Override
    public String templateName() {
        return "test";
    }

    public static void main(String[] args) {
        TestData td = new TestData();

        Creator creator = new Creator();
        try {
            creator.writeHtml("test", td);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
