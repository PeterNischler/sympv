import java.util.Calendar;


public class Simulation {

    public Simulation(House house, Weather weather, Irradiance irradiance){
        boolean debug = false;
        if (!debug){
            System.out.println("Starting new simulation...");
            System.out.println("House: " + house);
        }
        PVSystem PVSystem = house.getPVSystems()[0];
        double kWp = 0;                 // kilowatt peak over the year
        double kWhProduced = 0;         // total of kilowatt hours produced over the
        double kWhFedIntoGrid  = 0;     // total of kilowatt hours fed into public power grid over the year
        double kWhDrawnFromGrid  = 0;   // total of kilowatt hours drawn from public power grid over the year

        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 2022);
        date.set(Calendar.DAY_OF_YEAR, 1);

        int lastMonth = -1;
        double kWpThisMonth = 0;
        double kWhProducedThisMonth = 0;
        double kWhFedIntoGridThisMonth  = 0;
        double kWhDrawnFromGridThisMonth  = 0;

        while(date.get(Calendar.YEAR) < 2023){
            if (date.get(Calendar.MONTH) > lastMonth && debug){
                lastMonth += 1;
                System.out.println("Month: " + lastMonth + "-----------------------------------------");
                System.out.println("kWpThisMonth:                " + kWpThisMonth);
                System.out.println("kWhProducedThisMonth:        " + kWhProducedThisMonth);
                System.out.println("kWhFedIntoGridThisMonth:     " + kWhFedIntoGridThisMonth);
                System.out.println("kWhDrawnFromGridThisMonth:   " + kWhDrawnFromGridThisMonth);
                System.out.println("----------------------------------------------------------------");
                kWpThisMonth = 0;
                kWhProducedThisMonth = 0;
                kWhFedIntoGridThisMonth  = 0;
                kWhDrawnFromGridThisMonth  = 0;
            }

            for (int hour = 0; hour < 24; hour++) {
                // W/m^2

                /*GUT:  Objektkopplung zwischen Simulation und Irradiance schwach,
                 * es gibt eigentlich nur eine Methode die aufgerufen wird.
                 */
                double irradianceOnPV = irradiance.getHorizontalGlobalIrradiance(
                        date.get(Calendar.DAY_OF_YEAR),
                        hour
                );

                /*GUT:  Objektkopplung zwischen Simulation und PVSystem schwach,
                 * es gibt  nur eine Methode die aufgerufen wird.
                 * (unten wird auch noch einmalig die KWp ausgegeben)
                 */
                /*ERROR: weather.getTemperature erwartet sich als erstes Argument den Tag im Jahr, übergeben wird aber das Monat.
                Daher werden nur Werte zwischen 0 und 11 übergeben. (statt 0 und 365). Lösungsvorschlag: weather.getTemperature(date.get(Calendar.DAY_OF_YEAR), hour)  */
                double kWhProducedThisHour = PVSystem.currentEnergyProduction(
                        irradianceOnPV,
                        weather.getTemperature(date.get(Calendar.MONTH), hour)
                )/1000;

                /*GUT: Objektkopplung zwischen Simualtion und House schwach da nur eine Methode der Klasse aufgerufen wird je Simulationslauf (unten wird auch noch einmalig die TotalEnergyConsumption ausgegeben).
                 * Stärker wäre die Objektkopplung wenn die Berechnung der Differenz hier gemacht werden würde.
                 * Dann bräuchte man hier Getter für den Verbrauch und Getter für die Battery
                 */
                double currentNetEnergy = house.currentNetEnergy(hour,kWhProducedThisHour*1000)/1000;

                if (currentNetEnergy < 0) {
                    // abs at the end
                    kWhDrawnFromGrid += currentNetEnergy;
                    kWhDrawnFromGridThisMonth  += currentNetEnergy;
                } else {
                    kWhFedIntoGrid += currentNetEnergy;
                    kWhFedIntoGridThisMonth  += currentNetEnergy;
                }


                kWhProduced += kWhProducedThisHour;
                kWhProducedThisMonth += kWhProducedThisHour;

                if (kWhProducedThisHour > kWpThisMonth){
                    kWpThisMonth = kWhProducedThisHour;
                }
            }

            date.add(Calendar.DAY_OF_YEAR, 1);
        }

        kWhDrawnFromGrid = Math.abs(kWhDrawnFromGrid);
        System.out.println("kWp:                    " + PVSystem.getKWp());
        System.out.println("kWhProduced:            " + kWhProduced);
        System.out.println("kWhFedIntoGrid:         " + kWhFedIntoGrid);
        System.out.println("kWhDrawnFromGrid:       " + kWhDrawnFromGrid);
        System.out.println("kWhEnergyConsumption:   " + house.getTotalEnergyConsumption());
        System.out.println("MaxAirTemperature:      " + PVSystem.getMaxAirTemperature());
        System.out.println("MinAirTemperature:      " + PVSystem.getMinAirTemperature());
        System.out.println("--------------------------------");
    }

}
