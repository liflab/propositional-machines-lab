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

import ca.uqac.lif.cep.propman.MultiEventFunction;
import ca.uqac.lif.cep.propman.MultiEventFunction.Identity;
import ca.uqac.lif.cep.propman.ExplicitPropositionalMachine;

public class CartProxy extends ExplicitPropositionalMachine
{
  protected int m_shedInterval;
  
  public CartProxy()
  {
    this(3);
  }
  
  public CartProxy(int shed_interval)
  {
    super();
    m_shedInterval = shed_interval;
    for (int i = 0; i < shed_interval; i++)
    {
      addTransition(i, new TransitionOtherwise(i + 1, Identity.instance));
    }
    addTransition(shed_interval, new TransitionOtherwise(0, new MultiEventFunction.EmitConstant(CartMultiEvent.ALL)));
  }
}
