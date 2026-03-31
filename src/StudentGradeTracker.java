import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeTracker {
    static ArrayList<Student> students = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      STUDENT GRADE TRACKER v1.0      ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;
        while (running){
            printMenu();
            int choice = readInt("Enter Choice: ");
            switch (choice){
                case 1 -> addStudent();
                case 2 -> addGradeToStudent();
                case 3 -> viewAllStudents();
                case 4 -> viewStudentDetails();
                case 5 -> summaryReport();
                case 6 -> removeStudent();
                case 0 -> {
                    running = false;
                    System.out.println("\nGoodBye!");
                }
                default -> System.out.println(" [!] Invalid option. Try again.\n");
            }
        }
        scanner.close();
    }

    static void printMenu() {
        System.out.println("\n┌──────────────────────────────────┐");
        System.out.println("│             MAIN MENU            │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1. Add Student                  │");
        System.out.println("│  2. Add Grade to Student         │");
        System.out.println("│  3. View All Students            │");
        System.out.println("│  4. View Student Detail          │");
        System.out.println("│  5. Print Summary Report         │");
        System.out.println("│  6. Remove Student               │");
        System.out.println("│  0. Exit                         │");
        System.out.println("└──────────────────────────────────┘");
    }

    // 1 -> Add Student
    static void addStudent(){
        System.out.println("\n  Enter student name: ");
        String name = scanner.nextLine().trim();
        if(name.isEmpty()){
            System.out.println("[!] Name cannot be empty.");
            return;
        }
        students.add(new Student(name));
        System.out.println("  [✓] Student \"" + name + "\" added.");
        System.out.print("  Add grades now? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            addGradesFor(students.get(students.size() - 1));
        }
    }

    // 2 -> Add Grades
    static void addGradeToStudent(){
        Student s = selectStudent();
        if(s == null){
            return;
        }
        addGradesFor(s);
    }

    static void addGradesFor(Student s){
        System.out.println("Enter grades for "+ s.getName() + "(type 'done' to stop)");
        while(true){
            System.out.println("Grade : ");
            String input = scanner.nextLine().trim();
            if(input.equalsIgnoreCase("done")){
                break;
            }
            try {
                double grade = Double.parseDouble(input);
                if(grade < 0 || grade > 100){
                    System.out.println(" [!] Grade must be between 0 and 100.");
                } else {
                    s.addGrade(grade);
                    System.out.println("[✓] Grade " + grade + " added.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Enter a number or 'done'.");
            }
        }
    }

    // 3 -> View All Students
    static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("\n  No students recorded yet.");
            return;
        }
        System.out.println("\n  ┌────┬─────────────────────┬─────────┬────────┬────────┬───────┐");
        System.out.println("  │ #  │ Name                │ Average │  High  │  Low   │ Grade │");
        System.out.println("  ├────┼─────────────────────┼─────────┼────────┼────────┼───────┤");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("  │ %-2d │ %-19s │  %5.1f  │  %5.1f │  %5.1f │   %s   │%n",
                    i + 1, s.getName(), s.getAverage(),
                    s.getHighest(), s.getLowest(), s.getLetterGrades());
        }
        System.out.println("  └────┴─────────────────────┴─────────┴────────┴────────┴───────┘");
    }

    // 4 -> View Student Details
    static void viewStudentDetails(){
        Student s = selectStudent();
        if(s == null){
            return;
        }

        System.out.println("\n  ╔══════════════════════════════════╗");
        System.out.printf ("  ║  Student: %-22s ║%n", s.getName());
        System.out.println("  ╠══════════════════════════════════╣");

        ArrayList<Double> grades = s.getGrades();
        if(s.getGrades().isEmpty()){
            System.out.println("  ║  No grades recorded.             ║");
        }
        else {
            System.out.print("  ║  Grades : ");
            StringBuilder sb = new StringBuilder();
            for(double g : grades){
                sb.append(g).append(" ");
            }
            System.out.printf("%-23s║%n", sb.toString().trim());
            System.out.printf("  ║  Average: %-5.1f                    ║%n", s.getAverage());
            System.out.printf("  ║  Highest: %-5.1f                    ║%n", s.getHighest());
            System.out.printf("  ║  Lowest : %-5.1f                    ║%n", s.getLowest());
            System.out.printf("  ║  Grade  : %-5s                    ║%n", s.getLetterGrades());
            System.out.println("  ║  Progress: " + progressBar(s.getAverage()) + "         ║");
        }
        System.out.println("  ╚══════════════════════════════════╝");
    }

    // 5 -> Summary Report
    static void summaryReport() {
        if (students.isEmpty()) {
            System.out.println("\n  No data to report.");
            return;
        }

        System.out.println("\n  ════════════════════════════════════");
        System.out.println("         CLASS SUMMARY REPORT");
        System.out.println("  ════════════════════════════════════");

        double totalAvg = 0;
        Student topStudent = students.get(0);
        Student lowStudent = students.get(0);
        int[] gradeCounts = new int[5]; // A B C D F

        for (Student s : students) {
            double avg = s.getAverage();
            totalAvg += avg;
            if (avg > topStudent.getAverage()) topStudent = s;
            if (avg < lowStudent.getAverage()) lowStudent = s;
            switch (s.getLetterGrades()) {
                case "A" -> gradeCounts[0]++;
                case "B" -> gradeCounts[1]++;
                case "C" -> gradeCounts[2]++;
                case "D" -> gradeCounts[3]++;
                case "F" -> gradeCounts[4]++;
            }
        }

        double classAvg = totalAvg / students.size();

        System.out.printf("  Total Students : %d%n", students.size());
        System.out.printf("  Class Average  : %.2f %%%n", classAvg);
        System.out.printf("  Top Performer  : %s (%.1f)%n", topStudent.getName(), topStudent.getAverage());
        System.out.printf("  Needs Support  : %s (%.1f)%n", lowStudent.getName(), lowStudent.getAverage());

        System.out.println("\n  Grade Distribution:");
        String[] labels = {"A (90-100)", "B (80-89)", "C (70-79)", "D (60-69)", "F (0-59)"};
        for (int i = 0; i < 5; i++) {
            System.out.printf("    %-12s: %s (%d)%n",
                    labels[i], "█".repeat(gradeCounts[i] * 3), gradeCounts[i]);
        }

        System.out.println("\n  Full Roster:");
        viewAllStudents();
        System.out.println("  ════════════════════════════════════");
    }

    // 6 -> Remove Student
    static void removeStudent() {
        Student s = selectStudent();
        if (s == null) return;
        System.out.print("  Remove \"" + s.getName() + "\"? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            students.remove(s);
            System.out.println("  [✓] Student removed.");
        }
    }

    //Helpers
    static Student selectStudent() {
        if (students.isEmpty()) {
            System.out.println("\n  No students found. Add one first.");
            return null;
        }
        System.out.println("\n  Select a student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + students.get(i).getName());
        }
        int idx = readInt("  Enter number: ") - 1;
        if (idx < 0 || idx >= students.size()) {
            System.out.println("  [!] Invalid selection.");
            return null;
        }
        return students.get(idx);
    }

    static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static String progressBar(double avg) {
        int filled = (int) (avg / 10);
        return "█".repeat(filled) + "░".repeat(10 - filled) + " " + String.format("%.0f%%", avg);
    }
}
