import java.util.ArrayList;

public class CourseNode {
    // course id for each node
    private String courseID;
    // how many students are enrolled in particular course
    private int enrollment;
    // allocated time slot serial number
    private int allocation;
    // adjacency list of the graph nodes, overlapping/clashing courses having common students
    private ArrayList<CourseNode> adjacentCourses;
    private int dfsState;   // 0 => not visited (white), 1 => in stack (gray), 2 => visited (black)

    public CourseNode(String id, int students){
        this.courseID = id;
        this.enrollment = students;
        this.allocation = -1; // invalid graph coloring by default
        this.adjacentCourses = new ArrayList<>();
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public int getDfsState() {
        return dfsState;
    }

    public void setDfsState(int dfsState) {
        this.dfsState = dfsState;
    }
    public int getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(int enrollment) {
        this.enrollment = enrollment;
    }

    public int getAllocation() {
        return allocation;
    }

    public void setAllocation(int allocation) {
        this.allocation = allocation;
    }

    public CourseNode[] getAdjacentCourses() {
        CourseNode[] courses = new CourseNode[adjacentCourses.size()];
        for(int i=0; i<courses.length; i++){
            courses[i] = adjacentCourses.get(i);
        }
        return courses;
    }

    public void setAdjacentCourses(ArrayList<CourseNode> adjacentCourses) {
        this.adjacentCourses = adjacentCourses;
    }

    public void addEdges(CourseNode c){
        this.adjacentCourses.add(c);
    }

    @Override
    public String toString() {
        return "CourseNode{" +
                "courseID='" + courseID + '\'' +
                ", enrollment=" + enrollment + '\'' +
                ", conflicts=" + adjacentCourses.size() +
                '}';
    }
}
