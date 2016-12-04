import java.sql.*;
import simpledb.remote.SimpleDriver;
import java.util.Scanner;
import java.lang.*;
import java.util.*;

public class CreateStudentDB {
    public static void main(String[] args) throws Exception{
        System.out.println("simpledb client booted");
		System.out.println("10");
		Connection conn = null;
		try {
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();

			Scanner scan = new Scanner(System.in);

			String qry = scan.nextLine();
            qry = qry.trim().toLowerCase();
            
			while(qry.trim().equals("exit()") != true) {
                if(qry.substring(0, 6).equals("select")) {
                        ResultSet rs = stmt.executeQuery(qry);
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
					// Step 3: loop through the result set
                        
                        while (rs.next()) {
                            for (int i = 1; i <= columnsNumber; i++) {
                                int currType = rsmd.getColumnType(i);
                                if (i > 1) System.out.print(",  ");
                                if(currType==4) {
                                    int columnValue = rs.getInt(rsmd.getColumnName(i));
                                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                                }
                                else if(currType==12) {
                                    String columnValue = rs.getString(rsmd.getColumnName(i));
                                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                                }
                            }
                            System.out.println("");
                        }
                        
                }
				else{
					stmt.executeUpdate(qry);
				}
				qry = scan.nextLine();
                qry = qry.trim().toLowerCase();
            }

		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
