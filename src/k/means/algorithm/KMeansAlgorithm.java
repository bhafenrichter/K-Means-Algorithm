package k.means.algorithm;

import java.util.ArrayList;
import java.util.Random;

public class KMeansAlgorithm {

    static ArrayList<Individual> data;
    static int clusterCount;
    static boolean isNormalized;
    static ArrayList<Individual> means;

    public static void main(String[] args) {
        data = new ArrayList<Individual>();
        means = new ArrayList<Individual>();
        String[] fileText = GetTextFromFile();

        //gets the number of lcusters from user
        KeyboardInputClass input = new KeyboardInputClass();
        String str = input.getKeyboardInput("Specify the number of clusters:");
        clusterCount = Integer.parseInt(str);

        //react based on input
        str = input.getKeyboardInput("Normalized (Default) or Non-Normalized Values? \n1. Normalized \n2. Non-Normalized\n");
        if (str.equals("1")) {
            //normalize and create objects
            double[][] values = FormatAndNormalizeValues(fileText, true);
            createIndividuals(values);
            
            //generates first random means
            means = generateRandomMeans(clusterCount);
            
            cluster(means);
        } else {
            //Create individual objects
            double[][] values = FormatAndNormalizeValues(fileText, false);
            createIndividuals(values);
            
            //generates first random means
            means = generateRandomMeans(clusterCount);
            cluster(means);
        }
    }

    public static void cluster(ArrayList<Individual> means) {

    }

    public static ArrayList<Individual> generateRandomMeans(int size) {
//        Random rand = new Random();
//        Individual i = new Individual();
//        i.height = rand.nextInt((100 - 1) + 1) + 1;
//        i.weight = rand.nextInt((350 - 10) + 1) + 10;
//        i.sex = rand.nextInt((1 - 0) + 1) + 0;
//        i.college = rand.nextInt((4 - 1) + 1) + 1;
//        i.athleticism = rand.nextInt((10 - 1) + 1) + 1;
//        i.rad = rand.nextInt((10 - 1) + 1) + 1;
//        i.age = rand.nextInt((100 - 1) + 1) + 1;
//        i.income = rand.nextInt((1000000 - 10000) + 1) + 10000;
//        return i;
        ArrayList<Individual> means = new ArrayList<Individual>();
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            means.add(data.get(rand.nextInt((data.size() - 1) + 1) + 1));
        }
        return means;
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
                if (val > max) {
                    max = val;
                }
                if (val < min) {
                    min = val;
                }
            }

            //uses max and min to calculate new value
            for (int j = 0; j < breakpoint; j++) {
                vals[j][i] = (vals[j][i] - min) / (max - min);
            }
        }
        return vals;
    }

    public static double distance(Individual a, Individual b) {
        //closer to 0, the closer the distance is
        double distance = Math.pow(b.age - a.age, 2)
                + Math.pow(b.athleticism - a.athleticism, 2)
                + Math.pow(b.college - a.college, 2)
                + Math.pow(b.height - a.height, 2)
                + Math.pow(b.income - a.income, 2)
                + Math.pow(b.rad - a.rad, 2)
                + Math.pow(b.sex - a.sex, 2)
                + Math.pow(b.weight - a.weight, 2);
        return Math.sqrt(distance);
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
