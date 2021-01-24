import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This is a test class which is able to read data from a designated file and create a two-dimensional array with
 * those data. Then use that created array to do some tasks.
 * @author: Zhiping Yu   student id: 000822513   date: 2021-01-11
 *
 */
public class Test_1 {
    private static int rows; // rows of the 2D int array
    private static int cols; // columns of the 2D int array
    private static int exRadius; // exclusion radius
    private static double minDistance; // minimum distance between two peaks

    /**
     * Call other static methods to find the lowest elevation value in the data set and print the number of times it
     * appears, (to) find the local peaks in the data set, print them, (to) find the closest distance between two local
     * peaks and print the distance, row, column of the peaks, (to) find the elevation appears most frequently and print
     * it with the frequency
     * @param args not used
     */
    public static void main(String[] args) {
        File file = new File("ELEVATIONS.TXT");

        int[][] array = readFile(file);
        long startTime = System.nanoTime();
        int[] lowestElevation = FindLowest(array);
        int base = 98480; // the number used to find the local peaks
        Peak[] peaks = FindLocalPeaks(array,base,exRadius);
        Peak[] closestPeaks = findClosestPeaks(peaks);
        int[] commonestEle = findCommonestEve(array);
        long stopTime = System.nanoTime();
        System.out.printf("\n[Time = %d us]\n", (stopTime - startTime) / 1000);
        System.out.println("Part 1 answer: \nThe lowest value in the data set is: "+ lowestElevation[0] +" " +
                "and it appears "+ lowestElevation[1]+" times.");
        System.out.println("Part 2 answer: \nThe total number of peaks is : "+ peaks.length);
        System.out.println("Part 3 answer: \nThe closest peaks are at ["+closestPeaks[0].getRow()+","+
                closestPeaks[0].getCol()+" elevation = "+ closestPeaks[0].getValue()+" ]"+ " and ["+
                closestPeaks[1].getRow()+"," + closestPeaks[1].getCol()+" elevation = "+ closestPeaks[1].getValue()+
                " ]" );
        System.out.printf("The minimum distance between two peaks =  %.2f m", minDistance);
        System.out.println("\nPart 4 answer: \nThe most common elevation in the terrain is "+ commonestEle[0]+" " +
                "it occurs "+ commonestEle[1]+" times");

    }

    /**
     * Read data from the file and create a 2-dimensional array based on the data in the data set. Finally return
     * the array
     * @param file external txt file
     * @return 2D int array
     */
    private static int[][] readFile(File file) {
        int[][] intArray = new int[1][1];
        try {
            // read first three int and store them separately into rows, cols and radius, then create a new 2D int array
            Scanner console = new Scanner(file);
            rows = console.nextInt();
            cols = console.nextInt();
            exRadius = console.nextInt();
            intArray = new int[rows][cols];
            int totalNumbers = rows * cols;
            int count = 0;
            int[] readArray = new int[totalNumbers];
            // read the remaining data in the file into a one dimension array
            while (console.hasNextInt() && count <= totalNumbers) {
                int element = console.nextInt();
                readArray[count++] = element;
            }
            // Assign the value from one - dimension array to two - dimension array
            int newCount = 0;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    intArray[row][col] = readArray[newCount++];
                }
            }
            console.close();
        } catch (Exception ex) {
            System.out.println("File is not found, due to " + ex.getMessage());
        }
        return intArray;
    }

    /**
     * Find and print the lowest elevation value and the number of times it appears in the complete data set.
     * @param intArray 2D int array
     */
    private static int[] FindLowest(int[][] intArray) {

        int min = 99001; // one bigger than biggest value in the data set
        int[] lowest = new int[min];
        // find the lowest value in the data set and count the frequency of it
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if(intArray[row][col] <= min){
                    min = intArray[row][col];
                    lowest[min] ++;
                }
            }
        }
        return new int[]{min, lowest[min]};
    }

    /**
     * Find and print all the local peaks which has value greater or equal to 98480 and index radius is 11
     * @param intArray 2D int array
     * @param base the number used to get peaks
     * @param exRadius exclude radius
     * @return an array with peaks
     */

    private static Peak[] FindLocalPeaks(int[][] intArray, int base,int exRadius) {
        int counter = 0; // counter the number of peaks
        boolean flag; // check if an elevation is local peak
        Peak[] peaks = new Peak[rows*cols]; // a new peak array used to store local peaks
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if(intArray[row][col] >= base){
                    if(row >= exRadius && row < rows-exRadius && col>= exRadius && col< cols-exRadius){
                         flag = true;
                        for(int i= row-exRadius; ( i<= row + exRadius) && flag; i++){
                            for(int j= col-exRadius; ( j<= col+exRadius) && flag; j++){
                                if(i != row || j != col){ // exclude the value itself
                                    if(intArray[row][col] <= intArray[i][j]){
                                        flag = false;
                                    }
                                }

                            }
                        }
                        if(flag){ // find the local peak and store it into the peak array
                            Peak peak = new Peak(intArray[row][col], row, col);
                            peaks[counter] = peak;
                            counter++;
                        }
                    }
                }
            }
        }
        peaks = Arrays.copyOf(peaks,counter); // truncate the size of the peak array
        return peaks;
    }

    /**
     * Find and print the row and column of the two local peaks which has the lowest distance between them and
     * print the distance as well
     * @param peaks an array of peaks
     */
    private static Peak[] findClosestPeaks(Peak[] peaks) {
        minDistance = Double.MAX_VALUE; // use the biggest double value to compare
        Peak peak1 = null, peak2 = null;
            for (int i = 0; i < peaks.length-1; i++) {
                for (int j = i+1 ; j < peaks.length; j++) {
                    // use formula to calculate the distance between any two local peaks
                    double distance = Math.sqrt((peaks[i].getRow()-peaks[j].getRow()) * (peaks[i].getRow()-peaks[j].getRow())+
                            (peaks[i].getCol()-peaks[j].getCol()) * (peaks[i].getCol()-peaks[j].getCol()));
                    // get the minimum distance and store the pair of peaks
                    if(distance <= minDistance){
                        minDistance = distance;
                       peak1 = peaks[i];
                       peak2 = peaks[j];
                    }
                }
            }
            return new Peak[]{peak1, peak2};
    }

    /**
     * Find and print the most common elevation in the data set and the times of this value appearing.
     * @param array 2D int array
     */
    private static int[] findCommonestEve(int[][] array) {
        int[] commonArr = new int[99901];
        // find how many times of each elevation appears in the data set
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                commonArr[array[row][col]]++;
            }
        }
        // find the elevation appears most frequently
        int max = 0; // maximum times appearing
        int value = 0; // used to store the value appearing most of time
        for(int i= 15000; i< commonArr.length; i++){
            if(commonArr[i] > max){
                max = commonArr[i];
                value = i;
            }
        }
        return new int[]{value, max};
    }
}
