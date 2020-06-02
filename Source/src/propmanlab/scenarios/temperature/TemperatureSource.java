package propmanlab.scenarios.temperature;

import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.Valuation;
import propmanlab.source.MultiEventFileSource;

public class TemperatureSource extends MultiEventFileSource
{
  /**
   * The name of the file to read from
   */
  protected static final transient String FILENAME = "data/temperature/machine_temperature_system_failure.csv";
  
  /**
   * The minimum temperature found in the input file
   */
  protected static final float s_minTemp = 70;
  
  /**
   * The maximum temperature found in the input file
   */
  protected static final float s_maxTemp = 95;
  
  /**
   * The interval of temperatures
   */
  protected static final float s_interval = 1;
    
  public TemperatureSource()
  {
    super(FILENAME);
  }
  
  public TemperatureSource(int num_events)
  {
    super(FILENAME, num_events);
  }
  
  @Override
  protected MultiEvent getEvent()
  {
    String line = m_scanner.nextLine();
    while (line.startsWith("timestamp")) // Ignore 1st line
    {
      line = m_scanner.nextLine();
    }
    String[] parts = line.split(",");
    double temp = Double.parseDouble(parts[1]);
    return getEventFromTemperature(temp);
  }
  
  /**
   * Gets a multi-event from a temperature reading
   * @param temp The temperature
   * @return The multi-event encoding this temperature reading
   */
  public MultiEvent getEventFromTemperature(double temp)
  {
    int true_variable = getInterval(temp);
    Valuation v = new Valuation();
    for (int i = 0; i < Math.ceil(s_maxTemp - s_minTemp / s_interval); i++)
    {
      if (i == true_variable)
      {
        v.put("t" + i, Troolean.Value.TRUE);
      }
      else
      {
        v.put("t" + i, Troolean.Value.FALSE);
      }
    }
    return new ConcreteMultiEvent(v);
  }
  
  /**
   * Gets the interval number corresponding to a numerical value
   * @param value The value
   * @return The interval number
   */
  protected int getInterval(double value)
  {
    return (int) ((value - (double) s_minTemp) / (double) s_interval);
  }

}
