import java.util.*;
import java.sql.*;

public class App {
	public static void consoleOptions() {
		System.out.println("---------------------");
		System.out.println("Select the option by selecting the particular number");
		System.out.println("Press 1: Transactions History");
		System.out.println("Press 2: Withdraw");
		System.out.println("Press 3: Deposit");
		System.out.println("Press 4: Transfer");
		System.out.println("Press 5: Check your balance");
		System.out.println("Press 6: Quit");
		System.out.print("Enter your choice: ");
	}

	public static void withdrawCash(int amount, int Balance) {
		Scanner sc = new Scanner(System.in);
		System.out.println();
		int amountLeft = Balance - amount;
		if (amount <= Balance) {
			System.out.println("The amount of "+amount+" has been debited from your account");
			System.out.print("Account Balance:  ₹" + amountLeft + "\n");
			System.out.println();
			main(null);
		} else {
			System.out.println("Not enough money in your account");
		}
		sc.close();
	}

	public static void depositCash(int damount, int Balance) {

		Scanner sc = new Scanner(System.in);
		System.out.println();
		int amountLeft = Balance + damount;
		System.out.println("The amount of "+damount+" has been credited sucessfully");
		System.out.print("Account Balance:  ₹" + amountLeft + "\n");
		System.out.println();
		main(null);
		sc.close();
	}

	public static void quitConsole() {
		System.out.println("Quiting...");
		main(null);
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/atmConsole", "Username", "Password");
			System.out.println("APNA Bank Server Connected Sucessfully");
			Scanner sc = new Scanner(System.in);
			System.out.println("----------APNA BANK ATM CONSOLE----------");

			System.out.print("USER ID: ");
			int userId = sc.nextInt();
			Statement st = null;
			String query = "select * from customerDetails where customerId = ?";
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet rst = ps.executeQuery();
			if (rst.next()) {
				if (userId == rst.getInt(1)) {

					System.out.print("PIN: ");
					int userPin = sc.nextInt();

					if (userPin == rst.getInt(3)) {
						consoleOptions();
						int choice = sc.nextInt();

						switch (choice) {

						case 1:// Number of Transaction
							String q = "select * from customerDetails where customerId=?";
							ps = cn.prepareStatement(q);
							ps.setInt(1, userId);
							rst = ps.executeQuery();
							while (rst.next()) {
								System.out.print("\n Number of Transaction: " + rst.getInt(5) + "\n");
							}
							main(null);
							break;

						case 2:// Withdrawn amount
							System.out.print("Enter the amount to withdraw: ");
							int amount = sc.nextInt();
							ps = cn.prepareStatement("UPDATE customerDetails SET Balance=? where CustomerId=?");
							ps.setInt(1, rst.getInt(4) - amount);
							ps.setInt(2, userId);
							ps.executeUpdate();

							ps = cn.prepareStatement("UPDATE customerDetails SET Transaction=? where CustomerId=?");
							ps.setInt(1, rst.getInt(5) + 1);
							ps.setInt(2, userId);
							ps.executeUpdate();
							withdrawCash(amount, rst.getInt(4));

							break;

						case 3:// Cash Deposit
							System.out.print("Enter the amount to deposit: ");
							int damount = sc.nextInt();
							ps = cn.prepareStatement("UPDATE customerDetails SET Balance=? where CustomerId=?");
							ps.setInt(1, rst.getInt(4) + damount);
							ps.setInt(2, userId);
							ps.executeUpdate();

							ps = cn.prepareStatement("UPDATE customerDetails SET Transaction=? where CustomerId=?");
							ps.setInt(1, rst.getInt(5) + 1);
							ps.setInt(2, userId);
							ps.executeUpdate();
							depositCash(damount, rst.getInt(4));
							break;

						case 4:// Amount transfer
							System.out
									.println("Enter the details to the person to whom you have to transfer the amount");
							System.out.print("Account Number: ");
							int accNumber = sc.nextInt();
							System.out.println();
							System.out.print("Account holder name: ");
							String accName = sc.next();
							System.out.println();
							System.out.print("Amount to transfer: ");
							int amountTrans = sc.nextInt();

							ps = cn.prepareStatement("SELECT * from customerDetails where customerId=?");
							ps.setInt(1, userId);
							rst = ps.executeQuery();
							int currentBalance=0;
							while(rst.next()) {
								currentBalance=rst.getInt(4);
							}
							if(currentBalance>amountTrans) {
								ps = cn.prepareStatement("UPDATE customerDetails SET balance=? where customerId=?");
								ps.setInt(1, rst.getInt(4) + amountTrans);
								ps.setInt(2, accNumber);
								ps.executeUpdate();

								ps = cn.prepareStatement("UPDATE customerDetails SET Transaction=? where customerId=?");
								ps.setInt(1, rst.getInt(5) + 1);
								ps.setInt(2, accNumber);
								ps.executeUpdate();

								// Updating the sender account
								ps = cn.prepareStatement("Update customerDetails set balance=? where customerId=?");
								ps.setInt(1, rst.getInt(4) - amountTrans);
								ps.setInt(2, userId);
								ps.executeUpdate();

								ps = cn.prepareStatement("UPDATE customerDetails SET Transaction=? where customerId=?");
								ps.setInt(1, rst.getInt(5) + 1);
								ps.setInt(2, userId);
								ps.executeUpdate();

								System.out.println("$" + amountTrans + " sucessfully sent.");
								main(null);
								break;
							}else {
								System.out.println("Your account balance is sufficiently low");
								main(null);
								break;
							}
							
							// Updating the receiver account

							

						case 5:// Check balance
							ps = cn.prepareStatement("SELECT * FROM customerDetails where customerId=?");
							ps.setInt(1, userId);
							rst = ps.executeQuery();

							while (rst.next()) {
								System.out.println("\nYour balance is $" + rst.getInt(4) + "\n");
							}
							main(null);
							break;

						case 6:// Quit from application
							quitConsole();
							break;

						default:
							System.out.println("Wrong number entered");
							consoleOptions();
						}
					} else {
						System.out.println("Wrong Credentials");
						main(null);
					}
				} else {
					System.out.println("Wrong Credentials");
					main(null);
				}
			} else {
				System.out.println("Wrong Credentials");
				main(null);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
