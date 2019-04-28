package utp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.awt.event.ActionEvent;
import java.sql.*;

public class AdminController {
    @FXML
    private TableView<User> tableUsers = new TableView<>();
    @FXML
    private Button userAdd;
    @FXML
    private Button userDelete;


    private TableColumn<User,Integer> columnId = new TableColumn<>("Id");
    private TableColumn<User,String> columnLogin = new TableColumn<>("Login");
    private TableColumn<User,String> columnPassword = new TableColumn<>("Password");
    private TableColumn<User,String> columnFirstName = new TableColumn<>("FirstName");
    private TableColumn<User,String> columnLastName = new TableColumn<>("LastName");
    private TableColumn<User,String> columnEmail = new TableColumn<>("Email");
    private TableColumn<User,String> columnType = new TableColumn<>("Type");
    private User loggedUser;

    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/pwswlab05?serverTimezone=UTC";
    private Connection conn;
    private String sql;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private ObservableList<User> data = FXCollections.observableArrayList();

    private void sendUpdateUserQueryCell(String string, String parameter, Long id){
        try {
            sql = "UPDATE users SET " + string + " = ? WHERE id = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,parameter);
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
            sendUpdateUserQueryCell("login",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserLogin(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
                });
        columnPassword.setCellValueFactory(new PropertyValueFactory<>("userPassword"));
        columnPassword.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnPassword.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserPassword(t.getNewValue());
            sendUpdateUserQueryCell("password",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserPassword(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
        });
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("userFirstName"));
        columnFirstName.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnFirstName.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserFirstName(t.getNewValue());
            sendUpdateUserQueryCell("firstname",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserFirstName(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
        });
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("userLastName"));
        columnLastName.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnLastName.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserLastName(t.getNewValue());
            sendUpdateUserQueryCell("lastname",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserLastName(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
        });
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        columnEmail.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnEmail.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserEmail(t.getNewValue());
            sendUpdateUserQueryCell("email",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserEmail(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
        });
        columnType.setCellValueFactory(new PropertyValueFactory<>("userAccountType"));
        columnType.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        columnType.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) -> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setUserAccountType(t.getNewValue());
            sendUpdateUserQueryCell("type",t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserAccountType(),
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).getUserId());
        });
        tableUsers.getColumns().setAll(columnId,columnLogin,columnPassword,columnFirstName,columnLastName,columnEmail,columnType);
        tableUsers.setEditable(true);

        conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
            reloadTableUser();
        }catch (Exception e){
            System.err.println(e);
        }
        //Obsluga przycisku dodaj - uzytkownika
        userAdd.setOnAction((event -> {
            User newUser = new User(data.get(data.size()-1).getUserId()+1,"Nowy","Nowy","Nowy","Nowy","Nowy", "U");
            sendInsertUserQueryCell(newUser);
            data.add(newUser);
        }));
        //Obsluga przycisku usun - uzytkownika
        userDelete.setOnAction((event -> {
            User userToDelete = tableUsers.getSelectionModel().getSelectedItem();
            sendDeleteUserQueryCell(userToDelete);
            data.remove(userToDelete);
        }));

    }

}
