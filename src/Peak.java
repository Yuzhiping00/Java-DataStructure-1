/**
 * This is a help class which other classes can create objects of it and call its methods to complete
 * some useful tasks.
 * @author : Zhiping Yu  student id: 000822513 date: 2021-01-11
 *
 */
public class Peak {
    private final int value; // the value of the peak
    private final int row;  // the row position of the peak
    private final int col; // the column position of the peak

    /**
     * Constructor    initialize the instance variables
     * @param value the value of the instance
     * @param row the row number of the instance
     * @param col the column number of the instance
     */
    public Peak(int value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
    }

    /**
     * Get the row number of the peak
     * @return the row number
     */
    public int getRow() {
        return row;
    }

    public int getValue() {
        return value;
    }

    /**
     * Get the column number of the peak
     * @return column number
     */
    public int getCol() {
        return col;
    }

    /**
     * Get String representation of the Peak object
     * @return the representation of the peak object
     */
    public String toString(){
        return value+"("+row+","+col+")";
    }
}
