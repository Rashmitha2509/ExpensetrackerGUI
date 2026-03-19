import java.awt.*;
import java.util.*;
import javax.swing.*;

public class ChartPanel extends JPanel {

    Map<String,Double> data;

    public ChartPanel(Map<String,Double> data){
        this.data = data;
    }

    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        double total = 0;

        for(double v : data.values()){
            total += v;
        }

        int startAngle = 0;

        Color[] colors = {
                Color.RED,Color.BLUE,Color.GREEN,
                Color.ORANGE,Color.MAGENTA,Color.CYAN
        };

        int i=0;
        int legendY=80;

        g.setFont(new Font("Arial",Font.BOLD,18));
        g.drawString("Expense Distribution",180,40);

        for(String category : data.keySet()){

            double value = data.get(category);

            int angle = (int)((value/total)*360);

            g.setColor(colors[i%colors.length]);

            g.fillArc(100,100,250,250,startAngle,angle);

            g.fillRect(400,legendY,20,20);

            g.setColor(Color.BLACK);
            g.drawString(category+" : "+value,430,legendY+15);

            legendY+=30;

            startAngle+=angle;

            i++;
        }
    }
}