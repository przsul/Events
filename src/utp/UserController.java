package utp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserController {
    private User loggedUser;
    @FXML
    private ComboBox<Event> nameEventCombo = new ComboBox<>();


    private ObservableList<Event> events = FXCollections.observableArrayList();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/pwswlab05?serverTimezone=UTC";
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

    @FXML
    void initialize(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.err.println(e);
        }
        getAllEvents();
        nameEventCombo.setOnAction((event -> {
            System.out.println("EVENT");
        }));
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
