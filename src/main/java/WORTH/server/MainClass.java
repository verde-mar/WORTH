package WORTH.server;

//todo: se uno scrittore sta scrivendo e un lettore vuole leggere, il lettore rischia di leggere le informazioni vecchie. E' un problema?
public class MainClass {
    public static void main(String[] args) {
        try (SocketServices server = new SocketServices(8080)) {
            server.start();

        } catch (Exception e) {
            System.err.println("Error in start operation");
            e.printStackTrace();
        }
    }
}