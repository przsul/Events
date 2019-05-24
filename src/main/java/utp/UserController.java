package utp;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class UserController {
	private User loggedUser;
	@FXML
	private ComboBox<Event> nameEventCombo = new ComboBox<>();
	@FXML
	private TextArea agend;
	@FXML
	private Label agendLabel;
	@FXML
	private Label dateLabel;
	@FXML
	private Label userLabel;
	@FXML
	private Label foodLabel;
	@FXML
	private TextField date;
	@FXML
	private Button signButton;
	@FXML
	private ComboBox<String> userType = new ComboBox<>();
	@FXML
	private ComboBox<String> foodType = new ComboBox<>();
	@FXML
	private Label infoLabel;
	@FXML
	private TableView<UserSignedEvents> tableSignedEvents = new TableView<>();
	@FXML
	private Button logoutButton;
	@FXML
	private Tab yourSigns;

	private TableColumn<UserSignedEvents, String> columnEventName = new TableColumn<>("Name");
	private TableColumn<UserSignedEvents, String> columnEventAgend = new TableColumn<>("Agend");
	private TableColumn<UserSignedEvents, String> columnEventTime = new TableColumn<>("Time:");
	private TableColumn<UserSignedEvents, String> columnEventStatus = new TableColumn<>("Status:");

	private ObservableList<Event> events = FXCollections.observableArrayList();
	private ObservableList<String> userT = FXCollections.observableArrayList("Słuchacz", "Autor", "Sponsor",
			"Organizator");
	private ObservableList<String> food = FXCollections.observableArrayList("Bez preferencji", "Wegetariańskie",
			"Bez glutenu");
	private ObservableList<UserSignedEvents> userSignedEvents = FXCollections.observableArrayList();

	// Zrobione
	private void getAllEvents() {
		EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

		String hql = "FROM Event";
		TypedQuery<Event> tq = em.createQuery(hql, Event.class);
		List<Event> tmp = tq.getResultList();

		tmp.forEach((event) -> {
			events.add(event);
		});

		nameEventCombo.setItems(events);
	}

	// Zrobione
	public void signForEvent() {
		EntityTransaction et = null;
		try {
			EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();

			String hql = "INSERT INTO users_events (id_user,id_event,type,food,status) VALUES (?,?,?,?, 'Oczekujace')";

			et = em.getTransaction();

			et.begin();

			em.createNativeQuery(hql).setParameter(1, loggedUser.getUserId())
					.setParameter(2, nameEventCombo.getSelectionModel().getSelectedItem().getEventId())
					.setParameter(3, userType.getSelectionModel().getSelectedItem())
					.setParameter(4, foodType.getSelectionModel().getSelectedItem()).executeUpdate();

			et.commit();

			infoLabel.setText("INFO: Pomyslnie zostales zapisany na wydarzenie");
		} catch (PersistenceException e) {
			if (et != null)
				et.rollback();
			infoLabel.setText("INFO: Jestes juz zapisany na to wydarzenie");
		}

	}

	// Zrobione
	private void getUserSignedEvents(User user) {
		try {
			EntityManager em = Main.ENTITY_MANAGER_FACTORY.createEntityManager();
			userSignedEvents.clear();

			String hql = "SELECT events.id,events.name, events.agend,events.time,users_events.status FROM events"
					+ " INNER JOIN users_events ON events.id = users_events.id_event"
					+ " INNER JOIN users ON users_events.id_user = users.id WHERE users.id = ?1";

			@SuppressWarnings("unchecked")
			List<Object[]> objects = em.createNativeQuery(hql).setParameter(1, user.getUserId()).getResultList();

			if (!objects.isEmpty()) {
				objects.forEach(object -> {
					BigInteger o0 = (BigInteger) object[0];
					Timestamp time = (Timestamp) object[3];
					userSignedEvents.add(new UserSignedEvents(o0.longValue(), (String) object[1], (String) object[2],
							time.toString(), (String) object[4]));
				});
			}

			tableSignedEvents.setItems(userSignedEvents);

		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	void initialize() {
		agendLabel.setVisible(false);
		agend.setVisible(false);
		dateLabel.setVisible(false);
		date.setVisible(false);
		userLabel.setVisible(false);
		userType.setVisible(false);
		foodLabel.setVisible(false);
		foodType.setVisible(false);
		signButton.setVisible(false);
		infoLabel.setVisible(false);
		userType.setItems(userT);
		userType.getSelectionModel().selectFirst();
		foodType.setItems(food);
		foodType.getSelectionModel().selectFirst();
		columnEventName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnEventAgend.setCellValueFactory(new PropertyValueFactory<>("agend"));
		columnEventTime.setCellValueFactory(new PropertyValueFactory<>("time"));
		columnEventStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		tableSignedEvents.getColumns().setAll(columnEventName, columnEventAgend, columnEventTime, columnEventStatus);
		getAllEvents();
		getUserSignedEvents(loggedUser);
		nameEventCombo.setOnAction((event -> {
			agendLabel.setVisible(true);
			agend.setVisible(true);
			dateLabel.setVisible(true);
			date.setVisible(true);
			userLabel.setVisible(true);
			userType.setVisible(true);
			foodLabel.setVisible(true);
			foodType.setVisible(true);
			signButton.setVisible(true);
			infoLabel.setVisible(true);
			agend.setText(nameEventCombo.getSelectionModel().getSelectedItem().getAgend());
			date.setText(nameEventCombo.getSelectionModel().getSelectedItem().getTime());
		}));
		signButton.setOnAction((event -> {
			signForEvent();
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
		yourSigns.setOnSelectionChanged((event -> {
			getUserSignedEvents(loggedUser);
		}));
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}
}
