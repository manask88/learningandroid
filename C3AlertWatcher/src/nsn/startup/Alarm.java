package nsn.startup;

public class Alarm {
	
	
	
private int sensor_id;
	private int alarm_id;
	private int list_id;
	private String alarmName;
	private String assetName;
	private boolean ack;
	
	
	

	public int getSensor_id() {
		return sensor_id;
	}




	public void setSensor_id(int sensor_id) {
		this.sensor_id = sensor_id;
	}




	public int getAlarm_id() {
		return alarm_id;
	}




	public void setAlarm_id(int alarm_id) {
		this.alarm_id = alarm_id;
	}




	public int getList_id() {
		return list_id;
	}




	public void setList_id(int list_id) {
		this.list_id = list_id;
	}




	public String getAlarmName() {
		return alarmName;
	}




	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}




	public String getAssetName() {
		return assetName;
	}




	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}




	public boolean isAck() {
		return ack;
	}




	public void setAck(boolean ack) {
		this.ack = ack;
	}




	public Alarm(int alarm_id, int list_id, String alarmName, String assetName, int sensor_id) {
		super();
		this.alarm_id = alarm_id;
		this.list_id = list_id;
		this.alarmName = alarmName;
		this.assetName = assetName;
		this.sensor_id=sensor_id;
		this.ack = false;
	}
}