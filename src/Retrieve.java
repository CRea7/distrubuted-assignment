import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Retrieve implements ActionListener{

    //when the programme is opened the data is passed in here
    public static ResultSet rs = null;

    public static void main(String[] args) {
        String[] Genders = new String[] {"Male","Female", "Other"};

        JFrame f = new JFrame();
        JLabel blank =new JLabel("");
        JLabel title = new JLabel("Employee details");
        JLabel label1 = new JLabel("SSN: ");
        JLabel label2 = new JLabel("DOB: ");
        JLabel LName = new JLabel("Name");
        JLabel LAddress = new JLabel("Address");
        JLabel LSalary = new JLabel("Salary");
        JLabel LGender = new JLabel("gender");

        JTextField text1 = new JTextField(20);
        JTextField text2 = new JTextField(20);
        JTextField TName = new JTextField(20);
        JTextField TAddress = new JTextField(20);
        JTextField TSalary = new JTextField(20);
        JComboBox<String> TGender = new JComboBox<>(Genders);

        JButton next = new JButton("NEXT");
        JButton previous = new JButton("PREVIOUS");
        JButton clear = new JButton("CLEAR");
        JButton add = new JButton("ADD");
        JButton delete = new JButton("Delete");
        JButton update = new JButton("UPDATE");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_create_DB","root","");
            Statement st=con.createStatement();
            rs = st.executeQuery("select * from data");
            String name = "", gender = "",SSn = "", Address="", DOB = "", Salary = "";
            if (rs.next()) {
                name = rs.getString("name");
                gender = rs.getString("Gender");
                SSn = rs.getString("SSn");
                Address = rs.getString("Address");
                DOB = rs.getString("DOB");
                Salary = rs.getString("Salary");
            }
            text1.setText(SSn);
            text2.setText(DOB);
            TName.setText(name);
            TAddress.setText(Address);
            TSalary.setText(Salary);
            TGender.setSelectedItem(gender);

        } catch (Exception e) {}

        //gets the next person in list
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    if (rs.next()) {
                        next.setEnabled(true);
                        previous.setEnabled(true);

                        //sets the correct text boxes to the next record
                        text1.setText(rs.getString("SSn"));
                        text2.setText(rs.getString("DOB"));
                        TName.setText(rs.getString("name"));
                        TAddress.setText(rs.getString("Address"));
                        TSalary.setText(rs.getString("Salary"));
                        TGender.setSelectedItem(rs.getString("Gender"));

                        //checks if the record is the last record if it is disables the next button
                        if (rs.isLast()){
                            next.setEnabled(false);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        //gets previous person in list
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    if (rs.previous()){
                        previous.setEnabled(true);
                        next.setEnabled(true);

                        //sets the assigned text boxes to the previous record
                        text1.setText(rs.getString("SSn"));
                        text2.setText(rs.getString("DOB"));
                        TName.setText(rs.getString("name"));
                        TAddress.setText(rs.getString("Address"));
                        TSalary.setText(rs.getString("Salary"));
                        TGender.setSelectedItem(rs.getString("Gender"));

                        //checks if the record is the first record if it is it disables the previous button
                        if (rs.isFirst()) {
                            previous.setEnabled(false);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        //clears table
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //sets all the text boxes to blank
                text1.setText("");
                text2.setText("");
                TName.setText("");
                TAddress.setText("");
                TSalary.setText("");
                TGender.setSelectedItem("");
            }
        });
        //adds input to database
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                //this is an insert query that will be executed by the statement function
                String query = "INSERT INTO `data` " +
                        "(`Gender`, `SSn`, `DOB`, `Salary`, `Address`, `name`) VALUES " +
                        "(" +
                        "'" + TGender.getSelectedItem() + "'," +
                        "'" + text1.getText() + "'," +
                        "'" + text2.getText() + "'," +
                        "'" + TSalary.getText() + "'," +
                        "'" + TAddress.getText() + "'," +
                        "'" + TName.getText() + "');";
                //action this query does
                String action = "added";
                //function to execute query
                statement(query,action);
            }
        });
        //deletes selected person
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                //sql delete query
                String query  = "DELETE FROM `data` WHERE SSn ='"+ text1.getText()+"'";
                //action query completes
                String action = "delete";
                //calls function that executes the query
                statement(query,action);

            }
        });
        //updates selected person
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                //sql update query
                String query = "UPDATE data SET " +
                        "Gender ='" + TGender.getSelectedItem() +
                        "',SSn ='" + text1.getText() +
                        "',DOB ='" + text2.getText() +
                        "',Salary ='" + TSalary.getText() +
                        "',Address ='" + TAddress.getText() +
                        "',name ='" + TName.getText() +
                        "' WHERE SSn = '"+ text1.getText() +"';";

                //the action this query does
                String action = "update";
                //this function executes the query
                statement(query, action);

            }
        });

        JPanel p = new JPanel(new GridLayout(10, 2));

        p.add(title);
        p.add(blank);
        p.add(previous);
        p.add(next);
        p.add(label1);
        p.add(text1);
        p.add(label2);
        p.add(text2);
        p.add(LName);
        p.add(TName);
        p.add(LAddress);
        p.add(TAddress);
        p.add(LSalary);
        p.add(TSalary);
        p.add(LGender);
        p.add(TGender);
        p.add(clear);
        p.add(add);
        p.add(delete);
        p.add(update);
        f.add(p);
        f.setVisible(true);
        f.pack();
    }

    public void actionPerformed(ActionEvent e){
    }

    //this function executes an sql query that is passed into it and notifies the user if it has been a success
    public static void statement(String query, String action){
        try {
            // creates a connection string to the server
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_create_DB", "root", "");
            Statement st = con.createStatement();

            //executes the passed in query
            st.executeUpdate
                    (query);
            //this shows a pop up notifying user of action success
            JOptionPane.showMessageDialog(null, "Record "+ action + "!");
            st.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Record not "+ action + "!");
            e.printStackTrace();
        }
    }
}