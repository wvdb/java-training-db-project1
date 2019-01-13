# DB-project1

## Opdrachten

* MyApplicationH2 bevat een main method die wordt aangeroepen. Via de main method kunnen opdrachten worden opgestart.<br> 
Het is de bedoeling om eerst de eerste opdracht te starten. Daardoor gaat een stukje H2 compatibele DDL worden uitgevoerd.<br>
Als resultaat hebben we bvb de BIER_ACERTA tabel waar 3 entries zijn in ge-insert.
Belangrijk: we gebruiker hier dus een LOGGER library en willen nergens meer de 'sout' zien.  
* Opdracht 2: schrijf programma dat alle bieren opvraagt en afdrukt 
* Opdracht 3 doorloop de resultset van achter naar voor. Je moet 3 bieren vinden.  
* Opdracht 4: 
 - verhoog het alcoholpercentage van één van de bieren. Controleer of het aantal gewijzigde rijen één is. 
* Opdracht 5: 
 - voeg een brouwer toe 
 - wijzig alle bieren en doe een verwijzing naar de primary key van de brouwer    
* Opdracht 6: 
 - gebruik het preparestatement en verhoog opnieuw het alcoholpercentage van één van de bieren. Werk met 2 placeholders. 
* Opdracht 7: 
 - voeg 2 bieren toe maar doe dit in een transactie. Laat de transactie eerst falen op het einde en sluit de transactie daarna succesvol af.  
* Opdracht extra: 
 - doe een aantal wijzigen in een batch
 - (optioneel) vergelijk de snelheid met bvb 1000 statements zonder batch  
 - (optioneel) vergelijk de snelheid met het native import mechanisme (is BCP voor Sql Server)  
* Opdracht 8: 
 - maak een programma dat het alcoholpercentage van alle bieren veroogt met 1 % en gebruik de updatable result set
* Opdracht 9: 
 - maak gebruik van de resultsetmetadata (zie opdracht cursus)
* Opdracht 10: 
 - maak gebruik van de databasemetadata (zie opdracht cursus)
* Opdracht 12: 
 - schrijf een DAO (Data Acces Object) klasse 
 - (optioneel) schrijf een BASE DAO klasse en pas overerving toe 
* Opdracht extra: 
 - insert een entry in de project tabel en maak gebruik van de UUID en de LocalDate  
 - de java.sql.Date klasse heeft een methode bijgekregen om met de LocalDate te kunnen werken
 - vraag de toegevoegde entry op (doe een select) en let op het formaat van de datum (ISO 8601)   
* Opdracht extra: 
 - schrijf unit test(en) voor de zaken die je gecodeerd hebt  

## Unit Testing

Unit testing:
* don't go for the easy path
* unit tests should be small and should contain little logic
* when a unit test fails, it is easy to understand the reason for the failure
* A single test should not depend on running other tests before it, nor should it be affected by the order of execution of other tests.
* unit testing, integration testing, load testing, stress testing ....
* write your test with a given/when/then syntax (see example)
* TDD (test driven development)
* BDD (behavior driven development) -> cucumber : https://cucumber.io/school

## Webpages to read:
* https://www.tutorialspoint.com/sqlite/sqlite_java.htm
* https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html
* https://www.marcobehler.com/guides/a-guide-to-accessing-databases-in-java/#_java_first