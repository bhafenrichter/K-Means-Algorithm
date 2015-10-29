//Program: Hafenrichter.java
//Course: COSC 420
//Description: An implementation of the k-means algorithm
//Author: Brandon Hafenrichter
//Revised: October 29, 2015
//Language: Java
//IDE: Netbeans 8.0.2
//***********************************************************************************************************
//***********************************************************************************************************

package k.means.algorithm;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//Class Main.java
//Description: Contains the essential methods and calls required to run the k-means algorithm
//***********************************************************************************************************
//***********************************************************************************************************

public class KMeansAlgorithm {

    static ArrayList<Individual> data;  //contains all of the data cast into an arraylist of individuals
    static int clusterCount;            //the number of clusters specified by the user
    static ArrayList<Individual> means; //the collection of means (number specified by user) cast into Individual
    static double[][] dataValues;       //all of the data values read from the string in an double[][]
    static int[] previousClusterSizes;  //an array that keeps track of the cluster sizes that will be used to determine when to stop clustering
    static int errorCount;              //keeps track of how many times we've gotten a cluster with 0 elements in it
    static boolean isNormalized;        //tells program whether user would like the data normalized or not
    static String[] fileText;           //the file text specified by the user

//***********************************************************************************************************
//Method: Main
//Description: Contains the essential methods and calls required to run the algorithms
//Returns: None
//Calls: getUserAction()               
//Parameters: String[] args             standard parameters for the main method
//***********************************************************************************************************
    public static void main(String[] args) {
        getUserAction();
    }

//***********************************************************************************************************
//Method: getUserAction()
//Description: Initializes static variables and prompts user for necessary information
//Returns: None
//Calls: initializeClustering()         
//Parameters: None
//***********************************************************************************************************
    public static void getUserAction() {
        //initialize static variables
        data = new ArrayList<Individual>();
        means = new ArrayList<Individual>();
        fileText = GetTextFromFile();
        clusterCount = 0;
        
        //gets the number of custers from user
        KeyboardInputClass input = new KeyboardInputClass();
        String str = input.getKeyboardInput("Specify the number of clusters:");
        clusterCount = Integer.parseInt(str);

        previousClusterSizes = new int[clusterCount];

        //react based on input
        str = input.getKeyboardInput("Normalized (Default) or Non-Normalized Values? \n1. Normalized \n2. Non-Normalized\n");
        if (str.equals("1")) {
            //normalize and create objects
            isNormalized = true;
            initializeClustering(isNormalized);
        } else {
            isNormalized = false;
            initializeClustering(isNormalized);
        }
        str = input.getKeyboardInput("Run again? (Y / N)");
        if (str.toLowerCase().equals("y")) {
            getUserAction();
        } else {
            System.exit(-1);
        }
    }

//***********************************************************************************************************
//Method: initializeClustering
//Description: Generates the Means, normalizes values, and calls the clustering algorithms
//Returns:  None
//Calls:    generateRandomMeans()
//          FormatandNormalizeValues()
//          createIndividuals()
//          cluster()                   
//Parameters: None
//***********************************************************************************************************
    
    public static void initializeClustering(boolean isNormalized){

        means = generateRandomMeans(clusterCount);
        dataValues = FormatAndNormalizeValues(fileText, isNormalized);
        createIndividuals(dataValues);
        cluster(means);
    }

//***********************************************************************************************************
//Method: cluster
//Description: Clusters all of the data based on the means given
//Returns:  None
//Calls:    distance()
//          printClusters()
//          cluster()                   
//Parameters: ArrayList<Individual> gives the algorithm the neccessary means to use for clustering
//***********************************************************************************************************
    
    public static void cluster(ArrayList<Individual> means) {
        //evaluate all of the distances between the various means
        double[][] distances = new double[means.size()][data.size()];
        for (int i = 0; i < means.size(); i++) {
            Individual mean = means.get(i);
            for (int j = 0; j < data.size(); j++) {
                distances[i][j] = distance(mean, data.get(j));
            }
        }

        //designate the points to their various cluster based on the distance
        ArrayList<ArrayList<Individual>> clusters = new ArrayList<ArrayList<Individual>>();
        //initialize clusters
        for (int i = 0; i < clusterCount; i++) {
            clusters.add(new ArrayList<Individual>());
        }

        for (int i = 0; i < data.size(); i++) {
            Individual curIndividual = data.get(i);
            double bestDistance = 1000;
            int meanIndex = 0;
            for (int j = 0; j < means.size(); j++) {
                //find the best distance
                double distance = distance(curIndividual, means.get(j));
                if (distance < bestDistance) {
                    bestDistance = distance;
                    meanIndex = j;
                }
            }
            clusters.get(meanIndex).add(curIndividual);
        }

        ArrayList<Individual> newMeans = new ArrayList<Individual>();
        //recalculate the mean. Average all of the individuals attributes in cluster
        for (int i = 0; i < clusters.size(); i++) {
            //base attributes
            double h, w, s, c, a, r, ag, in;
            h = w = s = c = a = r = ag = in = 0;
            for (int j = 0; j < clusters.get(i).size(); j++) {
                h += clusters.get(i).get(j).height;
                w += clusters.get(i).get(j).weight;
                s += clusters.get(i).get(j).sex;
                c += clusters.get(i).get(j).college;
                a += clusters.get(i).get(j).athleticism;
                r += clusters.get(i).get(j).rad;
                ag += clusters.get(i).get(j).age;
                in += clusters.get(i).get(j).income;
            }
            Individual mean = new Individual();
            mean.height = h / clusters.get(i).size();
            mean.weight = w / clusters.get(i).size();
            mean.sex = s / clusters.get(i).size();
            mean.college = c / clusters.get(i).size();
            mean.athleticism = a / clusters.get(i).size();
            mean.rad = r / clusters.get(i).size();
            mean.age = ag / clusters.get(i).size();
            mean.income = in / clusters.get(i).size();

            newMeans.add(mean);
        }

        //evaluate whether any elements in the clusters moved
        int[] currentClusterSizes = new int[clusterCount];
        for (int i = 0; i < clusterCount; i++) {
            currentClusterSizes[i] = clusters.get(i).size();
        }

        if (Arrays.equals(currentClusterSizes, previousClusterSizes)) {
            boolean invalid = false;
            //check if any of the clusters came out to 0, if so restart
            for (int i = 0; i < currentClusterSizes.length; i++) {
                int cur = currentClusterSizes[i];
                if (cur == 0) {
                    invalid = true;
                }
            }

            if (invalid) {
                errorCount++;
                
                if(errorCount == 50){
                    System.out.println("There was a cluster with 0 elements. Try using less clusters.");
                    getUserAction();
                }
                
                initializeClustering(isNormalized);
            } else {
                errorCount = 0;
                printClusters(clusters);
            }

        } else {
            previousClusterSizes = currentClusterSizes;
            cluster(newMeans);
        }
    }

//***********************************************************************************************************
//Method: generateRandomMeans
//Description: Intializes the first random means based on the number of clusters specified
//Returns:  ArrayList<Individual>   all of the means in a collection
//Calls:    None                
//Parameters: int size              the number of clusters needed to be generated
//***********************************************************************************************************
    
    public static ArrayList<Individual> generateRandomMeans(int size) {
        Random rand = new Random();
        ArrayList<Individual> randomized = new ArrayList<Individual>();
        for (int j = 0; j < size; j++) {
            randomized.add(new Individual());
        }
        return randomized;
    }

//***********************************************************************************************************
//Method: GetTextFromFile
//Description: Gets the text from the file specified by the user and casts it to string[]
//Returns:  String[]                all of the elements in the text file
//Calls:    getFileName()
//          getFileContents()           
//Parameters: None
//***********************************************************************************************************
    
    public static String[] GetTextFromFile() {
        TextFileClass textFile = new TextFileClass();
        textFile.getFileName("Specify the text file to be read:");
        textFile.getFileContents();

        return textFile.text;
    }

//***********************************************************************************************************
//Method: FormatAndNormalizeValues
//Description: Takes the fileText and converts it into a double[][] that represents all of the data.  It also
//             normalizes the data if neccessary
//Returns:  double[][]              all of the data in a double[][]
//Calls:    updateMeans()              
//Parameters: String[] fileText     text from the file to be converted
//            boolean isNormalized  flag to see whether data needs to be normalized or not
//***********************************************************************************************************
    private static double[][] FormatAndNormalizeValues(String[] fileText, boolean isNormalized) {
        //global variable to be used later
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

        //splices all of the empty entries
        vals = Arrays.copyOf(vals, breakpoint);

        //find maxes and mins and calculate normalized value if neccessary
        for (int i = 1; i < 9; i++) {

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

            //normalizes values
            if (isNormalized) {
                //uses max and min to calculate new value
                for (int j = 0; j < breakpoint; j++) {
                    vals[j][i] = (vals[j][i] - min) / (max - min);
                }
            }

            updateMeans(i, max, min, isNormalized);

        }
        return vals;
    }

//***********************************************************************************************************
//Method: distance
//Description: Finds the distance between two individuals
//Returns:  double                  distance between the two individuals
//Calls:    None                  
//Parameters: Individual a          first individual to be compared
//            Individual b          second individual to be compared
//***********************************************************************************************************
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

//***********************************************************************************************************
//Method: createIndividuals
//Description: Casts double[][] data into a clean object
//Returns:  None
//Calls:    None                 
//Parameters: double[][] vals           all of the data to be converted to individuals with their attributes
//***********************************************************************************************************
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

//***********************************************************************************************************
//Method: updateMeans
//Description: randomizes and normalizes the means if neccessary. It uses the max and min to randomize intelligently
//Returns:  None
//Calls:    None                  
//Parameters: int i             index of the attribute we are modifying
//            double max        max value used for randomizing and normalizing
//            double min        min value used for randomizing and normalizing
//            boolean isNormalized  flag that tells us if we need to normalize value
//***********************************************************************************************************
    private static void updateMeans(int i, double max, double min, boolean isNormalized) {
        switch (i) {
            case 1:
                for (int j = 0; j < means.size(); j++) {
                    Random rand = new Random();
                    means.get(j).height = min + (max - min) * rand.nextDouble();
                    if (isNormalized) {
                        means.get(j).height = (means.get(j).height - min) / (max - min);
                    } else {
                        
                    }

                }
                break;
            case 2:
                for (int j = 0; j < means.size(); j++) {
                    Random rand = new Random();
                    means.get(j).weight = min + (max - min) * rand.nextDouble();
                    if (isNormalized) {
                        means.get(j).weight = (means.get(j).weight - min) / (max - min);
                    }else{
                        
                    }
                }
                break;
            case 3:
                for (int j = 0; j < means.size(); j++) {
                    Random rand = new Random();
                    means.get(j).sex = min + (max - min) * rand.nextDouble();
                    if (isNormalized) {
                        means.get(j).sex = (means.get(j).sex - min) / (max - min);
                    }else{
                        
                    }
                }
                break;
            case 4:
                for (int j = 0; j < means.size(); j++) {
                    Random rand = new Random();
                    means.get(j).college = min + (max - min) * rand.nextDouble();
                    if (isNormalized) {
                        means.get(j).college = (means.get(j).college - min) / (max - min);
                    }else{
                        
                    }
                }
                break;
            case 5:
                for (int j = 0; j < means.size(); j++) {
                    Random rand = new Random();
                    means.get(j).athleticism = min + (max - min) * rand.nextDouble();
                    if (isNormalized) {
                        means.get(j).athleticism = (means.get(j).athleticism - min) / (max - min);
                    }else{
                        
                    }
                }
                break;
            case 6:
                for (int j = 0; j < means.size(); j++) {
                    Random rand = new Random();
                    means.get(j).rad = min + (max - min) * rand.nextDouble();
                    if (isNormalized) {
                        means.get(j).rad = (means.get(j).rad - min) / (max - min);
                    }else{
                        
                    }
                }
                break;
            case 7:
                for (int j = 0; j < means.size(); j++) {
                    Random rand = new Random();
                    means.get(j).age = min + (max - min) * rand.nextDouble();
                    if (isNormalized) {
                        means.get(j).age = (means.get(j).age - min) / (max - min);
                    }else{
                        
                    }
                }
                break;
            case 8:
                for (int j = 0; j < means.size(); j++) {
                    Random rand = new Random();
                    means.get(j).income = min + (max - min) * rand.nextDouble();
                    if (isNormalized) {
                        means.get(j).income = (means.get(j).income - min) / (max - min);
                    }else{
                        
                    }
                }
                break;
        }
    }
//***********************************************************************************************************
//Method: printClusters
//Description: Prints the clusters once a valid solution has been created
//Returns:  None
//Calls:    None                
//Parameters: ArrayList<ArrayList<Individual>> the clusters that need to be printed 
//***********************************************************************************************************
    public static void printClusters(ArrayList<ArrayList<Individual>> clusters) {
        DecimalFormat df = new DecimalFormat("#.###");
        for (int i = 0; i < clusters.size(); i++) {
            ArrayList<Individual> cluster = clusters.get(i);
            System.out.println("Cluster " + (i + 1));
            System.out.println("ID\tHeight\tWeight\tSex\tCollege\tAth.\tRAD\tAge\tIncome");
            for (int j = 0; j < cluster.size(); j++) {
                Individual cur = cluster.get(j);
                System.out.println((int) cur.id + "\t" + df.format(cur.height) + "\t" + df.format(cur.weight) + "\t" + (int)cur.sex + "\t" + df.format(cur.college) + "\t" + df.format(cur.athleticism) + "\t" + df.format(cur.rad) + "\t" + df.format(cur.age) + "\t" + df.format(cur.income));
            }
            System.out.println("");
        }
    }
}
