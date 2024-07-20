import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class StockTradingPlatform extends Application {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Stock> stockMarket = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        seedStockMarket();

        primaryStage.setTitle("Stock Trading Platform");

        // Registration Form
        VBox registerBox = new VBox(10);
        registerBox.setPadding(new Insets(10));
        TextField registerUsername = new TextField();
        PasswordField registerPassword = new PasswordField();
        Button registerButton = new Button("Register");
        registerBox.getChildren().addAll(new Label("Register"), new Label("Username:"), registerUsername, new Label("Password:"), registerPassword, registerButton);

        // Login Form
        VBox loginBox = new VBox(10);
        loginBox.setPadding(new Insets(10));
        TextField loginUsername = new TextField();
        PasswordField loginPassword = new PasswordField();
        Button loginButton = new Button("Login");
        loginBox.getChildren().addAll(new Label("Login"), new Label("Username:"), loginUsername, new Label("Password:"), loginPassword, loginButton);

        // Main Layout
        HBox mainLayout = new HBox(20, registerBox, loginBox);
        mainLayout.setPadding(new Insets(20));
        Scene mainScene = new Scene(mainLayout, 600, 300);

        primaryStage.setScene(mainScene);
        primaryStage.show();

        registerButton.setOnAction(e -> {
            String username = registerUsername.getText();
            String password = registerPassword.getText();
            if (users.containsKey(username)) {
                showAlert(Alert.AlertType.ERROR, "Registration Error", "Username already exists.");
            } else {
                users.put(username, new User(username, password));
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully.");
            }
        });

        loginButton.setOnAction(e -> {
            String username = loginUsername.getText();
            String password = loginPassword.getText();
            User user = users.get(username);
            if (user != null && user.getPassword().equals(password)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username);
                showUserMenu(primaryStage, user);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid credentials.");
            }
        });
    }

    private void seedStockMarket() {
        stockMarket.put("AAPL", new Stock("AAPL", 150.0));
        stockMarket.put("GOOG", new Stock("GOOG", 2800.0));
        stockMarket.put("AMZN", new Stock("AMZN", 3400.0));
    }

    private void showUserMenu(Stage primaryStage, User user) {
        primaryStage.setTitle("Stock Trading Platform - " + user.getUsername());

        // Buy Stock Form
        VBox buyBox = new VBox(10);
        buyBox.setPadding(new Insets(10));
        TextField buySymbol = new TextField();
        TextField buyQuantity = new TextField();
        Button buyButton = new Button("Buy");
        buyBox.getChildren().addAll(new Label("Buy Stock"), new Label("Symbol:"), buySymbol, new Label("Quantity:"), buyQuantity, buyButton);

        // Sell Stock Form
        VBox sellBox = new VBox(10);
        sellBox.setPadding(new Insets(10));
        TextField sellSymbol = new TextField();
        TextField sellQuantity = new TextField();
        Button sellButton = new Button("Sell");
        sellBox.getChildren().addAll(new Label("Sell Stock"), new Label("Symbol:"), sellSymbol, new Label("Quantity:"), sellQuantity, sellButton);

        // Portfolio Display
        VBox portfolioBox = new VBox(10);
        portfolioBox.setPadding(new Insets(10));
        Button viewPortfolioButton = new Button("View Portfolio");
        portfolioBox.getChildren().addAll(new Label("Portfolio"), viewPortfolioButton);

        // User Menu Layout
        HBox userMenuLayout = new HBox(20, buyBox, sellBox, portfolioBox);
        userMenuLayout.setPadding(new Insets(20));
        Scene userMenuScene = new Scene(userMenuLayout, 800, 400);

        primaryStage.setScene(userMenuScene);
        primaryStage.show();

        buyButton.setOnAction(e -> {
            String symbol = buySymbol.getText();
            int quantity = Integer.parseInt(buyQuantity.getText());
            Stock stock = stockMarket.get(symbol);
            if (stock != null) {
                user.buyStock(stock, quantity);
                showAlert(Alert.AlertType.INFORMATION, "Stock Bought", "Bought " + quantity + " shares of " + symbol);
            } else {
                showAlert(Alert.AlertType.ERROR, "Buy Error", "Stock not found.");
            }
        });

        sellButton.setOnAction(e -> {
            String symbol = sellSymbol.getText();
            int quantity = Integer.parseInt(sellQuantity.getText());
            if (user.sellStock(symbol, quantity)) {
                showAlert(Alert.AlertType.INFORMATION, "Stock Sold", "Sold " + quantity + " shares of " + symbol);
            } else {
                showAlert(Alert.AlertType.ERROR, "Sell Error", "Not enough stock to sell.");
            }
        });

        viewPortfolioButton.setOnAction(e -> {
            StringBuilder portfolio = new StringBuilder("Your Portfolio:\n");
            for (StockHolding holding : user.getPortfolio().values()) {
                portfolio.append(holding).append("\n");
            }
            showAlert(Alert.AlertType.INFORMATION, "Portfolio", portfolio.toString());
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

class User {
    private final String username;
    private final String password;
    private final Map<String, StockHolding> portfolio = new HashMap<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void buyStock(Stock stock, int quantity) {
        StockHolding holding = portfolio.get(stock.getSymbol());
        if (holding == null) {
            holding = new StockHolding(stock, quantity);
        } else {
            holding.addQuantity(quantity);
        }
        portfolio.put(stock.getSymbol(), holding);
    }

    public boolean sellStock(String symbol, int quantity) {
        StockHolding holding = portfolio.get(symbol);
        if (holding != null && holding.getQuantity() >= quantity) {
            holding.removeQuantity(quantity);
            if (holding.getQuantity() == 0) {
                portfolio.remove(symbol);
            }
            return true;
        }
        return false;
    }

    public Map<String, StockHolding> getPortfolio() {
        return portfolio;
    }
}

class Stock {
    private final String symbol;
    private final double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }
}

class StockHolding {
    private final Stock stock;
    private int quantity;

    public StockHolding(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void removeQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Stock: " + stock.getSymbol() + ", Quantity: " + quantity + ", Buy Price: " + stock.getPrice();
    }
}
