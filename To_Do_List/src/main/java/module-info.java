module com.rfn.to_do_list {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;


    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;

    opens com.rfn.to_do_list to javafx.fxml;
    exports com.rfn.to_do_list;
}