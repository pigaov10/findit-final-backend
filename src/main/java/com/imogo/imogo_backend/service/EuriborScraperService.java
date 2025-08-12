package com.imogo.imogo_backend.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class EuriborScraperService {
    private double euribor6m = 0.0;
    private LocalDate lastUpdated = null;

    public Double fetchEuriborRate() {
        try {
            Document doc = Jsoup.connect("https://www.euribor-rates.eu/pt/taxas-euribor-actuais/").get();

            // Seleciona todas as linhas da tabela principal onde as taxas estão
            Elements rows = doc.select("table.table-bordered tbody tr");

            for (Element row : rows) {
                String term = row.select("td").first().text(); // Exemplo: "EURIBOR 6 meses"
                if (term.toLowerCase().contains("euribor 6 meses")) {
                    String rateText = row.select("td").get(1).text(); // Exemplo: "2,049%"
                    // Remove símbolo %, espaços e troca vírgula por ponto
                    rateText = rateText.replace("%", "").trim().replace(",", ".");
                    euribor6m = Double.parseDouble(rateText);
                    lastUpdated = LocalDate.now();
                    System.out.println("EURIBOR 6M atualizada: " + euribor6m + "%");
                    return euribor6m;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao buscar EURIBOR: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter taxa EURIBOR: " + e.getMessage());
        }
        return 2.08;
    }

    public double getEuribor6m() {
        return euribor6m;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }
}
