class C3 {
    static String chart(String[] values) {
        String out = "<html>";
        out += "<body>";
        out+="<title>Simulation</title>";
        out+="<h1>Simulation</h1>";
        int maximo=Integer.parseInt(values[0]);
        for (int i = 1; i < values.length; i++) {
            int num=Integer.parseInt(values[i]);
            if (num > maximo) {
               maximo = num;
            }
        }
        out+="<input type=text name=max value="+maximo+">";
        return out;
    }
}