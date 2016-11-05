import java.sql.*;
import simpledb.remote.SimpleDriver;


public class CreateStudentDB {
    public static void main(String[] args) throws Exception {
		Connection conn = null;
		try {
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();

			Scanner scan = new Scanner(System.in);

			String qry = scan.nextLine();
			while(qry.trim() != "exit()") {

				ResultSet rs = stmt.executeQuery(qry.trim());

				ResultSetMetaData rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				// Step 3: loop through the result set

				while (rs.next()) {
					for (int i = 1; i <= columnsNumber; i++) {
						if (i > 1) System.out.print(",  ");
						String columnValue = rs.getString(i);
						System.out.print(columnValue + " " + rsmd.getColumnName(i));
					}
					System.out.println("");
				}
				qry = scan.nextLine();
			}
			rs.close();
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
