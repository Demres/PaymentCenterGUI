package application;

import java.io.*;
import java.util.ArrayList;

import application.model.bank.Bank;
import application.model.bank.Customer;
import application.model.serviceCenter.RecipientOfService;
import application.model.serviceCenter.Shop;
import application.model.serviceCenter.Transaction;
import application.view.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final String DATABASE_PATH = "/home/kamil/Projects/Java/somegui/database/"; // on windows use backslash
    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Bank> bankData = FXCollections.observableArrayList();
    private ObservableList<RecipientOfService> recipientData = FXCollections.observableArrayList();
    private ObservableList<Transaction> transactionData = FXCollections.observableArrayList();


    public MainApp() {

        readObjects();

    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Centrum obsługi płatności");

        initRootLayout();
        showBankOverview();
    }

    private void readObjects() {
        try {
            FileInputStream fin1 = new FileInputStream(DATABASE_PATH + "banks.stored");
            FileInputStream fin2 = new FileInputStream(DATABASE_PATH + "recipients.stored");

            ObjectInputStream ois = new ObjectInputStream(fin1);
            ArrayList<Bank> list1 = (ArrayList<Bank>) ois.readObject();
            bankData = FXCollections.observableArrayList(list1);

            ois = new ObjectInputStream(fin2);
            ArrayList<RecipientOfService> list2 = (ArrayList<RecipientOfService>) ois.readObject();
            recipientData = FXCollections.observableArrayList(list2);
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveObjects() {
        try {
            FileOutputStream fout1 = new FileOutputStream(DATABASE_PATH + "banks.stored");
            FileOutputStream fout2 = new FileOutputStream(DATABASE_PATH + "recipients.stored");

            ObjectOutputStream oos = new ObjectOutputStream(fout1);
            oos.writeObject(new ArrayList<Bank>(bankData));

            oos = new ObjectOutputStream(fout2);
            oos.writeObject(new ArrayList<RecipientOfService>(recipientData));

            oos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> saveObjects());

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Shows the bank overview inside the root layout.
     */
    public void showBankOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BankOverview.fxml"));
            AnchorPane bankOverview = loader.load();
            rootLayout.setCenter(bankOverview);

            BankOverviewController controller = loader.getController();
            controller.setMainApp(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCustomerOverview(Bank bank) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CustomerOverview.fxml"));
            AnchorPane customerOverview = loader.load();
            rootLayout.setCenter(customerOverview);

            CustomerOverviewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setBank(bank);
            controller.addCustomers(FXCollections.observableArrayList(bank.getCustomers()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRecipientOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RecipientOverview.fxml"));
            AnchorPane recipientOverview = loader.load();
            rootLayout.setCenter(recipientOverview);

            RecipientOverviewController controller = loader.getController();
            controller.setMainApp(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showBankEditDialog(Bank bank) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BankEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edytuj bank");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);


            BankEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBank(bank);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showCustomerEditDialog(Customer customer) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CustomerEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edytuj klienta");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);


            CustomerEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomer(customer);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showRecipientEditDialog(RecipientOfService recipient) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RecipientEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edytuj przedsiębiorcę");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);


            RecipientEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRecipient(recipient);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the main stage.
     *
     * @return Stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Bank> getBankData() {
        return bankData;
    }

    public ObservableList<RecipientOfService> getRecipientData() {
        return recipientData;
    }

    public ObservableList<Transaction> getTransactionData() {
        return transactionData;
    }

    public static void main(String[] args) {
        launch(args);
    }
}