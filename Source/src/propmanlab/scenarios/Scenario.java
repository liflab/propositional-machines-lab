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
package propmanlab.scenarios;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.propman.PropositionalMachine;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonTrue;
import ca.uqac.lif.labpal.Region;
import propmanlab.AccessControlledStreamExperiment;

/**
 * A triplet made of a source, a proxy and a monitor, each with additional
 * parameters.
 * @author Sylvain Hallé, Rania Taleb
 */
public abstract class Scenario
{
  public static final transient String SCENARIO = "Scenario";
  
  protected String m_name;
  
  protected String m_sourceName;
  
  protected String m_proxyName;
  
  protected String m_monitorName;
  
  public Scenario(String name, String source_name, String proxy_name, String monitor_name)
  {
    super();
    m_name = name;
    m_sourceName = source_name;
    m_proxyName = proxy_name;
    m_monitorName = monitor_name;
  }
  
  public void setup(AccessControlledStreamExperiment e, Region r)
  {
    e.setInput(SCENARIO, m_name);
    getSource(e, r);
    getProxy(e, r);
    getMonitor(e, r);
    getImageUrl(e, r);
  }
  
  public abstract Processor getSource(AccessControlledStreamExperiment e, Region r);
  
  public abstract PropositionalMachine getProxyInstance(AccessControlledStreamExperiment e, Region r);
  
  public abstract PropositionalMachine getMonitor(AccessControlledStreamExperiment e, Region r);
  
  public final PropositionalMachine getProxy(AccessControlledStreamExperiment e, Region r)
  {
    JsonElement wp = r.get(AccessControlledStreamExperiment.WITH_PROXY);
    e.setInput(AccessControlledStreamExperiment.WITH_PROXY, wp);
    if (wp != null && wp instanceof JsonTrue)
    {
      return getProxyInstance(e, r);
    }
    return null;
  }
  
  public String getImageUrl(AccessControlledStreamExperiment e, Region r)
  {
    return null;
  }
  
  @Override
  public int hashCode()
  {
    return m_name.hashCode() + m_monitorName.hashCode();
  }
  
  @Override
  public boolean equals(Object o)
  {
    if (o == null || !(o instanceof Scenario))
    {
      return false;
    }
    Scenario s = (Scenario) o;
    return m_name.compareTo(s.m_name) == 0 && m_monitorName.compareTo(s.m_monitorName) == 0 
        && m_proxyName.compareTo(s.m_proxyName) == 0;
  }
}
