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

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.propman.PropositionalMachine;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.synthia.random.RandomFloat;
import propmanlab.AccessControlledStreamExperiment;
import propmanlab.MyLaboratory;
import propmanlab.scenarios.Scenario;

public class CartScenario extends Scenario
{
  public static final transient String NAME = "Shopping Cart";
  
  public CartScenario()
  {
    super(NAME, "CartSource", "Load shedding", "Amazon Lifecycle");
  }
  
  @Override
  public Processor getSource(AccessControlledStreamExperiment e, Region r)
  {
    CartEventSource src = new CartEventSource(MyLaboratory.MAX_TRACE_LENGTH, new RandomFloat());
    e.setSource(src);
    return src;
  }

  @Override
  public PropositionalMachine getProxyInstance(AccessControlledStreamExperiment e, Region r)
  {
    CartProxy proxy = new CartProxy(20);
    e.setProxy(proxy);
    return proxy;
  }

  @Override
  public PropositionalMachine getMonitor(AccessControlledStreamExperiment e, Region r)
  {
    CartLifecycleMonitor mon = new CartLifecycleMonitor();
    e.setMonitor(mon);
    return mon;
  }

}
