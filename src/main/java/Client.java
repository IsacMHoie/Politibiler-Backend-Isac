import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Client {
   private static final HttpClient client = HttpClient.newHttpClient();
   private static final ObjectMapper mapper = new ObjectMapper();



    static PolitiBilListe hentPolitibiler(){

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://erikrolspo.github.io/politibiler.json"))
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            String json = response.body();
            return mapper.readValue(json, PolitiBilListe.class);

        } catch (IOException e) {
            System.err.println("Request failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Request was interrupted");
        }

        return null;
    }


}
