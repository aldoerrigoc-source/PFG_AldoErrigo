import java.util.*;
import Jama.*;

public class MatrixVector {
    public static Vector<Matrix> v = new Vector<Matrix>();

    public static String printMatrix(Matrix m) {
        String out = "";
        out += "<TABLE border=1>";
        for (int i=0; i< m.getRowDimension(); i++) {
            out += "<TR>";
            for (int j=0; j< m.getColumnDimension(); j++) {
                out += "<TD>" + m.get(i,j) + "</TD>";
            }
            out += "</TR>";
        }
        out += "</table>";
        return out;
    }

}