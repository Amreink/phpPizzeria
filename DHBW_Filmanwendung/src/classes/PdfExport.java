package classes;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
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

public class PdfExport {

    //exportiert ein movie objekt 
    public void exportMovie(Movie movie) {
        //speicherort wird hier ausgewählt
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Ordner wählen");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setApproveButtonText("Speichern");
        
        //wenn speicherort ausgewählt wurde wird die methode weiter ausgeführt
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                //mittels der itext-librarie wird ein dokument objekt erstellt
                //und mit daten befüllt
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(chooser.getCurrentDirectory() + "/" + movie.getTitle() + ".pdf"));

                document.open();

                Image image = Image.getInstance(new URL(movie.getPoster()));
                image.setAlignment(1);
                image.scalePercent(70);
                LineSeparator ls = new LineSeparator();
                Font boldFont = FontFactory.getFont("Times-Roman", 20, Font.BOLD);

                Paragraph title = new Paragraph(movie.getTitle(), boldFont);
                title.setAlignment(1);

                //Element in der Reihnfolge wie sie angezeigt werden sollen
                document.add(title);
                document.add(new Chunk(ls));
                document.add(new Chunk().NEWLINE);
                document.add(image);
                document.add(new Paragraph("Jahr: " + movie.getYear()));
                document.add(new Paragraph("Genre: " + movie.getGenre()));
                document.add(new Paragraph("IMDB-Rating: " + movie.getImdbRating()));
                document.add(new Paragraph("Laufzeit: " + movie.getRuntime()));
                document.add(new Paragraph("Regisseur: " + movie.getDirector()));
                document.add(new Paragraph("Schauspieler: " + movie.getActors()));
                document.add(new Chunk(ls));
                document.add(new Paragraph(movie.getPlot()));
                document.add(new Chunk(ls));
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
        chooser.setApproveButtonText("Speichern");

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(chooser.getCurrentDirectory() + "/" + outputname + ".pdf"));
                document.open();
                for (Movie movie : Movies) {
                    Image image = Image.getInstance(new URL(movie.getPoster()));
                    image.setAlignment(1);
                    image.scalePercent(70);
                    LineSeparator ls = new LineSeparator();
                    Font boldFont = FontFactory.getFont("Times-Roman", 20, Font.BOLD);

                    Paragraph title = new Paragraph(movie.getTitle(), boldFont);
                    title.setAlignment(1);

                    //Element in der Reihnfolge wie sie angezeigt werden sollen
                    document.add(title);
                    document.add(new Chunk(ls));
                    document.add(new Chunk().NEWLINE);
                    document.add(image);
                    document.add(new Paragraph("Jahr: " + movie.getYear()));
                    document.add(new Paragraph("Genre: " + movie.getGenre()));
                    document.add(new Paragraph("IMDB-Rating: " + movie.getImdbRating()));
                    document.add(new Paragraph("Laufzeit: " + movie.getRuntime()));
                    document.add(new Paragraph("Regisseur: " + movie.getDirector()));
                    document.add(new Paragraph("Schauspieler: " + movie.getActors()));
                    document.add(new Chunk(ls));
                    document.add(new Paragraph(movie.getPlot()));
                    document.add(new Chunk(ls));
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
