public class Polynomial{
    public double[] coefficients;
    
    public Polynomial(){
        this.coefficients = new double[]{0};  
    }
    
    public Polynomial(double[] input1){
        this.coefficients = input1;
    }

    public Polynomial add(Polynomial p){

        //finding what to assign and loop over 
        int small = Math.min(p.coefficients.length, this.coefficients.length);
        int big = Math.max(p.coefficients.length, this.coefficients.length);
        
        //making ans array that will be the size of big
        double[] ans = new double[big];

        for(int i=0; i<big;i++){
            if(i<p.coefficients.length){
                ans[i] +=p.coefficients[i];
            }
            if(i<this.coefficients.length){
                ans[i] +=this.coefficients[i];
            }
        }

        Polynomial final_ans = new Polynomial(ans);
        return final_ans;

    }

    public double evaluate(double x){
        double ans = 0;

        for(int i=0; i<this.coefficients.length;i++){
            ans += Math.pow(x,i)*(this.coefficients[i]);
        }

        return ans;
    }
    
    public boolean hasRoot(double x){
        double sum = evaluate(x);
        return (sum==0);   
    }
    
}