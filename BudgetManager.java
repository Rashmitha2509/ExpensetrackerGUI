public class BudgetManager {

    private double budget;

    public void setBudget(double budget){
        this.budget = budget;
    }

    public boolean isExceeded(double total){
        return total > budget;
    }

    public double getBudget(){
        return budget;
    }
}