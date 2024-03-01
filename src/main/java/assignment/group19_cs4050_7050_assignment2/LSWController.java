package assignment.group19_cs4050_7050_assignment2;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ouda
 */
public class LSWController implements Initializable {

    @FXML
    private MenuBar mainMenu;
    @FXML
    private ImageView image;
    @FXML
    private BorderPane LSWPortal;
    @FXML
    private Label title;
    @FXML
    private Label about;
    @FXML
    private Button play;
    @FXML
    private Button puase;
    @FXML
    private ComboBox size;
    @FXML
    private TextField name;
    Media media;
    MediaPlayer player;
    OrderedDictionary database = null;
    LSWRecord lsw = null;
    int lswSize = 1;


    @FXML
    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }


    public void find() throws DictionaryException {
        String filePath = "StarWars_Lego_Database.txt";
        String substring = this.name.getText().toLowerCase();



        try {
            String result = searchForSubstring(filePath, substring);
            DataKey key = new DataKey(result, lswSize);
            lsw = database.find(key);
            showLSW();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
    }

    /*This function takes a fileName and substring to search as arguments.
    * It loads the file into a buffer and reads through the lines and checks for substrings matches on the certain
    * lines that the lego character names occur on. It will return if the substring provides a match.
    * */
    private static String searchForSubstring(String filePath, String subString) throws DictionaryException{
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            int lineNumber = 1;

            while((line = reader.readLine()) != null){
                String lowerCase_line = line.toLowerCase();
                if(isTargetLine(lineNumber) && lowerCase_line.contains(subString)){
                    return line.trim();
                }
                lineNumber++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        throw new DictionaryException("No record matching the given key.");

    }

    /*This is a helper function that checks if the line number is wihtin our correct line occurences of the database text file*/
    private static boolean isTargetLine(int lineNumber){
        return (lineNumber - 2) % 3 == 0 && lineNumber >= 2 && lineNumber <= 47;
    }


    public void delete() {
        LSWRecord previousLSW = null;
        try {
            previousLSW = database.predecessor(lsw.getDataKey());
        } catch (DictionaryException ex) {

        }
        LSWRecord nextLSW = null;
        try {
            nextLSW = database.successor(lsw.getDataKey());
        } catch (DictionaryException ex) {

        }
        DataKey key = lsw.getDataKey();
        try {
            database.remove(key);
        } catch (DictionaryException ex) {
            System.out.println("Error in delete "+ ex);
        }
        if (database.isEmpty()) {
            this.LSWPortal.setVisible(false);
            displayAlert("No more characters in the database to show");
        } else {
            if (previousLSW != null) {
                lsw = previousLSW;
                showLSW();
            } else if (nextLSW != null) {
                lsw = nextLSW;
                showLSW();
            }
        }
    }

    private void showLSW() {
        play.setDisable(false);
        puase.setDisable(true);
        if (player != null) {
            player.stop();
        }
        String img = lsw.getImage();
        Image lswImage = new Image("file:src/main/resources/assignment/group19_cs4050_7050_assignment2/lego_pics/" + img);
        image.setImage(lswImage);
        title.setText(lsw.getDataKey().getlswName());
        about.setText(lsw.getAbout());
    }

    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/assignment/group19_cs4050_7050_assignment2/lego_pics/UMIcon.png"));
            stage.setTitle("Dictionary Exception");
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

    public void getSize() {
        switch (this.size.getValue().toString()) {
            case "Small":
                this.lswSize = 1;
                break;
            case "Medium":
                this.lswSize = 2;
                break;
            case "Large":
                this.lswSize = 3;
                break;
            default:
                break;
        }
    }

    public void first() {
       try{
           lsw = database.smallest();
           showLSW();
       }catch(DictionaryException p){
           displayAlert(p.getMessage());
        }
    }

    public void last() {
        try{
            lsw = database.largest();
            showLSW();
        }catch(DictionaryException p){
            displayAlert(p.getMessage());
        }
    }

    public void next() {
        try{
            lsw = database.successor(lsw.getDataKey());
            showLSW();
        }catch(DictionaryException p){
            displayAlert(p.getMessage());
        }

    }

    public void previous() {
        try{
            lsw = database.predecessor(lsw.getDataKey());
            showLSW();
        }catch(DictionaryException p){
            displayAlert(p.getMessage());
        }
    }

    public void play() {
        try {
            String filename = "src/main/resources/assignment/group19_cs4050_7050_assignment2/lego_sounds/" + lsw.getSound();
            media = new Media(new File(filename).toURI().toString());
            player = new MediaPlayer(media);
            play.setDisable(true);
            puase.setDisable(false);
            player.play();
        } catch(Exception ex){
            displayAlert(ex.getMessage());//h
        }
    }//test

    public void pause() {
        play.setDisable(false);
        puase.setDisable(true);
        if (player != null) {
            player.stop();
        }
    }

    public void loadDictionary() {
        Scanner input;
        int line = 0;
        try {
            String lswName = "";
            String description;
            int size = 0;
            input = new Scanner(new File("StarWars_Lego_Database.txt"));
            while (input.hasNext()) // read until  end of file
            {
                String data = input.nextLine();
                switch (line % 3) {
                    case 0:
                        size = Integer.parseInt(data);
                        break;
                    case 1:
                        lswName = data;
                        break;
                    default:
                        description = data;
                        database.insert(new LSWRecord(new DataKey(lswName, size), description, lswName + ".mp3", lswName + ".jpg"));
                        break;
                }
                line++;
            }
            lsw = database.root.getData();
            showLSW();
        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: StarWars_Lego_Database.txt");
            System.out.println(e.getMessage());
        } catch (DictionaryException ex) {
            Logger.getLogger(LSWController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.LSWPortal.setVisible(true);
        this.first();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = new OrderedDictionary();
        size.setItems(FXCollections.observableArrayList(
                "Small", "Medium", "Large"
        ));
        size.setValue("Small");
    }

}
