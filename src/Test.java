import java.util.Random;

 /* ERROR: aktuell wird bei Methoden der Klasse: RechargeableBattery die Spannung übergeben, wird aber eigentlich nicht gebraucht.
  * daher wird derzeit bei den Methodenaufrufen die Spannung als fixer Wert übergeben.
  * ursprünglich ist das aus der Idee entstanden die Speicherkapazität des Akkus in Amperestunden anzugeben (bei definierter Spannung)
  * für die aktuelle Simulation wäre es einfacher die Kapazität in KWh anzugeben.
  */
public class Test {
    public static void main(String[] args) {


        Random random = new Random();
        random.setSeed(20);
        PVSystem[] PVSystems = new PVSystem[1] ;
        PVSystems[0] = new PVSystem("pv1", 40, 0.18, 21, 220, 40);
        House house = new House(6000, 48.4525,15.4733, 120, PVSystems, random);
        Weather weather = new WeatherRandom(random, 20.0, 10, 0);
        Irradiance irradiance = new IrradianceEstimate(house);
        Simulation sim = new Simulation(house, weather, irradiance);


        random.setSeed(69);
        Weather weather2 = new WeatherData("data/naturns2021-01-utf-8.csv");
        PVSystem[] PVSystems2 = new PVSystem[1] ;
        PVSystems2[0] = new PVSystem("pv1", 25, 0.15, 30, 185, 42);
        RechargeableBattery battery2 = new RechargeableBattery(20, 10);
        //HouseWithBattery house2 = new HouseWithBattery(3500, 46,11.069644, 600, PVSystems2, battery2, random);
        //HouseWithBattery house2 = new HouseWithBattery(6000, 48.4525,15.4733, 120, PVSystems, battery2, random);
        House house2 = new House(6000, 48.4525,15.4733, 120, PVSystems, random);
        Irradiance irradiance2 = new IrradianceData(house2, "data/naturns_irradiance_2020-12-31_2022-01-02_stündlich_wide.csv");
        Simulation sim2 = new Simulation(house2, weather, irradiance2);


        random.setSeed(69);
        Weather weather3 = new WeatherData("data/naturns2021-01-utf-8.csv");
        PVSystem[] PVSystems3 = new PVSystem[1] ;
        PVSystems3[0] = new PVSystem("pv1", 25, 0.15, 30, 185, 42);
        RechargeableBattery battery3 = new RechargeableBattery(20, 10);
        HouseWithBattery house3 = new HouseWithBattery(6000, 48.4525,15.4733, 120, PVSystems, battery3, random);
        Irradiance irradiance3 = new IrradianceData(house3, "data/naturns_irradiance_2020-12-31_2022-01-02_stündlich_wide.csv");
        Simulation sim3 = new Simulation(house3, weather3, irradiance3);

    }
}
