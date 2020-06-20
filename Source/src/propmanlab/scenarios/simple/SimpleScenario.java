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
package propmanlab.scenarios.simple;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.propman.PropositionalMachine;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.synthia.Picker;
import propmanlab.AccessControlledStreamExperiment;
import propmanlab.MyLaboratory;
import propmanlab.scenarios.RandomScenario;
import propmanlab.source.RandomMultiEventSource;

public class SimpleScenario extends RandomScenario<Boolean>
{
  public static final transient String NAME = "Simple";
  
  public SimpleScenario(Picker<Boolean> picker)
  {
    super(NAME, "Random Booleans", SimpleProxy.NAME, SimpleMonitor.NAME, picker);
  }
  
  @Override
  public Processor getSource(AccessControlledStreamExperiment e, Region r)
  {
    String[] vars = new String[] {"a", "b", "c"};
    RandomMultiEventSource source = new RandomConcreteUniEventSource(null, MyLaboratory.MAX_TRACE_LENGTH, m_picker, vars);
    e.setSource(source);
    return source;
  }

  @Override
  public PropositionalMachine getProxyInstance(AccessControlledStreamExperiment e, Region r)
  {
    SimpleProxy p = new SimpleProxy();
    e.setProxy(p);
    return p;
  }

  @Override
  public PropositionalMachine getMonitor(AccessControlledStreamExperiment e, Region r)
  {
    SimpleMonitor m = new SimpleMonitor();
    e.setMonitor(m);
    return m;
  }
}
