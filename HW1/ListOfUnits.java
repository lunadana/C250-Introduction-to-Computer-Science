
public class ListOfUnits {
	private Unit[] UnitArray;
	private int size;

	public ListOfUnits() {
		this.UnitArray = new Unit[10];
		this.size = 0;
	} 

	public int size() {
		return this.size;
	} 

	public Unit[] getUnits() {
		Unit[] a = new Unit[this.size];
		for(int i = 0; i<this.size; i++) {
			a[i]= UnitArray[i];
		}
		return a;
	}

	public Unit get(int a) {
		if (a >= UnitArray.length || a<0) {
			throw new IndexOutOfBoundsException("wrong");
		}else {
		return this.UnitArray[a];
	}}

	// test needed
	public void add(Unit u) {
		if(this.size == this.UnitArray.length) {
			Unit[] a = resize(UnitArray);
			a[size] = u;
			UnitArray = a;
		}
		else {
			UnitArray[size] = u;
		}
		size++;
	}

	private Unit[] resize(Unit [] initial) {
		int newCapacity = size + size/2 + 1;;
		Unit[] newOne = new Unit[newCapacity];

		for (int i = 0; i<initial.length; i++) {
			newOne[i] = initial[i];
		}
		return newOne;
	}

	public int indexOf(Unit c) {
		for(int i = 0; i <this.UnitArray.length; i++) {
			if (this.UnitArray[i].equals(c)) {
				return i;
			}
		}
		return -1;
	}


	public boolean remove(Unit e) {
		int i = indexOf(e);
		if(i == -1) {
			return false; 
		}
		for(int k = i; k<size-1;k++) {
			UnitArray[k] = UnitArray[k+1];
		}
		UnitArray[size-1] = null;
		size--;
	return true;
}

	public MilitaryUnit[] getArmy() {
		int count= 0;
		for (int i = 0; i<UnitArray.length; i++) {
			if (UnitArray[i] instanceof MilitaryUnit) {
				count++;
			}
		}
		MilitaryUnit[] o = new MilitaryUnit[count];
		int j = 0;
		for (int i = 0; i<UnitArray.length; i++) {
			if (UnitArray[i] instanceof MilitaryUnit) {
				o[j] = (MilitaryUnit) UnitArray[i];
				j++;
			}
		}

		return o;
	}
 





}

