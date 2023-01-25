import java.util.Comparator;

public class EnrollmentComparator implements Comparator<CourseNode> {

    @Override
    public int compare(CourseNode o1, CourseNode o2) {
        return o2.getEnrollment() - o1.getEnrollment();
    }
}