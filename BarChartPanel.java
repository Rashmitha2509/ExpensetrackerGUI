import java.awt.*;
import java.util.*;
import javax.swing.*;

public class BarChartPanel extends JPanel {

    Map<String,Double> data;

    public BarChartPanel(Map<String,Double> data){
        this.data = data;
    }

    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        int x = 120;
        int width = 60;

        g.setFont(new Font("Arial",Font.BOLD,18));
        g.drawString("Category Spending",200,40);

        for(String category : data.keySet()){

            int height = data.get(category).intValue()/5;

            g.setColor(Color.BLUE);

            g.fillRect(x,400-height,width,height);

            g.setColor(Color.BLACK);

            g.drawString(category,x,420);

            g.drawString(""+data.get(category),x,380-height);

            x+=120;
        }
    }
}