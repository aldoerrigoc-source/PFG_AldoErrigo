public class PoissonHTML {
    public static String masa(double u,double k){
        String out = " " ;
        double kfact=1;
        for (int i=1;i<=k;i++){
            kfact*=i;
        }
        double ans = (Math.exp(-u)*Math.pow(u,k))/(kfact);
        out += ("<html><h1>Solución</h1>");
        int aux=(int)k;
        out +=("<p><li> Probabilidad de X="+aux+ ": <b>" + ans+"</b><p>");
        out+=("</html>");
        return out;
    }
    public static String distr(double u,double k){
        String out = " " ;
        double ans=0;
        for (int i=0;i<=k;i++){
            double ifact=1;
            for (int j=1;j<=i;j++){
                ifact*=j;
            }
            ans = ans + (Math.exp(-u)*Math.pow(u,i))/(ifact);
        }
        out+=("<html>");
        int aux=(int)k;
        out +=("<p><li> Probabilidad acumulada de X<="+aux+ ": <b>" + ans+"</b><p>");
        out += ("<br>");
        out += ("<p><a href='index.html'>Volver a la p&aacute;gina principal</a></p></html>");
        out += ("<p><a href='Poisson.html'>Volver a calcular</a></p></html>");
        out+=("</html>");
        return out;
    }
}