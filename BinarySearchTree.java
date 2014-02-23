/**
 * Miguel Chateloin
 * Olena Tcachenko
 */
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

// BinarySearchTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x
// boolean contains( x )  --> Return true if x is present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate
/**
 * Implements an unbalanced binary search tree. Note that all "matching" is
 * based on the compareTo method.
 *
 * @author Mark Allen Weiss
 */
public class MyTreeSet<AnyType extends Comparable<? super AnyType>>
        implements Iterable<AnyType>
{

    private BinaryNode<AnyType> root;           //root of the tree

    /**
     * Construct the tree.
     */
    public MyTreeSet()
    {
        root = null;
    }

    /**
     * Returns an iterator pointing just before the item in the tree with the
     * lowest value.
     */
    public Iterator<AnyType> iterator()
    {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<AnyType>
    {

        BinaryNode<AnyType> current;

        public MyIterator()
        {
            current = findMin(root);
        }

        public boolean hasNext()
        {
            return current != null;
        }

        public AnyType next()
        {
            AnyType temp = current.element;
            current = successor(current);
            return temp;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the next value in the sequence, starting at the node pointed to
     * by the input parameter. This method is used by the iterator.
     */
    private BinaryNode<AnyType> successor(BinaryNode<AnyType> p)
    {

        if (p.right != null) {
            return findMin(p.right);
        }

        BinaryNode<AnyType> Pparent = p.parent;
        while (Pparent != null && p == Pparent.right) {
            p = Pparent;
            Pparent = Pparent.parent;
        }
        return Pparent;
    }
    /**
     * Returns the element at a given index position.
     * Throws an IndexOutOfBoundsException if the item is not found.
     */
    int sizeLeft = 0;

    public AnyType get(int index)
    {

        BinaryNode<AnyType> node = root;
        int count = index + 1;
        int sizeLeft = 0;

        while (node != null) {
            sizeLeft = node.sizeOfLeftSubtree();
            if (sizeLeft + 1 == count) {
                return node.element;
            } else if (sizeLeft < count) {
                node = node.right;
                count -= sizeLeft + 1;
            } else {
                node = node.left;
            }
        }

        throw new IndexOutOfBoundsException();
    }

    private BinaryNode<AnyType> getNode(int index)
    {

        BinaryNode<AnyType> node = root;
        int count = index + 1;
        int sizeLeft = 0;

        while (node != null) {
            sizeLeft = node.sizeOfLeftSubtree();
            if (sizeLeft + 1 == count) {
                return node;
            } else if (sizeLeft < count) {
                node = node.right;
                count -= sizeLeft + 1;
            } else {
                node = node.left;
            }
        }

        throw new IndexOutOfBoundsException();
    }

    /**
     * Returns all elements falling within a range of indexes. The range is
     * inclusive
     */
    public Collection<AnyType> getRange(int first, int last)
    {
        if (first < 0 || last >= root.size) {
            throw new IndexOutOfBoundsException();
        } else {
            //get the first index(Node)
            BinaryNode<AnyType> current = getNode(first);
            //adding the included indexes to the collection
            Collection<AnyType> out = new ArrayList<>();
            for (int i = first; i <= last; i++) {
                out.add(current.element);
                current = successor(current);
            }
            return out;
        }
    }

    /**
     * Prints the tree in level-order, which means the root is printed, then all
     * nodes at level 2, then nodes at level 3, and so on.
     */
    public void printLevelOrder()
    {
        if (root == null) System.out.println("Empty");
        for(int level = 1; level <= height(root); level++){
            printLevel(root, level);
        }
    }

    private void printLevel(BinaryNode<AnyType> t, int level)
    {
        if (t == null) return;
        if(level == 1) System.out.println(t);
        else if(level > 1){
            printLevel(t.left, level-1);
            printLevel(t.right, level-1);
        }
    }

    /**
     * Insert into the tree; duplicates are ignored.
     *
     * @param x the item to insert.
     */
    public void insert(AnyType x)
    {
        root = insert(x, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     *
     * @param x the item to remove.
     */
    public void remove(AnyType x)
    {
        root = remove(x, root);
    }

    /**
     * Find the smallest item in the tree.
     *
     * @return smallest item or null if empty.
     */
    public AnyType findMin()
    {
        if (isEmpty()) {
            return null;
        }
        return findMin(root).element;
    }

    /**
     * Find the largest item in the tree.
     *
     * @return the largest item of null if empty.
     */
    public AnyType findMax()
    {
        if (isEmpty()) {
            throw null;
        }
        return findMax(root).element;
    }

    /**
     * Find an item in the tree.
     *
     * @param x the item to search for.
     * <p/>
     * @return true if not found.
     */
    public boolean contains(AnyType x)
    {
        return contains(x, root);
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty()
    {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty()
    {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree()
    {
        if (isEmpty()) {
            System.out.println("Empty tree");
        } else {
            printTree(root);
        }
    }

    /**
     * Internal method to insert into a subtree.
     *
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * <p/>
     * @return the new root of the subtree.
     */
    private BinaryNode<AnyType> insert(AnyType x, BinaryNode<AnyType> t)
    {
        if (t == null) {
            //System.out.println(x + ", ");
            return new BinaryNode<>(x, null, null);
        }

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0) {
            t.left = insert(x, t.left);
            t.size += 1;	// update size of subtree
            t.left.parent = t;
        } else if (compareResult > 0) {
            t.right = insert(x, t.right);
            t.size += 1;		// update size of subtree
            t.right.parent = t;
        } else
            ;  // Duplicate; do nothing
        return t;
    }

    /**
     * Internal method to remove from a subtree.
     *
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * <p/>
     * @return the new root of the subtree.
     */
    private BinaryNode<AnyType> remove(AnyType x, BinaryNode<AnyType> t)
    {
        if (t == null) {
            return t;   // Item not found; do nothing
        }
        int compareResult = x.compareTo(t.element);

        if (compareResult < 0) {
            t.left = remove(x, t.left);
            t.size--;
            if (t.left != null) {
                t.left.parent = t;
            }
        } else if (compareResult > 0) {
            t.right = remove(x, t.right);
            t.size--;
            if (t.right != null) {
                t.right.parent = t;
            }
        } // We found the element: does it have two subtrees?
        else if (t.left != null && t.right != null) {
            t.element = findMin(t.right).element;
            t.right = remove(t.element, t.right);
            t.size--;
            if (t.right != null) {
                t.right.parent = t;
            }
        } else {  // the matching element has only one subtree
            t = (t.left != null) ? t.left : t.right;
        }
        return t;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     *
     * @param t the node that roots the subtree.
     * <p/>
     * @return node containing the smallest item.
     */
    private BinaryNode<AnyType> findMin(BinaryNode<AnyType> t)
    {
        if (t == null) {
            return null;
        } else if (t.left == null) {
            return t;
        }
        return findMin(t.left);
    }

    /**
     * Internal method to find the largest item in a subtree.
     *
     * @param t the node that roots the subtree.
     * <p/>
     * @return node containing the largest item.
     */
    private BinaryNode<AnyType> findMax(BinaryNode<AnyType> t)
    {
        if (t != null) {
            while (t.right != null) {
                t = t.right;
            }
        }

        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * <p/>
     * @return node containing the matched item.
     */
    private boolean contains(AnyType x, BinaryNode<AnyType> t)
    {
        if (t == null) {
            return false;
        }

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0) {
            return contains(x, t.left);
        } else if (compareResult > 0) {
            return contains(x, t.right);
        } else {
            return true;    // Match
        }
    }

    /**
     * Internal method to print a subtree in sorted order.
     *
     * @param t the node that roots the subtree.
     */
    private void printTree(BinaryNode<AnyType> t)
    {
        if (t != null) {
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

    /**
     * Internal method to compute height of a subtree.
     *
     * @param t the node that roots the subtree.
     */
    private int height(BinaryNode<AnyType> t)
    {
        if (t == null) {
            return -1;
        } else {
            return 1 + Math.max(height(t.left), height(t.right));
        }
    }

    private static class BinaryNode<AnyType>
    {
        AnyType element;            		// The data in the node
        BinaryNode<AnyType> left;   		// Left child
        BinaryNode<AnyType> right;  		// Right child
        BinaryNode<AnyType> parent; 		// added (Irvine)
        int size;                         // added (Irvine)

        BinaryNode(AnyType theElement)
        {
            this(theElement, null, null);
        }

        BinaryNode(AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt)
        {
            element = theElement;
            left = lt;
            right = rt;
            size = 1;
        }

        int sizeOfLeftSubtree()
        {
            if (left != null) {
                return left.size;
            } else {
                return 0;
            }
        }
        
        public String toString(){
            return this.element.toString();
        }
        
    } // BinaryNode class

    public <AnyType> void print(MyTreeSet<? extends Object> t)
    {
        for (Object x : t) {
            System.out.print(x + ", ");
        }
        System.out.println("\n");
    }

    void test1()
    {
        MyTreeSet<Integer> t = new MyTreeSet<>();
        int[] array = {20, 10, 11, 30, 2, 29, 33, 28, 17, 4};
        for (int i = 0; i < array.length; i++) {
            t.insert(array[i]);
        }
        print(t); // demonstrate the iterator
        System.out.println("\nLevel order");
        t.printLevelOrder();
        System.out.printf("\nThe value at index %d is %d\n", 0, t.get(0));
        System.out.printf("The value at index %d is %d\n", 1, t.get(1));
        System.out.printf("The value at index %d is %d\n", 2, t.get(2));
        System.out.printf("The value at index %d is %d\n", 3, t.get(3));
        System.out.printf("The value at index %d is %d\n", 8, t.get(8));
        System.out.printf("The value at index %d is %d\n", 9, t.get(9));
        System.out.print("\nRemoving ");
        for (int i = 1; i < array.length; i += 2) {
            System.out.print(array[i] + ", ");
            t.remove(array[i]);
        }
        System.out.println();
        System.out.println("\nTree contents after removing elements:");
        print(t);
        // verify that the get method still works
        System.out.printf("The value at index %d is %d\n", 0, t.get(0));
        System.out.printf("The value at index %d is %d\n", 1, t.get(1));
        System.out.printf("The value at index %d is %d\n", 2, t.get(2));
        System.out.printf("The value at index %d is %d\n", 3, t.get(3));
    }

    void test2()
    {
        MyTreeSet<String> t = new MyTreeSet<>();
        String[] array = {"Harry", "Maria", "Bob", "Dan", "Sue", "Ann", "Jose"};

        for (int i = 0; i < array.length; i++) {
            t.insert(array[i]);
        }
        print(t);

        System.out.print("Level order: ");
        t.printLevelOrder();
        System.out.println("\n");

        System.out.printf("The value at index %d is %s\n", 0, t.get(0));
        System.out.printf("The value at index %d is %s\n", 2, t.get(2));
        System.out.printf("The value at index %d is %s\n", 6, t.get(6));

        System.out.print("\nRemoving ");
        for (int i = 1; i < array.length; i += 2) {
            System.out.print(array[i] + ", ");
            t.remove(array[i]);
        }
        System.out.println();

        System.out.println("\nTree contents after removing elements:");
        print(t);

        // verify that the get method still works
        System.out.printf("The value at index %d is %s\n", 0, t.get(0));
        System.out.printf("The value at index %d is %s\n", 2, t.get(2));
        System.out.printf("The value at index %d is %s\n", 3, t.get(3));

    }
    // Test program

    public static void main(String[] args)
    {
        MyTreeSet app = new MyTreeSet();  //replaced Asg3_Test class name
        app.test1();                //first test code: using integers
        app.test2();                //second test code: using strings
    }
}
