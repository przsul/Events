package utp;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Controller {
	@FXML
	private TextField loginText;
	@FXML
	private PasswordField passwordText;
	@FXML
	private Button loginButton;
	@FXML
	private TextField registerLoginText;
	@FXML
	private PasswordField registerPasswordText;
	@FXML
	private PasswordField repeatPasswordText;
	@FXML
	private Button registerButton;
	@FXML
	private TextArea loginInfoText;
	@FXML
	private TextArea registerInfoText;
	@FXML
	private TextField firstNameText;
	@FXML
	private TextField lastNameText;
	@FXML
	private TextField emailText;
	@FXML
	private GridPane rootGridPane;

	private User user;

	@FXML
	void initialize() {

	}

	// Add new user
	public static User addUser(User user) {
		EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

		EntityTransaction et = null;
		et = em.getTransaction();

		et.begin();

		em.persist(user);
		
		et.commit();

		return user;
	}

	// Get users by login
	public List<User> getUsers(String login) {
		EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

		String strQuery = "SELECT u FROM User u WHERE u.userLogin = :login";
		TypedQuery<User> tq = em.createQuery(strQuery, User.class);
		List<User> users = tq.setParameter("login", login).getResultList();
		
		return users;
	}

	// Get users by login and password
	public List<User> getUsers(String login, String password) {
		EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

		String strQuery = "SELECT u FROM User u WHERE u.userLogin = :login AND u.userPassword = md5(:password)";

		TypedQuery<User> tq = em.createQuery(strQuery, User.class);
		tq.setParameter("login", loginText.getText());
		tq.setParameter("password", passwordText.getText());

		List<User> users = tq.getResultList();

		return users;
	}

	@FXML
	void onActionHandler(ActionEvent e) {
		if (e.getSource() == loginButton) {

			// Get users by login and password
			List<User> users = getUsers(loginText.getText(), passwordText.getText());

			if (!users.isEmpty()) {

				User loggedUser = new User(users.get(0).getUserId(), users.get(0).getUserPassword(),
						users.get(0).getUserLogin(), users.get(0).getUserFirstName(), users.get(0).getUserLastName(),
						users.get(0).getUserEmail(), users.get(0).getUserAccountType());

				FXMLLoader fxmlLoader = new FXMLLoader();

				if (loggedUser.getUserAccountType().equals("U")) {
					fxmlLoader.setLocation(getClass().getResource("/fxml/userview.fxml"));
					UserController userController = new UserController();
					userController.setLoggedUser(loggedUser);
					fxmlLoader.setController(userController);
				} else {
					fxmlLoader.setLocation(getClass().getResource("/fxml/adminview.fxml"));
					AdminController adminController = new AdminController();
					adminController.setUser(loggedUser);
					fxmlLoader.setController(adminController);
				}

				Parent root = null;
				try {
					root = fxmlLoader.load();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				Scene dashboard = new Scene(root);
				Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
				window.setScene(dashboard);
			} else
				loginInfoText.appendText("Nie znaleziono uzytkownika\n");

			Main.primaryStage.centerOnScreen();
		}

		if (e.getSource() == registerButton) {

			if (registerLoginText.getText().length() > 0 && firstNameText.getText().length() > 0
					&& lastNameText.getText().length() > 0 && emailText.getText().length() > 0
					&& registerPasswordText.getText().length() > 0 && repeatPasswordText.getText().length() > 0) {
				if (registerPasswordText.getText().equals(repeatPasswordText.getText())) {
					if (emailText.getText().matches("^\\S+@\\S+\\.\\S+$")) {

						// Get all users by login
						List<User> users = getUsers(registerLoginText.getText());

						// If user don't exist, create new user
						if (users.isEmpty()) {

							user = new User();
							user.setUserLogin(registerLoginText.getText());

							try {
								user.setUserPassword(registerPasswordText.getText());
							} catch (NoSuchAlgorithmException e2) {
								e2.printStackTrace();
							}

							user.setUserFirstName(firstNameText.getText());
							user.setUserLastName(lastNameText.getText());
							user.setUserEmail(emailText.getText());
							user.setUserAccountType("U");

							// Create new user
							addUser(user);

							User newUserLogged = new User(user.getUserId(), registerLoginText.getText(),
									registerPasswordText.getText(), firstNameText.getText(), lastNameText.getText(),
									emailText.getText(), "U");

							FXMLLoader fxmlLoader = new FXMLLoader();
							fxmlLoader.setLocation(getClass().getResource("/fxml/userview.fxml"));
							UserController userController = new UserController();
							userController.setLoggedUser(newUserLogged);
							fxmlLoader.setController(userController);

							Parent root = null;
							try {
								root = fxmlLoader.load();
							} catch (IOException e1) {
								e1.printStackTrace();
							}

							Scene dashboard = new Scene(root);
							Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
							window.setScene(dashboard);
						} else
							registerInfoText.appendText("Blad: Podany uzytkownik juz istnieje\n");
					} else
						registerInfoText.appendText("Blad: zly format adresu email\n");
				} else
					registerInfoText.appendText("Blad: Podane hasla roznia sie\n");
			} else
				registerInfoText.appendText("Blad: Kazde pole musi byc wypelnione\n");

			Main.primaryStage.centerOnScreen();
		}
	}

}
