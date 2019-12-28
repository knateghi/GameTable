package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Controller {
    @FXML
    private TableView<Game> _gameTable;

    @FXML
    private TableColumn<Game, Integer> _idColumn;

    @FXML
    private TableColumn<Game, String> _GameTitleColumn;

    @FXML
    private TextField _gameNameTextField;

    @FXML
    private Label _notifiLabel;

    @FXML
    private Label _gameIdLabel;

    private Connection connection;
    private Statement statement;
    private List<Game> games = new ArrayList<>();

    long start;
    long end;
    @FXML
    void onAdd() {

        String gameTitle = _gameNameTextField.getText();

        start =  System.currentTimeMillis();
        Runnable task = new
                Runnable() {
                    @Override
                    public void run() {
                        try {
                            CallableStatement cs = connection.prepareCall(String.format("call add_game_sp('%s')",gameTitle ));
                            cs.execute();
                        } catch (SQLException e) {
                            e.printStackTrace();

                        }
                        Platform.runLater(() -> {
                            end =  System.currentTimeMillis();
                            _notifiLabel.setText(gameTitle+" has added to database, it takes "+ (end - start));
                            updateTable();
                        });
                    }
                };
        new Thread(task).start();
    }

    @FXML
    void onDelete() {
        int id = Integer.parseInt(_gameIdLabel.getText());
        String gameTitle = _gameNameTextField.getText();
        start =  System.currentTimeMillis();
        try {
            CallableStatement cs = connection.prepareCall(String.format("call delete_game_sp(%d)",id));
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        end =  System.currentTimeMillis();
        _notifiLabel.setText(gameTitle+" has deleted to database, it takes "+ (end - start));
        _gameIdLabel.setText("?");
        _gameNameTextField.clear();
        updateTable();
    }

    @FXML
    void onUpdate() {
        int id = Integer.parseInt(_gameIdLabel.getText());
        String gameTitle = _gameNameTextField.getText();
        start =  System.currentTimeMillis();
        try {
            CallableStatement cs = connection.prepareCall(String.format("call update_game_sp(%d,'%s')",id, gameTitle ));
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        end =  System.currentTimeMillis();
        _notifiLabel.setText(gameTitle+" has updated to database, it takes "+ (end - start));
        updateTable();

    }


    @FXML
    protected void initialize() {
        try {
//                        comp214_f19_ers_75
//                        password
            connection = DriverManager.getConnection("jdbc:oracle:thin:@199.212.26.208:1521:sqld", "comp214_f19_ers_75", "password");
            System.out.println("connected to database");


            _idColumn.setCellValueFactory(
                    new PropertyValueFactory<>("gameId")
            );
            _GameTitleColumn.setCellValueFactory(
                    new PropertyValueFactory<>("gameTitle")
            );
            _gameTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue!=null){
                    _gameIdLabel.setText(Integer.toString(newValue.getGameId()));
                    _gameNameTextField.setText(newValue.getGameTitle());
                }
            });
            updateTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateTable() {

        try {
            statement = connection.createStatement();

            ResultSet resultset = statement.executeQuery("select * from game order by game_id ");
            games.clear();
//            _gameTable.refresh();
            while (resultset.next()) {
                Game game = new Game(resultset.getInt(1), resultset.getString(2));
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final ObservableList<Game> data = FXCollections.observableArrayList(games);
        _gameTable.setItems(data);

    }
}




