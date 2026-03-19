import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ExpenseGUI extends JFrame {

    ExpenseManager manager = new ExpenseManager();
    BudgetManager budgetManager = new BudgetManager();

    JTextField titleField = new JTextField(10);
    JComboBox<String> categoryBox =
            new JComboBox<>(new String[]{"Food","Travel","Bills","Shopping","Other"});
    JTextField amountField = new JTextField(8);
    JTextField dateField = new JTextField(8);
    JTextField descField = new JTextField(10);
    JTextField budgetField = new JTextField(8);

    JTextField searchField = new JTextField(10);

    JLabel totalLabel = new JLabel("Total Expense : 0");
    JLabel topCategoryLabel = new JLabel("Top Category : -");
    JLabel recordLabel = new JLabel("Records : 0");

    DefaultTableModel model = new DefaultTableModel();
    JTable table = new JTable(model);

    public ExpenseGUI(){

        UIManager.put("Panel.background", new Color(45,45,45));
        UIManager.put("Label.foreground", Color.WHITE);

        setTitle("Smart Expense Tracker Dashboard");
        setSize(1000,650);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Dashboard",createDashboard());
        tabs.add("Reports",createReportPanel());

        add(tabs);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    JPanel createDashboard(){

        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel();

        inputPanel.add(new JLabel("Title"));
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("Category"));
        inputPanel.add(categoryBox);

        inputPanel.add(new JLabel("Amount"));
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Date"));
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("Description"));
        inputPanel.add(descField);

        inputPanel.add(new JLabel("Budget"));
        inputPanel.add(budgetField);

        JButton addBtn = new JButton("Add Expense");
        JButton pieBtn = new JButton("Pie Chart");
        JButton barBtn = new JButton("Bar Chart");
        JButton reportBtn = new JButton("Monthly Report");
        JButton exportBtn = new JButton("Export CSV");

        inputPanel.add(addBtn);
        inputPanel.add(pieBtn);
        inputPanel.add(barBtn);
        inputPanel.add(reportBtn);
        inputPanel.add(exportBtn);

        panel.add(inputPanel,BorderLayout.NORTH);

        model.addColumn("Title");
        model.addColumn("Category");
        model.addColumn("Amount");
        model.addColumn("Date");

        panel.add(new JScrollPane(table),BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        bottomPanel.add(totalLabel);
        bottomPanel.add(topCategoryLabel);
        bottomPanel.add(recordLabel);

        panel.add(bottomPanel,BorderLayout.SOUTH);

        addBtn.addActionListener(e->addExpense());
        pieBtn.addActionListener(e->showPieChart());
        barBtn.addActionListener(e->showBarChart());
        reportBtn.addActionListener(e->generateReport());
        exportBtn.addActionListener(e->exportCSV());

        return panel;
    }

    JPanel createReportPanel(){

        JPanel panel = new JPanel();

        panel.add(new JLabel("Search Category"));
        panel.add(searchField);

        JButton searchBtn = new JButton("Search");

        panel.add(searchBtn);

        JTextArea area = new JTextArea(15,60);

        panel.add(new JScrollPane(area));

        searchBtn.addActionListener(e->{

            String category = searchField.getText();

            StringBuilder result = new StringBuilder();

            double total = 0;

            for(Expense exp: manager.getExpenses()){

                if(exp.getCategory().equalsIgnoreCase(category)){

                    result.append(exp.getTitle())
                            .append(" ")
                            .append(exp.getAmount())
                            .append("\n");

                    total+=exp.getAmount();
                }
            }

            result.append("\nTotal = ").append(total);

            area.setText(result.toString());
        });

        return panel;
    }

    void addExpense(){

        String title = titleField.getText();
        String category = (String)categoryBox.getSelectedItem();
        double amount = Double.parseDouble(amountField.getText());
        String date = dateField.getText();
        String desc = descField.getText();
        double budget = Double.parseDouble(budgetField.getText());

        budgetManager.setBudget(budget);

        Expense e = new Expense(title,category,amount,date,desc);

        manager.addExpense(e);

        FileManager.saveExpense(e);

        model.addRow(new Object[]{title,category,amount,date});

        double total = manager.getTotalExpense();

        totalLabel.setText("Total Expense : "+total);

        recordLabel.setText("Records : "+manager.getExpenses().size());

       if(budgetManager.isExceeded(total)){

    UIManager.put("OptionPane.background", Color.WHITE);
    UIManager.put("Panel.background", Color.WHITE);
    UIManager.put("OptionPane.messageForeground", Color.BLACK);
    UIManager.put("Button.background", Color.WHITE);
    UIManager.put("Button.foreground", Color.BLACK);

    JOptionPane.showMessageDialog(this,
            "⚠ Budget Exceeded!",
            "Warning",
            JOptionPane.WARNING_MESSAGE);
}
        updateTopCategory();
    }

    void updateTopCategory(){

        Map<String,Double> map = manager.getCategoryTotals();

        String top="-";
        double max=0;

        for(String c:map.keySet()){

            if(map.get(c)>max){
                max=map.get(c);
                top=c;
            }
        }

        topCategoryLabel.setText("Top Category : "+top);
    }

    void showPieChart(){

        JFrame frame=new JFrame("Expense Pie Chart");

        frame.add(new ChartPanel(manager.getCategoryTotals()));

        frame.setSize(600,500);
        frame.setVisible(true);
    }

    void showBarChart(){

        JFrame frame=new JFrame("Expense Bar Chart");

        frame.add(new BarChartPanel(manager.getCategoryTotals()));

        frame.setSize(650,500);
        frame.setVisible(true);
    }

    void generateReport(){

        Map<String,Double> map = manager.getCategoryTotals();

        StringBuilder report=new StringBuilder();

        report.append("MONTHLY EXPENSE REPORT\n\n");

        double total=0;

        for(String c:map.keySet()){

            double v=map.get(c);

            report.append(c).append(" : ").append(v).append("\n");

            total+=v;
        }

        report.append("\nTotal Expense : ").append(total);

        JTextArea area=new JTextArea(report.toString());

        JOptionPane.showMessageDialog(this,new JScrollPane(area));
    }

    void exportCSV(){

        JOptionPane.showMessageDialog(this,
                "Expenses saved in expenses.csv\nOpen with Excel.");
    }
}