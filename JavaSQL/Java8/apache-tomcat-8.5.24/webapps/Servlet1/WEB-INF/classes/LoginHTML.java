public class LoginHTML {
    public static boolean loginCorrecto(String nombre, String contrasena) {
        // Verifica si el nombre de usuario es "usuario1" y la contraseña es "cont9"
        return nombre.equals("usuario1") && contrasena.equals("cont9");
    }

    // Método main para probar la funcionalidad
    public static void main(String[] args) {
        // Ejemplo de uso
        String nombreUsuario = "usuario1";
        String contrasena = "cont9";
        boolean esLoginCorrecto = LoginHTML.loginCorrecto(nombreUsuario, contrasena);
        if (esLoginCorrecto) {
            System.out.println("¡Inicio de sesión exitoso!");
        } else {
            System.out.println("Nombre de usuario o contraseña incorrectos.");
        }
    }
    public static String salidaHTML(boolean x) {
        if (x==true) {
            return "<h1>Login correcto</h1>";
        } else {
            return "<h1>Login incorrecto</h1>" +
                   "<a href=\"login.html\">Volver al formulario de login</a>";
        }
    }
}
