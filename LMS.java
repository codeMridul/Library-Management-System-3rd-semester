/*
@author : Mridul Mishra
*/
import java.io.*;
import java.sql.*;
import java.util.*;
import java.time.*;

interface operations
{
    public int login()throws IOException;
    public int signup()throws IOException;
    public boolean bookIssue(int studentId,String bookName );
}
public class LMS implements operations
{
    static Connection con=null;
    int c=3;
    BufferedReader in =new BufferedReader(new InputStreamReader(System.in));
    @Override
    public int login()throws IOException                                       //Login into the database function
    {
        System.out.print("Enter your UserId: ");
        int uId= Integer.parseInt(in.readLine());
        System.out.print("\nEnter the Password: ");
        String psd= in.readLine();
        String fetchData="SELECT password from studentData where userId= '"+Integer.toString(uId)+"'"; 
        try
        {
            PreparedStatement pstmt = con.prepareStatement(fetchData);
            ResultSet rSet=pstmt.executeQuery();
            String receivedPassword=rSet.getString("password");
            if (receivedPassword.contentEquals(psd)==true)
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                System.out.println("Login Successful\n\n");
                c++;
            }
            else
            {   
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                System.out.println("Wrong Password, Enter Again.  :)");
                c--;
                System.out.println("Chances Left : "+c);
                if(c!=0)
                {
                    login();
                }
                else
                {
                    System.out.print("\033[H\033[2J");  
                    System.out.flush();
                    System.out.println("</Attempted to login 3 times with wrong userId/password>");
                    System.exit(0);
                }                
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return uId;
    }
    @Override
    public int signup() throws IOException                               //SignUp to the database function
    {
        System.out.print("Enter your UserId: ");
        int uId= Integer.parseInt(in.readLine());
        System.out.print("\nEnter the Password: ");
        String psd= in.readLine();
        String insertData= "INSERT INTO studentdata(userId, password) VALUES(?,?)";
        int stuId=0;
        try
        {
            PreparedStatement pstmt = con.prepareStatement(insertData);
            pstmt.setInt(1, uId);
            pstmt.setString(2, psd);
            pstmt.executeUpdate();
            System.out.print("\033[H\033[2J");  
            System.out.flush();
            System.out.println("</New User Created>");
            System.out.println("Login to continue: ");
            stuId=login();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("SignUp Error");
        } 
        return stuId;     
    } 
    @Override
    public boolean bookIssue(int studentId,String bookName )
    {
        String tableName="Student_"+Integer.toString(studentId)+"_book";
        String createTable="CREATE TABLE IF NOT EXISTS "+tableName+"(\n"
        + " id integer PRIMARY KEY,\n"
        + " studentId integer NOT NULL,\n"
        + " bookName text NOT NULL\n"
        + ");"; 
        String insertData= "INSERT INTO "+tableName+"(studentId, bookName) VALUES(?,?)"; 
        try
        {
            Statement stmt = con.createStatement();  
            stmt.execute(createTable);
            PreparedStatement pstmt = con.prepareStatement(insertData);
            pstmt.setInt(1, studentId);
            pstmt.setString(2, bookName);
            pstmt.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Error in book issue...!");
            return false;
        }
    } 
}

class Main extends LMS
{
    public static void connect()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            con=DriverManager.getConnection("jdbc:sqlite:LMS.db");
            System.out.println("\n\nConnected to the Library Database.....\n");
        }
        catch(Exception e)
        {
            System.out.println(e);
            System.out.println("\n\n\n\nConnection to the DATABASE could not be established...\n\n\n\n");
            System.exit(0);
        }
    }
    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
    public static void main(String[] args) throws IOException
    {
        System.out.print("Connecting");
        for(int i=1;i<=4;i++)
        {
            wait(1000);
            System.out.print(".");
        }
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        connect();

        Main ob=new Main();
        BufferedReader in =new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Student Portal :)\n ");
        System.out.println("Press 1: To login   \nPress 2: To Create New Account");
        int ch= Integer.parseInt(in.readLine());

        String createTable="CREATE TABLE IF NOT EXISTS studentData(\n"
        + " id integer PRIMARY KEY,\n"
        + " userId integer NOT NULL UNIQUE,\n"
        + " password text NOT NULL\n"
        + ");";  
        int studentId=0;    
        try
        {
            Statement stmt = con.createStatement();  
            stmt.execute(createTable);
            if(ch==1)
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                studentId=ob.login();
            }
            else if(ch==2)
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                studentId=ob.signup();
            }
            else
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                System.out.println("You don't wish to login/signup\nGood bye\n\n");
                System.exit(0);
            }  
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        String bookNames[]={"Introduction to Algorithms","The C Programming Language","Code","The Soul of a New Machine","Hackers: Heroes of the Computer Revolution","Thinking in Systems: A Primer","The Search: How Google and Its Rivals Rewrote the Rules of Business and Transformed Our Culture"};
        List<String> list = new ArrayList<>();
        Collections.addAll(list, bookNames);
        System.out.println("1. To issue.");
        System.out.println("2. To return.");
        System.out.print("Enter you choice: ");
        ch =Integer.parseInt(in.readLine());
        if (ch==1)
        {
            System.out.print("\033[H\033[2J");  
            System.out.flush();
            System.out.println("Books in the Library:");
            int i=1;
            for(String str: list)
            {
                System.out.println(i++ +". "+str);
            }
            System.out.print("Enter the index of the book: ");            
            int bookIndex= Integer.parseInt(in.readLine());
            if(ob.bookIssue(studentId,list.get(bookIndex-1)))
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                list.remove(bookIndex-1);
                System.out.println("User ID: "+ studentId);
                System.out.println("Books Issued: "+list.get(bookIndex-1));
                LocalDate date = LocalDate.now();
                date = date.plusDays(7);
                System.out.println("Book Return date: "+date);
                System.out.println("********Thank You*******");
            }             
        }
        else if(ch==2)
        {
            String tableName="Student_"+Integer.toString(studentId)+"_book";
            String deleteTable= "DROP TABLE '"+tableName+"'";
            try
            {
                Statement stmt = con.createStatement();
                System.out.println(deleteTable);
                stmt.executeUpdate(deleteTable);
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }  
        if (con != null) 
        {
            try 
            {
                con.commit();
                con.close(); 
            } 
            catch (SQLException e) {}
        }
    }    
}
