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

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.propman.AccessControlledMonitor;
import ca.uqac.lif.cep.propman.GetEntropy;
import ca.uqac.lif.cep.tmf.Pump;
import ca.uqac.lif.labpal.Random;
import ca.uqac.lif.synthia.random.RandomBoolean;
import ca.uqac.lif.synthia.util.Constant;
import propmanlab.source.RandomConcreteMultiEventSource;

public class Demo
{
  public static void main(String[] args)
  {
    RandomConcreteMultiEventSource source = new RandomConcreteMultiEventSource(new Random(), 50, new Constant<Integer>(1), new RandomBoolean(), "a", "b", "c");
    AccessControlledMonitor acm = new AccessControlledMonitor(new SimpleProxy(), new SimpleMonitor());
    ApplyFunction ent = new ApplyFunction(GetEntropy.instance);
    Pump pump = new Pump();
    Print print = new Print();
    print.setSeparator("\n");
    Connector.connect(source, acm, ent, pump, print);
    pump.start();
  }
}
