public class AulasHTML {
	
	public static String aulas(String pepe, String manolo, String alvarito, String hola, String adios, String hello){
		String out = " ";
		out += "<html><h3>Su reserva es la siguiente:</h3>";
		out += "<p><b>Nombre: </b>" + pepe +"</p>";
		out += "<p><b>Apellido: </b>" + manolo +"</p>";
		out += "<p><b>Correo electr&oacute;nico: </b>" + alvarito +"</p>";
		out += "<p><b>D&iacute;a: </b>" + hola + "</p>";
		out += "<p><b>Horario: </b>" + adios + "</p>";
		out += "<p>Aula seleccionada: </b>" + hello+ "</p>";
		
		out += "<p>Su reserva se ha completado correctamente.</p>";
		
		out += "<p><a href='index.html'>Volver a la p&aacute;gina principal</a></p></html>";
		return out;	
	}
	
}