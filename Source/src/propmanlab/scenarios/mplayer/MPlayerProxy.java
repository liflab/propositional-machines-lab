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
package propmanlab.scenarios.mplayer;

import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.MultiEvent;  
import ca.uqac.lif.cep.propman.MultiEventFunction;
import ca.uqac.lif.cep.propman.ExplicitPropositionalMachine;
import ca.uqac.lif.cep.propman.Valuation;
import ca.uqac.lif.cep.propman.ValuationIterator;
import ca.uqac.lif.cep.propman.MultiEventFunction.Identity;
import java.util.HashSet;
import java.util.Set;

import static propmanlab.scenarios.mplayer.MPlayerSource.BUFFER;
import static propmanlab.scenarios.mplayer.MPlayerSource.DECODE;
import static propmanlab.scenarios.mplayer.MPlayerSource.PAUSE;
import static propmanlab.scenarios.mplayer.MPlayerSource.PLAY;
import static propmanlab.scenarios.mplayer.MPlayerSource.STOP;

public class MPlayerProxy extends ExplicitPropositionalMachine
{
  /**
   * A single visible instance of the "ALL" cart multi-event
   */
  public static final MultiEvent ALL = new AllMultiEvent();
  
  protected int m_interval;
  
  protected int m_budget;

  public MPlayerProxy()
  {
    this(3, 2);
  }

  public MPlayerProxy(int interval, int budget)
  {
    super();
    m_interval = interval;
    m_budget = budget;
    for (int i = 0; i < interval - budget; i++)
    {
      addTransition(i, new TransitionOtherwise(i + 1, Identity.instance));
    }
    for (int i = interval - budget; i < interval; i++)
    {
      addTransition(i, new TransitionOtherwise(i + 1, new MultiEventFunction.EmitConstant(ALL)));
    }
    addTransition(interval, new TransitionOtherwise(0, new MultiEventFunction.EmitConstant(ALL)));
  }
  
  @Override
  public MPlayerProxy duplicate(boolean with_state)
  {
    return new MPlayerProxy(m_interval, m_budget);
  }
  
  protected static class AllMultiEvent extends ConcreteMultiEvent
  {
    protected static Set<Valuation> s_valuations = getAllValuations();
    
    public AllMultiEvent()
    {
      super();
    }
    
    protected static Set<Valuation> getAllValuations()
    {
      Set<Valuation> vals = new HashSet<Valuation>();
      ValuationIterator v_it = new ValuationIterator(PLAY, PAUSE, STOP, BUFFER, DECODE);
      while (v_it.hasNext())
      {
        vals.add(v_it.next());
      }
      return vals;
    }
    
    @Override
    public Set<Valuation> getValuations()
    {
      return s_valuations;
    }
    
    @Override
    public String toString()
    {
      return toString("*");
    }
  }
}
