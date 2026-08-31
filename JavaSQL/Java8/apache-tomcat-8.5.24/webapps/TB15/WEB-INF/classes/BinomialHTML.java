public class BinomialHTML {
    public static double combi(int N,int n){
        double Nfact=1;
        for (int i=1;i<=N;i++){
            Nfact*=i;
        }
        double nfact=1;
        for (int i=1;i<=n;i++){
            nfact*=i;
        }
        int x=N-n;
        double fact=1;
        for (int i=1;i<=x;i++){
            fact*=i;
        }
        double sol=(Nfact)/((nfact)*(fact));
        return sol;
    }
    public static String masa(double p,int n,int k){
        String out = " ";
        out+=("<html>");
        out+=("<body>");
        int aux=n-k;
        double q = 1-p;
        double ans=((BinomialHTML.combi(n,k))*(Math.pow(p,k))*(Math.pow(q,aux)));
        out += ("<html><h1>Soluci&oacute;n</h1>");
        out +=("<p><li> Probabilidad de X="+k+ ": <b>" + ans+"</b><p>");
        out+=("</body>");
        out+=("</html>");
        return out;
    }
    public static String distr(double p,int n,int k){
        String out = " ";
        out+=("<html>");
        out+=("<body>");
        double q = 1-p;
        double ans=0;
        for(int i=0;i<=k;i++){
            int aux=n-i;
            ans=ans+((BinomialHTML.combi(n,i))*(Math.pow(p,i))*(Math.pow(q,aux)));
        }
        out +=("<p><li> Probabilidad acumulada de X<="+k+ ": <b>" + ans+"</b><p>");
        out += ("<br>");
        out += ("<p><a href='index.html'>Volver a la p&aacute;gina principal</a></p></html>");
        out += ("<p><a href='Binomial.html'>Volver a calcular</a></p></html>");
        out+=("</body>");
        out+=("</html>");
        return out;
    }
}