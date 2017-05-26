package resources;

public final class Hasher {

	private Hasher() {
		
	}
	
	public static final int hash(Object... objects) {
		int hash = 0;
		for(Object object : objects) {
			hash = 31 * hash + object.hashCode();
		}
		return hash;
	}
}
