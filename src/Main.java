import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorDeMoedas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Digite o valor a ser convertido:");
            double valor = Double.parseDouble(scanner.nextLine());

            System.out.println("Digite a moeda de origem (ex: USD, BRL, EUR):");
            String moedaOrigem = scanner.nextLine().trim().toUpperCase();

            System.out.println("Digite a moeda de destino (ex: USD, BRL, EUR):");
            String moedaDestino = scanner.nextLine().trim().toUpperCase();

            double taxa = obterTaxaCambio(moedaOrigem, moedaDestino);
            double valorConvertido = valor * taxa;

            System.out.printf("%.2f %s equivalem a %.2f %s%n",
                    valor, moedaOrigem, valorConvertido, moedaDestino);

        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Certifique-se de digitar um número.");
        } catch (IOException e) {
            System.out.println("Erro ao obter taxa de câmbio: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static double obterTaxaCambio(String moedaOrigem, String moedaDestino) throws IOException {
        String apiKey = "8e41dc157d916c30b519cd11"; // Sua chave
        String urlStr = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + moedaOrigem + "/" + moedaDestino;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Resposta inválida da API: " + responseCode);
        }

        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder resposta = new StringBuilder();
        while (scanner.hasNext()) {
            resposta.append(scanner.nextLine());
        }
        scanner.close();

        JSONObject json = new JSONObject(resposta.toString());
        return json.getDouble("conversion_rate");
    }
}