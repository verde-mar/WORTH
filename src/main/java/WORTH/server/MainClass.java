package WORTH.server;

import WORTH.server.ERRORS.ConfigurationFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

//todo: se uno scrittore sta scrivendo e un lettore vuole leggere, il lettore rischia di leggere le informazioni vecchie. E' un problema?
public class MainClass {
    public static void main(String[] args) throws Exception {
        ConcurrentHashMap<String, Project> projects = new ConcurrentHashMap<>();
        set(projects);

        try (SocketServices server = new SocketServices(8080, projects)) {
            server.start();

        } catch (Exception e) {
            System.err.println("Error in start operation");
            e.printStackTrace();
        }
    }



    private static void set(ConcurrentHashMap<String, Project> projects) throws Exception {
        //deve scorrere la cartella dei progetti
        File direct = new File("./projects");
        if (!direct.exists()) {
            boolean worth_dir = direct.mkdir();
            if(!worth_dir)
                throw new Exception("Couldnt create the directory 'projects'");
        } else {
            if (direct.isDirectory()) {
                File[] fil = Objects.requireNonNull(direct).listFiles();
                assert fil != null;
                for (File file_corr : fil) {
                    String name = file_corr.getName();
                    if (file_corr.isDirectory()) {
                        projects.putIfAbsent(name, new Project(name));
                    }
                }
            }

        }
    }

}