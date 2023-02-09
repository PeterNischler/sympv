[### Aufgabe 1
Die Berechnung der Sonnenbestrahlungsstärke wurde anhand dieses Python Scriptes implementiert:
https://www.heizen-co2-sparen.de/experimentelles/berechnung-der-bestrahlungsstaerke-der-sonnenstrahlung
Aufteilung der Arbeit auf Harald und Peter. Primär hat:
     - Peter an Irradiance, PVSystem und House gearbeitet.
     - Harald an Simulation und Weather gearbeitet.
Aber sehr viel wurde zu zweit mit CodeWithMe gemacht.

Die Berechnung der Bestrahlungsstärke ist recht komplex, da wir keine einfache Formel/Berechnung gefunden haben. Es
wird beispielsweise auch die diffuse bestrahlung durch die Atmosphäre berechnet. Grundsätzlich fallen die Zahlen eher
etwas hoch aus und müssten noch besser angepasst werden. (Bzw. man könnte bestimmte berechnungen auch weglassen).
Die Berechnung des Haushaltverbrauchs ist sehr simpel. Beim Energieverbrauchm haben wir den maximal
möglichen Stromverbrauch in Watt mit einer Prozentzahl multipliziert, die je nach Tageszeit höher oder niedriger ist.
Die Berechnung des Wetters ist ebenfalls einfach gehalten.
### Aufgabe 2
Unser Konzept (haben wir auch per Email geschickt)
- Einfügen einer Akku Klasse mit welcher es möglich ist Energie zu speichern.
- Erstellen eines Interface für die Wetterdaten. Dadurch ist es möglich, zusätzlich zu unserer derzeitigen Implementierung auch diese Wetterdaten per extern einzulesen.
- Erstellen eines Interface für die Ermittlung der Strahlungsstärke. Dadurch ist es uns dann möglich zusätzlich zur aktuellen Implementierung der Strahlungsstärke auch die Strahlungsstärke extern einzulesen
- Einarbeiten deines Feedsback von der ersten Abgabe.
- Durch die Einführung von der Interfaces haben wir versucht Objektabhängigkeit zu verringern.
- 