package application.model.serviceCenter;


import java.io.Serializable;

public class Shop extends RecipientOfService implements Serializable {

    public Shop(String name) {
        super(name);
    }

    public Shop() {
        this(null);
    }
}
