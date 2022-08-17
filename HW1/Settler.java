
public class Settler extends Unit {

	public Settler(Tile position, double hp, String faction){
		super(position, hp, 2, faction); 
	
	}
	
	public void takeAction(Tile e) {
		if (e.equals(this.getPosition()) && !e.isCity()) {
			e.foundCity();
			e.removeUnit(this);
		}
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Settler)){
			return false;
		}
		Settler s = (Settler) o;
		return super.equals(s);
	}
	
}

 