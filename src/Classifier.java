/**
 * Naive Bayes Classifier. This class describes a classifier with attributes that have binary values.
 */

import java.util.ArrayList;

public class Classifier {
    private double[][] probabilities;   // probabilities of class value and attributes
    private String[] attributes;        // attribute array; contains attribute names

    /**
     * Constructor.
     * @param attr attribute array; contains attribute names
     */
    public Classifier(String[] attr) {
        attributes = attr;
        probabilities = new double[attr.length][2];
    }

    /**
     * Calculates the probabilities for each attribute given the class value and the probability of the class values.
     * @param dataValues values for attributes
     */
    public void findProbabilities(ArrayList<Integer>[] dataValues) {
        // count how many class values are 1's or 0's
        double oneIndicies = 0;
        double zeroIndicies = 0;

        // go through data row by row
        for (int i = 0; i < dataValues[0].size(); i++) {
            // if the class value is 1
            if (dataValues[attributes.length-1].get(i) == 1) {
                oneIndicies++;
                // go through each attribute except class
                for (int attrIndex = 0; attrIndex < dataValues.length-1; attrIndex++) {
                    // increment count if attribute is 1 given class is 1
                    if (dataValues[attrIndex].get(i) == 1) {
                        probabilities[attrIndex][1]++;
                    }
                }
            }
            // if class value is 0
            else {
                zeroIndicies++;
                // go through all attributes except class
                for (int attrIndex = 0; attrIndex < dataValues.length-1; attrIndex++) {
                    // increment count if attribute is 1 given class is 0
                    if (dataValues[attrIndex].get(i) == 1) {
                        probabilities[attrIndex][0]++;
                    }
                }
            }
        }

        // calculate conditional probabilities
        for(int i = 0; i < attributes.length-1; i++) {
            probabilities[i][0] = probabilities[i][0]/zeroIndicies;
            probabilities[i][1] = probabilities[i][1]/oneIndicies;
        }

        // calculate class probabilities
        probabilities[probabilities.length-1][0] = zeroIndicies/(zeroIndicies+oneIndicies);
        probabilities[probabilities.length-1][1] = oneIndicies/(zeroIndicies+oneIndicies);
    }

    /**
     * Calculates whether the record is more likely to be 1 or 0
     * and checks if it resolves to the correct value.
     * @param dataValues    values for attributes
     * @return              int[0] contains # of records that resolve to the correct class value
     *                      int[1] contains # of records that resolve to the incorrect class value
     */
    public int[] test(ArrayList<Integer>[] dataValues) {
        int[] accuracy = new int[2];
        // go through data row by row
        for(int row = 0; row < dataValues[0].size(); row++) {
            // probability that the record resolves to 0 or 1
            double probZero = 1;
            double probOne = 1;
            // go through each attribute
            for (int attrIndex = 0; attrIndex < dataValues.length-1; attrIndex++) {
                // if the attribute value is 0
                if (dataValues[attrIndex].get(row) == 0) {
                    // law of total probability means P(attribute=0|classval) =  1-P(attribute=1|classval)
                    probZero *= (1-probabilities[attrIndex][0]);
                    probOne *= (1-probabilities[attrIndex][1]);
                }
                // if the attribute value is 1
                else {
                    probZero *= probabilities[attrIndex][0];
                    probOne *= probabilities[attrIndex][1];
                }
            }
            // naive bayes, multiply by the class value probability, denominator don't matter
            probZero *= probabilities[probabilities.length-1][0];
            probOne *= probabilities[probabilities.length-1][1];

            // if probZero is higher, then the record evaluates to 0
            if (probZero > probOne) {
                // calculating accuracies
                if (dataValues[dataValues.length-1].get(row) == 0) {
                    accuracy[0]++;
                }
                else {
                    accuracy[1]++;
                }
            }
            // else probOne is higher and the record evaluates to 1
            else {
                // calculating accuracies
                if (dataValues[dataValues.length-1].get(row) == 1) {
                    accuracy[0]++;
                }
                else {
                    accuracy[1]++;
                }
            }
        }
        return accuracy;
    }

    /**
     * Prints the class and attribute probabilities in the proper format.
     */
    public void printProbabilities() {
        System.out.printf("P(%s=0)=%.2f ", attributes[attributes.length-1], probabilities[probabilities.length-1][0]);
        for (int i = 0; i < probabilities.length-1; i++) {
            System.out.printf("P(%s=0|%s=0)=%.2f ", attributes[i], attributes[attributes.length-1], 1-probabilities[i][0]);
            System.out.printf("P(%s=1|%s=0)=%.2f ", attributes[i], attributes[attributes.length-1], probabilities[i][0]);
        }
        System.out.printf("%nP(%s=1)=%.2f ", attributes[attributes.length-1], probabilities[probabilities.length-1][1]);
        for (int i = 0; i < probabilities.length-1; i++) {
            System.out.printf("P(%s=0|%s=1)=%.2f ", attributes[i], attributes[attributes.length-1], 1-probabilities[i][1]);
            System.out.printf("P(%s=1|%s=1)=%.2f ", attributes[i], attributes[attributes.length-1], probabilities[i][1]);
        }
        System.out.println();
    }
}