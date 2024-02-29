package assignment.group19_cs4050_7050_assignment2;

public class DataKey {
	private String lswName;
	private int lswSize;

	// default constructor
	public DataKey() {
		this(null, 0);
	}
        
	public DataKey(String name, int size) {
		lswName = name;
		lswSize = size;
	}

	public String getlswName() {
		return lswName;
	}

	public int getlswSize() {
		return lswSize;
	}

	/**
	 * Returns 0 if this DataKey is equal to k, returns -1 if this DataKey is smaller
	 * than k, and it returns 1 otherwise. 
	 */
	public int compareTo(DataKey k) {
            if (this.getlswSize() == k.getlswSize()) {
                int compare = this.lswName.compareTo(k.getlswName());
                if (compare == 0){
                     return 0;
                } 
                else if (compare < 0) {
                    return -1;
                }
            }
            else if(this.getlswSize() < k.getlswSize()){
                    return -1;
            }
            return 1;
            
	}
}
