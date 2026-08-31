public class ErasmusHTML {
	
	public static String erasmus(String pepe, String manolo, String alvarito, String hola, String adios){
		String out = " ";
		out += "<html><h3>Su solicitud es la siguiente:</h3>";
		out += "<p><b>Nombre: </b>" + pepe +"</p>";
		out += "<p><b>Apellido: </b>" + manolo+"</p>";
		out += "<p><b>Correo: </b>" + alvarito+"</p>";
		out += "<p><b>Semestre elegido: </b>" + hola +"</p>";
		out += "<p><b>Pa&iacute;s preferido: </b>" + adios +"</p>";
		out += "<p>Gracias, en breve se pondran en contacto con usted.</p>";
			
		
		out += "<p><a href='index.html'>Volver a la p&aacute;gina principal</a></p></html>";
		return out;	
	}
	
}