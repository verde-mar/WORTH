import java.io.IOException;

public class MainClass {
    public static void main(String[] args){
            try (SocketServices server = new SocketServices(8080)) {
                server.start();

            } catch (IOException | InterruptedException e) {
                System.err.println("Error in start operation");
                e.printStackTrace();
            }

    }
}
