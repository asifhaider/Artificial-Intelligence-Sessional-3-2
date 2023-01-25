import java.util.Comparator;

public class ScheduleComparator implements Comparator<CourseNode> {
    @Override
    public int compare(CourseNode o1, CourseNode o2) {
        return o1.getAllocation() - o2.getAllocation();
    }
}