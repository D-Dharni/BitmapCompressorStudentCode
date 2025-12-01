/******************************************************************************
 *  Compilation:  javac BitmapCompressor.java
 *  Execution:    java BitmapCompressor - < input.bin   (compress)
 *  Execution:    java BitmapCompressor + < input.bin   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   q32x48.bin
 *                q64x96.bin
 *                mystery.bin
 *
 *  Compress or expand binary input from standard input.
 *
 *  % java DumpBinary 0 < mystery.bin
 *  8000 bits
 *
 *  % java BitmapCompressor - < mystery.bin | java DumpBinary 0
 *  1240 bits
 ******************************************************************************/

/**
 *  The {@code BitmapCompressor} class provides static methods for compressing
 *  and expanding a binary bitmap input.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author Deven Dharni
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {
        // Variable to keep track of how many 0/1s in a row
        int currentRun = 0;

        // Set for beginning = false (0)
        boolean standardBit = false;

        while (!BinaryStdIn.isEmpty()) {
            // Read in first bit
            boolean bit = BinaryStdIn.readBoolean();

            if (standardBit != bit) {
                // Some code to add that run to the output file
                BinaryStdOut.write(currentRun, 8);

                // Reset run
                currentRun = 1;

                // Flip oldBit
                standardBit = !standardBit;
            }
            else {
                // The maximum value that can be represented w/8 bits
                if (currentRun == 255) {
                    BinaryStdOut.write(currentRun, 8);

                    // Write out 0 to keep same bit
                    BinaryStdOut.write(0, 8);

                    // Reset run
                    currentRun = 1;
                }
                else {
                    currentRun++;
                }
            }
        }
        // write in the final currentRun
        BinaryStdOut.write(currentRun, 8);
        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        boolean standardBit = false;

        while (!BinaryStdIn.isEmpty()) {
            // Save 8 bit chunks
            int numRepeats = BinaryStdIn.readInt(8);

            // Read all the 0s or 1s into the out
            for (int i = 0; i < numRepeats; i++) {
                BinaryStdOut.write(standardBit);
            }

            // Flip
            standardBit = !standardBit;
        }

        BinaryStdOut.close();
    }
    /**
     * When executed at the command-line, run {@code compress()} if the command-line
     * argument is "-" and {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}