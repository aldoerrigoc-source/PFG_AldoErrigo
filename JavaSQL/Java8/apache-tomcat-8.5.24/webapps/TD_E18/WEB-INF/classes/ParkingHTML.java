public class ParkingHTML {
	
	public static String parking(String pepe, String manolo, String alvarito, String hola){
		String out = " ";
		out += "<html><h3>Su reserva es la siguiente:</h3>";
		out += "<p><b>Plaza del Parking: </b>" + pepe +"</p>";
		out += "<p><b>Nombre y apellidos: </b>" + manolo +"</p>";
		out += "<p><b>Matr&iacute;cula: </b>" + alvarito +"</p>";
		out += "<p><b>D&iacute;a</b>: " + hola + "</p>";

		
		out += "<p><a href='index.html'>Volver a la p&aacute;gina principal</a></p></html>";
		return out;	
	}
	
}