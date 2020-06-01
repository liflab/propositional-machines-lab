package propmanlab;

import ca.uqac.lif.labpal.Laboratory;

public class MyLaboratory extends Laboratory
{
  /**
   * The maximum length of a trace
   */
  public static final int MAX_TRACE_LENGTH = 10000;
  
  /**
   * The interval, in number of events, between updates of the experiment's
   * measurements
   */
  public static final int s_eventStep = 10;
  
	@Override
	public void setup()
	{
		// Write your setup code here
	}
	
	public static void main(String[] args)
	{
		// Nothing else to do here
		MyLaboratory.initialize(args, MyLaboratory.class);
	}
}
