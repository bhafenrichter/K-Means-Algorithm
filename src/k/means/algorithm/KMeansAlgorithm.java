package k.means.algorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class KMeansAlgorithm {

    static ArrayList<Individual> data;
    static int clusterCount;
    static boolean isNormalized;

    public static void main(String[] args) {
        data = new ArrayList<Individual>();
        String[] fileText = GetTextFromFile();

        KeyboardInputClass input = new KeyboardInputClass();
        String str = input.getKeyboardInput("Specify the number of clusters:");
        clusterCount = Integer.parseInt(str);

        str = input.getKeyboardInput("Normalized (Default) or Non-Normalized Values? \n1. Normalized \n2. Non-Normalized\n");
        if (str.equals("1")) {
            double[][] values = FormatAndNormalizeValues(fileText, true);
            createIndividuals(values);
        } else {
            double[][] values = FormatAndNormalizeValues(fileText, false);
            createIndividuals(values);
        }
        
        
    }

    public static String[] GetTextFromFile() {
        TextFileClass textFile = new TextFileClass();
        textFile.getFileName("Specify the text file to be read:");
        textFile.getFileContents();

        return textFile.text;
    }

    private static double[][] FormatAndNormalizeValues(String[] fileText, boolean isNormalized) {
        double[][] vals = new double[fileText.length][9];
        int breakpoint = 0;
        //converts all vals in file to int[][]
        for (int i = 0; i < fileText.length; i++) {
            if (fileText[i] == "" || fileText[i] == null) {
                //where to stop iterating
                breakpoint = i;
                break;
            }

            String[] row = fileText[i].split(", ");
            for (int j = 0; j < row.length; j++) {
                int cell = Integer.parseInt(row[j]);
                vals[i][j] = cell;
            }
        }

        //don't normalize values, just return the values we currently have
        if (!isNormalized) {
            return vals;
        }

        //find maxes and mins and calculate normalized value
        for (int i = 0; i < 9; i++) {

            double max = 0;
            double min = 1000000;
            for (int j = 0; j < breakpoint; j++) {
                double val = vals[j][i];

                //calculates max and min
                if (val > max) max = val;
                if (val < min) min = val;
            }

            //uses max and min to calculate new value
            for (int j = 0; j < breakpoint; j++) {
                vals[j][i] = (vals[j][i] - min) / (max - min);
            }
        }
        return vals;
    }

    public static void createIndividuals(double[][] vals) {
        for (int i = 0; i < vals.length; i++) {
            Individual cur = new Individual(
                    vals[i][0],
                    vals[i][1],
                    vals[i][2],
                    vals[i][3],
                    vals[i][4],
                    vals[i][5],
                    vals[i][6],
                    vals[i][7],
                    vals[i][8]);
            data.add(cur);    
        }
    }
}
