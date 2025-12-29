/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package projektas;

/**
 *
 * @author User
 */
public interface UnrolledLinkedList {
    /**
     * Boolean abstract class to check if the UnrolledLinkedList is empty
     * @return 
     */
    boolean isEmpty();
    /**
     * Abstract class to get the node count
     * @return node count
     */
    int getSize();
    /**
     * Abstract class to get the node element array size
     * @return node element array size
     */
    int getNodeSize();
    /**
     * Abstract class to make the UnrolledLinkedList empty
     */
    void makeEmpty();
    /**
     * Abstract class to add students to the UnrolledLinkedList
     * @param name name
     * @param grades grades
     */
    void add(String name, int[] grades);
    /**
     * Abstract class to parse a line of data
     * @param data data
     */
    void parse(String data);
    /**
     * Abstract class to turn the grades array into a string
     * @param i node index
     * @return grades string
     */
    String gradesToString(int i);
    /**
     * Abstract class to get the grades array
     * @param i node index
     * @return grades array
     */
    int[] getGrades(int i);
    /**
     * Abstract class to get the students name
     * @param i node index
     * @return name
     */
    String getName(int i);
    /**
     * Abstract class to remove a student from the UnrolledLinkedList
     * @param index 
     */
    void remove(int index);
    /**
     * Abstract class to find all students average grades
     * @return average grades array
     */
    double[] average();
    
}
