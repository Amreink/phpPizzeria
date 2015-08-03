/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw_filmanwendung;

/**
 *
 * @author Artur
 */
public class IMDB {

    private static IMDB instance = null;

    protected IMDB() {
        // Exists only to defeat instantiation.
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

}
