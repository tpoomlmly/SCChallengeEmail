import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.lang.System.exit;

public class Person {

    private String username;
    private String name;
    private String email;
    private String webpage;
    private String description;
    private boolean inSoton;

    private Person() {}

    public static String getNameFromUsername(String username) {
        String encodedID = null;
        try {
            encodedID = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            exit(1);
        }
        String URL = "https://www.ecs.soton.ac.uk/people/".concat(encodedID);

    }

    public static Person parseSotonPage(@NotNull Document page) {
        String
        // Get name
        Elements nameElements = page.getElementsByAttributeValue("property", "name");
        String name;
        if(nameElements.size() < 1) {
            name = null;
        } else {
            name = nameElements.first().text();
        }

        // Get email address
        Elements emailElements = page.getElementsByAttributeValue("property", "email");

        return new Person().setName(name);
    }

    public Person setName(String newName) {
        this.name = newName;
        return this;
    }

    public Person setEmail(String newEmail) {
        this.email = newEmail;
        return this;
    }

    public Person setWebpage(String newWebpage) {
        this.webpage = newWebpage;
        return this;
    }

    public Person setUsername(String newUsername) {
        this.username = newUsername;
        return this;
    }

    private void setDescription(String newDescription) {
        this.description = newDescription;
    }
}
