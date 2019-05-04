package utp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserController {
    private User loggedUser;
    @FXML
    private ComboBox<Event> nameEventCombo = new ComboBox<>();
    @FXML
    private HBox hbox = new HBox();
    @FXML
    private HBox hbox2 = new HBox();

    private TextArea agend = new TextArea();
    private Label agendLabel = new Label("Agenda:");
    private Label dateLabel = new Label("Termin:");
    private Label userLabel = new Label("Typ uczesnictwa:");
    private Label foodLabel = new Label("Jedzenie:");
    private TextField date = new TextField();
    private Button signButton = new Button("Zapisz");

    private ComboBox<String> userType = new ComboBox<>();
    private ComboBox<String> foodType = new ComboBox<>();

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
        }
        catch(SQLIntegrityConstraintViolationException ex){
            System.out.println("Jestes juz zapisany na to wydarzenie");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void initialize(){
        agendLabel.setMaxHeight(20);
        agendLabel.setMaxWidth(50);
        agend.setPrefWidth(200);
        agend.setMaxHeight(200);
        agend.setWrapText(true);
        agend.setTranslateY(20);
        agend.setTranslateX(-45);
        agend.setEditable(false);
        dateLabel.setMaxHeight(20);
        dateLabel.setMaxWidth(50);
        dateLabel.setTranslateX(-35);
        date.setMaxWidth(125);
        date.setMaxHeight(30);
        date.setEditable(false);
        date.setTranslateY(20);
        date.setTranslateX(-75);
        userLabel.setMaxHeight(20);
        userLabel.setMaxWidth(100);
        userLabel.setTranslateX(-60);
        userType.setItems(userT);
        userType.getSelectionModel().selectFirst();
        userType.setMaxHeight(30);
        userType.setMaxWidth(100);
        userType.setTranslateX(-150);
        userType.setTranslateY(20);
        foodLabel.setMaxHeight(30);
        foodLabel.setMaxWidth(100);
        foodLabel.setTranslateX(-120);
        foodLabel.setTranslateY(-5);
        foodType.setItems(food);
        foodType.getSelectionModel().selectFirst();
        foodType.setMaxHeight(30);
        foodType.setMaxWidth(130);
        foodType.setTranslateX(-170);
        foodType.setTranslateY(20);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.err.println(e);
        }
        getAllEvents();
        nameEventCombo.setOnAction((event -> {
            hbox.getChildren().clear();
            hbox2.getChildren().clear();
            agend.setText(nameEventCombo.getSelectionModel().getSelectedItem().getAgend());
            date.setText(nameEventCombo.getSelectionModel().getSelectedItem().getTime());
            hbox.getChildren().setAll(agendLabel,agend,dateLabel,date,userLabel,userType,foodLabel,foodType);
            hbox2.getChildren().setAll(signButton);
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
