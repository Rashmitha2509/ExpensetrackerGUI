import java.util.*;

public class ExpenseManager {

    private List<Expense> expenses = new ArrayList<>();

    public void addExpense(Expense e){
        expenses.add(e);
    }

    public List<Expense> getExpenses(){
        return expenses;
    }

    public double getTotalExpense(){

        double total = 0;

        for(Expense e : expenses){
            total += e.getAmount();
        }

        return total;
    }

    public Map<String,Double> getCategoryTotals(){

        Map<String,Double> map = new HashMap<>();

        for(Expense e : expenses){

            map.put(
                    e.getCategory(),
                    map.getOrDefault(e.getCategory(),0.0)+e.getAmount()
            );
        }

        return map;
    }
}