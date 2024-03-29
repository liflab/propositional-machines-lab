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

import ca.uqac.lif.json.JsonFalse;
import ca.uqac.lif.json.JsonTrue;
import ca.uqac.lif.labpal.CliParser;
import ca.uqac.lif.labpal.CliParser.Argument;
import ca.uqac.lif.labpal.CliParser.ArgumentMap;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.LatexNamer;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.TitleNamer;
import ca.uqac.lif.labpal.table.ExperimentTable;
import ca.uqac.lif.mtnp.plot.TwoDimensionalPlot.Axis;
import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;
import ca.uqac.lif.mtnp.table.Composition;
import ca.uqac.lif.mtnp.table.ExpandAsColumns;
import ca.uqac.lif.mtnp.table.RenameColumns;
import ca.uqac.lif.mtnp.table.TransformedTable;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.random.RandomBoolean;
import ca.uqac.lif.synthia.random.RandomFloat;
import ca.uqac.lif.synthia.random.RandomInteger;
import propmanlab.macros.LabStats;
import propmanlab.macros.MacroVerdictPurity;
import propmanlab.macros.MaxTraceLength;
import propmanlab.macros.UniTraceCount;
import propmanlab.scenarios.cart.CartScenario;
import propmanlab.scenarios.cpuload.CpuLoadThresholdScenario;
import propmanlab.scenarios.cpuload.RandomWalkInteger;
import propmanlab.scenarios.mplayer.MPlayerScenario;
import propmanlab.scenarios.simple.SimpleScenario;
import propmanlab.scenarios.temperature.TemperatureThresholdScenario;

import static propmanlab.AccessControlledStreamExperiment.BEST_EFFORT;
import static propmanlab.AccessControlledStreamExperiment.PROXY;
import static propmanlab.AccessControlledStreamExperiment.WITH_PROXY;
import static propmanlab.StreamExperiment.LENGTH;
import static propmanlab.StreamExperiment.MAX_MEMORY;
import static propmanlab.StreamExperiment.MEMORY;
import static propmanlab.StreamExperiment.THROUGHPUT;
import static propmanlab.StreamExperiment.TIME;
import static propmanlab.StreamExperiment.NB_UNITRACES_FALSE;
import static propmanlab.StreamExperiment.NB_UNITRACES_INCONCLUSIVE;
import static propmanlab.StreamExperiment.NB_UNITRACES_TRUE;
import static propmanlab.scenarios.Scenario.SCENARIO;

@SuppressWarnings("unused")
public class MyLaboratory extends Laboratory
{
  /**
   * The maximum length of a trace
   */
  public static int MAX_TRACE_LENGTH = 100000;

  /**
   * The interval, in number of events, between updates of the experiment's
   * measurements
   */
  public static int s_eventStep = 1000;

  /**
   * A nicknamer
   */
  public static transient LatexNamer s_nicknamer = new LatexNamer();

  /**
   * A title namer
   */
  public static transient TitleNamer s_titleNamer = new TitleNamer();
  
  /**
   * Whether to use a small-scale benchmark (used for debugging)
   */
  protected static transient boolean s_small = false;

  /**
   * An experiment factory
   */
  public transient StreamExperimentFactory m_factory = new StreamExperimentFactory(this);

  @Override
  public void setup()
  {
    // Basic metadata
    setTitle("Benchmark for propositional machines in BeepBeep");
    setDoi("TODO");
    setAuthor("Rania Taleb, Sylvain Hallé");
    
    // Handle command-line parameters
    ArgumentMap args = getCliArguments();
    if (args.hasOption("small"))
    {
      s_small = true;
      System.out.println("Using small traces");
    }
    
    // Is it the small-scale benchmark?
    if (s_small)
    {
      MAX_TRACE_LENGTH = 20;
      s_eventStep = MAX_TRACE_LENGTH / 10;
    }
    
    // Setup of RNGs for the random experiments
    RandomFloat random_float = new RandomFloat();
    random_float.setSeed(getRandomSeed());
    RandomBoolean random_boolean = new RandomBoolean();
    random_boolean.setSeed(getRandomSeed());
    RandomInteger random_integer_100 = new RandomInteger(0, 100);
    random_integer_100.setSeed(getRandomSeed());
    RandomInteger random_integer_2 = new RandomInteger(-1, 1);
    random_integer_2.setSeed(getRandomSeed());
    
    // Factory setup: adding scenarios
    {
      m_factory.addScenario(SimpleScenario.NAME, new SimpleScenario(random_boolean));
      m_factory.addScenario(TemperatureThresholdScenario.NAME, new TemperatureThresholdScenario(random_float));
      m_factory.addScenario(CartScenario.NAME, new CartScenario(random_float));
      m_factory.addScenario(MPlayerScenario.NAME, new MPlayerScenario(random_float));
      m_factory.addScenario(CpuLoadThresholdScenario.NAME, new CpuLoadThresholdScenario(new RandomWalkInteger(0, 100, random_integer_100, random_integer_2)));
    }

    Region big_r = new Region();
    big_r.add(SCENARIO, SimpleScenario.NAME, TemperatureThresholdScenario.NAME, 
        CartScenario.NAME, MPlayerScenario.NAME, CpuLoadThresholdScenario.NAME);
    big_r.add(WITH_PROXY, JsonTrue.instance, JsonFalse.instance);
    big_r.add(BEST_EFFORT, JsonFalse.instance);

    // Impact of proxy
    {
      ExperimentTable t_comparison_tp = new ExperimentTable(SCENARIO, WITH_PROXY, THROUGHPUT);
      t_comparison_tp.setShowInList(false);
      add(t_comparison_tp);
      ExperimentTable t_comparison_mem = new ExperimentTable(SCENARIO, WITH_PROXY, MEMORY);
      t_comparison_mem.setShowInList(false);
      add(t_comparison_mem);
      for (Region r : big_r.all(SCENARIO))
      {
        Region r_with = new Region(r);
        r_with.add(WITH_PROXY, JsonTrue.instance);
        AccessControlledStreamExperiment e_with = m_factory.get(r_with);
        Region r_without = new Region(r);
        r_without.add(WITH_PROXY, JsonFalse.instance);
        AccessControlledStreamExperiment e_without = m_factory.get(r_without);
        if (e_with == null || e_without == null)
        {
          continue;
        }
        t_comparison_tp.add(e_with);
        t_comparison_tp.add(e_without);
        t_comparison_mem.add(e_with);
        t_comparison_mem.add(e_without);
        add(new UniTraceCount(this, e_with));
        {
          // Impact on time
          ExperimentTable et = new ExperimentTable(WITH_PROXY, LENGTH, TIME);
          et.setShowInList(false);
          add(et);
          et.add(e_with);
          et.add(e_without);
          TransformedTable tt = new TransformedTable(new Composition(new ExpandAsColumns(WITH_PROXY, TIME), new RenameTrueFalse(LENGTH)), et);
          tt.setTitle("Impact of proxy on throughput for " + r.getString(SCENARIO));
          s_nicknamer.setNickname(tt, r, "tTp", "");
          add(tt);
          Scatterplot plot = new Scatterplot(tt);
          plot.setTitle("Impact of proxy on throughput for " + r.getString(SCENARIO));
          plot.setCaption(Axis.X, LENGTH).setCaption(Axis.Y, TIME);
          s_nicknamer.setNickname(plot, r, "pTp", "");
          add(plot);
        }
        {
          // Impact on memory
          ExperimentTable et = new ExperimentTable(WITH_PROXY, LENGTH, MEMORY);
          et.setShowInList(false);
          add(et);
          et.add(e_with);
          et.add(e_without);
          TransformedTable tt = new TransformedTable(new Composition(new ExpandAsColumns(WITH_PROXY, MEMORY), new RenameTrueFalse(LENGTH)), et);
          tt.setTitle("Impact of proxy on memory for " + r.getString(SCENARIO));
          s_nicknamer.setNickname(tt, r, "tMem", "");
          add(tt);
          Scatterplot plot = new Scatterplot(tt);
          plot.setTitle("Impact of proxy on memory for " + r.getString(SCENARIO));
          plot.setCaption(Axis.X, LENGTH).setCaption(Axis.Y, MEMORY);
          s_nicknamer.setNickname(plot, r, "pMem", "");
          add(plot);
        }
      }
      TransformedTable tt_comparison_tp = new TransformedTable(new Composition(new ExpandAsColumns(WITH_PROXY, THROUGHPUT), new RenameTrueFalse(SCENARIO)), t_comparison_tp);
      tt_comparison_tp.setTitle("Impact of proxy on throughput");
      tt_comparison_tp.setNickname("tImpactThroughput");
      add(tt_comparison_tp);
      TransformedTable tt_comparison_mem = new TransformedTable(new Composition(new ExpandAsColumns(WITH_PROXY, MEMORY), new RenameTrueFalse(SCENARIO)), t_comparison_mem);
      tt_comparison_mem.setTitle("Impact of proxy on memory");
      tt_comparison_mem.setNickname("tImpactMemory");
      add(tt_comparison_mem);
    }
    
    // Comparison to best existing model
    {
      Region new_big_r = new Region();
      new_big_r.add(SCENARIO, SimpleScenario.NAME, TemperatureThresholdScenario.NAME, MPlayerScenario.NAME);
      new_big_r.add(WITH_PROXY, JsonTrue.instance);
      VerdictPurityTable vpt = new VerdictPurityTable();
      add(vpt);
      ExperimentTable et_t = new ExperimentTable(SCENARIO, PROXY, THROUGHPUT);
      ExperimentTable et_m = new ExperimentTable(SCENARIO, PROXY, MAX_MEMORY);
      et_t.setShowInList(false);
      et_m.setShowInList(false);
      TransformedTable tt_t = new TransformedTable(new ExpandAsColumns(PROXY, THROUGHPUT), et_t);
      tt_t.setTitle("Throughput for proxy vs. best effort");
      tt_t.setNickname("tTpBestVsProxy");
      add(tt_t);
      TransformedTable tt_m = new TransformedTable(new ExpandAsColumns(PROXY, MAX_MEMORY), et_m);
      tt_m.setTitle("Memory for proxy vs. best effort");
      tt_m.setNickname("tMemBestVsProxy");
      add(tt_m);
      for (Region r : new_big_r.all(SCENARIO))
      {
        Region r_best = new Region(r);
        r_best.add(BEST_EFFORT, JsonTrue.instance);
        AccessControlledStreamExperiment e_best = m_factory.get(r_best);
        Region r_prox = new Region(r);
        r_prox.add(BEST_EFFORT, JsonFalse.instance);
        AccessControlledStreamExperiment e_prox = m_factory.get(r_prox);
        if (e_best == null || e_prox == null)
        {
          continue;
        }
        vpt.add(e_best, e_prox);
        et_t.add(e_prox);
        et_t.add(e_best);
        et_m.add(e_prox);
        et_m.add(e_best);
      }
      add(new MacroVerdictPurity(this, vpt));
    }

    // Macros
    add(new LabStats(this));
    add(new MaxTraceLength(this, MAX_TRACE_LENGTH));
  }

  /**
   * Initializes the lab
   * @param args Command line arguments
   */
  public static void main(String[] args)
  {
    // Nothing else to do here
    MyLaboratory.initialize(args, MyLaboratory.class);
  }
  
  @Override
  public void setupCli(CliParser parser)
  {
    parser.addArgument(new Argument().withLongName("small").withDescription("Use short traces for debugging"));
  }
}
