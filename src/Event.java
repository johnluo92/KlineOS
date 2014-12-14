
public class Event implements Comparable<Event> {

	public int type;
	public long timestamp;
	
	public Event(int inputtype,long inputtime) {
		type = inputtype;
		timestamp = inputtime;
	}

	@Override
	public int compareTo(Event o) {
		if (timestamp == o.timestamp) {
			return 0;
		} else if (timestamp > o.timestamp) {
			return 1;
		} else {
			return -1;
		}
	}
	
}
