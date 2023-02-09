public interface Weather {

    /*GUT: die Verwendung eines Interfaces ermöglicht es die dahinter liegende Implementierung leicht auszutauschen*/
    /*SCHLECHT: getPrecipitation wird derzeit nicht verwendet */


    /*Vorbedingung:
        1<= dayOfYear <= 365
        0<= hour < 24
      Nachbedingung:

    */
    /**
     * in mm
     */
    public double getPrecipitation(int dayOfYear, int hour);

    /*Vorbedingung:
        1<= dayOfYear <= 365
        0<= hour < 24
      Nachbedingung:

    */
    /**
     * in °Celsius
     */
    public double getTemperature(int dayOfYear, int hour);
}
