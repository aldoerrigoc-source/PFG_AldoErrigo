public class PolideportivoHTML {
	
	public static String polideportivo(String pepe, String manolo, String alvarito, String hola, String adios){
		String out = " ";
		out += "<html><h3>Su solicitud es la siguiente:</h3>";
		out += "<p><b>Nombre</b>: " + pepe + "</p>";
		out += "<p><b>Apellidos</b>: " + manolo + "</p>";
		out += "<p><b>Correo electr&oacute;nico</b>: " + alvarito + "</p>";
		out += "<p><b>D&iacute;a</b>: " + hola +"</p>";
		out += "<p><b>Horario</b>: " + adios+ "</p>";
		
		out += "<p>En las pr&oacute;ximas horas le escribir&aacute;n de secretar&iacute;a para confirmar su solicitud. </p> ";

		
		out += "<p><a href='index.html'>Volver a la p&aacute;gina principal</a></p></html>";
		return out;	
	}
	
}