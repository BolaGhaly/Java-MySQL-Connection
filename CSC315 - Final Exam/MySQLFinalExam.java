package csc315final.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * 10. (9 points) Write an application in the back end language of your choice 
 * to access the database using the created user in task # 1 with a function 
 * to run each query in tasks 4-8 with the user ID passed as a parameter.
 * */
public class MySQLFinalExam {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	// 4. (9 points) Create a query to determine which sub_genres come from which regions.
	// This query does NOT use any parameters (No userId/uid being used)
	final String QUERY_4 = "SELECT sg.sgname AS \"Sub Genre Name\", r.rname AS \"Region Name\" FROM Sub_Genre sg\n"
			+ "INNER JOIN Band_Styles bs ON bs.sgname=sg.sgname\n" + "INNER JOIN Band_Origins bo ON bo.bname=bs.bname\n"
			+ "INNER JOIN Country c ON c.cname=bo.cname\n" + "INNER JOIN Region r ON r.rname=c.rname\n"
			+ "GROUP BY sg.sgname, r.rname\n" + "ORDER BY sg.sgname";

	// 5. (9 points) Create a query to determine what other bands,
	// not currently in their favorites, are of the same sub_genres as those which are.
	final String QUERY_5 = "SELECT bs.sgname AS \"Sub Genre Name\", bs.bname AS \"Band Name\" FROM Band_Styles bs WHERE bs.sgname IN(\n"
			+ "SELECT sg.sgname FROM Bands b\n" + "INNER JOIN Favorites f ON f.bandId=b.bid AND f.userId=?\n"
			+ "INNER JOIN Band_Styles bs ON bs.bname=b.bname\n" + "INNER JOIN Sub_Genre sg ON sg.sgname=bs.sgname) \n"
			+ "AND bs.bname NOT IN(\n" + "SELECT b.bname FROM Bands b\n"
			+ "INNER JOIN Favorites f ON f.bandId=b.bid AND f.userId=?\n"
			+ "INNER JOIN Band_Styles bs ON bs.bname=b.bname)\n" + "ORDER BY bs.sgname";

	// 6. (9 points) Create a query to determine what other bands,
	// not currently in their favorites, are of the same genres as those which are.
	final String QUERY_6 = "SELECT DISTINCT bs.bname AS \"Band Name\" FROM Band_Styles bs, Sub_Genre sg WHERE bs.sgname IN\n"
			+ "(SELECT sg.sgname FROM Sub_Genre sg WHERE sg.gname IN \n"
			+ "(SELECT DISTINCT sg.gname AS \"Genre Name\" FROM Sub_Genre sg WHERE sg.sgname IN\n"
			+ "(SELECT sg.sgname FROM Bands b\n" + "INNER JOIN Favorites f ON f.bandId=b.bid AND f.userId=?\n"
			+ "INNER JOIN Band_Styles bs ON bs.bname=b.bname\n" + "INNER JOIN Sub_Genre sg ON sg.sgname=bs.sgname))\n"
			+ "ORDER BY sg.gname, sg.sgname) AND sg.gname IN\n"
			+ "(SELECT DISTINCT sg.gname FROM Sub_Genre sg WHERE sg.gname IN (SELECT DISTINCT sg.gname AS \"Genre Name\" FROM Sub_Genre sg WHERE sg.sgname \n"
			+ "IN (SELECT sg.sgname FROM Bands b\n" + "INNER JOIN Favorites f ON f.bandId=b.bid AND f.userId=?\n"
			+ "INNER JOIN Band_Styles bs ON bs.bname=b.bname\n"
			+ "INNER JOIN Sub_Genre sg ON sg.sgname=bs.sgname))) AND bs.bname NOT IN\n"
			+ "(SELECT DISTINCT b.bname FROM Bands b\n" + "INNER JOIN Favorites f ON f.bandId=b.bid AND f.userId=?\n"
			+ "INNER JOIN Band_Styles bs ON bs.bname=b.bname)";

	// 7. (9 points) Create a query which finds other users who have the same band
	// in their favorites, and list their other favorite bands.
	final String QUERY_7 = "SELECT DISTINCT u.uname As \"Other Users\", b.bname \"Band Names\" FROM Favorites f\n"
			+ "JOIN Favorites f1 ON f.bandId=f1.bandId AND f.userId <> f1.userId \n"
			+ "JOIN `User` u ON f1.userId = u.uid\n"
			+ "JOIN favorites f2 ON u.uid=f2.userId AND f2.bandId <> f.bandId\n" + "JOIN Bands b ON f2.bandId=b.bid \n"
			+ "WHERE f.userId=?";

	// 8. (9 points) Create a query to list other countries, excluding the user’s home country,
	// where they could travel to where they could hear the same genres as the bands in their favorites.
	final String QUERY_8 = "SELECT DISTINCT bo.cname AS \"Other Countries\" FROM Band_Origins bo WHERE bname \n"
			+ "IN (SELECT b.bname FROM Bands b WHERE b.bname \n"
			+ "IN (SELECT DISTINCT bs.bname FROM Band_Styles bs WHERE bs.sgname \n"
			+ "IN (SELECT sg.sgname FROM Sub_Genre sg WHERE sg.gname \n"
			+ "IN (SELECT DISTINCT sg.gname FROM Sub_Genre sg WHERE sg.gname \n"
			+ "IN (SELECT DISTINCT sg.gname AS \"Genre Name\" FROM Sub_Genre sg WHERE sg.sgname \n"
			+ "IN (SELECT sg.sgname FROM Bands b\n" + "JOIN Favorites f2 ON f2.bandId=b.bid AND f2.userId=?\n"
			+ "JOIN Band_Styles bs ON bs.bname=b.bname\n"
			+ "JOIN Sub_Genre sg ON sg.sgname=bs.sgname))))) AND b.bname NOT IN \n"
			+ "(SELECT b.bname FROM Bands b, Favorites f WHERE f.userId = ? AND f.bandId = b.bid)) \n"
			+ "AND bo.cname NOT IN (SELECT c.cname FROM `User` u \n"
			+ "JOIN Country c ON c.rid = u.homeCountry WHERE u.uid = ?)";

	public void accessDB() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Setup the connection with the database
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/CSC315Final2022?user=api&password=apiUserpw1.");
			System.out.println("Connection established...\n");
			System.out.println("----------------- Database Name: " + connect.getCatalog() + " ------------------\n");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();

			System.out.println("---------------------- Running Task/Query #4 ----------------------");
			runTask4();
			System.out.println("--------------------- Task/Query #4 has ended. --------------------\n");

			System.out.println("---------------------- Running Task/Query #5 ----------------------");
			runTask5(1); // passing user ID as a param
			System.out.println("--------------------- Task/Query #5 has ended. --------------------\n");

			System.out.println("---------------------- Running Task/Query #6 ----------------------");
			runTask6(1); // passing user ID as a param
			System.out.println("--------------------- Task/Query #6 has ended. --------------------\n");

			System.out.println("---------------------- Running Task/Query #7 ----------------------");
			runTask7(1); // passing user ID as a param
			System.out.println("--------------------- Task/Query #7 has ended. --------------------\n");

			System.out.println("---------------------- Running Task/Query #8 ----------------------");
			runTask8(3); // passing user ID as a param
			System.out.println("--------------------- Task/Query #8 has ended. --------------------\n");

			/*
			 * 11. (10 points) Create functions to insert users and insert & delete favorites. Insert at least 3 users
			 * and 4 favorites for each user. You may use static data in the program to call these functions
			 * programmatically so long as SQL calls are still properly parameterized. Note that a user may only add
			 * existing bands to favorites list and function should return an error if they add a band not in the
			 * database. (You may add your favorite bands to the template provided, just remember to add matching
			 * genre/sub_genre and region/country.)
			 */
			insertNewUser("Kylian Mbappé", 14);
			insertNewUser("Luka Modrić", 15);
			insertNewUser("Harry Kane", 11);
			insertNewFavorite(4, 9);
			insertNewFavorite(4, 13);
			insertNewFavorite(4, 7);
			insertNewFavorite(4, 5);
			insertNewFavorite(5, 3);
			insertNewFavorite(5, 4);
			insertNewFavorite(5, 11);
			insertNewFavorite(5, 10);
			insertNewFavorite(6, 14);
			insertNewFavorite(6, 2);
			insertNewFavorite(6, 3);
			insertNewFavorite(6, 8);

			// This will NOT work because user #7 does NOT exist in the "User" table (not yet)
			// Prints the SQL error returned
			insertNewFavorite(7, 11);

			// This will NOT work because band #30 does NOT exist in the "Bands" table
			// Prints the SQL error returned
			insertNewFavorite(6, 30);

			// This will NOT work because user "api" does NOT have delete permissions in this database
			// Prints the SQL error returned
			deleteExistingFavorite(4, 7);

			extraCredit();
		} catch (Exception e) {
			throw e;
		} finally {
			closeDBConnection();
		}
	}

	private void extraCredit() {
		/*
		 * Extra Credit: Create a User with "Paul Pena" and "The Hu" in their favorites list. Create 2 other Users,
		 * each with only one of those in their favorites, (1 each) as well as both users with "Tengger Cavalry",
		 * "Sade", and "Battuvshin" in their favorites. Modify query #7 so that Tengger Cavalry will be ranked first
		 * in the results order. Hint: consider country, genre, and/or sub_genre.
		 */
		insertNewUser("Vini Jr.", 7);
		insertNewFavorite(7, 8);
		insertNewFavorite(7, 6);

		insertNewUser("Ángel Di María", 8);
		insertNewFavorite(8, 6);
		insertNewFavorite(8, 15);
		insertNewFavorite(8, 10);
		insertNewFavorite(8, 9);

		insertNewUser("Bruno Fernandes", 13);
		insertNewFavorite(9, 8);
		insertNewFavorite(9, 15);
		insertNewFavorite(9, 10);
		insertNewFavorite(9, 9);

		// runTask7(7);
	}

	private void insertNewUser(String userName, Integer userHomeCountryRefId) {
		try {
			preparedStatement = connect.prepareStatement("INSERT INTO `User` (uname, homeCountry) VALUES (?, ?)");
			preparedStatement.setString(1, userName);
			preparedStatement.setInt(2, userHomeCountryRefId);
			preparedStatement.executeUpdate();
		} catch (SQLException sqlException) {
			System.err.println("Error Code = " + sqlException.getErrorCode());
			System.err.println("SQL state = " + sqlException.getSQLState());
			System.err.println("Message = " + sqlException.getMessage() + "\n");
		}
	}

	private void insertNewFavorite(Integer userIdRefId, Integer bandIdRefId) {
		try {
			preparedStatement = connect.prepareStatement("INSERT INTO Favorites (userId, bandId) VALUES (?, ?)");
			preparedStatement.setInt(1, userIdRefId);
			preparedStatement.setInt(2, bandIdRefId);
			preparedStatement.executeUpdate();
		} catch (SQLException sqlException) {
			System.err.println("Error Code = " + sqlException.getErrorCode());
			System.err.println("SQL state = " + sqlException.getSQLState());
			System.err.println("Message = " + sqlException.getMessage() + "\n");
		}
	}

	private void deleteExistingFavorite(Integer userIdRefId, Integer bandIdRefId) {
		try {
			preparedStatement = connect.prepareStatement("DELETE FROM Favorites WHERE userId = ? AND bandId = ?");
			preparedStatement.setInt(1, userIdRefId);
			preparedStatement.setInt(2, bandIdRefId);
			preparedStatement.executeUpdate();
		} catch (SQLException sqlException) {
			System.err.println("Error Code = " + sqlException.getErrorCode());
			System.err.println("SQL state = " + sqlException.getSQLState());
			System.err.println("Message = " + sqlException.getMessage() + "\n");
		}
	}

	private void runTask4() {
		try {
			resultSet = statement.executeQuery(QUERY_4);
			printResultSet(resultSet);
		} catch (SQLException sqlException) {
			System.err.println("Error Code = " + sqlException.getErrorCode());
			System.err.println("SQL state = " + sqlException.getSQLState());
			System.err.println("Message = " + sqlException.getMessage() + "\n");
		}
	}

	private void runTask5(Integer userId) {
		try {
			preparedStatement = connect.prepareStatement(QUERY_5);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, userId);
			printResultSet(preparedStatement.executeQuery());
		} catch (SQLException sqlException) {
			System.err.println("Error Code = " + sqlException.getErrorCode());
			System.err.println("SQL state = " + sqlException.getSQLState());
			System.err.println("Message = " + sqlException.getMessage() + "\n");
		}
	}

	private void runTask6(Integer userId) {
		try {
			preparedStatement = connect.prepareStatement(QUERY_6);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, userId);
			preparedStatement.setInt(3, userId);
			printResultSet(preparedStatement.executeQuery());
		} catch (SQLException sqlException) {
			System.err.println("Error Code = " + sqlException.getErrorCode());
			System.err.println("SQL state = " + sqlException.getSQLState());
			System.err.println("Message = " + sqlException.getMessage() + "\n");
		}
	}

	private void runTask7(Integer userId) {
		try {
			preparedStatement = connect.prepareStatement(QUERY_7);
			preparedStatement.setInt(1, userId);
			printResultSet(preparedStatement.executeQuery());
		} catch (SQLException sqlException) {
			System.err.println("Error Code = " + sqlException.getErrorCode());
			System.err.println("SQL state = " + sqlException.getSQLState());
			System.err.println("Message = " + sqlException.getMessage() + "\n");
		}
	}

	private void runTask8(Integer userId) {
		try {
			preparedStatement = connect.prepareStatement(QUERY_8);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, userId);
			preparedStatement.setInt(3, userId);
			printResultSet(preparedStatement.executeQuery());
		} catch (SQLException sqlException) {
			System.err.println("Error Code = " + sqlException.getErrorCode());
			System.err.println("SQL state = " + sqlException.getSQLState());
			System.err.println("Message = " + sqlException.getMessage() + "\n");
		}
	}

	private void printResultSet(ResultSet resultSet) {
		try {
			if (resultSet.next() == false) {
				System.out.println("...Nothing was returned.");
				return;
			} else {
				do {
					for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
						System.out.println(resultSet.getMetaData().getColumnLabel(i) + ": " + resultSet.getString(i));
					}
					System.out.print("\n");
				} while (resultSet.next());
			}
		} catch (SQLException sqlException) {
			System.err.println("Error Code = " + sqlException.getErrorCode());
			System.err.println("SQL state = " + sqlException.getSQLState());
			System.err.println("Message = " + sqlException.getMessage() + "\n");
		}
	}

	private void closeDBConnection() {
		try {
			resultSet.close();
			statement.close();
			connect.close();
			System.exit(0);
		} catch (SQLException sqlException) {
			System.err.println("Error Code = " + sqlException.getErrorCode());
			System.err.println("SQL state = " + sqlException.getSQLState());
			System.err.println("Message = " + sqlException.getMessage() + "\n");
		}
	}

	public static void main(String[] args) throws Exception {
		MySQLFinalExam mysql = new MySQLFinalExam();
		mysql.accessDB();
		System.exit(0);
	}
}