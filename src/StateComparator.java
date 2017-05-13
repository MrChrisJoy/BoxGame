import java.util.Comparator;

public class StateComparator implements Comparator<State> {

	@Override
	public int compare(State s1, State s2) {
		int result = 0;
		int costS1 = s1.getTotalCost();
		int costS2 = s2.getTotalCost();
		if(costS1 < costS2) {
			result = -1;
		}else if (costS1 > costS2) {
			result = 1;
		} else {
			result = 0;
		}
		return result;
	}

}
