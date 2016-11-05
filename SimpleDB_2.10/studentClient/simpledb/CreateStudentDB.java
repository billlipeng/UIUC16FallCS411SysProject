import java.sql.*;
import simpledb.remote.SimpleDriver;
import java.util.Scanner;
import java.lang.*;

public class CreateStudentDB {
    public static void main(String[] args) throws Exception{
		Connection conn = null;
		try {
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();

			Scanner scan = new Scanner(System.in);

			String qry = scan.nextLine();

			CharSequence cs0 = "SELECT";
			CharSequence cs1 = "select";
			while(qry.trim() != "exit()") {

				if(qry.contains(cs0) || qry.contains(cs1)){
					ResultSet rs = stmt.executeQuery(qry.trim());

					ResultSetMetaData rsmd = rs.getMetaData();
					int columnsNumber = rsmd.getColumnCount();
					// Step 3: loop through the result set
					System.out.println(columnsNumber);
					while (rs.next()) {
						for (int i = 1; i <= columnsNumber; i++) {
							if (i > 1) System.out.print(",  ");
							String columnValue = rs.getString(i);
							System.out.print(columnValue + " " + rsmd.getColumnName(i));
						}
						System.out.println("");
					}
					
					rs.close();
				} else{
					stmt.executeUpdate(qry.trim());
					// ResultSet rs = stmt.executeUpdate(qry.trim());
					
					// rs.close();
				}
				qry = scan.nextLine();
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
