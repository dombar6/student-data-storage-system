/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projektas;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

/**
 *
 * @author User
 */
public class Greitaveika {

    public static final String FINISH_COMMAND = "finishCommand";

    private final BlockingQueue<String> resultsLogger = new SynchronousQueue<>();

    private final Semaphore semaphore = new Semaphore(-1);

    private final Timekeeper tk;

    private final String[] TYRIMU_VARDAI = {"add", "remove"};

    private final int[] TIRIAMI_KIEKIAI = {10000, 20000, 40000, 80000};

    private final UnrolledLinkedListx unrolledLinkedList = new UnrolledLinkedListx();

    private final Queue<String> chainsSizes = new LinkedList<>();
    
    Random ag = new Random();

    /**
     * Constructs a new Benchmark instance.
     */
    public Greitaveika() {
        semaphore.release();
        tk = new Timekeeper(TIRIAMI_KIEKIAI, resultsLogger, semaphore);
    }

    /**
     * Initiates the benchmarking process.
     */
    public void pradetiTyrima() {
        try {
            SisteminisTyrimas();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    /**
     * Performs the benchmarking of set and get operations on the SparseArray.
     *
     * @throws InterruptedException If the benchmark is interrupted.
     */
    public void SisteminisTyrimas() throws InterruptedException {
        try {
            chainsSizes.add("   kiekis      " + TYRIMU_VARDAI[0] + "   " + TYRIMU_VARDAI[1]);
            for (int k : TIRIAMI_KIEKIAI) {
                // Generate data for your SparseArray
                String[] names = generateNames(k);
                int[][] grades = generateGrades(k);

                unrolledLinkedList.makeEmpty();
                tk.startAfterPause();
                tk.start();

                
                for (int i = 0; i < k; i++) {
                    unrolledLinkedList.add(names[i], grades[i]);
                }
                tk.finish(TYRIMU_VARDAI[0]);

                
                for (int i = k-1; i > -1; i--) {
                    unrolledLinkedList.remove(i);
                }
                tk.finish(TYRIMU_VARDAI[1]);

                tk.seriesFinish();
            }

            StringBuilder sb = new StringBuilder();
            chainsSizes.forEach(p -> sb.append(p).append(System.lineSeparator()));
            tk.logResult(sb.toString());
            tk.logResult(FINISH_COMMAND);
        } catch (NumberFormatException e) {
            tk.logResult("Netinkama įvestis. Prašome naudoti skaitines reikšmes.");
        }
    }

    private String[] generateNames(int k) {
        String[][] nameMatrix = {
			{"Jonas", "Petrauskas", "Rudys", "Jankus", "Morkus", "Rudaitis"},
			{"Tadas", "Petrauskas", "Rudys", "Sutkus", "Morkus", "Jankus"},
			{"Simas", "Petrauskas", "Rudys"},
			{"Romas", "Petrauskas", "Sutkus", "Rudaitis"},
			{"Rimas", "Petrauskas", "Sutkus", "Jankus", "Morkus"},
			{"Lukas", "Petrauskas", "Rudys", "Jankus"},
                        {"Vytas", "Petrauskas", "Morkus", "Rudys"}};
        
        String[] names = new String[k];
        ag.setSeed(2023);
        for (int i = 0; i < k; i++) {
            int ma = ag.nextInt(nameMatrix.length);
            int mo = ag.nextInt(nameMatrix[ma].length - 1) + 1;
            names[i] = nameMatrix[ma][0] + " " + nameMatrix[ma][mo];
        }
        return names;
    }

    private int[][] generateGrades(int k) {
        int[][] grades = new int[k][unrolledLinkedList.getNodeSize()];
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            for(int j = 0; j < unrolledLinkedList.getNodeSize(); j++)
                grades[i][j] = random.nextInt(10) + 1;
        }
        return grades;
    }

    public BlockingQueue<String> getResultsLogger() {
        return resultsLogger;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public static void main(String[] args) {
        Greitaveika greitaveika = new Greitaveika();

        
        new Thread(() -> {
            greitaveika.pradetiTyrima();

            // Signal the end of the benchmark
            try {
                greitaveika.getResultsLogger().put(Greitaveika.FINISH_COMMAND);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // Handle the results in the main thread
        try {
            String result;
            while (!(result = greitaveika.getResultsLogger().take()).equals(Greitaveika.FINISH_COMMAND)) {
                // Process the result as needed, e.g., print it
                System.out.println(result);

                // Release the semaphore
                greitaveika.getSemaphore().release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
