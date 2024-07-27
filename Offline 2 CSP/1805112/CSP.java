import java.util.ArrayList;
import java.util.HashMap;

public class CSP {
    private int length;
    private ArrayList<Variable> variables;

    // first n entries will be row constraint and later n entries will be column constraint
    private ArrayList<Constraint> constraints;

    public CSP(int n) {
        this.length = n;
        this.variables = new ArrayList<Variable>();
        this.constraints = new ArrayList<Constraint>();
        for (int i=0; i<2*n; i++){
            this.constraints.add(new Constraint());
        }
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public void addVariable(Variable v){
        this.variables.add(v);
    }

    public void updateConstraint(int index, Variable v){
        this.constraints.get(index).addScope(v);
    }

    public boolean isConsistent(HashMap<Variable, Integer> map, Variable var, int val){
        if(this.getConstraints().get(var.getPosition().getRow()).holds(map, var)
        && this.getConstraints().get(var.getPosition().getCol()+this.length).holds(map, var)){
            return true;
        }
        return false;
    }
}