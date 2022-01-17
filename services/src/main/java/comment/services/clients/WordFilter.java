package comment.services.clients;

import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class WordFilter {

    public boolean hasBadWords(String text){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://phonetic-bad-word-filter.p.rapidapi.com/PhoneticCheck"))
                    .header("content-type", "application/json")
                    .header("x-rapidapi-host", "phonetic-bad-word-filter.p.rapidapi.com")
                    .header("x-rapidapi-key", "715bad8c7dmsh72628d62a50ab07p1ba6bdjsnbd8c7bfff9fb")
                    .method("POST", HttpRequest.BodyPublishers.ofString("{" +
                            "\t\"phrase\": \"" + text + "\"," +
                            "\"blacklist\": [" +
                            "\t\t\"fuck\"," +
                            "\t\t\"shit\"," +
                            "\t\t\"hell\"" +
                            "\t]" +
                            "}"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject a = new JSONObject(response.body());
            System.out.println("Word filter status (1 means clean):" + a.getInt("code"));

            if(a.getInt("code")==1){
                return false;
            }else{
                return true;
            }

        } catch(Exception e){

            System.out.println(e);
        }

        return true;

    }

}
