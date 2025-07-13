import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Weather {

    private static final String API_KEY = "a34e186311dfb6f745af453fae605666";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city = scanner.nextLine().trim();

        try {
            String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String body = response.body();

                System.out.println("\n=== Weather Report for " + city + " ===");
                printField(body, "\"temp\":", "Temperature", "°C");
                printField(body, "\"feels_like\":", "Feels Like", "°C");
                printField(body, "\"humidity\":", "Humidity", "%");
                printField(body, "\"pressure\":", "Pressure", "hPa");
                printStringField(body, "\"main\":\"", "Weather");
                System.out.println("=====================================");
            } else {
                System.out.println("Error: " + response.statusCode() + " - " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Request failed: " + e.getMessage());
        }

        scanner.close();
    }

    private static void printField(String json, String key, String label, String unit) {
        int start = json.indexOf(key);
        if (start != -1) {
            start += key.length();
            int end = json.indexOf(",", start);
            String value = json.substring(start, end).trim();
            System.out.println(label + ": " + value + " " + unit);
        }
    }

    private static void printStringField(String json, String key, String label) {
        int start = json.indexOf(key);
        if (start != -1) {
            start += key.length();
            int end = json.indexOf("\"", start);
            String value = json.substring(start, end);
            System.out.println(label + ": " + value);
        }
    }
}