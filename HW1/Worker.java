
public class Worker extends Unit{

	private int jobs;

	public Worker(Tile position, double hp, String faction) {
		super(position, hp, 2, faction);
		this.jobs = 0;
	}
	
	public void takeAction(Tile w) {
		if (w.equals(this.getPosition()) && !w.isImproved()){
			w.buildImprovement();
			this.jobs++;
			if(this.jobs >= 10) {
				w.removeUnit(this);
			}
		}
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Worker)) {
			return false;
		}
		Worker i = (Worker) o;
		if(i.jobs != this.jobs) {
			return false;
		}
		return super.equals(o);
	}
	
}
