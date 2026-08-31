public class VariacionHTML {
	public static String normal(int N,int n){
		String out = " ";
		out += ("<html>");
        out +=("<h3>Solucion de la variacion:</h3>");
        int Nfact=1;
        for (int i=1;i<=N;i++){
            Nfact*=i;
        }
        int nfact=1;
        for (int i=1;i<=n;i++){
            nfact*=i;
        }
        int x=N-n;
        int fact=1;
        for (int i=1;i<=x;i++){
            fact*=i;
        }
        int sol=(Nfact)/((fact));
		out += ("<p>El numero total de variaciones que puede hacer es <b>" +sol+"</b></p>");
		out += ("<p><a href='index.html'>Volver a la p&aacute;gina principal</a></p>");
        out +=("</html>");
		return out;
	}
	
	
	public static String repeticion(int N,int n){
        String out = " ";
        out += "<html><h3>Solucion de la variacion:</h3>";
        
		double resultado=1;
		for (int i=1; 1<=n; i++){
			resultado*=N;
		}
        
        
        double sol=resultado;
		out += "<p>El numero total de variaciones con repeticion que pueden hacerse es <b>" +sol+"</b></p>";
		out += "<p><a href='form.html'>Volver a la p&aacute;gina principal</a></p></html>";
		return out;
        
    }
}
