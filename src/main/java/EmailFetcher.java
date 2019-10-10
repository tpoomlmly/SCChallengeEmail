import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import static java.lang.System.exit;
import static java.lang.System.out;
import static java.lang.System.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EmailFetcher {
    public static void main(String[] args) {
        String ID = readID();
        String URL = genURL(ID);
        String name = parsePage(URL);
        boolean inSoton = name != null;
        if(!inSoton) {
            name = ID;
        }
        out.println("# " + name); out.println();

        //out.println(inSoton ? name : "This person doesn't exist or doesn't have a page.");
    }

    private static String readID() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        out.print("Enter username:  ");
        String id = "";
        try {
            id = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
        return id;
    }

    private static String genURL(String username) {
        String userID = username.split("@")[0];
        String encodedID = null;
        try {
            encodedID = URLEncoder.encode(userID, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            exit(1);
        }
        return "https://www.ecs.soton.ac.uk/people/".concat(encodedID);
    }

    private static String parsePage(String URL) {
        String name;
        Document page = null;
        try {
            page = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
        Elements nameElements = page.getElementsByAttributeValue("property", "name");
        if(nameElements.size() < 1) {
            name = null;
        } else {
            name = nameElements.first().text();
        }
        return name;
    }
}