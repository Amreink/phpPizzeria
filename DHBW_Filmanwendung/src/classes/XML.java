/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Karsten
 */
public class XML {

    //Erzeugt eine xml-Datei aus einem Objekt
    public void exportXml(Object object, JAXBContext jaxbContext, String path) {
        try {
            //Erzeugt eine Datei in "path"
            File file = new File(path);
            //Schreibt in die File
            FileOutputStream fs = new FileOutputStream(file);
            //Erzeugt ein Marshaller Objekt, welches das Objekt movies in eine xml-Datei umwandelt
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            //Sorgt für Zeilenumbrüche und Einrückungen in der xml-Datei
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //Schreibt xml-Code per Outputstream in die File
            jaxbMarshaller.marshal(object, fs);
        } catch (PropertyException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException | JAXBException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Erzeugt ein Objekt aus einer xml-Datei
    public Object importXml(JAXBContext jaxbContext, String path) {
        try {
            File file = new File(path);
            //Erzeugt ein Marshaller Objekt, welches xml-Code in ein Objekt umwandelt
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            //Erzeugt ein Objekt, welches den Inhalt einer xml-Datei erhält
            Object object = jaxbUnmarshaller.unmarshal(file);
            //Gibt das Objekt zurück
            return object;
        } catch (JAXBException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
