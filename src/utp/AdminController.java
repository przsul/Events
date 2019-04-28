package utp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminController {
    @FXML
    private TableView<User> tableUsers = new TableView<>();
    private TableColumn<User,Integer> columnId = new TableColumn<>("Id");
    private TableColumn<User,String> columnLogin = new TableColumn<>("Login");
    private TableColumn<User,String> columnPassword = new TableColumn<>("Password");
    private TableColumn<User,String> columnFirstName = new TableColumn<>("FirstName");
    private TableColumn<User,String> columnLastName = new TableColumn<>("LastName");
    private TableColumn<User,String> columnEmail = new TableColumn<>("Email");
    private TableColumn<User,String> columnType = new TableColumn<>("Type");
    private User user;

    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/pwswlab05?serverTimezone=UTC";
    private Connection conn;
    private String sql;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private ObservableList<User> data = FXCollections.observableArrayList();
    @FXML
    void initialize(){
        columnId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        columnLogin.setCellValueFactory(new PropertyValueFactory<>("userLogin"));
        columnLogin.setCellFactory(TextFieldTableCell.<User>forTableColumn());

        columnPassword.setCellValueFactory(new PropertyValueFactory<>("userPassword"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("userFirstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("userLastName"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("userAccountType"));
        tableUsers.getColumns().setAll(columnId,columnLogin,columnPassword,columnFirstName,columnLastName,columnEmail,columnType);
        tableUsers.setEditable(true);

        conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
            System.out.println("Connected");
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
        }catch (Exception e){
            System.err.println(e);
        }
        tableUsers.setItems(data);
        System.out.println(user);
    }
    public void setUser(User x){
        this.user = x;
    }
}
