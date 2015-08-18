/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import javafx.scene.image.ImageView;

/**
 *
 * @author Arth
 */
public class TableRow {

    private String name;
    private String value;
    private ImageView imageView;

    public TableRow(String name, ImageView imageView) {
        this.name = name;
        this.imageView = imageView;
    }
    
    public TableRow(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
