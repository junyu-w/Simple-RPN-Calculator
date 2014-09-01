import java.util.*;

public class Storage {
	
	private Stack dataSet;
	private ArrayList<Integer> numbersInStack = new ArrayList<Integer>(); 
	
	protected Storage() {
		dataSet = new Stack();
	}
	
	protected void set(Integer inputNumber) {
		dataSet.push(inputNumber);
		numbersInStack.add(inputNumber);
	}
	
	protected int get() {
		Integer storedInteger = (Integer) dataSet.pop();
		int number = storedInteger.intValue();
		numbersInStack.remove(numbersInStack.size()-1);
		return number;
	}
	
	protected int size() {
		return dataSet.size();
	}
/**	
	protected int getWhich(int n) {
		if (n==0) {
			int result = this.get();
			Integer repushResult = new Integer(result);
			this.set(repushResult);
			return result;
		}else {
			Integer[] repushArray = new Integer[n];
			for (int i = 0; i<n; i++) {
				System.out.println("I'm good");
				int temporaryGet = this.get();
				Integer repushItem = new Integer(temporaryGet);
				repushArray[i] = repushItem;
			}
			//this temporarily takes out the numbers above the one we want, put them into an array so that we can easily put them back later.
			int result = this.get();
			Integer repushResult = new Integer(result);
			
			for (int j = n-1; j>0; j--) {
				System.out.println("I'm bad");
				this.set(repushArray[j]);
			}
			this.set(repushResult);
			return result;
		}
	}
**/
	
	public String toString() {
		String result = " ";
		int stackHeight = this.size();
		System.out.println("here "+stackHeight);
		result += "+- STACK -- top ---------+\n";
		for (int i = 0; i<stackHeight; i++) {
			result += " |                       "+numbersInStack.get(stackHeight-1-i)+"|\n";
		}
		result += " --------------------------\n";
		return result;
	}
	
}
