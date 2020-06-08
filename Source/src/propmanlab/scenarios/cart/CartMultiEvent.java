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
package propmanlab.scenarios.cart;

import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.Valuation;
import java.util.HashSet;
import java.util.Set;

/**
 * A multi-event made of multiple {@link CartEvent}s
 */
public class CartMultiEvent implements MultiEvent
{
  /**
   * The set of cart events contained in this multi-event
   */
  protected Set<CartEvent> m_events;
  
  /**
   * Creates a new empty cart multi-event
   */
  public CartMultiEvent()
  {
    super();
    m_events = new HashSet<CartEvent>();
  }
  
  /**
   * Creates a new cart multi-event with given elements
   * @param events The elements
   */
  public CartMultiEvent(CartEvent ... events)
  {
    this();
    for (CartEvent e : events)
    {
      m_events.add(e);
    }
  }
  
  @Override
  public Set<Valuation> getValuations()
  {
    Set<Valuation> vals = new HashSet<Valuation>(m_events.size());
    for (CartEvent ce : m_events)
    {
      vals.add(ce.getValuation());
    }
    return vals;
  }

  @Override
  public Set<Valuation> getIntersection(MultiEvent e)
  {
    if (!(e instanceof CartMultiEvent))
    {
      throw new UnsupportedOperationException("A CartMultiEvent may only intersect with another CartMultiEvent");
    }
    CartMultiEvent cme = (CartMultiEvent) e;
    Set<CartEvent> common = new HashSet<CartEvent>();
    for (CartEvent ce : cme.m_events)
    {
      if (m_events.contains(ce)) 
      {
        common.add(ce);
      }
    }
    Set<Valuation> vals = new HashSet<Valuation>(common.size());
    for (CartEvent ce : common)
    {
      vals.add(ce.getValuation());
    }
    return vals;
  }

  @Override
  public Set<String> getDomain()
  {
    return CartEvent.getDomain();
  }
  
  @Override
  public String toString()
  {
    return toString("");
  }

  @Override
  public String toString(String... variables)
  {
    StringBuilder out = new StringBuilder();
    out.append("{");
    boolean first = true;
    for (CartEvent ce : m_events)
    {
      if (first)
      {
        first = false;
      }
      else
      {
        out.append(",");
      }
      out.append(ce.toString());
    }
    out.append("}");
    return out.toString();
  }
}