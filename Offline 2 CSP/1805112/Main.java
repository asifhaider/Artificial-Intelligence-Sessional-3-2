import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

enum Solver{
    BACKTRACK, FORWARD_CHECK;
}

enum VariableOrderHeuristic{
    vah1, vah2, vah3, vah4, vah5;
}
public class Main {
    // to store initial latin square
    private static int [][] square;
    private static CSP_Solver solver;
    private static HashMap<Variable, Integer> assignment = new HashMap<Variable, Integer>();
    public static void main(String[] args) {
        System.out.println("Welcome to Latin Square Solver!");
        File file = new File("E:\\BUET 3-2\\CSE 318\\Offline 2 CSP\\Latin Square Fillup\\data\\test.txt");
        try {
            Scanner sc = new Scanner(file);
            String firstLine = sc.nextLine();
            String degree = firstLine.substring(2, firstLine.length()-1);
//                System.out.println(degree);
            int n = Integer.parseInt(degree);
            square = new int[n][n];
            CSP csp = new CSP(n);

            sc.nextLine(); sc.nextLine();
            try{
                for(int i=0; i<n; i++)
                {
                    String[] inputs = sc.nextLine().split(", ");
                    for(int j=0; j<n; j++)
                    {
                        String val = inputs[j];
                        if(j==n-1 && i==n-1){
                            val = val.substring(0, inputs[j].length()-3);
                        } else if (j==n-1){
                            val = val.substring(0, inputs[j].length()-1);
                        }
                        System.out.println(val);
                        square[i][j] = Integer.parseInt(val.trim());
//                        System.out.println(square[i][j]);
                    }
                }

                // Creating a new CSP from the input matrix


                for(int i=0; i<n; i++){
                    for(int j=0; j<n; j++){
                        if(square[i][j]==0){
                            Variable v = new Variable(i,j);
                            assignment.put(v, 0);
                            csp.addVariable(v);
                            csp.updateConstraint(i, v);
                            csp.updateConstraint(n+j, v);
                        } else{

                        }
                    }
                }

                for(int i=0; i<n; i++){
                    for(int j=0; j<n; j++){
                        if(square[i][j]!=0){
                            for(Variable v: csp.getConstraints().get(i).getScope()){
                                v.reduceDomain(square[i][j]);
                            }
                            for(Variable v: csp.getConstraints().get(j+n).getScope()){
                                v.reduceDomain(square[i][j]);
                            }
                        }
                    }
                }

                CSP_Solver solver = new CSP_Solver(csp, assignment);
                HashMap<Variable, Integer> solution = solver.solve(Solver.BACKTRACK, VariableOrderHeuristic.vah1);

            } catch (Exception e){
                e.printStackTrace();
                return;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }
}