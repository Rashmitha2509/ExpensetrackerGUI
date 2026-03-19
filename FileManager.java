import java.io.*;

public class FileManager {

    public static void saveExpense(Expense e){

        try{

            FileWriter fw = new FileWriter("expenses.csv",true);

            fw.write(
                    e.getTitle()+","+
                    e.getCategory()+","+
                    e.getAmount()+","+
                    e.getDate()+","+
                    e.getDescription()+"\n"
            );

            fw.close();

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}