/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projektas;

import static java.lang.Integer.parseInt;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class UnrolledLinkedListx implements UnrolledLinkedList{
    /**
     * Node class
     */
    public static class Node {
        String name;
        int[] grades;
        int numElements;  
        Node next;

        public Node(String name, int[] grades) {
            this.name = name;
            this.grades = grades;
            this.numElements = 0;
            this.next = null;
        }
        
    }
    
    private Node start;
    private Node current;
    private int nodeCount;
    private int nodeSize;
    /**
     * UnrolledLinkedListx constructor
     */
    public UnrolledLinkedListx(){
        start = null;
        current = null;
        nodeCount = 0;
        nodeSize = 10;
    }
    /**
     * Boolean class to check if the UnrolledLinkedList is empty
     * @return true if list is empty, else false
     */
    @Override
    public boolean isEmpty()  
    {  
        return start == null;  
    }  
  
    /**
     * Class to get the node count
     * @return node count
     */
    @Override
    public int getSize()  
    {  
        return nodeCount;  
    }
    /**
     * Class to get the node element array size
     * @return node element array size
     */
    @Override
    public int getNodeSize()  
    {  
        return nodeSize;  
    }
    /**
     * Class to turn the grades array into a string
     * @param i node index
     * @return grades string
     */
    @Override
    public String gradesToString(int i){
        int k = 0;
        current = start;
        while(k < i && current.next != null){
            current = current.next;
            k++;
        }
        
        String line = "";
        for(int j = 0; j < current.numElements; j++){
            if(current.grades[j] != 0)
                line = line + current.grades[j] + ";";
        }
        return line;
    }
    /**
     * Class to get the students name
     * @param i node index
     * @return name
     */
    @Override
    public String getName(int i){
        int k = 0;
        current = start;
        while(k < i && current.next != null){
            current = current.next;
            k++;
        }
        
        return current.name;
    }
    /**
     * class to get the students grades
     * @param i node index
     * @return grades array
     */
    @Override
    public int[] getGrades(int i){
        int k = 0;
        current = start;
        while(k < i && current.next != null){
            current = current.next;
            k++;
        }
        
        return current.grades;
    }
    /**
     * Class to make the UnrolledLinkedList empty
     */
    @Override
    public void makeEmpty()  
    {  
        start = null;  
        current = null;
        nodeCount = 0;  
    }  
    /**
     * Class to add students to the UnrolledLinkedList
     * @param name name
     * @param grades grades
     */
    @Override
    public void add(String name, int[] grades) {
        if (start == null) {
            start = new Node(name, grades);
            start.numElements = grades.length;
            current = start;
            nodeCount++;
        } else {
            Node newNode = new Node(name, grades);
            current.next = newNode;
            current = newNode;
            current.numElements = grades.length;
            nodeCount++;
        }
    }
    /**
     * Class to parse a line of data
     * @param data data
     */
    @Override
    public void parse(String data){
        try {   
                System.out.println(data);
                Scanner ed = new Scanner(data);
                ed.useDelimiter(";");
                String studName = ed.next();
                int[] grades = new int[nodeSize];
                int i = 0;
                int temp;
                while(ed.hasNext() && i < nodeSize){
                    temp = parseInt(ed.next());
                    if(temp > 0 && temp < 11){
                        grades[i] = temp;
                        i++;
                    } else{
                        grades = null;
                        break;
                    }
                }
                if(grades != null && grades[0] != 0)
                    add(studName, grades);
                else
                    Ks.ern("Blogas duomenų formatas -> " + data);
            } catch (InputMismatchException e) {
			Ks.ern("Blogas duomenų formatas -> " + data);
            } catch (NoSuchElementException e) {
			Ks.ern("Trūksta duomenų -> " + data);
            }
    }
    /**
     * Class to remove a student from the UnrolledLinkedList
     * @param index node index
     */
    @Override
    public void remove(int index){
        if (index < 0 || index >= nodeCount) {
            Ks.ern("Netinkama indeksacija");
            return;
        }

        if (index == 0) {
            start = start.next;
        } else {
            int k = 0;
            Node prev = null;
            current = start;

            while (k < index && current.next != null) {
                prev = current;
                current = current.next;
                k++;
            }

            // Remove the node at the specified index
            if (prev != null) {
                prev.next = current.next;
            }
        }

        nodeCount--;
    }
    /**
     * Class to find all students average grades
     * @return average grades array
     */
    @Override
    public double[] average(){
        double[] avrGrades = new double[nodeCount];
        current = start;
        
        for(int i = 0; i < nodeCount; i++){
            int sum = 0;
            int k = 0;
            for(int j = 0; j < current.numElements; j++)
                if(current.grades[j] > 0){
                    sum = sum + current.grades[j];
                    k++;
                }
            avrGrades[i] = (double)sum / k;
            current = current.next;
        }
        return avrGrades;
    }
    
}
