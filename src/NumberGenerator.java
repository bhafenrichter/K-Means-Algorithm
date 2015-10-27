
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bhafenri
 */
public class NumberGenerator {

    public static void main(String[] args) {
        Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            int height = rand.nextInt((100 - 0) + 1) + 0;
            int weight = rand.nextInt((350 - 10) + 1) + 10;
            int sex = rand.nextInt((1 - 0) + 1) + 0;
            int college = rand.nextInt((4 - 1) + 1) + 1;
            int athleticism = rand.nextInt((10 - 1) + 1) + 1;
            int RAD = rand.nextInt((10 - 1) + 1) + 1;
            int age = rand.nextInt((100 - 1) + 1) + 1;
            int income = rand.nextInt((1000000 - 10000) + 1) + 10000;
            System.out.println(i + ", " + height + ", " + weight + ", " + sex + ", " + college + ", " + athleticism + ", " + RAD + ", " + age + ", " + income);
        }
    }
}
