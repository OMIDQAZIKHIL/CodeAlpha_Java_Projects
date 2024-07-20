import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Room {
    private int roomNumber;
    private String category;
    private double price;
    private boolean isAvailable;

    public Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isAvailable = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                '}';
    }
}

class Reservation {
    private static int idCounter = 1;
    private int reservationId;
    private User user;
    private Room room;
    private Date startDate;
    private Date endDate;

    public Reservation(User user, Room room, Date startDate, Date endDate) {
        this.reservationId = idCounter++;
        this.user = user;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getReservationId() {
        return reservationId;
    }

    public User getUser() {
        return user;
    }

    public Room getRoom() {
        return room;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", user=" + user +
                ", room=" + room +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

class Payment {
    private static int idCounter = 1;
    private int paymentId;
    private Reservation reservation;
    private double amount;
    private Date paymentDate;

    public Payment(Reservation reservation, double amount) {
        this.paymentId = idCounter++;
        this.reservation = reservation;
        this.amount = amount;
        this.paymentDate = new Date();
    }

    public int getPaymentId() {
        return paymentId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public double getAmount() {
        return amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", reservation=" + reservation +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                '}';
    }
}

public class HotelReservationSystem extends Application {
    private static List<Room> rooms = new ArrayList<>();
    private static List<Reservation> reservations = new ArrayList<>();
    private static List<Payment> payments = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        seedRooms();

        primaryStage.setTitle("Hotel Reservation System");

        TabPane tabPane = new TabPane();

        Tab searchTab = new Tab("Search Rooms");
        searchTab.setContent(createSearchTabContent());
        searchTab.setClosable(false);

        Tab reservationTab = new Tab("Make Reservation");
        reservationTab.setContent(createReservationTabContent());
        reservationTab.setClosable(false);

        Tab viewTab = new Tab("View Booking Details");
        viewTab.setContent(createViewTabContent());
        viewTab.setClosable(false);

        tabPane.getTabs().addAll(searchTab, reservationTab, viewTab);

        Scene scene = new Scene(tabPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void seedRooms() {
        rooms.add(new Room(101, "Single", 100));
        rooms.add(new Room(102, "Double", 150));
        rooms.add(new Room(103, "Suite", 300));
    }

    private VBox createSearchTabContent() {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        Label categoryLabel = new Label("Enter room category (Single/Double/Suite):");
        TextField categoryField = new TextField();

        Button searchButton = new Button("Search");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        searchButton.setOnAction(event -> {
            String category = categoryField.getText();
            StringBuilder results = new StringBuilder("Available rooms:\n");
            for (Room room : rooms) {
                if (room.getCategory().equalsIgnoreCase(category) && room.isAvailable()) {
                    results.append(room).append("\n");
                }
            }
            resultArea.setText(results.toString());
        });

        vbox.getChildren().addAll(categoryLabel, categoryField, searchButton, resultArea);
        return vbox;
    }

    private VBox createReservationTabContent() {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        Label nameLabel = new Label("Enter your name:");
        TextField nameField = new TextField();

        Label emailLabel = new Label("Enter your email:");
        TextField emailField = new TextField();

        Label roomNumberLabel = new Label("Enter room number:");
        TextField roomNumberField = new TextField();

        Label startDateLabel = new Label("Enter start date (yyyy-mm-dd):");
        TextField startDateField = new TextField();

        Label endDateLabel = new Label("Enter end date (yyyy-mm-dd):");
        TextField endDateField = new TextField();

        Label amountLabel = new Label("Enter payment amount:");
        TextField amountField = new TextField();

        Button reserveButton = new Button("Reserve");

        reserveButton.setOnAction(event -> {
            String name = nameField.getText();
            String email = emailField.getText();
            User user = new User(name, email);

            int roomNumber = Integer.parseInt(roomNumberField.getText());
            Room room = findRoomByNumber(roomNumber);
            if (room == null || !room.isAvailable()) {
                showAlert(Alert.AlertType.ERROR, "Room not available.");
                return;
            }

            String startDateStr = startDateField.getText();
            String endDateStr = endDateField.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate;
            Date endDate;
            try {
                startDate = sdf.parse(startDateStr);
                endDate = sdf.parse(endDateStr);
            } catch (ParseException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid date format.");
                return;
            }

            Reservation reservation = new Reservation(user, room, startDate, endDate);
            reservations.add(reservation);
            room.setAvailable(false);

            double amount = Double.parseDouble(amountField.getText());
            Payment payment = new Payment(reservation, amount);
            payments.add(payment);

            showAlert(Alert.AlertType.INFORMATION, "Reservation successful.");
        });

        vbox.getChildren().addAll(nameLabel, nameField, emailLabel, emailField,
                roomNumberLabel, roomNumberField, startDateLabel, startDateField,
                endDateLabel, endDateField, amountLabel, amountField, reserveButton);
        return vbox;
    }

    private VBox createViewTabContent() {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        Label reservationIdLabel = new Label("Enter reservation ID:");
        TextField reservationIdField = new TextField();

        Button viewButton = new Button("View");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        viewButton.setOnAction(event -> {
            int reservationId = Integer.parseInt(reservationIdField.getText());
            for (Reservation reservation : reservations) {
                if (reservation.getReservationId() == reservationId) {
                    resultArea.setText(reservation.toString());
                    return;
                }
            }
            resultArea.setText("Reservation not found.");
        });

        vbox.getChildren().addAll(reservationIdLabel, reservationIdField, viewButton, resultArea);
        return vbox;
    }

    private Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
