
public abstract class MilitaryUnit extends Unit{

	private double dammage;
	private int range;
	private int armor;
	
 public MilitaryUnit(Tile position, double hp, int movingRange, String faction, double dammage, int range, int armor){		
	super(position,hp, movingRange, faction);
	this.dammage = dammage ;
	this.range = range;
	this.armor = armor;
	}

 public void takeAction(Tile x) {
	 double di = Tile.getDistance(this.getPosition(),x);
	
	 if(di>=this.range+1) {
		 return;
	 }
	 Unit w = x.selectWeakEnemy(this.getFaction());
	 if(w==null) {
		 
	 }else {
	if(this.getPosition().isImproved()) {
	 w.receiveDamage(this.dammage*1.05);
	 }
	 w.receiveDamage(this.dammage);
 }}
 
 
 public void receiveDamage(double n) {
	 double multiplier = 100/(100 + this.armor);
	 super.receiveDamage(n*multiplier);
 }
 
}
