//***********************************************************************************************************
//***********************************************************************************************************

//Class:    Individual
//Description: Object that is used to represent an individual

//***********************************************************************************************************
//***********************************************************************************************************
package k.means.algorithm;

/**
 *
 * @author bhafenri
 */
public class Individual {
    public double id;
    public double height;
    public double weight;
    public double sex;
    public double college;
    public double athleticism;
    public double rad;
    public double age;
    public double income;
    
    public Individual(){
        
    }
    
    public Individual(double id, double height, double weight, double sex, double college, double athleticism, double rad, double age, double income){
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.college = college;
        this.athleticism = athleticism;
        this.rad = rad;
        this.age = age;
        this.income = income;
    }
    
    @Override
    public String toString(){
        return "height: " + height + 
                ", weight: " + weight + 
                ", sex: " + sex + 
                ", college: " + college + 
                ", athleticism: " + athleticism + 
                ", RAD: " + rad + 
                ", age: " + age + 
                ", income: " + income; 
    }
}
