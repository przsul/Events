package utp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

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

    private TableColumn<User,Long> columnId = new TableColumn<>("Id");
    private TableColumn<User,String> columnLogin = new TableColumn<>("Login");
    private TableColumn<User,String> columnPassword = new TableColumn<>("Password");
    private TableColumn<User,String> columnFirstName = new TableColumn<>("FirstName");
    private TableColumn<User,String> columnLastName = new TableColumn<>("LastName");
    private TableColumn<User,String> columnEmail = new TableColumn<>("Email");
    private TableColumn<User,String> columnType = new TableColumn<>("Type");

    private TableColumn<Event,Long> columnEventId = new TableColumn<>("Id");
    private TableColumn<Event,String> columnEventName = new TableColumn<>("Name");
    private TableColumn<Event,String> columnEventAgend = new TableColumn<>("Agend");
    private TableColumn<Event, String> columnEventTime = new TableColumn<>("Time");

    private TableColumn<EventSign, User> columnEventSignUser = new TableColumn<>("User");
    private TableColumn<EventSign, Event> columnEventSignEvent = new TableColumn<>("Event");
    private TableColumn<EventSign, String> columnEventSignType = new TableColumn<>("Type");
    private TableColumn<EventSign, String> columnEventSignFood = new TableColumn<>("Food");
    private TableColumn<EventSign, String> columnEventSignStatus = new TableColumn<>("Status");


    private User loggedUser;

    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/pwswlab05?serverTimezone=Europe/Warsaw&useLegacyDatetimeCode=false&useUnicode=true&characterEncoding=utf-8";
    private Connection conn;
    private String sql;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private ObservableList<User> data = FXCollections.observableArrayList();
    private ObservableList<Event> eventData = FXCollections.observableArrayList();
    private ObservableList<EventSign> eventSignData = FXCollections.observableArrayList();

    private void sendUpdateUserQueryCell(String table,String string, String parameter, Long id){
        try {
            sql = "UPDATE " + table + " SET " + string + " = ? WHERE id = ?";
            preparedStatement = conn.prepareStatement(sql);
            if(table.equals("events") && string.equals("time")) preparedStatement.setTimestamp(1,
                    Timestamp.valueOf(LocalDateTime.parse(parameter,formatter)));
            else preparedStatement.setString(1,parameter);
            preparedStatement.setLong(2,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void sendInsertUserQueryCell(User user){
        try {
            sql = "INSERT INTO users (login, password, firstname, lastname, email, type)" +
                    "VALUES (?, md5(?), ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) ;
            preparedStatement.setString(1,user.getUserLogin());
            preparedStatement.setString(2,user.getUserPassword());
            preparedStatement.setString(3,user.getUserFirstName());
            preparedStatement.setString(4,user.getUserLastName());
            preparedStatement.setString(5,user.getUserEmail());
            preparedStatement.setString(6,user.getUserAccountType());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                user.setUserId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void sendInsertEventQueryCell(Event event){
        try {
            sql = "INSERT INTO events (name, agend, time)" +
                    "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) ;
            preparedStatement.setString(1,event.getName());
            preparedStatement.setString(2,event.getAgend());
            preparedStatement.setTimestamp(3,Timestamp.valueOf(LocalDateTime.parse(event.getTime(),formatter)));
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                event.setEventId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void sendDeleteUserQueryCell(User user){
        try {
            sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql) ;
            preparedStatement.setLong(1,user.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void sendDeleteEventQueryCell(Event event){
        try {
            sql = "DELETE FROM events WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql) ;
            preparedStatement.setLong(1,event.getEventId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void reloadTableUser(){
        try {
            data.clear();;
            sql = "SELECT * FROM users";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                data.add(new User(resultSet.getInt(1),
                        resultSet.getString(3),
                        resultSet.getString(2),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)));
            }
            tableUsers.setItems(data);
        }catch (Exception e){
            System.err.println(e);
        }
    }
    private void reloadTableEvent(){
        try {
            eventData.clear();;
            sql = "SELECT id, name, agend, time FROM events";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                eventData.add(new Event(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getTimestamp(4).toLocalDateTime().format(formatter)));
            }
            tableEvents.setItems(eventData);
        }catch (Exception e){
            System.err.println(e);
        }
    }
    private void reloadTableSignedEvent(){

        try {
            eventSignData.clear();
            sql = "SELECT users.id,users.login,users.password,users.firstname,users.lastname,users.email,users.type,events.id,events.name,events.agend,events.time,users_events.type,users_events.food,users_events.status FROM users INNER JOIN users_events ON users.id = users_events.id_user INNER JOIN events ON users_events.id_event = events.id";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                User user = new User(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                        );
                Event event = new Event(
                        resultSet.getLong(8),
                        resultSet.getString(9),
                        resultSet.getString(10),
                        resultSet.getTimestamp(11).toLocalDateTime().format(formatter)
                );

                eventSignData.add(new EventSign(user,event,resultSet.getString(12),resultSet.getString(13),resultSet.getString(14)));
            }
            tableEvents.setItems(eventData);
        }catch (Exception e){
            System.err.println(e);
        }
    }
    private void setEventSignedStatus(String status,User user, Event event){
        try {
            sql = "UPDATE users_events SET status = ? WHERE id_user = ? AND id_event = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2,user.getUserId());
            preparedStatement.setLong(3,event.getEventId());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.err.println(e);
        }
    }
    public void setUser(User x){
        this.loggedUser = x;
    }
    @FXML
    void initialize(){
        columnId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        columnLogin.setCellValueFactory(new PropertyValueFactory<>("userLogin"));
        columnLogin.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnLogin.setOnEditCommit((TableColumn.CellEditEvent<User, String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserLogin(t.getNewValue());
            sendUpdateUserQueryCell("users","login",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserLogin(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
                });
        columnPassword.setCellValueFactory(new PropertyValueFactory<>("userPassword"));
        columnPassword.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnPassword.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserPassword(t.getNewValue());
            sendUpdateUserQueryCell("users","password",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserPassword(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
        });
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("userFirstName"));
        columnFirstName.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnFirstName.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserFirstName(t.getNewValue());
            sendUpdateUserQueryCell("users","firstname",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserFirstName(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
        });
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("userLastName"));
        columnLastName.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnLastName.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserLastName(t.getNewValue());
            sendUpdateUserQueryCell("users","lastname",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserLastName(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
        });
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        columnEmail.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnEmail.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserEmail(t.getNewValue());
            sendUpdateUserQueryCell("users","email",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserEmail(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
        });
        columnType.setCellValueFactory(new PropertyValueFactory<>("userAccountType"));
        columnType.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnType.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserAccountType(t.getNewValue());
            sendUpdateUserQueryCell("users","type",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserAccountType(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
        });
        tableUsers.getColumns().setAll(columnId,columnLogin,columnPassword,columnFirstName,columnLastName,columnEmail,columnType);
        tableUsers.setEditable(true);
        //Kolumny Event
        columnEventId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        columnEventName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnEventName.setCellFactory(TextFieldTableCell.<Event>forTableColumn());
        columnEventName.setOnEditCommit((TableColumn.CellEditEvent<Event,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setName(t.getNewValue());
            sendUpdateUserQueryCell("events", "name",t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getEventId());
        });
        columnEventAgend.setCellValueFactory(new PropertyValueFactory<>("agend"));
        columnEventAgend.setCellFactory(TextFieldTableCell.<Event>forTableColumn());
        columnEventAgend.setOnEditCommit((TableColumn.CellEditEvent<Event,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setAgend(t.getNewValue());
            sendUpdateUserQueryCell("events", "agend",t.getTableView().getItems().get(t.getTablePosition().getRow()).getAgend(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getEventId());
        });
        columnEventTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        columnEventTime.setCellFactory(TextFieldTableCell.<Event>forTableColumn());
        columnEventTime.setOnEditCommit((TableColumn.CellEditEvent<Event,String> t) -> {
            if(t.getNewValue().matches("[0-9]{2}-[0-9]{2}-[0-9]{4} [0-9]{2}:[0-9]{2}")){
                t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        .setTime(t.getNewValue());
                sendUpdateUserQueryCell("events", "time",t.getTableView().getItems().get(t.getTablePosition().getRow()).getTime(),
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).getEventId());
            }

        });
        tableEvents.getColumns().setAll(columnEventId,columnEventName,columnEventAgend,columnEventTime);
        tableEvents.setItems(eventData);
        tableEvents.setEditable(true);
        //SignEvent
        columnEventSignUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        columnEventSignEvent.setCellValueFactory(new PropertyValueFactory<>("event"));
        columnEventSignType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnEventSignFood.setCellValueFactory(new PropertyValueFactory<>("food"));
        columnEventSignStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableSigned.setItems(eventSignData);
        tableSigned.getColumns().setAll(columnEventSignUser,columnEventSignEvent,columnEventSignType,columnEventSignFood,columnEventSignStatus);
        tableSigned.setEditable(false);
        conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
            reloadTableUser();
            reloadTableEvent();
            reloadTableSignedEvent();
        }catch (Exception e){
            System.err.println(e);
        }
        //Obsluga przycisku dodaj - uzytkownika
        userAdd.setOnAction((event -> {
            User newUser = new User(0,"Nowy","Nowy","Nowy","Nowy","Nowy", "U");
            sendInsertUserQueryCell(newUser);
            data.add(newUser);
        }));
        //Obsluga przycisku usun - uzytkownika
        userDelete.setOnAction((event -> {
            User userToDelete = tableUsers.getSelectionModel().getSelectedItem();
            sendDeleteUserQueryCell(userToDelete);
            data.remove(userToDelete);
        }));
        eventAdd.setOnAction((event -> {
            Event newEvent = new Event(0,"Nowy","Nowy", LocalDateTime.now().format(formatter));
            sendInsertEventQueryCell(newEvent);
            eventData.add(newEvent);
        }));
        eventDelete.setOnAction(event -> {
            Event eventToDelete = tableEvents.getSelectionModel().getSelectedItem();
            sendDeleteEventQueryCell(eventToDelete);
            eventData.remove(eventToDelete);
        });
        signedAccept.setOnAction((event -> {
            setEventSignedStatus("Potwierdzone", tableSigned.getSelectionModel().getSelectedItem().getUser(),
                    tableSigned.getSelectionModel().getSelectedItem().getEvent());
            reloadTableSignedEvent();
        }));
        signedDecline.setOnAction((event -> {
            setEventSignedStatus("Odrzucone", tableSigned.getSelectionModel().getSelectedItem().getUser(),
                    tableSigned.getSelectionModel().getSelectedItem().getEvent());
            reloadTableSignedEvent();
        }));
        logoutButton.setOnAction((event -> {
            try{
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("mainview.fxml"));
                Parent root = fxmlLoader.load();
                Scene dashboard = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(dashboard);
            }
            catch (Exception e){
                System.out.println(e);
            }
        }));
    }

}
