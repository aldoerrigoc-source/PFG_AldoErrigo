public class CafeteriaHTML {
	
	public static String cafeteria(String pepe, String[] manolo, String alvarito){
		String out = " ";
		out += "<html><h3>Su pedido es el siguiente:</h3>";
		out += "<p><b>Bocatas: </b>" + pepe +"</p>";
		out += "<p><b>Hamburguesa: </b>";
		for(int i=0;i<manolo.length;i++){
           out += manolo[i] +" ";
       }
	    out +="</p>";
		out += "<p><b>Almuezo: </b>" + alvarito +"</p>";
		
		out += "<p>Gracias por la compra!</p>";
			
		
		out += "<p><a href='index.html'>Volver a la p&aacute;gina principal</a></p></html>";
		return out;	
	}
	
}