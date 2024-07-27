import java.rmi.activation.ActivationDesc;
import java.util.HashMap;

public class CSP_Solver {
    private CSP csp;
    private HashMap<Variable, Integer> assignment;
    private Heuristic heuristic = new Heuristic();

    public CSP_Solver(CSP csp, HashMap<Variable, Integer> assignment) {
        this.csp = csp;
        this.assignment = assignment;

    }

    public HashMap<Variable, Integer> solve(Solver solver, VariableOrderHeuristic vah) {
        HashMap<Variable, Integer> solution = null;
        if(solver.equals(Solver.BACKTRACK)){
            solution = this.BackTrack(this.csp, this.assignment, vah);
        } else if(solver.equals(Solver.FORWARD_CHECK)){
            solution = this.ForwardCheck(this.csp, this.assignment, vah);
        }
        return solution;
    }

    public HashMap<Variable, Integer> BackTrack(CSP csp, HashMap<Variable, Integer>assignment, VariableOrderHeuristic vah){

        if(this.isComplete(assignment))
            return assignment;

        Variable v = heuristic.getNextVariable(csp, String.valueOf(vah));
        System.out.println(v.toString());

        // random value order heuristics by default
        for (int val: v.getDomain()){
            assignment.replace(v, val);
            if(csp.isConsistent(assignment, v, val)){
                v.reduceDomain(val);
                for(Variable var: csp.getConstraints().get(v.getPosition().getRow()).getScope()){
                    var.reduceDomain(val);
                }

                showAssignment(assignment);
                return BackTrack(csp, assignment, vah);
            }
            assignment.replace(v, 0);
        }
        return null;
    }

    public HashMap<Variable, Integer> ForwardCheck(CSP csp, HashMap<Variable, Integer> assignment, VariableOrderHeuristic vah){
        return null;
    }

    public boolean isComplete(HashMap<Variable, Integer>map){
        for(Variable v: map.keySet()){
            if(map.get(v)==0){
                return false;
            }
        }
        return true;
    }

    public void showAssignment(HashMap<Variable, Integer> map){
        for(Variable var:map.keySet()){
            System.out.print(var.getPosition().toString() + ": " + map.get(var) + ", ");
        }
        System.out.println("\n");
    }

//    public int ValueOrderHeuristic(Variable v){
//        return v.getDomain().get(0);
//    }
}
