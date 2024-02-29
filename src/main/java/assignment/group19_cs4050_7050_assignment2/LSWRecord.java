package assignment.group19_cs4050_7050_assignment2;

/**
 * This class represents an LSW record in the database. Each record consists of two
 * parts: a DataKey and the data associated with the DataKey.
 */
public class LSWRecord {

    private DataKey key;
    private String about;
    private String sound;
    private String image;

    // default constructor
    public LSWRecord() {
        this(null, null, null, null);
    }

    public LSWRecord(DataKey k, String a, String s, String i) {
        key = k;
        about = a;
        sound = s;
        image = i;
    }

    public DataKey getDataKey() {
        return key;
    }

    public String getAbout() {
        return about;
    }
    
 
    public String getSound() {
        return sound;
    }
    
    public String getImage() {
        return image;
    }
    
 


}
