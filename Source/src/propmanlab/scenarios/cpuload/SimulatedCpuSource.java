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

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.propman.MultiEvent;
import propmanlab.source.MultiEventSource;

/**
 * Generates a pre-programmed stream of temperature values.
 */
public class SimulatedCpuSource extends MultiEventSource
{
  protected List<Integer> m_readings;

  protected int m_index = 0;

  public SimulatedCpuSource(List<Integer> readings)
  {
    super(readings.size() - 1, CpuSource.getVariables());
    m_readings = readings;
  }

  public SimulatedCpuSource(int ... readings)
  {
    super(readings.length - 1, CpuSource.getVariables());
    m_readings = new ArrayList<Integer>(readings.length);
    for (int f : readings)
    {
      m_readings.add(f);
    }
  }

  @Override
  protected MultiEvent getEvent()
  {
    return CpuSource.getEventFromLoad(m_readings.get(m_index++));
  }

  @Override
  public String getFilename()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    // TODO Auto-generated method stub
    return null;
  }  
}
