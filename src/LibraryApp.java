import com.dao.BookDAO;
import com.dao.UserDAO;
import com.dao.IssueDAO;
import com.dao.RequestDAO;
import com.model.User;

import java.util.Scanner;

public class LibraryApp {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		UserDAO userDAO = new UserDAO();
		BookDAO bookDAO = new BookDAO();
		IssueDAO issueDAO = new IssueDAO();
		RequestDAO requestDAO = new RequestDAO();

		try {

			System.out.print("Username: ");
			String username = sc.next();

			System.out.print("Password: ");
			String password = sc.next();

			User user = userDAO.login(username, password);

			if (user == null) {
				System.out.println("Invalid username or password!");
				return;
			}

			System.out.println("\nWelcome " + user.username + " (" + user.role + ")");

			int choice;

			do {
				System.out.println("\n========= MENU =========");
				System.out.println("1. View Books");

				if (user.role.equals("ADMIN")) {
					System.out.println("2. Add Book");
					System.out.println("3. Create Student Account");
					System.out.println("4. View Issue Requests");
					System.out.println("5. Approve Issue Request");
					System.out.println("6. View Issued Books");
				} else { // STUDENT
					System.out.println("2. Request Book");
					System.out.println("3. View My Issued Books");
				}

				System.out.println("0. Exit");
				System.out.print("Enter choice: ");
				choice = sc.nextInt();

				switch (choice) {

				case 1:
					bookDAO.viewBooks();
					break;

				case 2:
					if (user.role.equals("ADMIN")) {
						sc.nextLine();
						System.out.print("Enter Book Title: ");
						String title = sc.nextLine();

						System.out.print("Enter Author Name: ");
						String author = sc.nextLine();

						System.out.print("Enter Quantity: ");
						int qty = sc.nextInt();

						bookDAO.addBook(title, author, qty);
						System.out.println("Book added successfully!");
					} else {
						System.out.print("Enter Book ID: ");
						int bookId = sc.nextInt();
						requestDAO.requestBook(bookId, user.id);
					}
					break;

				case 3:
					if (user.role.equals("ADMIN")) {
						sc.nextLine();
						System.out.print("Enter Student Username: ");
						String newUser = sc.nextLine();

						System.out.print("Enter Student Password: ");
						String newPass = sc.nextLine();

						userDAO.createStudent(newUser, newPass);
						System.out.println("Student account created successfully!");
					} else {
						requestDAO.viewMyBooks(user.id);
					}
					break;

				case 4:
					if (user.role.equals("ADMIN")) {
						requestDAO.viewPendingRequests();
					}
					break;

				case 5:
					if (user.role.equals("ADMIN")) {

						System.out.println("\n--- Pending Issue Requests ---");
						requestDAO.viewPendingRequests();

						System.out.print("\nEnter Request ID to approve (0 to cancel): ");
						int reqId = sc.nextInt();

						if (reqId != 0) {
							requestDAO.approveRequest(reqId, issueDAO);
						} else {
							System.out.println("Approval cancelled.");
						}
					}
					break;

				case 6:
					if (user.role.equals("ADMIN")) {
						issueDAO.viewIssuedBooks();
					}
					break;

				case 0:
					System.out.println("Thank you! Logging out...");
					break;

				default:
					System.out.println("Invalid choice!");
				}

			} while (choice != 0);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
	}
}
