package application.model.bank;



import application.model.exceptions.FundsException;
import application.model.exceptions.PaymentRefusedException;
import application.util.MySimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

public class Bank implements Serializable {

    private final StringProperty name;
    private final int bankPrefix = new Random().nextInt(90000) + 10000;
    private ArrayList<Customer> customers = new ArrayList<>();

    public int getBankPrefix() {
        return bankPrefix;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getCustomersNumber() {
        return customers.size();
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public Bank() {
        this(null);
    }

    public Bank(String name) {
        this.name = new MySimpleStringProperty(name);
    }

    public void chargeCard(int cardNumber, BigDecimal amount)
            throws FundsException, PaymentRefusedException {
        if (new Random().nextInt(100) + 1 > 20) {
            Card card = findCard(cardNumber);
            if (card == null) throw new PaymentRefusedException("Card not found");
            card.charge(amount);
        }
        else {
            throw new PaymentRefusedException("Bank has refused transaction");
        }
    }

    public Card findCard(int cardNumber) {
        for (Customer customer : customers) {
            for (Card card : customer.getCards())
                if (card.getCardNumber() == cardNumber) return card;
        }

        return null;
    }
}
