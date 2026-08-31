public class GeometricaHTML {
        
    public static String masa(double p,int k){
        String out = " ";
        out+=("<html>");
        out+=("<body>");
        
        double q = 1-p;
        double ans=((Math.pow(q,k))*p);
        out +=(ans);
        out+=("</body>");
        out+=("</html>");
        return out;
    }
    public static String distr(double p,int k){
        String out = " ";
        out+=("<html>");
        out+=("<body>");
        double q = 1-p;
		double aux= k+1;
        double ans=(1-(Math.pow(q,aux)));
        out +=(ans);
        out+=("</body>");
        out+=("</html>");
        return out;
    }
}
