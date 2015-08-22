/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import controller.MainFXMLController;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author arth
 */
public class PdfExport {

    public void exportMovie(Movie movie) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Ordner wählen");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(chooser.getCurrentDirectory() + "/" + movie.getTitle() + ".pdf"));
                document.open();
                document.add(Image.getInstance(new URL(movie.getPoster())));
                document.add(new Paragraph("Titel: " + movie.getTitle()));
                document.add(new Paragraph("Jahr: " + movie.getYear()));
                document.add(new Paragraph("Genre: " + movie.getGenre()));
                document.add(new Paragraph("IMDB-Rating: " + movie.getImdbRating()));
                document.add(new Paragraph("Laufzeit: " + movie.getRuntime()));
                document.add(new Paragraph("Handlung: " + movie.getPlot()));
                document.add(new Paragraph("Regisseur: " + movie.getDirector()));
                document.add(new Paragraph("Schauspieler: " + movie.getActors()));
                document.add(new LineSeparator(0.5f, 100, null, 0, -5));
                document.close();
            } catch (DocumentException ex) {
                Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(PdfExport.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PdfExport.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("No Selection ");
        }
    }

    public void exportMovies(ArrayList<Movie> Movies, String outputname) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Ordner wählen");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(chooser.getCurrentDirectory() + "/" + outputname + ".pdf"));
                document.open();
                for (Movie movie : Movies) {
                    document.add(Image.getInstance(new URL(movie.getPoster())));
                    document.add(new Paragraph("Titel: " + movie.getTitle()));
                    document.add(new Paragraph("Jahr: " + movie.getYear()));
                    document.add(new Paragraph("Genre: " + movie.getGenre()));
                    document.add(new Paragraph("IMDB-Rating: " + movie.getImdbRating()));
                    document.add(new Paragraph("Laufzeit: " + movie.getRuntime()));
                    document.add(new Paragraph("Handlung: " + movie.getPlot()));
                    document.add(new Paragraph("Regisseur: " + movie.getDirector()));
                    document.add(new Paragraph("Schauspieler: " + movie.getActors()));
                    document.add(new LineSeparator(0.5f, 100, null, 0, -5));
                    document.newPage();
                }
                document.close();
            } catch (DocumentException ex) {
                Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(PdfExport.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PdfExport.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("No Selection ");
        }
    }

}
