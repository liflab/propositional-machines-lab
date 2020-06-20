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

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.ltl.Troolean.Value;
import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.Valuation;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.util.ElementPicker;
import java.util.Set;
import java.util.HashSet;
import propmanlab.source.MultiEventSource;

public class MPlayerSource extends MultiEventSource 
{
  /**
   * The play state
   */
  public static enum PlayState {STOPPED, PAUSED, PLAYING}
  
  /**
   * A singleton instance of the "play" event
   */
  public static PlayEvent s_play = new PlayEvent();
  
  /**
   * A singleton instance of the "stop" event
   */
  public static StopEvent s_stop = new StopEvent();
  
  /**
   * A singleton instance of the "pause" event
   */
  public static PauseEvent s_pause = new PauseEvent();
  
  /**
   * A singleton instance of the "buffer" event
   */
  public static BufferEvent s_buffer = new BufferEvent();
  
  /**
   * A singleton instance of the "decode" event
   */
  public static DecodeEvent s_decode = new DecodeEvent();
  
  /**
   * The current play state of the player
   */
  protected PlayState m_state;
  
  /**
   * Whether the last action of the player is a buffering
   */
  protected boolean m_buffered;
  
  /**
   * A float source used to pick the next event
   */
  protected Picker<Float> m_floatSource;
  
  public MPlayerSource(int length, Picker<Float> float_source)
  {
    super(length, "play", "stop", "pause", "buffer", "decode");
    m_floatSource = float_source;
    m_buffered = false;
    m_state = PlayState.STOPPED;
  }

  @Override
  protected MultiEvent getEvent()
  {
    Set<MultiEvent> options = new HashSet<MultiEvent>();
    if (m_state == PlayState.STOPPED)
    {
      options.add(s_play);
    }
    else if (m_state == PlayState.PAUSED)
    {
      options.add(s_play);
      options.add(s_stop);
    }
    else if (m_state == PlayState.PLAYING)
    {
      options.add(s_stop);
      options.add(s_pause);
      if (m_buffered)
      {
        options.add(s_decode);
      }
      else
      {
        options.add(s_buffer);
      }
    }
    ElementPicker<MultiEvent> picker = new ElementPicker<MultiEvent>(m_floatSource);
    float prob = 1 / (float) options.size();
    for (MultiEvent e : options)
    {
      picker.add(e, prob);
    }
    MultiEvent chosen = picker.pick();
    if (chosen == null)
    {
      System.out.println("ERROR");
    }
    updateState(chosen);
    return chosen;
  }
  
  protected void updateState(MultiEvent e)
  {
    if (e instanceof StopEvent)
    {
      m_state = PlayState.STOPPED;
      m_buffered = false;
    }
    else if (e instanceof PlayEvent)
    {
      m_state = PlayState.PLAYING;
    }
    else if (e instanceof PauseEvent)
    {
      m_state = PlayState.PAUSED;
    }
  }

  @Override
  public String getFilename()
  {
    return "mplayer.txt";
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    throw new UnsupportedOperationException("This processor cannot be cloned");
  }
  
  /**
   * Event representing the "play" action
   */
  public static class PlayEvent extends ConcreteMultiEvent
  {
    protected static final Set<Valuation> s_valuations = createValuations();
    
    private PlayEvent()
    {
      super();
    }
    
    protected static Set<Valuation> createValuations()
    {
      Set<Valuation> set = new HashSet<Valuation>();
      Valuation v = new Valuation();
      v.put("play", Value.TRUE);
      v.put("stop", Value.FALSE);
      v.put("pause", Value.FALSE);
      v.put("buffer", Value.FALSE);
      v.put("decode", Value.FALSE);
      set.add(v);
      return set;
    }
    
    @Override
    public String toString()
    {
      return "play";
    }
    
    @Override
    public Set<Valuation> getValuations()
    {
      return s_valuations;
    }
  }
  
  /**
   * Event representing the "stop" action
   */
  public static class StopEvent extends ConcreteMultiEvent
  {
    protected static final Set<Valuation> s_valuations = createValuations();
    
    private StopEvent()
    {
      super();
    }
    
    protected static Set<Valuation> createValuations()
    {
      Set<Valuation> set = new HashSet<Valuation>();
      Valuation v = new Valuation();
      v.put("play", Value.FALSE);
      v.put("stop", Value.TRUE);
      v.put("pause", Value.FALSE);
      v.put("buffer", Value.FALSE);
      v.put("decode", Value.FALSE);
      set.add(v);
      return set;
    }
    
    @Override
    public String toString()
    {
      return "stop";
    }
    
    @Override
    public Set<Valuation> getValuations()
    {
      return s_valuations;
    }
  }
  
  /**
   * Event representing the "pause" action
   */
  public static class PauseEvent extends ConcreteMultiEvent
  {
    protected static final Set<Valuation> s_valuations = createValuations();
    
    private PauseEvent()
    {
      super();
    }
    
    protected static Set<Valuation> createValuations()
    {
      Set<Valuation> set = new HashSet<Valuation>();
      Valuation v = new Valuation();
      v.put("play", Value.FALSE);
      v.put("stop", Value.FALSE);
      v.put("pause", Value.TRUE);
      v.put("buffer", Value.FALSE);
      v.put("decode", Value.FALSE);
      set.add(v);
      return set;
    }
    
    @Override
    public String toString()
    {
      return "pause";
    }
    
    @Override
    public Set<Valuation> getValuations()
    {
      return s_valuations;
    }
  }
  
  /**
   * Event representing the "buffer" action
   */
  public static class BufferEvent extends ConcreteMultiEvent
  {
    protected static final Set<Valuation> s_valuations = createValuations();
    
    private BufferEvent()
    {
      super();
    }
    
    protected static Set<Valuation> createValuations()
    {
      Set<Valuation> set = new HashSet<Valuation>();
      Valuation v = new Valuation();
      v.put("play", Value.TRUE);
      v.put("stop", Value.FALSE);
      v.put("pause", Value.FALSE);
      v.put("buffer", Value.TRUE);
      v.put("decode", Value.FALSE);
      set.add(v);
      return set;
    }
    
    @Override
    public String toString()
    {
      return "buffer";
    }
    
    @Override
    public Set<Valuation> getValuations()
    {
      return s_valuations;
    }
  }
  
  /**
   * Event representing the "buffer" action
   */
  public static class DecodeEvent extends ConcreteMultiEvent
  {
    protected static final Set<Valuation> s_valuations = createValuations();
    
    private DecodeEvent()
    {
      super();
    }
    
    protected static Set<Valuation> createValuations()
    {
      Set<Valuation> set = new HashSet<Valuation>();
      Valuation v = new Valuation();
      v.put("play", Value.TRUE);
      v.put("stop", Value.FALSE);
      v.put("pause", Value.FALSE);
      v.put("buffer", Value.TRUE);
      v.put("decode", Value.TRUE);
      set.add(v);
      return set;
    }
    
    @Override
    public String toString()
    {
      return "decode";
    }
    
    @Override
    public Set<Valuation> getValuations()
    {
      return s_valuations;
    }
  }
}
