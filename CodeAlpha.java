import java.util.ArrayList;
import java.util.Scanner;

public class StudentGrades {
    public static void main(String[] args) {
        ArrayList<Integer> grades = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nWelcome to the Student Grades System");
            System.out.println("1. Add Student Grades");
            System.out.println("2. Calculate Average Grade");
            System.out.println("3. Find Highest Grade");
            System.out.println("4. Find Lowest Grade");
            System.out.println("5. Exit");
            System.out.print("Please choose an option (1-5): ");
            
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }
            
            switch (choice) {
                case 1:
                    addGrades(grades, scanner);
                    break;
                case 2:
                    if (grades.isEmpty()) {
                        System.out.println("No grades entered yet.");
                    } else {
                        double average = getAverageGrade(grades);
                        System.out.println("Average grade: " + average);
                    }
                    break;
                case 3:
                    if (grades.isEmpty()) {
                        System.out.println("No grades entered yet.");
                    } else {
                        int highest = getHighestGrade(grades);
                        System.out.println("Highest grade: " + highest);
                    }
                    break;
                case 4:
                    if (grades.isEmpty()) {
                        System.out.println("No grades entered yet.");
                    } else {
                        int lowest = getLowestGrade(grades);
                        System.out.println("Lowest grade: " + lowest);
                    }
                    break;
                case 5:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    public static void addGrades(ArrayList<Integer> grades, Scanner scanner) {
        System.out.print("Enter the number of students: ");
        int numberOfStudents;
        try {
            numberOfStudents = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        for (int i = 0; i < numberOfStudents; i++) {
            System.out.print("Enter the grade for student " + (i + 1) + ": ");
            int grade;
            try {
                grade = Integer.parseInt(scanner.nextLine());
                if (grade < 0 || grade > 100) {
                    System.out.println("Please enter a valid grade between 0 and 100.");
                    i--; // Repeat this iteration for a valid input
                } else {
                    grades.add(grade);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                i--; // Repeat this iteration for a valid input
            }
        }
    }

    public static int getHighestGrade(ArrayList<Integer> grades) {
        int highest = grades.get(0);
        for (int grade : grades) {
            if (grade > highest) {
                highest = grade;
            }
        }
        return highest;
    }

    public static int getLowestGrade(ArrayList<Integer> grades) {
        int lowest = grades.get(0);
        for (int grade : grades) {
            if (grade < lowest) {
                lowest = grade;
            }
        }
        return lowest;
    }

    public static double getAverageGrade(ArrayList<Integer> grades) {
        int sum = 0;
        for (int grade : grades) {
            sum += grade;
        }
        return (double) sum / grades.size();
    }
}
