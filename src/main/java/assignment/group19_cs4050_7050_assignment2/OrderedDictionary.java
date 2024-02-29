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
        Node current = root;
        int comparison;
        if (root.isEmpty()) {         
            throw new DictionaryException("There is no record matches the given key");
        }

        while (true) {
            comparison = current.getData().getDataKey().compareTo(k);
            if (comparison == 0) { // key found
                return current.getData();
            }
            if (comparison == 1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getLeftChild();
            } else if (comparison == -1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getRightChild();
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
        if(root == null){
            root = new Node(r);
        }
        else if (root.getData().getDataKey() == null){
            root.setData(r);
        }
        else recursiveInsert(root, r);

    }


    public void recursiveInsert(Node node, LSWRecord r) throws DictionaryException {
        DataKey key = r.getDataKey();
        DataKey nodeKey = node.getData().getDataKey();
        int comparison = key.compareTo(nodeKey);

        if (comparison == 0) {
            throw new DictionaryException("A record with this key already exists");
        } else if (comparison < 0) {
            if (node.hasLeftChild()) {
                recursiveInsert(node.getLeftChild(), r);
            } else {
                node.setLeftChild(new Node(r));
            }
        } else {
            if (node.hasRightChild()) {
                recursiveInsert(node.getRightChild(), r);
            } else {
                node.setRightChild(new Node(r));
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
        root = recursiveRemove(root, k);
    }

    private Node recursiveRemove(Node node, DataKey k) throws DictionaryException{
        if(root == null){
            throw new DictionaryException("Record with given key not found");
        }
        if(k.compareTo(node.getData().getDataKey()) < 0){
            node.setLeftChild(recursiveRemove(node.getLeftChild(), k));;
        } else if(k.compareTo(node.getData().getDataKey()) > 0){
            node.setRightChild(recursiveRemove(node.getRightChild(), k));
        } else{
            if(node.getLeftChild() == null){
                return node.getRightChild();
            } else if (node.getRightChild() == null){
                return node.getLeftChild();
            }


        node.setData(minimum(node.getRightChild()).getData());
        node.setRightChild(recursiveRemove(node.getRightChild(), node.getData().getDataKey()));
        }
        return node;
    }


    private Node minimum(Node node) {
        Node current = node;
        while (current.getLeftChild() != null) {
            current = current.getLeftChild();
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

        while (current != null)
        {
            if(current.getData().getDataKey().compareTo(k) > 0){
                next = current;
                current = current.getLeftChild();
            } else{
                current = current.getRightChild();
            }
        }

        if(next == null){
            throw new DictionaryException("No successor for current key");
        }

        return next.getData();
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

        while (current != null)
        {
            if(current.getData().getDataKey().compareTo(k) < 0){
                before = current;
                current = current.getRightChild();
            } else{
                current = current.getLeftChild();
            }
        }

        if(before == null){
            throw new DictionaryException("No predecessor for current key");
        }

        return before.getData();
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
        while(current.getLeftChild() != null){
            current = current.getLeftChild();
        }
        return current.getData();
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
        while(current.getRightChild() != null){
            current = current.getRightChild();
        }
        return current.getData();
    }
      
    /* Returns true if the dictionary is empty, and true otherwise. */
    @Override
    public boolean isEmpty (){
        return root == null;
    }
}
