package CODEalpha;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class StudentGrades extends Application {

    private ArrayList<Double> grades = new ArrayList<>();
    private TextArea resultArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Grades");

        Label instructionLabel = new Label("Enter grades for the students (type -1 to stop):");
        TextField gradeField = new TextField();
        gradeField.setPromptText("Enter grade");

        Button addButton = new Button("Add Grade");
        Button computeButton = new Button("Compute Statistics");

        resultArea = new TextArea();
        resultArea.setEditable(false);

        addButton.setOnAction(e -> handleAddGrade(gradeField));
        computeButton.setOnAction(e -> handleComputeStatistics());

        VBox vbox = new VBox(10, instructionLabel, gradeField, addButton, computeButton, resultArea);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleAddGrade(TextField gradeField) {
        try {
            double grade = Double.parseDouble(gradeField.getText());
            if (grade == -1) {
                return; // Stop adding grades
            }
            if (grade < 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a positive grade.");
            } else {
                grades.add(grade);
                resultArea.appendText("Added grade: " + grade + "\n");
                gradeField.clear();
                gradeField.requestFocus();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid number.");
        }
    }

    private void handleComputeStatistics() {
        if (grades.isEmpty()) {
            resultArea.setText("No grades entered.");
            return;
        }

        double average = computeAverage(grades);
        double highest = findHighest(grades);
        double lowest = findLowest(grades);

        resultArea.setText(String.format("Grades:\n%s%n%nAverage grade: %.2f%nHighest grade: %.2f%nLowest grade: %.2f",
                grades, average, highest, lowest));
    }

    private double computeAverage(ArrayList<Double> grades) {
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }

    private double findHighest(ArrayList<Double> grades) {
        double highest = grades.get(0);
        for (double grade : grades) {
            if (grade > highest) {
                highest = grade;
            }
        }
        return highest;
    }

    private double findLowest(ArrayList<Double> grades) {
        double lowest = grades.get(0);
        for (double grade : grades) {
            if (grade < lowest) {
                lowest = grade;
            }
        }
        return lowest;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
