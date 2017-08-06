package vladi_yaki_project.domain;
import java.io.Serializable;

import javafx.scene.paint.Color;
import vladi_yaki_project.raceMVC.Car;

public class CarData implements Serializable{
	private int id;
	private String name;
	private String type;
	private String size;
	private String shape;
	private String color;
	private int raceID;
	
	public CarData (Car car){
		this.id = car.getId();
		this.raceID = car.getModelId();
		this.color = car.getColor().toString();
		this.type = car.getType();
		this.size = car.getSize();
		this.shape = car.getShape();
		this.name = id+"/"+raceID+"_"+size+"_"+type;
	}
	
	public CarData(int id, String name, String type, String size, String shape, String color, int raceID) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.size = size;
		this.shape = shape;
		this.color = color;
		this.raceID = raceID;
	}

	public CarData (){}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "CarData [id=" + id + ", type=" + type + ", size=" + size + ", color=" + color + "]";
	}
	public int getRaceID() {
		return raceID;
	}
	public void setRaceID(int raceID) {
		this.raceID = raceID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	
	
}
