import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    // the graph coloring logic
    public static int scheduleCourses(ArrayList<CourseNode> courses) {
        int totalSlotNeeded = 0;
        // traverse for all courses already influenced by a certain heuristic
        for (int i = 0; i < courses.size(); i++) {
            CourseNode[] overlapping = courses.get(i).getAdjacentCourses();
            int[] occupiedSlots = new int[overlapping.length];  // time slots occupied by neighbors
            for (int j = 0; j < occupiedSlots.length; j++) {
                occupiedSlots[j] = overlapping[j].getAllocation();
            }
            Arrays.sort(occupiedSlots);

            int probableTimeSlot = 0;
            for (int j = 0; j < occupiedSlots.length; j++) {
                // if already colored
                if (occupiedSlots[j] != -1) {
                    // color matched with my current pick
                    if (probableTimeSlot == occupiedSlots[j]) {
                        // move to next pick
                        probableTimeSlot++;
                    }
                    // color available to fill up
                    if (probableTimeSlot < occupiedSlots[j]) {
                        courses.get(i).setAllocation(probableTimeSlot);
                    }
                }
            }

            // if color not set yet, but already greater than the largest occupied one
            if (courses.get(i).getAllocation() == -1) {
                if (probableTimeSlot == totalSlotNeeded) {
                    // largest available time is the total number of color so far
                    courses.get(i).setAllocation(totalSlotNeeded++);
                } else {
                    // largest available time slot is free to be assigned yet
                    courses.get(i).setAllocation(probableTimeSlot);
                }
            }
        }
        return totalSlotNeeded;
    }

    public static int scheduleByLargestDegree(ArrayList<CourseNode> courses) {
        Collections.sort(courses, new DegreeComparator());
        return scheduleCourses(courses);
    }

    // DSatur algorithm
    public static int scheduleBySaturationDegree(ArrayList<CourseNode> courses) {
        Collections.sort(courses, new DegreeComparator());
        courses.get(0).setAllocation(0);

        // starting from the 2nd slot
        int totalTimeSlot = 1;
        for (int i = 1; i < courses.size(); i++) {
            HashSet<Integer> temp, selected = null;
            int maxSaturationDegree = -1, maxIndex = -1;

            for (int j = 0; j < courses.size(); j++) {
                // if unallocated
                if (courses.get(j).getAllocation() == -1) {
                    temp = new HashSet<>();

                    CourseNode[] overlapping = courses.get(j).getAdjacentCourses();
                    for (int k = 0; k < overlapping.length; k++) {
                        // allocated colors of neighbors are stored without duplication
                        if (overlapping[k].getAllocation() != -1)
                            temp.add(overlapping[k].getAllocation());
                    }

                    // fixing already seen maximum saturation degree (with tie breaking) and the node for which it occurs
                    if (temp.size() > maxSaturationDegree ||
                            (temp.size() == maxSaturationDegree && courses.get(j).getAdjacentCourses().length > courses.get(maxIndex).getAdjacentCourses().length)) {
                        maxSaturationDegree = temp.size();
                        maxIndex = j;
                        selected = temp;
                    }
                }
            }

            int probableTimeSlot = 0;
            while (courses.get(maxIndex).getAllocation() == -1) {
                if (!selected.contains(probableTimeSlot)) {
                    courses.get(maxIndex).setAllocation(probableTimeSlot);
                    if (probableTimeSlot == totalTimeSlot)
                        totalTimeSlot++;
                } else
                    probableTimeSlot++;
            }
        }
        return totalTimeSlot;
    }

    public static int scheduleByLargestEnrollment(ArrayList<CourseNode> courses){
        Collections.sort(courses, new EnrollmentComparator());
        return scheduleCourses(courses);
    }

    public static int scheduleRandomly(ArrayList<CourseNode> courses){
        Collections.shuffle(courses);
        return scheduleCourses(courses);
    }

    // average penalty for perturbation heuristic
    public static double calculateAveragePenalty(ArrayList<Student> students, boolean exponentialPentalty){
        double averagePenalty = 0;
        for(int i=0; i<students.size(); i++){
            if(exponentialPentalty)
                averagePenalty += students.get(i).calculateExponentialPenalty();
            else
                averagePenalty += students.get(i).calculateLinearPenalty();
        }
        averagePenalty /= students.size();
        return averagePenalty;
    }

    // specialized depth first search routine for finding kempe chain
    public static void doTraversal(CourseNode current, int timeSlot){
        current.setDfsState(1); // visiting in stack/gray
        CourseNode[] overlaps = current.getAdjacentCourses();
        for(int i=0; i<overlaps.length; i++){
            // if neighbor not visited and neighbor has same color
            if(overlaps[i].getDfsState() == 0 && overlaps[i].getAllocation()==timeSlot){
                doTraversal(overlaps[i], current.getAllocation());
            }
        }

        current.setDfsState(2); // traversal done/black
        return;
    }

    public static void performKempeChainInterchange(ArrayList<CourseNode> courses, ArrayList<Student> students, boolean exp, CourseNode current, int neighborSlot){
        doTraversal(current, neighborSlot);
        // kempe chain formed

        double currentPenalty = calculateAveragePenalty(students, exp);
        int currentTimeSlot = current.getAllocation();

        // start interchanging
        for(int i=0; i<courses.size(); i++){
            // already visited nodes
            if(courses.get(i).getDfsState() == 2){
                // color swapping
                if(courses.get(i).getAllocation() == currentTimeSlot){
                    courses.get(i).setAllocation(neighborSlot);
                } else {
                    courses.get(i).setAllocation(currentTimeSlot);
                }
            }
        }

        if(currentPenalty <= calculateAveragePenalty(students, exp)){
            for(int i=0; i<courses.size(); i++){
                // kempe chain swap unsuccessful, undo changes
                if(courses.get(i).getAllocation()==2){
                    if(courses.get(i).getAllocation() == currentTimeSlot)
                        courses.get(i).setAllocation(neighborSlot);
                    else
                        courses.get(i).setAllocation(currentTimeSlot);
                }
            }
        }

        // marking not visited again for future reuse
        for(int i=0; i<courses.size(); i++){
            if(courses.get(i).getDfsState()==2)
                courses.get(i).setDfsState(0);
        }
        return;
    }

    public static void performPairSwap(ArrayList<Student>students, boolean exp, CourseNode c1, CourseNode c2){
        int c1TimeSlot = c1.getAllocation();
        int c2TimeSlot = c2.getAllocation();

        // pair swap not possible
        if(c1TimeSlot == c2TimeSlot)
            return;

        // check if u and v are mutually non-adjacent
        CourseNode[] overlaps = c1.getAdjacentCourses();
        for(int i=0; i<overlaps.length; i++){
            if(overlaps[i].getAllocation() == c2.getAllocation())
                return;
        }
        overlaps = c2.getAdjacentCourses();
        for(int i=0; i<overlaps.length; i++){
            if(overlaps[i].getAllocation() == c1.getAllocation())
                return;
        }

        // perform pair swap
        double currentPenalty = calculateAveragePenalty(students, exp);
        c1.setAllocation(c2.getAllocation());
        c2.setAllocation(c1.getAllocation());

        if(currentPenalty <= calculateAveragePenalty(students, exp)){
            // undo pair swap
            c1.setAllocation(c1TimeSlot);
            c2.setAllocation(c2TimeSlot);
        }
        return;
    }

    public static void performPenaltyReduction(ArrayList<CourseNode> courses, ArrayList<Student> students, boolean exp, boolean doKempeChainExchange){
        Random random = new Random(5);
        for(int i=0; i<1000; i++){
            if(doKempeChainExchange){
                // getting a random node
                int current = random.nextInt(courses.size());
                CourseNode[] overlaps = courses.get(current).getAdjacentCourses();
                if(overlaps.length!=0){
                    // takes a random neighbour and apply kempe chain interchange heuristic
                    performKempeChainInterchange(courses, students, exp, courses.get(current), overlaps[random.nextInt(overlaps.length)].getAllocation());
                }
            } else {
                // pick any random two nodes
                performPairSwap(students, exp, courses.get(random.nextInt(courses.size())), courses.get(random.nextInt(courses.size())));
            }
        }
        return;
    }
    public static void applySolverOnDataset(String filename) throws FileNotFoundException {
        ArrayList<CourseNode> courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();

        File file = new File("E:\\BUET 3-2\\CSE 318\\Offline 3 Local Search\\data\\" + filename + ".crs");
        Scanner scn = new Scanner(file);
        while (scn.hasNextLine()) {
            String[] inputLine = scn.nextLine().split(" ");
            if(inputLine.length != 2){
                System.out.println("input file data organization error");
                return;
            }
            courses.add(new CourseNode(inputLine[0], Integer.parseInt(inputLine[1])));
        }
        scn.close();

        file = new File("E:\\BUET 3-2\\CSE 318\\Offline 3 Local Search\\data\\" + filename + ".stu");
        scn = new Scanner(file);

        int [][] courseConflicts = new int[courses.size()][courses.size()];
        // populating with no conflicts (zeros)
        for(int i=0; i<courseConflicts.length; i++){
            for(int j=0; j<courseConflicts.length; j++){
                courseConflicts[i][j] = 0;
            }
        }

        int index = 0;  // student count
        while(scn.hasNextLine()){
            String[] inputFile = scn.nextLine().split(" ");
            students.add(index, new Student());
            for(int i=0; i<inputFile.length; i++){
                students.get(index).addCourse(courses.get(Integer.parseInt(inputFile[i])-1));
            }
            index++;

            for(int i=0; i<inputFile.length-1; i++){
                for(int j=i+1; j<inputFile.length; j++){
                    if(courseConflicts[Integer.parseInt(inputFile[i])-1][Integer.parseInt(inputFile[j])-1] == 0){
                        // updating if common students take one course (ones)
                        courseConflicts[Integer.parseInt(inputFile[i])-1][Integer.parseInt(inputFile[j])-1] = courseConflicts[Integer.parseInt(inputFile[j])-1][Integer.parseInt(inputFile[i])-1] = 1;
                    }
                }
            }
        }
        scn.close();

        // creating edges
        for(int i=0; i<courseConflicts.length; i++){
            for(int j=0; j<courseConflicts.length; j++){
                if(courseConflicts[i][j] == 1){
                    // if conflict found, add to the adjacency list of both nodes
                    courses.get(i).addEdges(courses.get(j));
                }
            }
        }

        System.out.println("Required Timeslot by Constructive Heuristic (Largest Degree): " +
                        scheduleByLargestDegree(courses));

//        System.out.println("Required Timeslot by Constructive Heuristic (Saturation Degree): " +
//                        scheduleBySaturationDegree(courses));

//        System.out.println("Required Timeslot by Constructive Heuristic (Largest Enrollment): " +
//                        scheduleByLargestEnrollment(courses));

//        System.out.println("Required Timeslot by Constructive Heuristic (Random Order): " +
//                        scheduleRandomly(courses));

        // Exponential Penalty
        // after constructive heuristic
        System.out.println("Exponential Penalty (after constructive heuristic): " + calculateAveragePenalty(students, true));
        performPenaltyReduction(courses, students, true,  true);
        // after kempe chain exchange
        System.out.println("Exponential Penalty (after kempe chain interchange heuristic): " + calculateAveragePenalty(students, true));
        performPenaltyReduction(courses, students, true,  false);
        // after pair swap
        System.out.println("Exponential Penalty (after pair swap operator heuristic): " + calculateAveragePenalty(students, true));

        // Linear Penalty
        // after constructive heuristic
//        System.out.println("Linear Penalty (after constructive heuristic): " + calculateAveragePenalty(students, false));
//        performPenaltyReduction(courses, students, true,  true);
//        // after kempe chain exchange
//        System.out.println("Linear Penalty (after kempe chain interchange heuristic): " + calculateAveragePenalty(students, false));
//        performPenaltyReduction(courses, students, true,  false);
//        // after pair swap
//        System.out.println("Linear Penalty (after pair swap operator heuristic): " + calculateAveragePenalty(students, false));

    }
    public static void main(String[] args) throws FileNotFoundException {
        applySolverOnDataset("car-f-92");
//        applySolverOnDataset("car-s-91");
//        applySolverOnDataset("kfu-s-93");
//        applySolverOnDataset("tre-s-92");
//        applySolverOnDataset("yor-f-83");
    }
}