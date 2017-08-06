package vladi_yaki_project;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class DynamicQuery extends Application {
	private ComboBox<String> cboTableName = new ComboBox<>();
	private ComboBox<String> cboTableOrderBy = new ComboBox<>();
	private TableView<?> tableView = new TableView<Object>();
	private Button btShowContents = new Button("Show Contents");
	private Label lblStatus = new Label();
	private String orderBy="";
	// Statement for executing queries
	private Statement stmt;

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		HBox hBox = new HBox(5);
		hBox.getChildren().addAll(new Label("Table Name"), cboTableName,new Label("OrderBy"), cboTableOrderBy, btShowContents);
		hBox.setAlignment(Pos.CENTER);
		BorderPane pane = new BorderPane();
		pane.setCenter(tableView);
		pane.setTop(hBox);
		pane.setBottom(lblStatus);
		// Create a scene and place it in the stage
		Scene scene = new Scene(pane, 560, 300);
		primaryStage.setTitle("Exercise38_04JavaFx"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.setAlwaysOnTop(true);
		primaryStage.show(); // Display the stage
		initializeDB();
		btShowContents.setOnAction(e -> showContents());
	}

	private void initializeDB() {
		try { // Load the JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
			// Establish a connection
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/race_project", "scott", "tiger");
			System.out.println("Database connected");
			// Create a statement
			stmt = connection.createStatement();
			DatabaseMetaData dbMetaData = connection.getMetaData();
			ResultSet rsTables = dbMetaData.getTables(null, null, null, new String[] { "TABLE" });
			System.out.print("User tables: ");
			while (rsTables.next()) {
				cboTableName.getItems().add(rsTables.getString("TABLE_NAME"));
			}
			cboTableName.getSelectionModel().selectFirst();
			updateOrderByList();
			cboTableName.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					updateOrderByList();
				}
			});
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void updateOrderByList(){
		
		try {
			String tableName = cboTableName.getValue();
			String queryString = "select * from " + tableName;
			ResultSet rs;
			rs = stmt.executeQuery(queryString);
			int numOfColums = rs.getMetaData().getColumnCount();
			cboTableOrderBy.getItems().clear();
			for(int i = 1 ; i <= numOfColums ; i++)
			{
				//final int columNum = i - 1;
				//TableColumn<String[],String> column = new TableColumn<>(rs.getMetaData().getColumnLabel(i));
				cboTableOrderBy.getItems().add(rs.getMetaData().getColumnLabel(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	private void showContents() {
		String tableName = cboTableName.getValue();
		orderBy = " ORDER BY "+cboTableOrderBy.getValue() +" ASC";
		try {
			String queryString = "select * from " + tableName + orderBy;
			ResultSet resultSet = stmt.executeQuery(queryString);
			resultSet = stmt.executeQuery(queryString);
			populateTableView(resultSet, tableView);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void populateTableView(ResultSet rs, TableView tableView) { 
		tableView.getColumns().clear();
		tableView.setItems(FXCollections.observableArrayList());
		try { 
			int numOfColums = rs.getMetaData().getColumnCount();
			for(int i = 1 ; i <= numOfColums ; i++)
			{
				final int columNum = i - 1;
				TableColumn<String[],String> column = new TableColumn<>(rs.getMetaData().getColumnLabel(i));
				column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[columNum]));
				tableView.getColumns().add(column);
				//cboTableOrderBy.getItems().add(column.getText());
			}
			while(rs.next())
			{
				String[] cells = new String[numOfColums];
				for(int i = 1 ; i <= numOfColums ; i++)
					cells[i - 1] = rs.getString(i);
				tableView.getItems().add(cells);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
