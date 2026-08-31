public class Links {
    public static String salida (int num) {
        String out = "";
        for (int i=1;i<=num;i++){
            out += " <a href=Celdas?filas="+i+"&columnas="+i+">Matriz "+i+"</a>";
        }
        return out;
    }
}