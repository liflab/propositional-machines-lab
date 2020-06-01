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
package propmanlab;

import ca.uqac.lif.cep.propman.AccessControlledMonitor;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.PropositionalMachine;
import ca.uqac.lif.json.JsonFalse;
import ca.uqac.lif.json.JsonTrue;

public class AccessControlledStreamExperiment extends StreamExperiment<MultiEvent>
{
  public static transient final String PROXY = "Access proxy";
  public static transient final String WITH_PROXY = "With proxy";
  
  public AccessControlledStreamExperiment()
  {
    super();
    describe(PROXY, "The access proxy used in this experiment");
    describe(WITH_PROXY, "Whether an access proxy is applied on the monitor");
  }
  
  public void setProcessors(PropositionalMachine proxy, PropositionalMachine monitor)
  {
    if (proxy == null)
    {
      super.setProcessor(monitor);
      setInput(WITH_PROXY, JsonFalse.instance);
    }
    else
    {
      AccessControlledMonitor acm = new AccessControlledMonitor(proxy, monitor);
      super.setProcessor(acm);
      setInput(WITH_PROXY, JsonTrue.instance);
    }
  }
}
