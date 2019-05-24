package utp;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class AdminController {
	@FXML
	private TableView<User> tableUsers = new TableView<>();
	@FXML
	private TableView<Event> tableEvents = new TableView<>();
	@FXML
	private TableView<EventSign> tableSigned = new TableView<>();
	@FXML
	private Button userAdd;
	@FXML
	private Button userDelete;
	@FXML
	private Button eventAdd;
	@FXML
	private Button eventDelete;
	@FXML
	private Button signedDecline;
	@FXML
	private Button signedAccept;
	@FXML
	private Button logoutButton;
	@FXML
	private Button resetPasswordButton;

	private TableColumn<User, Long> columnId = new TableColumn<>("Id");
	private TableColumn<User, String> columnLogin = new TableColumn<>("Login");
	private TableColumn<User, String> columnPassword = new TableColumn<>("Password");
	private TableColumn<User, String> columnFirstName = new TableColumn<>("FirstName");
	private TableColumn<User, String> columnLastName = new TableColumn<>("LastName");
	private TableColumn<User, String> columnEmail = new TableColumn<>("Email");
	private TableColumn<User, String> columnType = new TableColumn<>("Type");

	private TableColumn<Event, Long> columnEventId = new TableColumn<>("Id");
	private TableColumn<Event, String> columnEventName = new TableColumn<>("Name");
	private TableColumn<Event, String> columnEventAgend = new TableColumn<>("Agend");
	private TableColumn<Event, String> columnEventTime = new TableColumn<>("Time");

	private TableColumn<EventSign, User> columnEventSignUser = new TableColumn<>("User");
	private TableColumn<EventSign, Event> columnEventSignEvent = new TableColumn<>("Event");
	private TableColumn<EventSign, String> columnEventSignType = new TableColumn<>("Type");
	private TableColumn<EventSign, String> columnEventSignFood = new TableColumn<>("Food");
	private TableColumn<EventSign, String> columnEventSignStatus = new TableColumn<>("Status");

	@SuppressWarnings("unused")
	private User loggedUser;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private ObservableList<User> data = FXCollections.observableArrayList();
	private ObservableList<Event> eventData = FXCollections.observableArrayList();
	private ObservableList<EventSign> eventSignData = FXCollections.observableArrayList();
	private EmailSender emailSender = new EmailSender();
	private PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder().useDigits(true)
			.useLower(true).useUpper(true).build();

	// Zrobione
	private void sendUpdateUserQueryCell(String table, String string, String parameter, Long id) {
		EntityTransaction et = null;
		try {
			EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

			String hql = "UPDATE " + table + " SET " + string + " = ?1 WHERE id = ?2";

			et = em.getTransaction();
			et.begin();

			em.createQuery(hql);

			Query q = em.createQuery(hql);

			if (table.equals("Event e") && string.equals("e.time"))
				q.setParameter(1, Timestamp.valueOf(LocalDateTime.parse(parameter, formatter)));
			else
				q.setParameter(1, parameter);

			q.setParameter(2, id);

			q.executeUpdate();
			et.commit();

		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	// Zrobione
	private void sendUpdateUserPasswordQueryCell(String parameter, Long id) {
		EntityTransaction et = null;
		try {
			EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

			String hql = "UPDATE User u SET u.userPassword = MD5(?1) WHERE u.userId = ?2";

			et = em.getTransaction();
			et.begin();

			Query q = em.createQuery(hql);
			q.setParameter(1, parameter);
			q.setParameter(2, id);
			q.executeUpdate();

			et.commit();

		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	// Zrobione
	private void sendInsertUserQueryCell(User user) {
		try {
			Controller.addUser(user);
			user.setUserId(user.getUserId());
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	private Event addEvent(Event event) {
		EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

		EntityTransaction et = null;
		et = em.getTransaction();

		et.begin();

		em.persist(event);

		et.commit();

		return event;
	}

	// Zrobione
	private void sendInsertEventQueryCell(Event event) {
		try {
			addEvent(event);
			event.setEventId(event.getEventId());
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	// Zrobione
	private void sendDeleteUserQueryCell(User user) {
		EntityTransaction et = null;
		try {
			EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

			String hql = "DELETE FROM User u WHERE u.userId = ?1";

			et = em.getTransaction();
			et.begin();

			Query q = em.createQuery(hql);
			q.setParameter(1, user.getUserId()).executeUpdate();

			et.commit();

		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	// Zrobione
	private void sendDeleteEventQueryCell(Event event) {
		EntityTransaction et = null;
		try {
			EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

			String hql = "DELETE FROM Event e WHERE e.eventId = ?1";

			et = em.getTransaction();
			et.begin();

			Query q = em.createQuery(hql);
			q.setParameter(1, event.getEventId()).executeUpdate();

			et.commit();

		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	// Zrobione
	private void reloadTableUser() {
		try {
			EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

			data.clear();

			String hql = "FROM User";

			Query q = em.createQuery(hql);
			@SuppressWarnings("unchecked")
			List<User> users = q.getResultList();

			if (!users.isEmpty())
				users.forEach(user -> data.add(
						new User(user.getUserId(), user.getUserPassword(), user.getUserLogin(), user.getUserFirstName(),
								user.getUserLastName(), user.getUserEmail(), user.getUserAccountType())));

			tableUsers.setItems(data);

		} catch (PersistenceException e) {
			System.err.println(e);
		}
	}

	// Zrobione
	private void reloadTableEvent() {
		try {
			EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

			eventData.clear();

			String hql = "FROM Event";

			Query q = em.createQuery(hql);
			@SuppressWarnings("unchecked")
			List<Event> events = q.getResultList();

			if (!events.isEmpty())
				events.forEach(event -> eventData
						.add(new Event(event.getEventId(), event.getName(), event.getAgend(), event.getTime())));

			tableEvents.setItems(eventData);

		} catch (PersistenceException e) {
			System.err.println(e);
		}
	}

	// Zrobione
	public void reloadTableSignedEvent() {
		try {
			eventSignData.clear();
			EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

			String hql = "SELECT u.id AS userID, u.login AS login, u.password AS password, u.firstname AS firstname, u.lastname AS lastname, u.email AS email, u.type AS u_type, e.id AS eventID, e.name AS name, e.agend AS agend, e.time AS time, ue.type AS ue_type, ue.food AS food, ue.status AS status FROM users u INNER JOIN users_events ue ON u.id = ue.id_user INNER JOIN events e ON ue.id_event = e.id";

			@SuppressWarnings("unchecked")
			List<Object[]> objects = em.createNativeQuery(hql).getResultList();

			objects.forEach(object -> {
				BigInteger o0 = (BigInteger) object[0];
				BigInteger o7 = (BigInteger) object[7];
				Timestamp time = (Timestamp) object[10];
				String type1 = Character.toString((char) object[6]);
				User user = new User(o0.longValue(), (String) object[1], (String) object[2], (String) object[3],
						(String) object[4], (String) object[5], type1);
				Event event = new Event(o7.longValue(), (String) object[8], (String) object[9], time.toString());
				eventSignData.add(new EventSign(user, event, type1, (String) object[12], (String) object[13]));
			});

			tableEvents.setItems(eventData);
		} catch (PersistenceException e) {
			System.err.println(e);
		}
	}

	//Zrobione
	private void setEventSignedStatus(String status, User user, Event event) {
		EntityTransaction et = null;
		try {
			EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

			String hql = "UPDATE users_events SET status = ?1 WHERE id_user = ?2 AND id_event = ?3";

			et = em.getTransaction();
			et.begin();

			Query q = em.createNativeQuery(hql);
			q.setParameter(1, status);
			q.setParameter(2, user.getUserId());
			q.setParameter(3, event.getEventId());
			q.executeUpdate();

			et.commit();

		} catch (PersistenceException e) {
			System.err.println(e);
		}
	}

	public void setUser(User x) {
		this.loggedUser = x;
	}

	@SuppressWarnings("unchecked")
	@FXML
	void initialize() {
		columnId.setCellValueFactory(new PropertyValueFactory<>("userId"));
		columnLogin.setCellValueFactory(new PropertyValueFactory<>("userLogin"));
		columnLogin.setCellFactory(TextFieldTableCell.<User>forTableColumn());
		columnLogin.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setUserLogin(t.getNewValue());
			sendUpdateUserQueryCell("User u", "u.userLogin",
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserLogin(),
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
		});
		columnPassword.setCellValueFactory(new PropertyValueFactory<>("userPassword"));
		columnPassword.setCellFactory(TextFieldTableCell.<User>forTableColumn());
		columnPassword.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
			try {
				t.getTableView().getItems().get(t.getTablePosition().getRow()).setUserPassword(t.getNewValue());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			sendUpdateUserQueryCell("User u", "u.userPassword",
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserPassword(),
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
		});
		columnFirstName.setCellValueFactory(new PropertyValueFactory<>("userFirstName"));
		columnFirstName.setCellFactory(TextFieldTableCell.<User>forTableColumn());
		columnFirstName.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setUserFirstName(t.getNewValue());
			sendUpdateUserQueryCell("User u", "u.userFirstName",
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserFirstName(),
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
		});
		columnLastName.setCellValueFactory(new PropertyValueFactory<>("userLastName"));
		columnLastName.setCellFactory(TextFieldTableCell.<User>forTableColumn());
		columnLastName.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setUserLastName(t.getNewValue());
			sendUpdateUserQueryCell("User u", "u.userLastName",
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserLastName(),
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
		});
		columnEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
		columnEmail.setCellFactory(TextFieldTableCell.<User>forTableColumn());
		columnEmail.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setUserEmail(t.getNewValue());
			sendUpdateUserQueryCell("User u", "u.userEmail",
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserEmail(),
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
		});
		columnType.setCellValueFactory(new PropertyValueFactory<>("userAccountType"));
		columnType.setCellFactory(TextFieldTableCell.<User>forTableColumn());
		columnType.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setUserAccountType(t.getNewValue());
			sendUpdateUserQueryCell("User u", "u.userAccountType",
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserAccountType(),
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
		});
		tableUsers.getColumns().setAll(columnId, columnLogin, columnPassword, columnFirstName, columnLastName,
				columnEmail, columnType);
		tableUsers.setEditable(true);
		// Kolumny Event
		columnEventId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
		columnEventName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnEventName.setCellFactory(TextFieldTableCell.<Event>forTableColumn());
		columnEventName.setOnEditCommit((TableColumn.CellEditEvent<Event, String> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
			sendUpdateUserQueryCell("Event e", "e.name",
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(),
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getEventId());
		});
		columnEventAgend.setCellValueFactory(new PropertyValueFactory<>("agend"));
		columnEventAgend.setCellFactory(TextFieldTableCell.<Event>forTableColumn());
		columnEventAgend.setOnEditCommit((TableColumn.CellEditEvent<Event, String> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setAgend(t.getNewValue());
			sendUpdateUserQueryCell("Event e", "e.agend",
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getAgend(),
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getEventId());
		});
		columnEventTime.setCellValueFactory(new PropertyValueFactory<>("time"));
		columnEventTime.setCellFactory(TextFieldTableCell.<Event>forTableColumn());
		columnEventTime.setOnEditCommit((TableColumn.CellEditEvent<Event, String> t) -> {
			if (t.getNewValue().matches("[0-9]{2}-[0-9]{2}-[0-9]{4} [0-9]{2}:[0-9]{2}")) {
				t.getTableView().getItems().get(t.getTablePosition().getRow()).setTime(t.getNewValue());
				sendUpdateUserQueryCell("Event e", "e.time",
						t.getTableView().getItems().get(t.getTablePosition().getRow()).getTime(),
						t.getTableView().getItems().get(t.getTablePosition().getRow()).getEventId());
			}

		});
		tableEvents.getColumns().setAll(columnEventId, columnEventName, columnEventAgend, columnEventTime);
		tableEvents.setItems(eventData);
		tableEvents.setEditable(true);
		// SignEvent
		columnEventSignUser.setCellValueFactory(new PropertyValueFactory<>("user"));
		columnEventSignEvent.setCellValueFactory(new PropertyValueFactory<>("event"));
		columnEventSignType.setCellValueFactory(new PropertyValueFactory<>("type"));
		columnEventSignFood.setCellValueFactory(new PropertyValueFactory<>("food"));
		columnEventSignStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		tableSigned.setItems(eventSignData);
		tableSigned.getColumns().setAll(columnEventSignUser, columnEventSignEvent, columnEventSignType,
				columnEventSignFood, columnEventSignStatus);
		tableSigned.setEditable(false);
		reloadTableUser();
		reloadTableEvent();
		reloadTableSignedEvent();
		// Obsluga przycisku dodaj - uzytkownika
		userAdd.setOnAction((event -> {
			User newUser = new User(0, "Nowy", "Nowy", "Nowy", "Nowy", "Nowy", "U");
			sendInsertUserQueryCell(newUser);
			data.add(newUser);
		}));
		// Obsluga przycisku usun - uzytkownika
		userDelete.setOnAction((event -> {
			User userToDelete = tableUsers.getSelectionModel().getSelectedItem();
			sendDeleteUserQueryCell(userToDelete);
			data.remove(userToDelete);
		}));
		eventAdd.setOnAction((event -> {
			Event newEvent = new Event(0, "Nowy", "Nowy", LocalDateTime.now().format(formatter));
			sendInsertEventQueryCell(newEvent);
			eventData.add(newEvent);
		}));
		eventDelete.setOnAction(event -> {
			Event eventToDelete = tableEvents.getSelectionModel().getSelectedItem();
			sendDeleteEventQueryCell(eventToDelete);
			eventData.remove(eventToDelete);
		});
		signedAccept.setOnAction((event -> {
			if (tableSigned.getSelectionModel().getSelectedItem() != null) {
				setEventSignedStatus("Potwierdzone", tableSigned.getSelectionModel().getSelectedItem().getUser(),
						tableSigned.getSelectionModel().getSelectedItem().getEvent());
				reloadTableSignedEvent();
			}
		}));
		signedDecline.setOnAction((event -> {
			if (tableSigned.getSelectionModel().getSelectedItem() != null) {
				setEventSignedStatus("Odrzucone", tableSigned.getSelectionModel().getSelectedItem().getUser(),
						tableSigned.getSelectionModel().getSelectedItem().getEvent());
				reloadTableSignedEvent();
			}
		}));
		logoutButton.setOnAction((event -> {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/fxml/mainview.fxml"));
				Parent root = fxmlLoader.load();
				Scene dashboard = new Scene(root);
				Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
				window.setScene(dashboard);
			} catch (Exception e) {
				System.out.println(e);
			}
		}));
		resetPasswordButton.setOnAction((event) -> {
			if (tableUsers.getSelectionModel().getSelectedItem() != null) {
				String newPassword = passwordGenerator.generate(8);
				User selectedUser = tableUsers.getSelectionModel().getSelectedItem();
				sendUpdateUserPasswordQueryCell(newPassword, selectedUser.getUserId());
				reloadTableUser();
				new Thread(() -> {
					try {
						emailSender.sendAsHtml(selectedUser.getUserEmail(), "Nowe haslo",
								"Twoje nowe haslo to: " + newPassword);
					} catch (Exception e) {
						System.out.println(e);
					}
				}).start();

			}
		});
	}

}
