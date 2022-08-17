
public class Archer extends MilitaryUnit {

	private int arrows;

	public Archer(Tile position, double hp, String faction){
		super(position,hp,2,faction,15.0,2,0);
		this.arrows = 5; 
	}
	
	public void takeAction(Tile x) {
	if (arrows == 0) {
		this.arrows = 5;
	}
	else {
		this.arrows--;
		super.takeAction(x);
	}}
	
public boolean equals(Object o) {
	
	if(!(o instanceof Archer)) {
		return false;
	}
	Archer u = (Archer) o;
	if(!(u.arrows == this.arrows)){
		return false;
	}
	
	return super.equals(o);

	}

}
