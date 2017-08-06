package vladi_yaki_project.raceMVC;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

public class Car implements CarEvents,Serializable {
	private int id;
	private int model_id;
	//private CarLog log;
	private double speed;
	private Color color;
	private int wheelRadius;
	private Map<eventType, ArrayList<EventHandler<Event>>> carHashMap;
	//private CarSize size;
	//private CarType type;
	//private CarShape shape;
	private String size;
	private String type;
	private String shape;
	
	public Car(int id, int model_id) {
		this.id = id;
		this.model_id = model_id;
		//this.log = log;
		this.speed = 1;
		this.color = Color.RED;
		this.wheelRadius = 5;
		carHashMap = new HashMap<eventType, ArrayList<EventHandler<Event>>>();
		for (eventType et : eventType.values())
			carHashMap.put(et, new ArrayList<EventHandler<Event>>());
		int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		String[] sizes = {"small","meduim","big"};
		this.setSize(sizes[randomNum]);
		randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
		String[] types = {"ford","subarue","mazda","bmw"};
		this.setType(types[randomNum]);
		randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
		String[] shapes = {"shape1","shape2","shape3","shape4"};
		this.setShape(shapes[randomNum]);
		Color colors[] = { Color.RED, Color.AQUA, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK,
				Color.VIOLET, Color.WHITE, Color.TRANSPARENT };
		randomNum = ThreadLocalRandom.current().nextInt(0, 9 + 1);
		this.setColor(colors[randomNum]);
	}

	public int getId() {
		return id;
	}

	public int getModelId() {
		return model_id;
	}

	public Color getColor() {
		return color;
	}

	public int getRadius() {
		return wheelRadius;
	}

	public double getSpeed() {
		return speed;
	}

	public void setColor(Color color) {
		this.color = color;
		processEvent(eventType.COLOR, new ActionEvent());
	}

	public void setSpeed(double speed) {
		this.speed = speed;
		processEvent(eventType.SPEED, new ActionEvent());
	}

	public void setRadius(int wheelRadius) {
		this.wheelRadius = wheelRadius;
		processEvent(eventType.RADIUS, new ActionEvent());
	}

	public synchronized void addEventHandler(EventHandler<Event> l, eventType et) {
		ArrayList<EventHandler<Event>> al;
		al = carHashMap.get(et);
		if (al == null)
			al = new ArrayList<EventHandler<Event>>();
		al.add(l);
		carHashMap.put(et, al);
	}

	public synchronized void removeEventHandler(EventHandler<Event> l, eventType et) {
		ArrayList<EventHandler<Event>> al;
		al = carHashMap.get(et);
		if (al != null && al.contains(l))
			al.remove(l);
		carHashMap.put(et, al);
	}

	private void processEvent(eventType et, Event e) {
		String msg;
		ArrayList<EventHandler<Event>> al;
		synchronized (this) {
			al = carHashMap.get(et);
			if (al == null)
				return;
		}
		msg = "CarRaceView" + (getModelId() + 1) + " | car number: " + (getId() + 1) + " | actionCommand: "
				+ et.toString() + " | array size is: " + al.size();
		//log.printMsg(msg);
		for (int i = 0; i < al.size(); i++) {
			EventHandler<Event> handler = (EventHandler<Event>) al.get(i);
			handler.handle(e);
		}
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}
}