import java.util.Comparator;

public class DegreeComparator implements Comparator<CourseNode> {
    @Override
    public int compare(CourseNode o1, CourseNode o2) {
        return o2.getAdjacentCourses().length - o1.getAdjacentCourses().length;
    }
}