### How to use
There is no GUI, all information has to be inserted in the Test.java, then you have to compile Test.java with the 
command ``javac Test.java`` and run the simulation with ``java Test``.
#### Input 
### What is a photovoltaic system?
### How is the solar irradiance calculated?
The Irradiance can either be calculated, or read from a csv file of this structure:
This calculation relies a lot on the following information and partly implements this python script:
https://www.heizen-co2-sparen.de/experimentelles/berechnung-der-bestrahlungsstaerke-der-sonnenstrahlung
### How is the weather calculated?
The Irradiance can either be calculated, or read from a csv file of this structure:

### How does the calculation of obstructions work?
### How does the battery work
wird beispielsweise auch die diffuse Bestrahlung durch die Atmosphäre berechnet. Grundsätzlich fallen die Zahlen eher
etwas hoch aus und müssten noch besser angepasst werden.
Die Berechnung des Haushaltverbrauchs ist sehr simpel. Beim Energieverbrauchm haben wir den maximal
möglichen Stromverbrauch in Watt mit einer Prozentzahl multipliziert, die je nach Tageszeit höher oder niedriger ist.
Die Berechnung des Wetters ist ebenfalls einfach gehalten.

Dictionary:
- Irradiance
- Elevation
- Azimuth
- Zenith
- 