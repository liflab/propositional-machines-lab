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

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.size.SizePrinter;
import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.Pushable;
import ca.uqac.lif.cep.ltl.Troolean.Value;
import ca.uqac.lif.cep.propman.AccessControlledMonitor;
import ca.uqac.lif.cep.propman.MultiMonitor.VerdictCount;
import ca.uqac.lif.cep.propman.PropositionalMachine;
import ca.uqac.lif.cep.tmf.SinkLast;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.ExperimentException;
import java.math.BigInteger;
import propmanlab.source.BoundedSource;

/**
 * Experiment that connects a source to a processor and measures its
 * throughput
 */
public class StreamExperiment<T> extends Experiment
{
  public static transient final String TOOL = "Tool";
  public static transient final String TIME = "Time";
  public static transient final String LENGTH = "Length";
  public static transient final String TRACE_LENGTH = "Trace length";
  public static transient final String PROPERTY = "Query";
  public static transient final String MEMORY = "Memory";
  public static transient final String MEM_PER_EVENT = "Memory per event";
  public static transient final String THROUGHPUT = "Throughput";
  public static transient final String MAX_MEMORY = "Max memory";
  public static transient final String NB_UNITRACES = "Number of uni-traces";
  public static transient final String NB_UNITRACES_TRUE = "Number of true uni-traces";
  public static transient final String NB_UNITRACES_FALSE = "Number of false uni-traces";
  public static transient final String NB_UNITRACES_INCONCLUSIVE = "Number of inconclusive uni-traces";
  
  /**
   * The proxy that is being used in this experiment
   */
  protected transient PropositionalMachine m_proxy;
  
  /**
   * The monitor that is being used in this experiment
   */
  protected transient PropositionalMachine m_monitor;

  /**
   * The source from which the input events will originate
   */
  protected transient BoundedSource<T> m_source;

  /**
   * The interval at which the experiment updates its data on
   * runtime and throughput
   */
  protected int m_eventStep = 1000;
  
  /**
   * The predicted throughput for this experiment. This is only used to give an
   * estimate of the time remaining before the experiment is done.
   */
  protected float m_predictedThroughput = 0;
  
  /**
   * An internal URL to the picture that represents the property
   */
  protected transient String m_imageUrl = null;
  
  /**
   * The description of the property being monitored
   */
  protected transient String m_propertyDescription = null;
  
  /**
   * An Azrael printer used to evaluate memory usage
   */
  protected transient SizePrinter m_sizePrinter;
  
  /**
   * Creates a new empty stream experiment
   */
  public StreamExperiment()
  {
    super();
    m_sizePrinter = new SizePrinter();
    describe(THROUGHPUT, "The average number of events processed per second");
    describe(TIME, "Cumulative running time (in ms)");
    describe(LENGTH, "Number of events processed");
    describe(TRACE_LENGTH, "The total length of the trace processed");
    describe(PROPERTY, "The name of the query being evaluated on the event log");
    describe(MAX_MEMORY, "The maximum amount of memory consumed during the evaluation of the property (in bytes)");
    describe(NB_UNITRACES, "The base-10 logarithm of the number of uni-traces processed by the monitor");
    describe(NB_UNITRACES_TRUE, "The base-10 logarithm of the number of uni-traces processed by the monitor and associated to the true verdict");
    describe(NB_UNITRACES_FALSE, "The base-10 logarithm of the number of uni-traces processed by the monitor and associated to the false verdict");
    describe(NB_UNITRACES_INCONCLUSIVE, "The base-10 logarithm of the number of uni-traces processed by the monitor and associated to the inconclusive verdict");
    JsonList x = new JsonList();
    x.add(0);
    write(LENGTH, x);
    JsonList y = new JsonList();
    y.add(0);
    write(TIME, y);
    JsonList z = new JsonList();
    z.add(0);
    write(MEMORY, z);
  }

  @Override
  public void execute() throws ExperimentException, InterruptedException 
  {
    // Setup processor chain
    Pullable s_p = m_source.getPullableOutput();
    Processor proc = null;
    if (m_proxy != null)
    {
      proc = new AccessControlledMonitor(m_proxy, m_monitor);
    }
    else
    {
      proc = m_monitor;
    }
    Pushable t_p = proc.getPushableInput();
    SinkLast hole = new SinkLast();
    Connector.connect(proc, hole);
    long start = System.currentTimeMillis();
    int event_count = 0;
    m_source.reset();
    int source_length = m_source.getEventBound();
    long max_mem = 0;
    while (s_p.hasNext())
    {
      if (event_count % m_eventStep == 0 && event_count > 0)
      {
        
        long mem_p = 0, mem_t = 0;
        try 
        {
          m_sizePrinter.reset();
          mem_p = ((Number) m_sizePrinter.print(proc)).longValue();
        } 
        catch (PrintException e) 
        {
          throw new ExperimentException(e);
        }
        long total_mem = mem_p + mem_t;
        max_mem = Math.max(total_mem, max_mem);
        addReading(event_count, System.currentTimeMillis() - start, total_mem);
        float prog = ((float) event_count) / ((float) source_length);
        setProgression(prog);        
      }
      Object o = s_p.pull();
      t_p.push(o);
      event_count++;
    }
    long end = System.currentTimeMillis();
    write(THROUGHPUT, (1000f * (float) MyLaboratory.MAX_TRACE_LENGTH) / ((float) (end - start)));
    write(MAX_MEMORY, max_mem);
    write(MEM_PER_EVENT, max_mem / MyLaboratory.MAX_TRACE_LENGTH);
    write(TRACE_LENGTH, m_source.getEventBound());
    if (m_proxy == null)
    {
      write(NB_UNITRACES, 1);
    }
    else
    {
      Object[] last = hole.getLast();
      if (last == null)
      {
        throw new ExperimentException("The monitor did not output anything");
      }
      VerdictCount vc = (VerdictCount) last[0];
      if (vc == null)
      {
        throw new ExperimentException("Multi-monitor verdict is null");
      }
      BigInteger bi_true = vc.get(Value.TRUE);
      BigInteger bi_false = vc.get(Value.FALSE);
      BigInteger bi_inconclusive = vc.get(Value.INCONCLUSIVE);
      BigInteger total = bi_true.add(bi_inconclusive).add(bi_false);
      write(NB_UNITRACES, (int) BigMath.logBigInteger(total));
      if (!bi_true.equals(BigInteger.ZERO))
      {
        write(NB_UNITRACES_TRUE, (float) BigMath.logBigInteger(bi_true));
      }
      if (!bi_false.equals(BigInteger.ZERO))
      {
        write(NB_UNITRACES_FALSE, (float) BigMath.logBigInteger(bi_false));
      }
      if (!bi_inconclusive.equals(BigInteger.ZERO))
      {
        write(NB_UNITRACES_INCONCLUSIVE, (float) BigMath.logBigInteger(bi_inconclusive));
      }
    }
  }

  /**
   * Sets the monitor that is being used in this experiment
   * @param m The monitor
   */
  public void setMonitor(PropositionalMachine m)
  {
    m_monitor = m;
  }
  
  /**
   * Sets the proxy that is being used in this experiment
   * @param p The proxy
   */
  public void setProxy(PropositionalMachine p)
  {
    m_proxy = p;
  }

  /**
   * Sets the source from which the input events will originate
   * @param s The source
   */
  public void setSource(BoundedSource<T> s)
  {
    m_source = s;
  }

  /**
   * Sets the interval at which the experiment updates its data on
   * runtime and throughput
   * @param step The interval
   */
  public void setEventStep(int step)
  {
    m_eventStep = step;
  }
  
  /**
   * Sets a picture for the processor chain evaluated by this experiment
   * @param url The URL for the picture
   */
  public void setImageUrl(String url)
  {
    m_imageUrl = url;
  }
  
  /**
   * Sets the textual description of the property being evaluated
   * @param description The description
   */
  public void setPropertyDescription(String description)
  {
    m_propertyDescription = description;
  }
  
  /**
   * Sets the predicted throughput for this experiment. This is only used to 
   * give an estimate of the time remaining before the experiment is done. 
   * @param throughput The throughput, in number of events per second
   */
  public void setPredictedThroughput(float throughput)
  {
    m_predictedThroughput = throughput;
  }
  
  @Override
  public float getDurationEstimate(float factor)
  {
    if (m_predictedThroughput <= 0)
    {
      return 0f;
    }
    return factor * ((float) MyLaboratory.MAX_TRACE_LENGTH) / m_predictedThroughput;
  }
  
  @Override
  public int countDataPoints()
  {
    int points = 3; // Throughput, max memory and mem per event
    points += ((JsonList) read(TIME)).size();
    points += ((JsonList) read(MEMORY)).size();
    return points;
  }
  
  /**
   * Adds a new reading to the set of data points collected by the experiment
   * @param length
   * @param time
   * @param memory
   */
  public void addReading(int length, long time, long memory)
  {
    JsonList l_len = (JsonList) read(LENGTH);
    JsonList l_time = (JsonList) read(TIME);
    JsonList l_mem = (JsonList) read(MEMORY);
    l_len.add(new JsonNumber(length));
    l_time.add(new JsonNumber(time));
    l_mem.add(new JsonNumber(memory));
    write(LENGTH, l_len);
    write(TIME, l_time);
    write(MEMORY, l_mem);
  }
}