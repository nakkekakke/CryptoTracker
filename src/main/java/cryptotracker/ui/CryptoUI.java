package cryptotracker.ui;

import cryptotracker.domain.*;
import cryptotracker.dao.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/** The class for the user interface
 * 
 */
public class CryptoUI extends Application {
    
    private CryptoService service;
    private InputChecker inputChecker;
    
    private Stage primaryStage;
    
    private Scene loginScene;
    private Scene loggedInScene;
    private Scene cryptoAddScene;
    private Scene batchScene;
    
    
    private VBox cryptoList;
    private VBox batchList;
    private Label menuLabel;
    
    
    /** Initializes the program
     *
     * @throws java.lang.ClassNotFoundException If initializing the database fails
     * @throws java.io.FileNotFoundException If configuration file was not found
     * @throws java.io.IOException
     * @throws java.sql.SQLException 
     */
    @Override
    public void init() throws ClassNotFoundException, FileNotFoundException, IOException, SQLException {
        Properties properties = new Properties();
        
        properties.load(new FileInputStream("config.properties"));
        
        String databaseAddress = properties.getProperty("databaseAddress");
        
        Database database = new Database("jdbc:sqlite:" + databaseAddress);
        database.initializeTables();
        
        DBUserDao userDao = new DBUserDao(database);
        DBPortfolioDao portfolioDao = new DBPortfolioDao(database, userDao);
        DBCryptocurrencyDao cryptoDao = new DBCryptocurrencyDao(database, portfolioDao);
        DBCryptoBatchDao batchDao = new DBCryptoBatchDao(database, cryptoDao);
        this.service = new CryptoService(userDao, portfolioDao, cryptoDao, batchDao);
        this.inputChecker = new InputChecker();
        this.menuLabel = new Label();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        
        loginScene = constructLogInScene();
        loggedInScene = constructLoggedInScene();
        cryptoAddScene = constructCryptoAddScene();
        batchScene = constructCryptoBatchScene();
        
        primaryStage.setTitle("CryptoTracker");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Closing program");
        });
    }
    
    //
    // CONSTRUCT SCENES
    //
    
    private Scene constructLoggedInScene() {
        ScrollPane mainScrollPane = new ScrollPane();
        BorderPane mainPane = new BorderPane(mainScrollPane);
        
        
        HBox menuPane = new HBox(10);
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button addCryptoSceneButton = new Button("Add Crypto");
        Button logoutButton = new Button("Logout");
        menuPane.getChildren().addAll(menuLabel, menuSpacer, addCryptoSceneButton, logoutButton);
        
        cryptoList = new VBox(10);
        cryptoList.setMaxWidth(1000);
        cryptoList.setMinWidth(280);
        refreshCryptoList();
        
        mainScrollPane.setContent(cryptoList);
        mainPane.setTop(menuPane);
        
        addCryptoSceneButton.setOnAction(e -> {
            primaryStage.setScene(cryptoAddScene);
        });
        
        logoutButton.setOnAction(e -> {
            service.logout();
            primaryStage.setScene(loginScene);
        });
        
        return new Scene(mainPane, 400, 250);
    }
    
    private Scene constructLogInScene() {
        VBox loginPane = new VBox(10);
        HBox inputPane = new HBox(10);
        loginPane.setPadding(new Insets(10));
        Label loginLabel = new Label("Enter your username");
        TextField usernameInput = new TextField();
        
        inputPane.getChildren().addAll(loginLabel, usernameInput);
        Label loginMessage = new Label();
        
        Button loginButton = new Button("Login");
        Button registrationButton = new Button("Register");
        
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            menuLabel.setText("Welcome, " + username + "!");
            if (service.login(username)) {
                loginMessage.setText("");
                usernameInput.setText("");
                primaryStage.setScene(loggedInScene);
                refreshCryptoList();
            } else {
                loginMessage.setText("Username doesn't exist!");
                loginMessage.setTextFill(Color.RED);
            }
        });
        
        registrationButton.setOnAction(e -> {
            String username = usernameInput.getText();
            
            if (service.usernameLengthValid(username) == false) {
                loginMessage.setText("Username must be " + 
                        service.getUsernameMinLength() + "-" + 
                        service.getUsernameMaxLength() + " characters long!");
                loginMessage.setTextFill(Color.RED);
            } else if (service.createUser(username)) {
                loginMessage.setText("");
                loginMessage.setText("New user created!");
                loginMessage.setTextFill(Color.GREEN);
            } else {
                loginMessage.setText("Username already exists!");
                loginMessage.setTextFill(Color.RED);
            }
        });
        
        loginPane.getChildren().addAll(loginMessage, inputPane, loginButton, registrationButton);
        
        return new Scene(loginPane, 400, 250);
    }
    
    private Scene constructCryptoAddScene() {
        VBox cryptoAddPane = new VBox(10);
        
        HBox cryptoNamePane = new HBox(10);
        cryptoNamePane.setPadding(new Insets(10));
        Label cryptoNameLabel = new Label("The name of the crypto");
        TextField cryptoNameInput = new TextField();
        cryptoNameInput.setPrefWidth(100);
        cryptoNamePane.getChildren().addAll(cryptoNameLabel, cryptoNameInput);
        
        HBox cryptoAmountPane = new HBox(10);
        cryptoAmountPane.setPadding(new Insets(10));
        Label cryptoAmountLabel = new Label("Amount of crypto");
        TextField cryptoAmountInput = new TextField();
        cryptoAmountPane.getChildren().addAll(cryptoAmountLabel, cryptoAmountInput);
        
        HBox cryptoPricePane = new HBox(10);
        cryptoPricePane.setPadding(new Insets(10));
        Label cryptoPriceLabel = new Label("Price paid (€)");
        TextField cryptoPriceInput = new TextField();
        cryptoPricePane.getChildren().addAll(cryptoPriceLabel, cryptoPriceInput);
        
        HBox cryptoDatePane = new HBox(10);
        cryptoDatePane.setPadding(new Insets(10));
        Label cryptoDateLabel = new Label("Date of purchase (YYYY-MM-DD)");
        TextField cryptoDateInput = new TextField();
        cryptoDatePane.getChildren().addAll(cryptoDateLabel, cryptoDateInput);
        
        HBox cryptoAddButtons = new HBox(10);
        cryptoAddButtons.setPadding(new Insets(10));
        Button cryptoAddButton = new Button("Add crypto");
        Button backToMainSceneButton = new Button("Go back");
        Region cryptoButtonSpacer = new Region();
        HBox.setHgrow(cryptoButtonSpacer, Priority.ALWAYS);
        cryptoAddButtons.getChildren().addAll(cryptoAddButton, cryptoButtonSpacer, backToMainSceneButton);
        
        Label cryptoAddLabel = new Label("Make sure to fill every field!");
        
        
        cryptoAddPane.getChildren().addAll(cryptoAddLabel, cryptoNamePane, cryptoAmountPane, cryptoPricePane, cryptoDatePane, cryptoAddButtons);
        
        
        cryptoAddButton.setOnAction(e -> {
            String name = cryptoNameInput.getText();
            String amount = cryptoAmountInput.getText();
            String price = cryptoPriceInput.getText();
            String date = cryptoDateInput.getText();
            
            Integer intAmount = inputChecker.tryParseInt(amount);
            Integer intPrice = inputChecker.tryParseInt(price);
            LocalDate parsedDate = inputChecker.tryParseDate(date);
            
            if (name.length() < 1 || name.length() > 50) {
                cryptoAddLabel.setText("That's not a valid name...");
                cryptoAddLabel.setTextFill(Color.RED);
            } else if (inputChecker.isInteger(amount) == false || inputChecker.isInteger(price) == false) {
                cryptoAddLabel.setText("Sorry, for now only integers are allowed!");
                cryptoAddLabel.setTextFill(Color.RED);
            } else if (intAmount == null || intAmount < 1) {
                cryptoAddLabel.setText("Invalid amount value!");
                cryptoAddLabel.setTextFill(Color.RED);
            } else if (intPrice == null || intPrice < 1) {
                cryptoAddLabel.setText("Invalid price value!");
                cryptoAddLabel.setTextFill(Color.RED);
            } else if (inputChecker.isValidDate(date) == false) {
                cryptoAddLabel.setText("Invalid format of date!");
                cryptoAddLabel.setTextFill(Color.RED);
            } else if (parsedDate == null) {
                cryptoAddLabel.setText("Invalid date value!");
                cryptoAddLabel.setTextFill(Color.RED);
            } else {
                CryptoBatch newBatch = service.createCrypto(name, intAmount, intPrice, parsedDate);
                if (newBatch == null) {
                    cryptoAddLabel.setText("Adding a new batch of " + name + " failed! :O");
                    cryptoAddLabel.setTextFill(Color.RED);
                } else {
                    cryptoAddLabel.setText("A new batch of " + name + " added!");
                    cryptoAddLabel.setTextFill(Color.GREEN);
                }
                resetCryptoAddFields(cryptoNameInput, cryptoAmountInput, cryptoPriceInput, cryptoDateInput);
                refreshCryptoList();
            }
        });
        
        backToMainSceneButton.setOnAction(e -> {
            cryptoAddLabel.setText("Make sure to fill every field!");
            cryptoAddLabel.setTextFill(Color.BLACK);
            cryptoNameInput.setText("");
            primaryStage.setScene(loggedInScene);
        });
        
        return new Scene(cryptoAddPane, 500, 300);
    }
    
    private Scene constructCryptoBatchScene() {
        ScrollPane batchScrollPane = new ScrollPane();
        BorderPane batchPane = new BorderPane(batchScrollPane);
        
        HBox batchMenuPane = new HBox(10);
        Region batchMenuSpacer = new Region();
        HBox.setHgrow(batchMenuSpacer, Priority.ALWAYS);
        Button fromBatchToMainButton = new Button("Go back");
        batchMenuPane.getChildren().addAll(batchMenuSpacer, fromBatchToMainButton);
        
        batchList = new VBox(10);
        batchList.setMaxWidth(280);
        batchList.setMinWidth(280);
        
        batchScrollPane.setContent(batchList);
        batchPane.setTop(batchMenuPane);
        
        fromBatchToMainButton.setOnAction(e -> {
            primaryStage.setScene(loggedInScene);
        });
        
        return new Scene(batchPane, 500, 300);
    }
    
    // ASSISTANCE METHODS
    
    public Node createCryptoNode(Cryptocurrency crypto) {
        HBox node = new HBox(20);
        Label label = new Label(crypto.getName());
        Button batchButton = new Button("See batches");
        Button deleteButton = new Button("Delete crypto");
        
        batchButton.setOnAction(e -> {
            refreshBatchList(crypto);
            primaryStage.setScene(batchScene);
        });
        
        deleteButton.setOnAction(e -> {
            service.deleteCryptocurrency(crypto);
            refreshCryptoList();
        });
        Region nodeSpacer = new Region();
        HBox.setHgrow(nodeSpacer, Priority.ALWAYS);
        node.setPadding(new Insets(0, 5, 0, 5));
        
        node.getChildren().addAll(label, nodeSpacer, batchButton, deleteButton);
        return node;
    }
    
    private void refreshCryptoList() {
        cryptoList.getChildren().clear();
        
        List<Cryptocurrency> cryptos = service.getCryptosInPortfolio();
        cryptos.forEach(crypto -> {
            cryptoList.getChildren().add(createCryptoNode(crypto));
        });
    }
    
    public Node createBatchNode(CryptoBatch batch) {
        VBox node = new VBox(20);
        HBox dateNode = new HBox(10);
        HBox amountNode = new HBox(10);
        HBox priceNode = new HBox(10);
        
        Label dateLabel = new Label("Date of purchase:");
        Label date = new Label(batch.getDate().toString());
        Label amountLabel = new Label("Amount:");
        Label amount = new Label(String.valueOf(batch.getAmount()));
        Label priceLabel = new Label("Price paid:");
        Label price = new Label(String.valueOf(batch.getTotalPaid()) + " €");
        Button deleteButton = new Button("Delete batch");
        
        deleteButton.setOnAction(e -> {
            service.deleteBatch(batch.getId());
            refreshBatchList(batch.getCrypto());
        });
        
        dateNode.getChildren().addAll(dateLabel, date);
        amountNode.getChildren().addAll(amountLabel, amount);
        priceNode.getChildren().addAll(priceLabel, price);
        node.setPadding(new Insets(5, 5, 5, 0));
        
        String layout = "-fx-border-style: hidden hidden solid hidden;\n" +
                "-fx-border-color: black;\n" +
                "-fx-border-width: 1;";
        node.setStyle(layout);
        
        node.getChildren().addAll(dateNode, amountNode, priceNode, deleteButton);
        return node;
    }
    
    private void refreshBatchList(Cryptocurrency crypto) {
        batchList.getChildren().clear();
        
        List<CryptoBatch> batches = service.getBatchesOfCrypto(crypto);
        
        if (!batches.isEmpty()) {
            batches.forEach(batch -> {
                batchList.getChildren().add(createBatchNode(batch));
            });
        } else {
            service.deleteCryptocurrency(crypto);
            refreshCryptoList();
        }
    }
    
    private void resetCryptoAddFields(TextField nameInput, TextField amountInput, TextField priceInput, TextField dateInput) {
        nameInput.setText("");
        amountInput.setText("");
        priceInput.setText("");
        dateInput.setText("");
    }
    
    @Override
    public void stop() {
        service.logout();
        System.out.println("Bye!");
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
