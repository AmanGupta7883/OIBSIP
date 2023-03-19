import java.util.*;

public class App {
	public static void consoleOptions() {
		
		System.out.println("------------------------------------------");
		System.out.print  ("PRESS 1: START \n");
		System.out.print  ("PRESS 2: EXIT\n");
		System.out.println("------------------------------------------");
		System.out.println("Enter your choice: ");
	}

	public static void startGame(String name) {
		Scanner sc = new Scanner(System.in);
		String userName = System.getProperty("user.name");
		Random random = new Random();
		int randomNumber = random.nextInt(10);
		System.out.print("Guess number");
		int number = 0;
		boolean cond = true;
		while (cond) {
			number = sc.nextInt();
			if (number > randomNumber) {
				System.out.println("higher");
			} else if (number == randomNumber) {
				System.out.println("Congratulations!"+name+" You won");
				cond = false;
			} else {
				System.out.println("lower");
			}
		}
		System.out.println("------------------------------------------");
		System.out.println("PRESS 1: EXIT");
		System.out.println("PRESS 2: To start again");
		System.out.println("------------------------------------------");
		System.out.println("Enter your choice: ");
	    int ch = sc.nextInt();
	    if(ch==1) {
	    	System.out.println("THANKU YOU FOR VISITING");
	    	System.exit(0);
	    }else if(ch == 2) {
	    	startGame(userName);
	    }
		
		sc.close();
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String userName = System.getProperty("user.name");
		System.out.println("(Guest Account)Logined sucessfully as "+userName);
		System.out.println("------------------------------------------");
		System.out.println("-----------NUMBER GUESSING GAME-----------");
		consoleOptions();
		int choice = sc.nextInt();
		switch (choice) {
		case 1:
			startGame(userName);
			break;
		case 2:
			System.out.println("THANKU YOU FOR VISITING");
			System.exit(0);
		default:
			System.out.println("Wong choice entered");
			main(null);
			break;
		}
		sc.close();
	}
}
