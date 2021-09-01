module org.itmo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    exports org.itmo;
    exports org.itmo.controllers to javafx.fxml;

    requires org.apache.commons.lang3;
    opens org.itmo.entities to javafx.base;
    opens org.itmo.controllers;;
}