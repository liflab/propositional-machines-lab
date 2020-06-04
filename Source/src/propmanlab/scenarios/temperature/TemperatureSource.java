/*
    A benchmark for propositional machines in BeepBeep 3
    Copyright (C) 2020 Laboratoire d'informatique formelle

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
  public static MultiEvent getEventFromTemperature(double temp, double min_temp, double max_temp, double interval)
  {
    int true_variable = getInterval(temp, min_temp, interval);
    Valuation v = new Valuation();
    for (int i = 0; i < Math.ceil((max_temp - min_temp) / interval); i++)
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
   * Gets a multi-event from a temperature reading
   * @param temp The temperature
   * @return The multi-event encoding this temperature reading
   */
  public MultiEvent getEventFromTemperature(double temp)
  {
    int true_variable = getInterval(temp);
    Valuation v = new Valuation();
    for (int i = 0; i < Math.ceil((s_maxTemp - s_minTemp) / s_interval); i++)
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
  
  public static String[] getVariables(float min_temp, float max_temp, float interval)
  {
    int num_intervals = (int) Math.ceil((max_temp - min_temp) / interval);
    String[] names = new String[num_intervals];
    for (int i = 0; i < num_intervals; i++)
    {
      names[i] = "t" + i;
    }
    return names;
  }
  
  /**
   * Gets the interval number corresponding to a numerical value
   * @param value The value
   * @return The interval number
   */
  protected int getInterval(double value)
  {
    return getInterval(value, (double) s_minTemp, (double) s_interval);
  }
  
  public static int getInterval(double value, double min_temp, double interval_width)
  {
    return (int) ((value - min_temp) / interval_width);
  }

}
