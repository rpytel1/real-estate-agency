package sample.components;

import com.sun.org.apache.bcel.internal.generic.NEW;
import javafx.scene.control.Label;

/**
 * Created by Rafal on 2017-01-12.
 */
public class NewEstateLabel extends Label {
    public NewEstateLabel(String str) {
        super(str);
        this.setMinWidth(200);
    }
}
