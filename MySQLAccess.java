package bg.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	final String INJECTION_TEST_STRING = "'bob' OR 1=1"; // 1=1 is always true, try it in any WHERE clause

	public void accessDB() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Setup the connection with the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost/feedback?user=sqluser&password=sqlUserpw1.");
			System.out.println("Connection established...\n");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();

			// resultSet gets the result of the SQL query
			resultSet = statement.executeQuery("SELECT * FROM COMMENTS");

			// Prints names of the database and table being used/accessed
			// and columns (number + name) inside of that table.
			printDBInfo(resultSet);

			// Inserts 3 new rows into the table, where all fields are parameterized.
			// Parameters start with 1
			preparedStatement = connect.prepareStatement("INSERT INTO COMMENTS VALUES (default, ?, ?, ?, ?)");
			preparedStatement.setString(1, "sqluser1");
			preparedStatement.setString(2, "sqluser1@gmail.com");
			preparedStatement.setString(3, "My first comment!");
			preparedStatement.setString(4, getCurrentDateTime());
			preparedStatement.executeUpdate();

			preparedStatement = connect.prepareStatement("INSERT INTO COMMENTS VALUES (default, ?, ?, ?, ?)");
			preparedStatement.setString(1, "sqluser2");
			preparedStatement.setString(2, "sqluser2@gmail.com");
			preparedStatement.setString(3, "My second comment.");
			preparedStatement.setString(4, getCurrentDateTime());
			preparedStatement.executeUpdate();

			preparedStatement = connect.prepareStatement("INSERT INTO COMMENTS VALUES (default, ?, ?, ?, ?)");
			preparedStatement.setString(1, "sqluser3");
			preparedStatement.setString(2, "sqluser3@gmail.com");
			preparedStatement.setString(3, "My third comment?");
			preparedStatement.setString(4, getCurrentDateTime());
			preparedStatement.executeUpdate();

			// Injection Test
			injectionTest(resultSet);

			// Executes 3 different SELECT statements from the table using a WHERE clause,
			// where all fields are parameterized.
			preparedStatement = connect
					.prepareStatement("SELECT id, comment, timestamp FROM COMMENTS WHERE username = ? AND email = ?");
			preparedStatement.setString(1, "sqluser3");
			preparedStatement.setString(2, "sqluser3@gmail.com");
			printResultSet(preparedStatement.executeQuery());

			preparedStatement = connect.prepareStatement("SELECT * FROM COMMENTS WHERE username = ?");
			preparedStatement.setString(1, "sqluser1");
			printResultSet(preparedStatement.executeQuery());

			// should print out "...Nothing was returned."
			// because username = "sqluser" is NOT stored in the database table.
			preparedStatement = connect.prepareStatement("SELECT * FROM COMMENTS WHERE username = ?");
			preparedStatement.setString(1, "sqluser");
			printResultSet(preparedStatement.executeQuery());
		} catch (Exception e) {
			throw e;
		} finally {
			closeDBConnection();
		}
	}

	private String getCurrentDateTime() {
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
		String strDate = dateFormat.format(date);
		return strDate;
	}

	private void printDBInfo(ResultSet resultSet) throws SQLException {
		System.out.println("----------------- Database Name: " + connect.getCatalog() + " ------------------");
		System.out.println(
				"------------------ Table Name: " + resultSet.getMetaData().getTableName(1) + " --------------------");

		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
		}
	}

	private void injectionTest(ResultSet resultSet) throws SQLException {
		System.out.println("\n----------> Start Injection Test\n");

		// Vulnerable
		System.out.println("-- Test Vulnerable:");
		ResultSet injectionTestResultSet1 = statement
				.executeQuery("SELECT * FROM COMMENTS WHERE username = " + INJECTION_TEST_STRING);
		printResultSet(injectionTestResultSet1);

		// Secure (should NOT return anything)
		System.out.println("-- Test Secure: (should NOT return anything)\n");
		PreparedStatement preparedStatement1 = connect.prepareStatement("SELECT * FROM COMMENTS WHERE username = ?");
		preparedStatement1.setString(1, INJECTION_TEST_STRING);
		ResultSet injectionTestResultSet2 = preparedStatement1.executeQuery();
		printResultSet(injectionTestResultSet2);

		System.out.println("\n----------> End Injection Test\n");
	}

	private void printResultSet(ResultSet resultSet) throws SQLException {
		if (resultSet.next() == false) {
			System.out.println("...Nothing was returned.");
			return;
		} else {
			do {
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					System.out.println(resultSet.getMetaData().getColumnName(i) + ": " + resultSet.getString(i));
				}
				System.out.print("\n");
			} while (resultSet.next());
		}
	}

	private void closeDBConnection() {
		try {
			resultSet.close();
			statement.close();
			connect.close();
			System.exit(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		MySQLAccess mysql = new MySQLAccess();
		mysql.accessDB();
		System.exit(0);
	}
}