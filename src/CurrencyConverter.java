import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_KEY = "41e915c70e25ec139213fee6";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueConversion = true;

        while (continueConversion) {
            System.out.println("Elige una opción de conversión:");
            System.out.println("1) Dolar =>>  Peso argentino");
            System.out.println("2. Peso argentino =>> Dolar");
            System.out.println("3. Dolar =>> Real brasileño");
            System.out.println("4. Real brasileño =>> Dolar");
            System.out.println("5. Dolar =>> Peso colombiano");
            System.out.println("6. Peso colombiano =>> Dolar");
            System.out.println("7. Salir");

            System.out.println("Elija una opcion valida");
            System.out.println("******************************************************************");

            int option = scanner.nextInt();
            double amount = 0;
            String baseCurrency = "";
            String targetCurrency = "";

            switch (option) {
                case 1:
                    baseCurrency = "USD";
                    targetCurrency = "ARS";
                    System.out.print("Ingrese la cantidad en Dolar: ");
                    amount = scanner.nextDouble();
                    break;
                case 2:
                    baseCurrency = "ARS";
                    targetCurrency = "USD";
                    System.out.print("Ingrese la cantidad en Peso argentino: ");
                    amount = scanner.nextDouble();
                    break;
                case 3:
                    baseCurrency = "USD";
                    targetCurrency = "BRL";
                    System.out.print("Ingrese la cantidad en Dolar: ");
                    amount = scanner.nextDouble();
                    break;
                case 4:
                    baseCurrency = "BRL";
                    targetCurrency = "USD";
                    System.out.print("Ingrese la cantidad en Peso brasileño: ");
                    amount = scanner.nextDouble();
                    break;
                case 5:
                    baseCurrency = "USD";
                    targetCurrency = "COP";
                    System.out.print("Ingrese la cantidad en Dolar: ");
                    amount = scanner.nextDouble();
                    break;
                case 6:
                    baseCurrency = "COP";
                    targetCurrency = "USD";
                    System.out.print("Ingrese la cantidad en peso colombiano: ");
                    amount = scanner.nextDouble();
                    break;
                case 7:
                    continueConversion = false;
                    System.out.println("Saliendo del programa...");
                    continue;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
                    continue;
            }

            if (continueConversion) {
                try {
                    double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);
                    double convertedAmount = amount * exchangeRate;
                    System.out.printf("%.2f %s = %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

        scanner.close();
    }

    private static double getExchangeRate(String baseCurrency, String targetCurrency) throws Exception {
        String urlString = API_URL + baseCurrency;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            if (!jsonResponse.has("conversion_rates")) {
                throw new Exception("Respuesta inválida de la API.");
            }

            JsonObject conversionRates = jsonResponse.getAsJsonObject("conversion_rates");
            if (!conversionRates.has(targetCurrency)) {
                throw new Exception("Moneda objetivo inválida.");
            }

            return conversionRates.get(targetCurrency).getAsDouble();
        } else {
            throw new Exception("Código de error HTTP: " + responseCode);
        }
    }
}
