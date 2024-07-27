import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
public class Variable {
    private ArrayList<Integer> domain;

    private Position position;
//    private int value;

    // O(1)
    public Variable(int x, int y) {
        this.domain = new ArrayList<Integer>();
        // ascending order
//        for(int i=1; i<=10; i++){
//            domain.add(i);
//        }

        // randomly generated
        while(domain.size()<10) {
            Random random = new Random();
            int val = random.nextInt(11);
//            System.out.println(val);
            if (domain.contains(val))
                continue;
            else
                domain.add(val);
        }

        this.position = new Position(x, y);
//        this.value = 0;
    }

    public ArrayList<Integer> getDomain() {
        return domain;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return position.equals(variable.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    //
    public void reduceDomain(int val){
        this.domain.remove(Integer.valueOf(val));
    }

    @Override
    public String toString() {
        return "Variable{" +
                "position=" + position +
                '}';
    }

    //    public int getValue() {
//        return value;
//    }
}