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
package propmanlab.scenarios.cpuload;

import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.Valuation;
import propmanlab.source.MultiEventFileSource;

public class CpuSource extends MultiEventFileSource
{
  /**
   * The name of the file to read from
   */
  protected static final transient String FILENAME = "data/temperature/machine_temperature_system_failure.csv";
  
  /**
   * The minimum temperature found in the input file
   */
  protected static final float s_minTemp = 0;
  
  /**
   * The maximum temperature found in the input file
   */
  protected static final float s_maxTemp = 100;
  
  /**
   * The interval of temperatures
   */
  protected static final float s_interval = 1;
    
  public CpuSource()
  {
    super(FILENAME);
  }
  
  public CpuSource(int num_events)
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
    int load = Integer.parseInt(parts[1]);
    return getEventFromLoad(load);
  }
  
  /**
   * Gets a multi-event from a temperature reading
   * @param temp The temperature
   * @return The multi-event encoding this temperature reading
   */
  public static MultiEvent getEventFromLoad(int load)
  {
    Valuation v = new Valuation();
    for (int i = 0; i < 101; i++)
    {
      if (i == load)
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
  
  public static String[] getVariables()
  {
    String[] names = new String[101];
    for (int i = 0; i < 101; i++)
    {
      names[i] = "t" + i;
    }
    return names;
  }
}
