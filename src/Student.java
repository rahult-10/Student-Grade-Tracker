import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

public class Student {

    private String name;
    private ArrayList<Double> grades;

    public Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public void addGrade(double grade){
        grades.add(grade);
    }

    public String getName(){
        return name;
    }

    public ArrayList<Double> getGrades() {
        return grades;
    }

    public double getAverage(){
        if(grades.isEmpty()){
            return 0.0;
        }
        double sum = 0;
        for (double g: grades){
            sum += g;
        }
        return sum/grades.size();
    }

    public double getHighest(){
        if(grades.isEmpty()){
            return 0.0;
        }
        return Collections.max(grades);
    }

    public double getLowest(){
        if(grades.isEmpty()){
            return 0.0;
        }
        return Collections.min(grades);
    }

    public String getLetterGrades(){
        double avg = getAverage();
        if(avg >= 90){
            return "A";
        }
        if(avg >= 80){
            return "B";
        }
        if(avg >= 70){
            return "C";
        }
        if(avg >= 60){
            return "D";
        }
        return "F";
    }
}
