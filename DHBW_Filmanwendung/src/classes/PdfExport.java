package classes;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import controller.MainFXMLController;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class PdfExport {

    //exportiert ein movie objekt 
    public void exportMovie(Movie movie) {
        //speicherort wird hier ausgewählt
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Ordner wählen");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        //wenn speicherort ausgewählt wurde wird die methode weiter ausgeführt
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                //mittels der itext-librarie wird ein dokument objekt erstellt
                //und mit daten befüllt
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(chooser.getCurrentDirectory() + "/" + movie.getTitle() + ".pdf"));
                document.open();
                document.add(Image.getInstance(new URL(movie.getPoster())));
                document.add(new Paragraph(movie.getTitle()));
                document.add(new Paragraph(movie.getYear()));
                document.add(new Paragraph(movie.getGenre()));
                document.add(new Paragraph(movie.getImdbRating()));
                document.add(new Paragraph(movie.getRuntime()));
                document.add(new Paragraph(movie.getPlot()));
                document.add(new Paragraph(movie.getDirector()));
                document.add(new Paragraph(movie.getActors()));
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

    //export eine ganze liste von movies
    //parameter: arraylist vom typ movies, name der zu speicherden datei
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
                    document.add(new Paragraph(movie.getTitle()));
                    document.add(new Paragraph(movie.getYear()));
                    document.add(new Paragraph(movie.getGenre()));
                    document.add(new Paragraph(movie.getImdbRating()));
                    document.add(new Paragraph(movie.getRuntime()));
                    document.add(new Paragraph(movie.getPlot()));
                    document.add(new Paragraph(movie.getDirector()));
                    document.add(new Paragraph(movie.getActors()));
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
