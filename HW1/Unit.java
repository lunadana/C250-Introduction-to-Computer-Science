
public abstract class Unit {

	private Tile position;
	private double hp;
	private int movingRange;
	private String faction;

	public Unit(Tile position, double hp, int movingRange, String faction) {
		this.position = position;
		this.hp = hp;
		this.faction = faction;
		this.movingRange = movingRange;

		if (this.position.addUnit(this) == false) {
			throw new IllegalArgumentException("no");
		} 
	}

	public final Tile getPosition() {
		return this.position;
	}

	public final double getHP() {
		return this.hp;
	}

	public final String getFaction() {
		return this.faction;
	}

	public boolean moveTo(Tile newPosition) {
		double di = Tile.getDistance(this.position, newPosition);

		if (di >= this.movingRange + 1) {
			return false;
		}
		if (newPosition.addUnit(this) == true) {
			position.removeUnit(this);
			this.position = newPosition;
			return true;
		}
		return false;
	}

	public void receiveDamage(double d) {
		if (this.position.isCity()) {
			d = 0.9 * d;
		}
		this.hp = this.hp - d;
		if (hp <= 0) {
			this.position.removeUnit(this);
		}
	}

	public abstract void takeAction(Tile i);

	public boolean equals(Object o) {
		if (!(o instanceof Unit)) {
			return false;
		}
		Unit u = (Unit) o;
		if (!(u.getPosition().equals(this.position))) {
			return false;
		}

		if (!(u.getHP() == this.hp)) {
			return false;
		}
		if (!(u.getFaction().equals(this.faction))) {
			return false;
		}
		return true;
	}

}
