import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.lang.System.exit;

class Person {

    String name;
    String email;
    String webpage;
    String description;
    boolean inSoton;

    Person(String protoname) {
        String encodedID = null;
        try {
            encodedID = URLEncoder.encode(protoname, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            exit(1);
        }
        // Try Southampton first
        String URL = "https://www.ecs.soton.ac.uk/people/".concat(encodedID);
        Document sotonPage;
        try {
            sotonPage = Jsoup.connect(URL).get();
        } catch (IOException e) {
            sotonPage = null;
            e.printStackTrace();
            exit(1);
        }
        if(sotonPage.getElementsByAttributeValue("property", "name").size() == 1) { // They are in Southampton
            this.parseSotonPage(sotonPage);
            this.setWebpage(URL);
        } else {
            // Google search
            Document searchResults;
            try {
                searchResults = Jsoup.connect("https://www.google.co.uk/search").data("q", encodedID).get();
            } catch (IOException e) {
                searchResults = null;
                e.printStackTrace();
                exit(1);
            }
            this.setName(protoname);
            this.setInSoton(false);
            this.parseSearchResults(searchResults);
        }
    }

    private void parseSotonPage(Document page) {
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
        String email;
        if(emailElements.size() < 1) {
            email = null;
        } else {
            email = emailElements.first().text();
        }

        // Get description
        Elements descElements = page.getElementsByAttributeValue("property", "description");
        if(descElements.size() == 1) {
            this.setDescription(descElements.first().previousElementSibling().text()); // The real description is the element above this one
        }

        this.setName(name);
        this.setEmail(email);
        this.setInSoton(true);
    }

    private void parseSearchResults(Document page) {
        Elements descriptionElements = page.getElementsByClass("kno-rdesc");
        if(descriptionElements.size() == 1) {
            this.setDescription(descriptionElements.first().child(0).child(1).text());
        }
        Elements srgClassElements = page.getElementsByClass("srg");
        if(srgClassElements.size() > 0) {
            String firstLink = srgClassElements.first().getElementsByTag("a").first().attr("href");
            Document sotonPage;
            try {
                sotonPage = Jsoup.connect(firstLink).get();
            } catch (IOException e) {
                sotonPage = null;
                e.printStackTrace();
                exit(1);
            }
            if(sotonPage.getElementsByAttributeValue("property", "name").size() == 1) { // They are in Southampton
                this.parseSotonPage(sotonPage);
                this.setWebpage(firstLink);
                this.setInSoton(true);
            }
        }
    }

    private void setName(String newName) {
        this.name = newName;
    }

    private void setEmail(String newEmail) {
        this.email = newEmail;
    }

    private void setWebpage(String newWebpage) {
        this.webpage = newWebpage;
    }

    private void setDescription(String newDescription) {
        this.description = newDescription;
    }

    private void setInSoton(boolean inSoton) {
        this.inSoton = inSoton;
    }
}
