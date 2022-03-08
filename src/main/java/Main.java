import Controller.KursController;
import Controller.LehrerController;
import Controller.StudentController;
import KonsoleView.KonsoleView;

import Repository.KursRepository;
import Repository.LehrerRepository;
import Repository.StudentRepository;
import Exception.DasElementExistiertException;
import Exception.ListIsEmptyException;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException, ListIsEmptyException, DasElementExistiertException, InterruptedException {

        KursRepository kursRepository = new KursRepository("kurs.json");
        kursRepository.readFromFile();
        LehrerRepository lehrerRepository = new LehrerRepository("lehrer.json");
        lehrerRepository.readFromFile();
        StudentRepository studentRepository = new StudentRepository("student.json");
        studentRepository.readFromFile();
        KursController kursController = new KursController(kursRepository,studentRepository,lehrerRepository);
        LehrerController lehrerController = new LehrerController(lehrerRepository,studentRepository,kursRepository);
        StudentController studentController = new StudentController(studentRepository);
        KonsoleView view = new KonsoleView(kursController,lehrerController,studentController);
        view.start();
    }
}
