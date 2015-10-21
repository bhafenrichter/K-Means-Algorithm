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
        if(str.equals("1")){
            
            createIndividuals(NormalizeValues(fileText));
        }else{
            createIndividuals(fileText);
        }
    }
    
    public static String[] GetTextFromFile() {
        TextFileClass textFile = new TextFileClass();
        textFile.getFileName("Specify the text file to be read:");
        textFile.getFileContents();

        return textFile.text;
    }

    private static String[] NormalizeValues(String[] fileText) {
        String[] normalizedVals = new String[9];
        return normalizedVals;
    }
    
    public static void createIndividuals(String[] fileText){
        for (int i = 0; i < fileText.length; i++) {
            if(fileText[i] != null){
                String[] attributes = fileText[i].split(", ");
                Individual cur = new Individual(Integer.parseInt(attributes[0]),
                    Double.parseDouble(attributes[1]),
                    Double.parseDouble(attributes[2]),
                    Double.parseDouble(attributes[3]),
                    Double.parseDouble(attributes[4]),
                    Double.parseDouble(attributes[5]),
                    Double.parseDouble(attributes[6]),
                    Double.parseDouble(attributes[7]),
                    Double.parseDouble(attributes[8]));
                data.add(cur);
                
            }else{
                break;
            }
        }
    }
}
