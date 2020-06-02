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
package propmanlab.source;

import ca.uqac.lif.cep.Processor;
import java.util.Queue;
import java.util.Scanner;
import propmanlab.MyLaboratory;

public abstract class MultiEventFileSource extends MultiEventSource
{
  /**
   * The file to read in order to produce the events
   */
  /*@ non_null @*/ protected String m_filename;
  
  /**
   * A scanner to read the file
   */
  protected Scanner m_scanner;
  
  /**
   * Creates a new multi-event file source
   * @param filename The file to read in order to produce the events
   */
  protected MultiEventFileSource(/*@ non_null @*/ String filename)
  {
    super(-1);
    m_filename = filename;
  }
  
  /**
   * Creates a new multi-event file source
   * @param filename The file to read in order to produce the events
   */
  protected MultiEventFileSource(/*@ non_null @*/ String filename, int num_events)
  {
    super(num_events);
    m_filename = filename;
  }
  
  @Override
  protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
  {
    if (m_scanner == null)
    {
      m_scanner = new Scanner(MyLaboratory.class.getResourceAsStream(m_filename));
    }
    if (!m_scanner.hasNextLine())
    {
      return false;
    }
    outputs.add(new Object[]{getEvent()});
    m_eventCount++;
    if (m_numEvents < 0)
    {
      return true;
    }
    return m_eventCount <= m_numEvents;
  }
  
  @Override
  public String getFilename()
  {
    return m_filename;
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    // TODO Auto-generated method stub
    return null;
  }
}
