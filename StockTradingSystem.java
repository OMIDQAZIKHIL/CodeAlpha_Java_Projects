package StockTradingSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class StockTradingSystem extends Application {

    private final Map<String, Double> marketData = new HashMap<>();
    private final Map<String, Integer> portfolio = new HashMap<>();
    private final TextArea marketArea = new TextArea();
    private final TextArea portfolioArea = new TextArea();
    private final TextField stockField = new TextField();
    private final TextField quantityField = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Stock Trading System");

        // Seed market data
        seedMarketData();

        // UI Components
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label marketLabel = new Label("Market Data:");
        marketArea.setEditable(false);
        updateMarketDataDisplay();

        Label buySellLabel = new Label("Buy/Sell Stocks:");
        stockField.setPromptText("Stock Symbol");
        quantityField.setPromptText("Quantity");

        Button buyButton = new Button("Buy");
        Button sellButton = new Button("Sell");

        buyButton.setOnAction(e -> handleBuy());
        sellButton.setOnAction(e -> handleSell());

        Label portfolioLabel = new Label("Your Portfolio:");
        portfolioArea.setEditable(false);
        updatePortfolioDisplay();

        // Layout for buying and selling
        GridPane buySellPane = new GridPane();
        buySellPane.setHgap(10);
        buySellPane.setVgap(10);
        buySellPane.setPadding(new Insets(10, 0, 10, 0));
        buySellPane.add(new Label("Stock Symbol:"), 0, 0);
        buySellPane.add(stockField, 1, 0);
        buySellPane.add(new Label("Quantity:"), 0, 1);
        buySellPane.add(quantityField, 1, 1);
        buySellPane.add(buyButton, 0, 2);
        buySellPane.add(sellButton, 1, 2);

        // Adding components to the VBox
        vbox.getChildren().addAll(marketLabel, marketArea, buySellLabel, buySellPane, portfolioLabel, portfolioArea);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void seedMarketData() {
        marketData.put("AAPL", 150.0);
        marketData.put("GOOGL", 2800.0);
        marketData.put("AMZN", 3400.0);
        marketData.put("MSFT", 300.0);
    }

    private void updateMarketDataDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : marketData.entrySet()) {
            sb.append(entry.getKey()).append(": $").append(entry.getValue()).append("\n");
        }
        marketArea.setText(sb.toString());
    }

    private void updatePortfolioDisplay() {
        StringBuilder sb = new StringBuilder();
        double totalValue = 0.0;
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            double price = marketData.getOrDefault(symbol, 0.0);
            double value = quantity * price;
            totalValue += value;
            sb.append(symbol).append(": ").append(quantity).append(" shares, Value: $").append(value).append("\n");
        }
        sb.append("\nTotal Portfolio Value: $").append(totalValue);
        portfolioArea.setText(sb.toString());
    }

    private void handleBuy() {
        String symbol = stockField.getText().toUpperCase();
        String quantityText = quantityField.getText();
        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be positive.");
                return;
            }
            if (!marketData.containsKey(symbol)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Stock Symbol", "Stock symbol does not exist.");
                return;
            }
            portfolio.put(symbol, portfolio.getOrDefault(symbol, 0) + quantity);
            updatePortfolioDisplay();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a valid number.");
        }
        stockField.clear();
        quantityField.clear();
    }

    private void handleSell() {
        String symbol = stockField.getText().toUpperCase();
        String quantityText = quantityField.getText();
        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be positive.");
                return;
            }
            if (!portfolio.containsKey(symbol) || portfolio.get(symbol) < quantity) {
                showAlert(Alert.AlertType.ERROR, "Insufficient Shares", "Not enough shares to sell.");
                return;
            }
            portfolio.put(symbol, portfolio.get(symbol) - quantity);
            if (portfolio.get(symbol) == 0) {
                portfolio.remove(symbol);
            }
            updatePortfolioDisplay();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a valid number.");
        }
        stockField.clear();
        quantityField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
