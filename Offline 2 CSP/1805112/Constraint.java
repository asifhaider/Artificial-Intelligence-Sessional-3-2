import java.util.ArrayList;
import java.util.HashMap;

// constraints can be either row or column-wise
public class Constraint {
    private ArrayList<Variable> scope;

    public Constraint() {
        this.scope = new ArrayList<Variable>();
    }

    public void addScope(Variable v){
        this.scope.add(v);
    }

    public ArrayList<Variable> getScope() {
        return scope;
    }

    public boolean holds(HashMap<Variable, Integer> map, Variable var){
        for(Variable v: this.scope){
            if(!v.equals(var)){
                if(map.get(v)==map.get(var))
                    return false;
            } else{
                System.out.println("We are same!");
            }
        }
        return true;
    }
}
