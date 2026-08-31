public class CombinacionHTML {
    public static String normal(int N,int n){
        String out = " ";
        out += "<html><h1>Soluci&oacute;n</h1>";
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
        int ans = (int) sol;
        out += "<p>Con "+N+" elementos tomados de " + n + " en " + n + " se pueden formar: <b>" +ans+"</b> grupos distintos.</p>";
        out += ("<br>");
        out += "<p><a href='index.html'>Volver a la p&aacute;gina principal</a></p></html>";
        out += "<p><a href='Combinacion.html'>Volver a calcular</a></p></html>";
        return out;
    }
    public static String repeticion(int N,int n){
        String out = " ";
        out += "<html><h1>Soluci&oacute;n</h1>";
        double Nfact=1;
        int x=N+n-1;
        for (int i=1;i<=x;i++){
            Nfact*=i;
        }
        double nfact=1;
        for (int i=1;i<=n;i++){
            nfact*=i;
        }
        int y=N-1;
        double fact=1;
        for (int i=1;i<=y;i++){
            fact*=i;
        }
        double sol=(Nfact)/((nfact)*(fact));
        int ans = (int) sol;
        out += "<p>Con "+N+" elementos tomados de " + n + " en " + n + " con repetici&oacute;n se pueden formar: <b>" +ans+"</b> grupos distintos.</p>";
        out += "<p><a href='index.html'>Volver a la p&aacute;gina principal</a></p></html>";
        out += "<p><a href='Combinacion.html'>Volver a calcular</a></p></html>";
        return out;
    }
}