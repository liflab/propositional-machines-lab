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
import ca.uqac.lif.labpal.Random;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.synthia.random.RandomBoolean;
import propmanlab.AccessControlledStreamExperiment;
import propmanlab.MyLaboratory;
import propmanlab.scenarios.Scenario;
import propmanlab.source.RandomMultiEventSource;

public class SimpleScenario extends Scenario
{
  public static final transient String NAME = "Simple";
  
  protected Random m_random;
  
  public SimpleScenario(Random random)
  {
    super(NAME, "Random Booleans", SimpleProxy.NAME, SimpleMonitor.NAME);
    m_random = random;
  }
  
  @Override
  public Processor getSource(AccessControlledStreamExperiment e, Region r)
  {
    RandomBoolean rand = new RandomBoolean();
    String[] vars = new String[] {"a", "b", "c"};
    RandomMultiEventSource source = new RandomConcreteUniEventSource(m_random, MyLaboratory.MAX_TRACE_LENGTH, rand, vars);
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
