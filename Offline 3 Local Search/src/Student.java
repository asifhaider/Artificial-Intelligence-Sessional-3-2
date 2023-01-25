import java.util.ArrayList;
import java.util.Collections;

public class Student {
    private ArrayList<CourseNode> enrolledCourses;

    public Student() {
        this.enrolledCourses = new ArrayList<>();
    }

    public ArrayList<CourseNode> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(ArrayList<CourseNode> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void addCourse(CourseNode course) {
        this.enrolledCourses.add(course);
    }

    public double calculateExponentialPenalty(){
        double penalty = 0;
        Collections.sort(enrolledCourses, new ScheduleComparator());

        for(int i=0; i<enrolledCourses.size()-1; i++){
            for(int j=i+1; j<enrolledCourses.size(); j++){
                int gap = enrolledCourses.get(j).getAllocation()-enrolledCourses.get(i).getAllocation();
                if(gap <= 5)
                    penalty += Math.pow(2, 5-gap);
            }
        }
        return penalty;
    }

    public double calculateLinearPenalty(){
        double penalty = 0;
        Collections.sort(enrolledCourses, new ScheduleComparator());

        for(int i=0; i<enrolledCourses.size()-1; i++){
            for(int j=i+1; j<enrolledCourses.size(); j++){
                int gap = enrolledCourses.get(j).getAllocation()-enrolledCourses.get(i).getAllocation();
                if(gap <= 5)
                    penalty += (2*(5-gap));
            }
        }
        return penalty;
    }

    @Override
    public String toString() {
        return "Student{" +
                "enrolledCourses=" + enrolledCourses.size() +
                '}';
    }
}