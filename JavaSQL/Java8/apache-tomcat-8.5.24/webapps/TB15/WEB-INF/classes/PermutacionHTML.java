public class PermutacionHTML {
	public static String normal(int N,int n){
		String out = " ";
		out += "<html><h3>Solucion de la permutacion:</h3>";
        double Nfact=1;
        for (int i=1;i<=N;i++){
            Nfact*=i;
        }
        
        
        double sol=(Nfact);
		out += "<p>La permutacion es <b>" +sol+"</b></p>";
		out += "<p><a href='form.html'>Volver a la p&aacute;gina principal</a></p></html>";
		return out;
	}
	
	
	
	
    public static String repeticion(int N,int numeros){
        String out = " ";
        out += "<html><h3>Solucion de la permutacion:</h3>";
        double Nfact=1;
        
        for (int i=1;i<=N;i++){
            Nfact*=i;
        }
		
         
		double resultado= 1;
		for (String numeroStr : numeros){
		  int numero =Integer.parseInt(numeroStr.trim());
		  resultado*=factorial(numero);
		
		}
		private long factorial(int k){
		  double resultado = 1;
		  for (int i=2; i <=n; i++){
		      resultado*= i;
		  }
		
		}
		
		
        double sol=(Nfact)/(resultado);
		out += "<p>El numero total de permutaciones con repeticion que pueden hacerse es <b>" +sol+"</b></p>";
		out += "<p><a href='form.html'>Volver a la p&aacute;gina principal</a></p></html>";
		return out;
        
    }
}