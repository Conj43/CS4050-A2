package assignment.group19_cs4050_7050_assignment2;

public class OrderedDictionary implements OrderedDictionaryADT {

    Node root;

    OrderedDictionary() {
        root = new Node();
    }

    /**
     * Returns the Record object with key k, or it returns null if such a record
     * is not in the dictionary.
     *
     * @param k
     * @return
     * @throws assignment/group19_cs4050_7050_assignment2/DictionaryException.java
     */
    @Override
    public LSWRecord find(DataKey k) throws DictionaryException {
        Node current = root; //sets the root node as the root of the tree
        int comparison;
        if (root.isEmpty()) {          //throws exception if no root node is available
            throw new DictionaryException("There is no record matches the given key");
        }

        while (true) {
            comparison = current.getData().getDataKey().compareTo(k); //sets the int to the return of the compareTo method
            if (comparison == 0) { // key found
                return current.getData(); //returns the data of the LSW that directly matched K
            }
            if (comparison == 1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getLeftChild();  //starts again but looking at the left child since K was < current
            } else if (comparison == -1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getRightChild(); //starts again but looks at the right child since K was > current
            }
        }

    }

    /**
     * Inserts r into the ordered dictionary. It throws a DictionaryException if
     * a record with the same key as r is already in the dictionary.
     *
     * @param r
     * @throws assignment.group19_cs4050_7050_assignment2.DictionaryException
     */
    @Override
    public void insert(LSWRecord r) throws DictionaryException {
        if(root == null){ //checks to see if the tree is empty
            root = new Node(r); //if the tree is empty it creates a new tree with r as the root
        }
        else if (root.getData().getDataKey() == null){
            root.setData(r); //sets the data of r
        }
        else recursiveInsert(root, r); //if a tree is created and information can be found it will insert the new r into the correct postion

    }

    /*
    this is a helped function that helps with the insertion of new nodes, it takes the root node of the tree
    and the new record, and returns nothing
     */
    public void recursiveInsert(Node node, LSWRecord r) throws DictionaryException {
        DataKey key = r.getDataKey(); //gets the data key for comparing, in order to find its correct position
        DataKey nodeKey = node.getData().getDataKey(); //gets the data key of the root node for comparison in finding the correct position
        int comparison = key.compareTo(nodeKey); //compares the new node to insert with the root node

        if (comparison == 0) { //exact match was already stored in the database
            throw new DictionaryException("A record with this key already exists");
        } else if (comparison < 0) { //the new node r datakey is less than the root node
            if (node.hasLeftChild()) { //if the root node already has a left node it will call the function again with the left node as the new root node
                recursiveInsert(node.getLeftChild(), r);
            } else {
                node.setLeftChild(new Node(r)); //if no left node is currently there it will insert r there
            }
        } else { //the new node is greater than the root
            if (node.hasRightChild()) { //if the root already has a right node it will call itself again with the right child as the new root
                recursiveInsert(node.getRightChild(), r);
            } else {
                node.setRightChild(new Node(r)); //no right child was found so it will insert r there
            }

        }
    }

    /**
     * Removes the record with Key k from the dictionary. It throws a
     * DictionaryException if the record is not in the dictionary.
     *
     * @param k
     * @throws assignment.group19_cs4050_7050_assignment2.DictionaryException
     */
    @Override
    public void remove(DataKey k) throws DictionaryException {
        root = recursiveRemove(root, k); //calls the recursive helper function
    }

    /*
    helper function for the remove method, takes the root node and the datakey to be removed, returns the root node of tree
     */
    private Node recursiveRemove(Node node, DataKey k) throws DictionaryException{
        if(root == null){ //the tree is empty
            throw new DictionaryException("Record with given key not found");
        }
        if(k.compareTo(node.getData().getDataKey()) < 0){ //if k is < than the node it will recursively call itself on the left side of the tree
            node.setLeftChild(recursiveRemove(node.getLeftChild(), k));;
        } else if(k.compareTo(node.getData().getDataKey()) > 0){ //if k is > than the node it will recursively call itself on the right side of the tree
            node.setRightChild(recursiveRemove(node.getRightChild(), k));
        } else{
            if(node.getLeftChild() == null){ //if no left child is found return the right child
                return node.getRightChild();
            } else if (node.getRightChild() == null){ //if no right child is found return the left child
                return node.getLeftChild();
            }

        //update the tree to maintain the correct ordering
        node.setData(minimum(node.getRightChild()).getData());
        node.setRightChild(recursiveRemove(node.getRightChild(), node.getData().getDataKey()));

        }
        return node;
    }

    /*
    returns the minimum value held in the tree
     */
    private Node minimum(Node node) {
        Node current = node;
        while (current.getLeftChild() != null) {
            current = current.getLeftChild(); //if there is no lower LSW character it returns this
        }
        return current;
    }



    /**
     * Returns the successor of k (the record from the ordered dictionary with
     * smallest key larger than k); it returns null if the given key has no
     * successor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws assignment.group19_cs4050_7050_assignment2.DictionaryException
     */
    @Override
    public LSWRecord successor(DataKey k) throws DictionaryException{
        Node current = root;
        Node next = null;

        while (current != null) //checks to see if the current node is the final node
        {
            if(current.getData().getDataKey().compareTo(k) > 0){ //checks if there is left child that is the successora
                next = current;
                current = current.getLeftChild(); //sets the current to hold the value of the left child
            } else{
                current = current.getRightChild(); //if no left child is found it sets the current to the right child
            }
        }

        if(next == null){ //if there is no successor throws exception
            throw new DictionaryException("No successor for current key");
        }

        return next.getData(); //returns the next nodes data
    }


    /**
     * Returns the predecessor of k (the record from the ordered dictionary with
     * largest key smaller than k; it returns null if the given key has no
     * predecessor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws assignment.group19_cs4050_7050_assignment2.DictionaryException
     */
    @Override
    public LSWRecord predecessor(DataKey k) throws DictionaryException{
        Node current = root;
        Node before = null;

        while (current != null) //makes sure the current is not the first node
        {
            if(current.getData().getDataKey().compareTo(k) < 0){ //if the current values is less than K it will set before == current
                before = current;
                current = current.getRightChild();
            } else{ //if equal or greater it will set current equal to the left child
                current = current.getLeftChild();
            }
        }

        if(before == null){ //if before is null then throws exception
            throw new DictionaryException("No predecessor for current key");
        }

        return before.getData(); //returns the data of the predecessor
    }

    /**
     * Returns the record with smallest key in the ordered dictionary. Returns
     * null if the dictionary is empty.
     *
     * @return
     */
    @Override
    public LSWRecord smallest() throws DictionaryException{
        if(isEmpty()){
            throw new DictionaryException("Dictionary is empty");
        }
        Node current = root;
        while(current.getLeftChild() != null){ //checks if the node is the furthest left
            current = current.getLeftChild(); //get the smallest or left most value in the tree
        }
        return current.getData(); //returns the left most node
    }

    /*
     * Returns the record with largest key in the ordered dictionary. Returns
     * null if the dictionary is empty.
     */
    @Override
    public LSWRecord largest() throws DictionaryException{
        if(isEmpty()){
            throw new DictionaryException("Dictionary is empty");
        }
        Node current = root;
        while(current.getRightChild() != null){ //checks if the ndoe is the furthest right
            current = current.getRightChild(); //gets the largest or right most value in the tree
        }
        return current.getData(); //returns the largest item in the dataset
    }

    /* Returns true if the dictionary is empty, and true otherwise. */
    @Override
    public boolean isEmpty (){
        return root == null;
    }
}