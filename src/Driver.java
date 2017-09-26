/**CS 4375 Assignment 2: Bayesian Learning
 * Author: Elizabeth Trinh
 *
 * Command line arguments (2): file name for training data and file name for test data, in that order.
 *
 * Function: The program will output to stdout the probabilities of the class and attributes given class values.
 *           It will report the accuracy of the classifier on training data, then the accuracy on test data.
 *
 * The program reads in each file with a helper method, readData, which stores values in an array of ArrayLists.
 * The array of ArrayLists corresponds to the array holding the attribute names/labels.
 * Each ArrayList holds values for its corresponding attribute.
 * Creates a Classifier object to calculate, store, and use probabilities.
 */

import java.io.*;
import java.util.*;

public class Driver {
    public static void main(String[] args) throws FileNotFoundException{
        // checks for the correct amount of commandline arguments
        if (args.length == 2) {
            // read in training data
            Scanner trainIn = new Scanner(new File(args[0]));
            // ignore lines with only whitespace characters
            String line;
            do {
                line = trainIn.nextLine().trim();
            } while(line.isEmpty());
            String[] attributes = line.split("\\s+");
            ArrayList<Integer>[] atrVal = readData(trainIn, attributes.length);

            // instantiate and find probabilities for each attribute
            Classifier nb = new Classifier(attributes);
            nb.findProbabilities(atrVal);
            nb.printProbabilities();

            // calculate accuracy on training data
            int[] acc = nb.test(atrVal);
            double percent = (acc[0]*1.0/(acc[1]+acc[0])) * 100;
            System.out.printf("Accuracy on training set (%d instances):  %.1f%%%n", atrVal[0].size(), percent);


            // read in test data
            Scanner testIn = new Scanner(new File(args[1]));
            // ignore lines with only whitespace characters
            do {
                line = testIn.nextLine().trim();
            } while(line.isEmpty());
            attributes = line.split("\\s+");
            atrVal = readData(testIn, attributes.length);

            // calculate accuracy on test data
            acc = nb.test(atrVal);
            percent = (acc[0]*1.0/(acc[1]+acc[0])) * 100;
            System.out.printf("Accuracy on test set (%d instances):  %.1f%%%n", atrVal[0].size(), percent);

        }
        else {
            System.out.println("Incorrect number of arguments. Requires two arguments: training file and test file.");
        }
    }

    /**
     * Reads in attribute data and stores it for later reference.
     *
     * @param in        scanner to read the appropriate file
     * @param len       length of the attribute array
     *                  used to instantiate the data structure that holds the attribute values
     * @return returns  data structure that holds the values for attributes
     * @throws FileNotFoundException
     */
    public static ArrayList<Integer>[] readData(Scanner in, int len)  throws FileNotFoundException{
        ArrayList<Integer>[] rtn = (ArrayList<Integer>[])new ArrayList[len];

        for (int z = 0; z < rtn.length; z++) {
            rtn[z] = new ArrayList<>();
        }
        int pos = 0;
        while(in.hasNextInt()) {
            rtn[pos].add(in.nextInt());

            // if pos reaches the class attribute, reset pos; else increment pos
            pos = pos == rtn.length-1?0:pos+1;
        }
        in.close();

        return rtn;
    }
}