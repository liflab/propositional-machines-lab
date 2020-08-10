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
import ca.uqac.lif.cep.ProcessorException;
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
   * The string designating the PLAY variable.
   */
  public static final transient String PLAY = "play";
  
  /**
   * The string designating the STOP variable.
   */
  public static final transient String STOP = "stop";
  
  /**
   * The string designating the PAUSE variable.
   */
  public static final transient String PAUSE = "pause";
  
  /**
   * The string designating the BUFFER variable.
   */
  public static final transient String BUFFER = "buffer";
  
  /**
   * The string designating the DECODE variable.
   */
  public static final transient String DECODE = "decode";
  
  /**
   * The play state
   */
  public static enum PlayState {STOPPED, PAUSED, PLAYING}
  
  /**
   * A singleton instance of the PLAY event
   */
  public static PlayEvent s_play = new PlayEvent();
  
  /**
   * A singleton instance of the STOP event
   */
  public static StopEvent s_stop = new StopEvent();
  
  /**
   * A singleton instance of the PAUSE event
   */
  public static PauseEvent s_pause = new PauseEvent();
  
  /**
   * A singleton instance of the BUFFER event
   */
  public static BufferEvent s_buffer = new BufferEvent();
  
  /**
   * A singleton instance of the DECODE event
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
    super(length, PLAY, STOP, PAUSE, BUFFER, DECODE);
    m_floatSource = float_source;
    m_buffered = false;
    m_state = PlayState.STOPPED;
  }

  @Override
  protected MultiEvent getEvent() throws ProcessorException
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
      // Not supposed to happen, just for safety
      throw new ProcessorException("No event to choose from");
    }
    updateState(chosen);
    System.out.println(chosen);
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
      m_buffered = false;
    }
    else if (e instanceof PauseEvent)
    {
      m_state = PlayState.PAUSED;
    }
    else if (e instanceof BufferEvent)
    {
      m_buffered = true;
    }
    else if (e instanceof DecodeEvent)
    {
      m_buffered = false;
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
  
  @Override
  public void reset()
  {
    super.reset();
    m_floatSource.reset();
    System.out.println("------RESET");
  }
  
  /**
   * Event representing the PLAY action
   */
  public static class PlayEvent extends ConcreteMultiEvent
  {
    private PlayEvent()
    {
      super(createValuations());
    }
    
    protected static Set<Valuation> createValuations()
    {
      Set<Valuation> set = new HashSet<Valuation>();
      Valuation v = new Valuation();
      v.put(PLAY, Value.TRUE);
      v.put(STOP, Value.FALSE);
      v.put(PAUSE, Value.FALSE);
      v.put(BUFFER, Value.FALSE);
      v.put(DECODE, Value.FALSE);
      set.add(v);
      return set;
    }
    
    @Override
    public String toString()
    {
      return PLAY;
    }    
  }
  
  /**
   * Event representing the STOP action
   */
  public static class StopEvent extends ConcreteMultiEvent
  {
    private StopEvent()
    {
      super(createValuations());
    }
    
    protected static Set<Valuation> createValuations()
    {
      Set<Valuation> set = new HashSet<Valuation>();
      Valuation v = new Valuation();
      v.put(PLAY, Value.FALSE);
      v.put(STOP, Value.TRUE);
      v.put(PAUSE, Value.FALSE);
      v.put(BUFFER, Value.FALSE);
      v.put(DECODE, Value.FALSE);
      set.add(v);
      return set;
    }
    
    @Override
    public String toString()
    {
      return STOP;
    }
  }
  
  /**
   * Event representing the PAUSE action
   */
  public static class PauseEvent extends ConcreteMultiEvent
  {
    private PauseEvent()
    {
      super(createValuations());
    }
    
    protected static Set<Valuation> createValuations()
    {
      Set<Valuation> set = new HashSet<Valuation>();
      Valuation v = new Valuation();
      v.put(PLAY, Value.FALSE);
      v.put(STOP, Value.FALSE);
      v.put(PAUSE, Value.TRUE);
      v.put(BUFFER, Value.FALSE);
      v.put(DECODE, Value.FALSE);
      set.add(v);
      return set;
    }
    
    @Override
    public String toString()
    {
      return PAUSE;
    }
  }
  
  /**
   * Event representing the BUFFER action
   */
  public static class BufferEvent extends ConcreteMultiEvent
  {
    private BufferEvent()
    {
      super(createValuations());
    }
    
    protected static Set<Valuation> createValuations()
    {
      Set<Valuation> set = new HashSet<Valuation>();
      Valuation v = new Valuation();
      v.put(PLAY, Value.TRUE);
      v.put(STOP, Value.FALSE);
      v.put(PAUSE, Value.FALSE);
      v.put(BUFFER, Value.TRUE);
      v.put(DECODE, Value.FALSE);
      set.add(v);
      return set;
    }
    
    @Override
    public String toString()
    {
      return BUFFER;
    }
  }
  
  /**
   * Event representing the BUFFER action
   */
  public static class DecodeEvent extends ConcreteMultiEvent
  {
    private DecodeEvent()
    {
      super(createValuations());
    }
    
    protected static Set<Valuation> createValuations()
    {
      Set<Valuation> set = new HashSet<Valuation>();
      Valuation v = new Valuation();
      v.put(PLAY, Value.TRUE);
      v.put(STOP, Value.FALSE);
      v.put(PAUSE, Value.FALSE);
      v.put(BUFFER, Value.TRUE);
      v.put(DECODE, Value.TRUE);
      set.add(v);
      return set;
    }
    
    @Override
    public String toString()
    {
      return DECODE;
    }
  }
}
