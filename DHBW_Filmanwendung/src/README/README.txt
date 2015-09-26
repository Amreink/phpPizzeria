**************************************************************
*************************Filmanwendung************************
**Autoren: Artur Stalbaum, Timo Schmidt, Karsten Amrein     **
**Kurs:WWI14B                                               **
**Datum: 26.09.2015                                         **
**Dozent: Prof. Dr. Erik Behrends                           **
**************************************************************
****************************README:***************************
****************JavaFX Version 8.0.40 benötigt****************
***************************Imports****************************
gson-2.3.1.jar
sqlite-jdbc-3.8.11.1.jar
itextpdf-5.5.6.jar
controlfx-8.40.9.jar
**************************************************************
*******************Inhalte der Anwendung:*********************
*************************MUST-HAVE****************************
Suche in der OMDB
Ergebnis der Suche als Liste angezeigt
Detailansicht mit Titel, Erscheinungsjahr, Regisseur, Laufzeit,
Genre, IMDB-Bewertung und Filmplakat

Favoritenliste:
Anzeige der Fav-Liste und Detailansicht
Film kann von der Liste entfernt werden

Merkliste:
Anzeige der Merk-Liste und Detailansicht
Film kann von dieser Liste entfernt werden
Film kann von dieser Liste zu Fav hinzugefuegt werden!
Film als gesehen markieren
Liste zeigt erst gesehene und dann ungesehene Filme
************************NICE-TO-HAVE*************************
Filmen auf Favoriten und Merkliste ein eigenes Rating geben
Live-Suche
Schauspieler in der Detailansicht ausgeben
Favoriten und Merkliste sortieren
SQL-Datenbank
PDF-Ausdruck zu Detailansicht, Favliste und Merkliste
**************************************************************
**************************Teil 2******************************
 -Karsten: 
    Must-Have
        -Statistik-Seite
            -Diagramme
            -Auswertungen (Labels)
    Nice-to-Have
        -XML-Import und Export
**************************************************************
    -Timo:
        ​-Popup Rücksprung -> Main Window aktualisieren 
        -Code aufräumen / verschönern
**************************************************************
    -Artur:
        - live-suche als service umgesetzt
        - regestrierung gefixt (!= null fehler)
        - tab-reiter wechsel fehler behoben 
            (currentmovie wurde gelöscht bei wechsel)
        - kein bild verfügbar fehler
    - pdf export (wenn keine url verfügbar ist wird 
            überhaupt kein bild angezeigt)
    - detailansicht (x icon wird angezeigt)
        - pdf export ausgewählter ordner fehler 
            (Permission denied)
        - bug fixing
        - logout ​
**************************************************************