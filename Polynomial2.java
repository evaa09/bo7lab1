import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
public class Polynomial2{

    //from lab 1
    //public double[] coefficients;

    //from lab 2
    public double[] coefficients_unor;
    public int[] exponents;

    public Polynomial2(){
        this.coefficients_unor = new double[]{0};
        this.exponents = new int[]{0};  
    }
    
    public Polynomial2(double[] input_co, int[] input_exp){
        this.coefficients_unor = input_co;
        this.exponents = input_exp;
    }

    public Polynomial2(File file){
        try{
        Scanner my_scan= new Scanner(file);
        String my_poly = my_scan.nextLine();
        //We will add pluses before the minuses so we can split it
        String new_poly = "";
        for(int i=0; i<my_poly.length();i++){
            if(my_poly.charAt(i)=='-'){
                new_poly += "+-";
            }else{
                new_poly += String.valueOf(my_poly.charAt(i));
            }
        }

        String[] terms= new_poly.split("+");
        int[] exp = new int[terms.length];
        double[] coeff= new double[terms.length];

        for(int i=0; i<terms.length;i++){
            String temp = terms[i];
            if(temp.indexOf('x')==-1){
                //this is for constant term
                exp[i] = 0;
                coeff[i] = Double.parseDouble(temp);
            }else if(temp.length()==1){
                //this means it is just x
                exp[i] = 1;
                coeff[i] = 1;
            }else if(temp.length()>=2 && temp.charAt(0)=='x'){
                //there is no coefficient
                coeff[i] = 1;
                exp[i] = Integer.parseInt(temp.substring(1));
            }else if(temp.indexOf('x')== (temp.length()-1)){
                //means it is just x with some coefficient
                exp[i]=1;
                coeff[i] = Double.parseDouble(temp.substring(0, temp.length()-1));
            }else{
                //now there is an exponent and coefficient
                String[] parts = temp.split("x");
                coeff[i] = Double.parseDouble(parts[0]);
                exp[i]= Integer.parseInt(parts[1]);
            }

            this.coefficients_unor= coeff;
            this.exponents = exp;
        }
        }
        catch(FileNotFoundException e){

        }


    }

    public Polynomial2 add(Polynomial2 p){
        //Strategy is to make an array of all the elements of both polynomials and then add and remove redundent terms
        int[] master_exp = new int[(p.exponents.length)+(this.exponents.length)];
        double[] master_coeff = new double[(p.exponents.length)+(this.exponents.length)];
        int count=0;
        for(int i=0; i<this.exponents.length; i++){
            master_exp[count]= this.exponents[i];
            master_coeff[count] = this.coefficients_unor[i];
            count++;
        }

        for(int j=0; j<p.exponents.length;j++){
            master_exp[count]= p.exponents[j];
            master_coeff[count] = p.coefficients_unor[j];
            count++;
        }

        return add_redundent_terms(master_coeff, master_exp, count);



    }

    public double evaluate(double x){
        double ans = 0;

        for(int i=0; i<this.coefficients_unor.length;i++){
            ans += Math.pow(x,this.exponents[i])*(this.coefficients_unor[i]);
        }

        return ans;
    }
    
    public boolean hasRoot(double x){
        double sum = evaluate(x);
        return (sum==0);   
    }

    public Polynomial2 multiply(Polynomial2 p){
        // making some variables needed to expand polynomials
        int len = (int) (p.coefficients_unor.length)*(this.coefficients_unor.length);
        double[] ans_co = new double[len];
        int[] ans_exp = new int[len];

        //count is to update answer arrays and spot last place of array
        int count=0;

        //iterate and multiply all the terms, updates answer arrays 
        for(int i=0; i<(int)p.coefficients_unor.length; i++){
            for(int j=0; j<(int)this.coefficients_unor.length;j++){
                //we need to multiply coeffecients
                ans_co[count] = (p.coefficients_unor[i])*(this.coefficients_unor[j]);
                ans_exp[count] = p.exponents[i] + (this.exponents[j]);
                count++;
            }
        }

        //Now removes redundent terms and returns
        return add_redundent_terms(ans_co, ans_exp, count);


    }

    public Polynomial2 add_redundent_terms(double[] temp_co, int[] temp_exp, int len){
        //Make a hashmap
        HashMap<Integer,Double> ans = new HashMap<>();

        for(int i=0; i<len; i++){
            //Case 1: exponent already exists
            if(ans.containsKey(temp_exp[i])){
                //check if they make zero
                double sum = ans.get(temp_exp[i])+temp_co[i];
                if(sum!=0.0){
                    ans.put(temp_exp[i], sum);
                }else{
                    //need to remove key because 0 times anything is zero
                    ans.remove(temp_exp[i]);
                }
                
            }else{
                //Case 2: expoenet doesnt exist
                ans.put(temp_exp[i], temp_co[i]);
            }
            
        }
        
        //Lets make the answer arrays and count to accumulate
        int size = ans.size();
        int ans_exp[] = new int[size];
        double ans_coeff[] = new double[size];
        int count = 0;

        for(Map.Entry<Integer, Double> entry: ans.entrySet()){
            //must make sure that coefficient is not 0
            if(entry.getValue() != 0.0){
                ans_exp[count] = entry.getKey();
                ans_coeff[count] = entry.getValue();
                count++;
            }
        }
        return new Polynomial2(ans_coeff, ans_exp);


    }

    public void saveToFile(String name){
        String ans = "";
        try{
            for(int i=0; i<this.exponents.length;i++){
                ans = ans +  this.coefficients_unor[i] + "x" + this.exponents[i];
            }
            FileWriter myWriter = new FileWriter(name);
            myWriter.write(ans);
            myWriter.close();
        }
        catch(Exception e){
            
        }
    }
    
}