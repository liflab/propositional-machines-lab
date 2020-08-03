package propmanlab.scenarios.simple;

import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.MultiEventFactory;
import ca.uqac.lif.cep.propman.MultiEventFunction;
import ca.uqac.lif.cep.propman.PropositionalMachine;
import ca.uqac.lif.cep.propman.MultiEventFunction.Identity;

public class BestEffortProxy extends PropositionalMachine
{
  public static final transient String NAME = "Best effort";
  
  public BestEffortProxy()
  {
    MultiEventFunction f = new MultiEventFunction.EmitConstant(new ConcreteMultiEvent.All("a", "b", "c"));
    MultiEventFactory factory = new MultiEventFactory("a", "b", "c");
    //addTransition(1, new TransitionOtherwise(1, f));
    addTransition(1, new Transition(2, factory.readFromValuations("TFF,FTF"), f));
    addTransition(1, new TransitionOtherwise(1, Identity.instance));
    addTransition(2, new Transition(1, factory.readFromValuations("TFF,FTF"), f));
    addTransition(2, new TransitionOtherwise(2, Identity.instance));
  }
}
