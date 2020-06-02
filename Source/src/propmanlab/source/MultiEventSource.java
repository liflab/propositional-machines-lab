/*
    Monitors for multi-events
    Copyright (C) 2020 Sylvain Hallé, Rania Taleb, Raphaël Khoury

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package propmanlab.source;

import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.MultiEventFactory;

public abstract class MultiEventSource extends BoundedSource<MultiEvent>
{
  /**
   * The Boolean variables in the multi-events to generate
   */
  /*@ non_null @*/ protected String[] m_variables;
  
  /**
   * A multi-event factory to read events from a text source
   */
  /*@ non_null @*/ protected MultiEventFactory m_factory;
  
  public MultiEventSource(int num_events, /*@ non_null @*/ String ... variables)
  {
    super(num_events);
    m_variables = variables;
    m_factory = new MultiEventFactory(variables);
  }

  @Override
  public MultiEvent readEvent(String line)
  {
    return m_factory.readFromString(line);
  }

  @Override
  public String printEvent(MultiEvent e)
  {
    return e.toString();
  }
}