package utp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private TableColumn<UserSignedEvents,String> columnEventName = new TableColumn<>("Name");
    private TableColumn<UserSignedEvents,String> columnEventAgend = new TableColumn<>("Agend");
    private TableColumn<UserSignedEvents, String> columnEventTime = new TableColumn<>("Time:");
    private TableColumn<UserSignedEvents, String> columnEventStatus = new TableColumn<>("Status:");

    private ObservableList<Event> events = FXCollections.observableArrayList();
    private ObservableList<String> userT = FXCollections.observableArrayList(
            "Słuchacz",
            "Autor",
            "Sponsor",
            "Organizator"
    );
    private ObservableList<String> food = FXCollections.observableArrayList(
            "Bez preferencji",
            "Wegetariańskie",
            "Bez glutenu"
    );
    private ObservableList<UserSignedEvents> userSignedEvents = FXCollections.observableArrayList();




    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/pwswlab05?serverTimezone=Europe/Warsaw&useLegacyDatetimeCode=false&useUnicode=true&characterEncoding=utf-8";
    private Connection conn;
    private String sql;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;



    private void getAllEvents(){
        try {
            sql = "SELECT * FROM events";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                events.add(new Event(
                   resultSet.getLong(1),
                   resultSet.getString(2),
                   resultSet.getString(3),
                        resultSet.getTimestamp(4).toLocalDateTime().format(formatter)
                ));
            }
            nameEventCombo.setItems(events);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void signForEvent(){
        try {
            sql = "INSERT INTO users_events (id_user,id_event,type,food,status) VALUES (?,?,?,?,'Oczekujace')";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setLong(1,loggedUser.getUserId());
            preparedStatement.setLong(2,nameEventCombo.getSelectionModel().getSelectedItem().getEventId());
            preparedStatement.setString(3,userType.getSelectionModel().getSelectedItem());
            preparedStatement.setString(4,foodType.getSelectionModel().getSelectedItem());
            preparedStatement.executeUpdate();
            infoLabel.setText("INFO: Pomyślnie zostałeś zapisany na wydarzenie");
        }
        catch(SQLIntegrityConstraintViolationException ex){
            infoLabel.setText("INFO: Jestes juz zapisany na to wydarzenie");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void getUserSignedEvents(User user){
        try{
            userSignedEvents.clear();
            sql = "SELECT events.id,events.name, events.agend,events.time,users_events.status FROM events" +
                    " INNER JOIN users_events ON events.id = users_events.id_event" +
                    " INNER JOIN users ON users_events.id_user = users.id WHERE users.id = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setLong(1,user.getUserId());
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                userSignedEvents.add(new UserSignedEvents(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getTimestamp(4).toLocalDateTime().format(formatter),
                        resultSet.getString(5)));
            }
            tableSignedEvents.setItems(userSignedEvents);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void initialize(){
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
        tableSignedEvents.getColumns().setAll(columnEventName,columnEventAgend,columnEventTime,columnEventStatus);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.err.println(e);
        }
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
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
