import java.util.ArrayList;
import java.util.Collections;

// variable order heuristic class
public class Heuristic {
    public Variable getNextVariable(CSP csp, String vah){
        switch (vah){
            case "vah1":
                return this.vah1(csp);
            case "vah5":
                return this.vah5(csp);
            default:
                return null;
        }
    }

    public Variable vah1(CSP csp){
        int leastDomain = Integer.MAX_VALUE;
        int chosenIndex = 0;

        // finding variable with the smallest domain
        ArrayList<Variable> vars = csp.getVariables();

        for(int i=0; i<vars.size(); i++){
            Variable v = vars.get(i);
            if(v.getDomain().size() <= leastDomain){
                leastDomain = v.getDomain().size();
                System.out.println("Domain size " + leastDomain);
                chosenIndex = i;
            }
        }
        return vars.get(chosenIndex);
    }

    public Variable vah5(CSP csp){
        // random shuffling done out of the current array list, not in the original array list itself
        ArrayList<Variable> shuffled = csp.getVariables();
        Collections.shuffle(shuffled);
        return shuffled.get(0);
    }
}
