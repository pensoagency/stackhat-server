package penso.stackhat.builtwith;

public class Tech {
	private String name;
	private boolean status; // true for current, false for historical
	private long firstDetectedTime;
	private long LastDetectedTime;
	private String duration;
	private String PensoName;
	
	
	public String getPensoName() {
		return PensoName;
	}
	public void setPensoName(String pensoName) {
		PensoName = pensoName;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public long getFirstDetectedTime() {
		return firstDetectedTime;
	}
	public void setFirstDetectedTime(long firstDetectedTime) {
		this.firstDetectedTime = firstDetectedTime;
	}
	public long getLastDetectedTime() {
		return LastDetectedTime;
	}
	public void setLastDetectedTime(long end_time) {
		this.LastDetectedTime = end_time;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	@Override
	public String toString() {
		return "Tech [name=" + name + ", status=" + status + ", firstDetectedTime=" + firstDetectedTime
				+ ", LastDetectedTime=" + LastDetectedTime + ", duration=" + duration + ", PensoName=" + PensoName
				+ "]";
	}
	
}
