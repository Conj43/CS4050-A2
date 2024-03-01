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
 *Group 19  assignment 2  2-29-2024
 * @author ouda
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


    /*
    this funtion closes the stage
     */
    @FXML
    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow(); //gets the current window stage
        stage.close(); //closes the state
    }

    public void find() throws DictionaryException {
        String filePath = "StarWars_Lego_Database.txt"; //the filepath to search in
        String substring = this.name.getText().toLowerCase(); //get the input and puts it all to lower case



        try {
            String result = searchForSubstring(filePath, substring); //looks in the file for the substring
            DataKey key = new DataKey(result, lswSize); //makes a new datakey for what was returned
            lsw = database.find(key); //locates the information that corrsponds to that key
            showLSW(); //outputs the finding to the user
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

            while((line = reader.readLine()) != null){ //while there is still lines in the file to check
                String lowerCase_line = line.toLowerCase(); //ignores the case of the input
                if(isTargetLine(lineNumber) && lowerCase_line.contains(subString)){
                    return line.trim(); //returns the line in where the match was found
                }
                lineNumber++; //if not found go to the next line
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        throw new DictionaryException("No record matching the given key.");

    }

    /*This is a helper function that checks if the line number is within our correct line occurrences of the database text file*/
    private static boolean isTargetLine(int lineNumber){
        return (lineNumber - 2) % 3 == 0 && lineNumber >= 2 && lineNumber <= 47; //returns true or false dependent upon if the line occurrence falls within our database
    }

    /*
    this implements the delete button and removes the character from the database
     */
    public void delete() {
        LSWRecord previousLSW = null;
        try {
            previousLSW = database.predecessor(lsw.getDataKey()); //tries to set the previous value to the predecessor
        } catch (DictionaryException ex) {

        }
        LSWRecord nextLSW = null;
        try {
            nextLSW = database.successor(lsw.getDataKey()); //tries to set the next value to the successor
        } catch (DictionaryException ex) {

        }
        DataKey key = lsw.getDataKey(); //gets the datakey of the value set to be removed
        try {
            database.remove(key); //tries to delete the value
        } catch (DictionaryException ex) {
            System.out.println("Error in delete "+ ex);
        }
        if (database.isEmpty()) { //if the database is empty show that there are no more characters to remove
            this.LSWPortal.setVisible(false);
            displayAlert("No more characters in the database to show");
        } else {
            if (previousLSW != null) { //if deletion occurs and there is a predecessor show that value
                lsw = previousLSW;
                showLSW();
            } else if (nextLSW != null) { //if deletion occurs and there is no predecessor it will show the successor
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
    /*
    this method implements the first button
     */
    public void first() {
       try{
           lsw = database.smallest(); //finds the smallest or left most element in the tree
           showLSW(); //shows that character
       }catch(DictionaryException p){
           displayAlert(p.getMessage());
        }
    }
    /*
    this method implements the last button
     */
    public void last() {
        try{
            lsw = database.largest(); //finds the largest or right most element in the tree
            showLSW(); //shows that character
        }catch(DictionaryException p){
            displayAlert(p.getMessage());
        }
    }
    /*
    this method implements the next button
     */
    public void next() {
        try{
            lsw = database.successor(lsw.getDataKey()); //if the character has a successor
            showLSW(); //show that character
        }catch(DictionaryException p){
            displayAlert(p.getMessage());
        }

    }
    /*
    this method implements the previous button
     */
    public void previous() {
        try{
            lsw = database.predecessor(lsw.getDataKey());//if the character has a predecessor
            showLSW(); //show that character
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
