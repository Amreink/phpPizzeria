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

    //
    public void exportXml(Object object, JAXBContext jaxbContext, String path) {
        try {
            File file = new File(path);
            FileOutputStream fs = new FileOutputStream(file);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(object, fs);
        } catch (PropertyException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException | JAXBException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //
    public Object importXml(JAXBContext jaxbContext, String path) {
        try {
            File file = new File(path);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Object object = jaxbUnmarshaller.unmarshal(file);
            return object;
        } catch (JAXBException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
