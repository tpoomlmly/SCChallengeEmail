import static java.lang.System.exit;
import static java.lang.System.out;
import static java.lang.System.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EmailFetcher {
    public static void main(String[] args) {
        String ID = readID();
        Person person = new Person(ID);
        out.println("# " + person.name); out.println();
        if(person.inSoton) {
            out.println("* " + person.email); out.println();
            out.println("**Related people**: " + person.webpage + "/related_people"); out.println();
        } else {
            out.println("* " + person.webpage); out.println();
        }
        if(person.description != null) {
            out.println(person.description); out.println();
        }
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
}