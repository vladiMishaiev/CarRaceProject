package vladi_yaki_project.raceMVC;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

public class CarPane extends Pane implements CarEvents {
	class ColorEvent implements EventHandler<Event> {
		@Override
		public void handle(Event event) {
			setColor(car.getColor());
		}
	}

	class RadiusEvent implements EventHandler<Event> {
		@Override
		public void handle(Event event) {
			setRadius(car.getRadius());
		}
	}

	class SpeedEvent implements EventHandler<Event> {
		@Override
		public void handle(Event event) {
			setSpeed(car.getSpeed());
		}
	}

	final int MOVE = 1;
	final int STOP = 0;
	private double xCoor;
	private double yCoor;
	private Timeline tl; // speed=setRate()
	private Color color;
	private int r;// radius
	private Car car;

	public CarPane() {
		xCoor = 0;
		r = 5;
	}

	public void setCarModel(Car myCar) {
		car = myCar;
		if (car != null) {
			car.addEventHandler(new ColorEvent(), eventType.COLOR);
			car.addEventHandler(new RadiusEvent(), eventType.RADIUS);
			car.addEventHandler(new SpeedEvent(), eventType.SPEED);
		}
		switch(car.getSize()){
		case "small":{
			r=5;
		}break;
		case "meduim":{
			r=6;
		}break;
		case "big":{
			r=7;
		}break;
		}
		setColor(car.getColor());
	}

	public Car getCarModel() {
		return car;
	}

	public void moveCar(int n) {
		yCoor = getHeight();
		setMinSize(10 * r, 6 * r);
		if (xCoor > getWidth()) {
			xCoor = -10 * r;
		} else {
			xCoor += n;
		}
		
		Polygon polygon;
		Polygon polygon1 = null;
		Group lines = new Group();
		int shiftx = -8;
		int shifty = -5;
		switch(car.getShape()){
		case "shape1":{
			polygon = new Polygon(
				xCoor, yCoor - r, 
				xCoor, yCoor - 3 * r, 
				xCoor + 2 * r, yCoor - 3 * r,
				xCoor + 2 * r, yCoor - 5 * r, 
				xCoor + 7 * r, yCoor - 5 * r, 
				xCoor + 7 * r, yCoor - 3 * r,
				xCoor + 10 * r, yCoor - 3 * r, 
				xCoor + 10 * r, yCoor - r);
			polygon1 = new Polygon(
				(xCoor)+shiftx,( yCoor - r)+shifty, 
				(xCoor)+shiftx, (yCoor - 3 * r)+shifty, 
				(xCoor + 2 * r)+shiftx,( yCoor - 3 * r)+shifty,
				(xCoor + 2 * r)+shiftx, (yCoor - 5 * r)+shifty, 
				(xCoor + 7 * r)+shiftx,( yCoor - 5 * r)+shifty, 
				(xCoor + 7 * r)+shiftx,( yCoor - 3 * r)+shifty,
				(xCoor + 10 * r)+shiftx,( yCoor - 3 * r)+shifty, 
				(xCoor + 10 * r)+shiftx,( yCoor - r)+shifty);
			lines.getChildren().addAll(
				new Line(xCoor, yCoor - r, (xCoor)+shiftx, ( yCoor - r)+shifty),
				new Line(xCoor, yCoor - 3 * r, (xCoor)+shiftx, (yCoor - 3 * r)+shifty),
				new Line(xCoor + 2 * r, yCoor - 3 * r, (xCoor + 2 * r)+shiftx,( yCoor - 3 * r)+shifty),
				new Line(xCoor + 2 * r, yCoor - 5 * r, (xCoor + 2 * r)+shiftx, (yCoor - 5 * r)+shifty),
				new Line(xCoor + 7 * r, yCoor - 5 * r, (xCoor + 7 * r)+shiftx,( yCoor - 5 * r)+shifty),
				new Line(xCoor + 7 * r, yCoor - 3 * r, (xCoor + 7 * r)+shiftx,( yCoor - 3 * r)+shifty),
				new Line(xCoor + 10 * r, yCoor - 3 * r, (xCoor + 10 * r)+shiftx,( yCoor - 3 * r)+shifty)
				);
			
			
		}break;
		case "shape2":{
			polygon = new Polygon(
				xCoor, yCoor - r, 
				xCoor, yCoor - 4 * r, 
				xCoor + 2 * r, yCoor - 4 * r,
				xCoor + 2 * r, yCoor - 2.5 * r, 
				xCoor + 5 * r, yCoor - 2.5 * r, 
				xCoor + 5 * r, yCoor - 4 * r,
				xCoor + 10 * r, yCoor - 4 * r, 
				xCoor + 10 * r, yCoor - r);
			polygon1 = new Polygon(
				(xCoor)+shiftx,( yCoor - r)+shifty, 
				(xCoor)+shiftx, (yCoor - 4 * r)+shifty, 
				(xCoor + 2 * r)+shiftx,( yCoor - 4 * r)+shifty,
				(xCoor + 2 * r)+shiftx,( yCoor - 2.5 * r)+shifty, 
				(xCoor + 5 * r)+shiftx,( yCoor - 2.5 * r)+shifty, 
				(xCoor + 5 * r)+shiftx,( yCoor - 4 * r)+shifty,
				(xCoor + 10 * r)+shiftx,( yCoor - 4 * r)+shifty, 
				(xCoor + 10 * r)+shiftx,( yCoor - r)+shifty);
			lines.getChildren().addAll(
				new Line(xCoor, yCoor - r, (xCoor)+shiftx,( yCoor - r)+shifty),
				new Line(xCoor, yCoor - 4 * r, (xCoor)+shiftx, (yCoor - 4 * r)+shifty),
				new Line(xCoor + 2 * r, yCoor - 4 * r, (xCoor + 2 * r)+shiftx,( yCoor - 4 * r)+shifty),
				new Line(xCoor + 2 * r, yCoor - 2.5 * r, (xCoor + 2 * r)+shiftx,( yCoor - 2.5 * r)+shifty),
				new Line(xCoor + 5 * r, yCoor - 2.5 * r, (xCoor + 5 * r)+shiftx,( yCoor - 2.5 * r)+shifty),
				new Line(xCoor + 5 * r, yCoor - 4 * r, (xCoor + 5 * r)+shiftx,( yCoor - 4 * r)+shifty),
				new Line(xCoor + 10 * r, yCoor - 4 * r, (xCoor + 10 * r)+shiftx,( yCoor - 4 * r)+shifty),
				new Line(xCoor + 10 * r, yCoor - r,(xCoor + 10 * r)+shiftx,( yCoor - r)+shifty),
				new Line(xCoor + 5 * r, yCoor - 4 * r, xCoor + 5 * r, yCoor - 6 * r),
				new Line((xCoor + 5 * r)+shiftx,( yCoor - 4 * r)+shifty, (xCoor + 5 * r)+shiftx,( yCoor - 6 * r)+shifty),
				new Line(xCoor + 5 * r, yCoor - 6 * r,(xCoor + 5 * r)+shiftx,( yCoor - 6 * r)+shifty)
					);
		}break;
		case "shape3":{
			polygon = new Polygon(
				xCoor, yCoor - r, 
				xCoor, yCoor - 5 * r, 
				xCoor + 6 * r, yCoor - 5 * r,
				xCoor + 6 * r, yCoor - 3.2 * r, 
				xCoor + 10 * r, yCoor - 3.2 * r, 
				xCoor + 10 * r, yCoor - 1 * r);
			polygon1 = new Polygon(
				(xCoor)+shiftx,( yCoor - r)+shifty, 
				(xCoor)+shiftx,( yCoor - 5 * r)+shifty, 
				(xCoor + 6 * r)+shiftx,( yCoor - 5 * r)+shifty,
				(xCoor + 6 * r)+shiftx,( yCoor - 3.2 * r)+shifty, 
				(xCoor + 10 * r)+shiftx,( yCoor - 3.2 * r)+shifty, 
				(xCoor + 10 * r)+shiftx,( yCoor - 1 * r)+shifty);
			lines.getChildren().addAll(
				new Line(xCoor, yCoor - r, (xCoor)+shiftx,( yCoor - r)+shifty),
				new Line(xCoor, yCoor - 5 * r, (xCoor)+shiftx,( yCoor - 5 * r)+shifty),
				new Line(xCoor + 6 * r, yCoor - 5 * r, (xCoor + 6 * r)+shiftx,( yCoor - 5 * r)+shifty),
				new Line(xCoor + 6 * r, yCoor - 3.2 * r, (xCoor + 6 * r)+shiftx,( yCoor - 3.2 * r)+shifty),
				new Line(xCoor + 10 * r, yCoor - 3.2 * r, (xCoor + 10 * r)+shiftx,( yCoor - 3.2 * r)+shifty),
				new Line(xCoor + 10 * r, yCoor - 1 * r, (xCoor + 10 * r)+shiftx,( yCoor - 1 * r)+shifty)
					);
		}break;
		case "shape4":{
			polygon = new Polygon(
				xCoor, yCoor - r, 
				xCoor, yCoor - 3 * r, 
				xCoor + 3 * r, yCoor - 3 * r,
				xCoor + 3.4 * r, yCoor - 4 * r, 
				xCoor + 3.8 * r, yCoor - 5 * r, 
				
				xCoor + 4.2 * r, yCoor - 5 * r,
				xCoor + 4.6 * r, yCoor - 4 * r,
				xCoor + 5 * r, yCoor - 3 * r, 
				xCoor + 10 * r, yCoor - 3 * r, 
				xCoor + 10 * r, yCoor - 1 * r);
			polygon1 = new Polygon(
				(xCoor)+shiftx, (yCoor - r)+shifty, 
				(xCoor)+shiftx, (yCoor - 3 * r)+shifty, 
				(xCoor + 3 * r)+shiftx,( yCoor - 3 * r)+shifty,
				(xCoor + 3.4 * r)+shiftx,( yCoor - 4 * r)+shifty, 
				(xCoor + 3.8 * r)+shiftx,( yCoor - 5 * r)+shifty, 
				
				(xCoor + 4.2 * r)+shiftx,( yCoor - 5 * r)+shifty,
				(xCoor + 4.6 * r)+shiftx,( yCoor - 4 * r)+shifty,
				(xCoor + 5 * r)+shiftx,( yCoor - 3 * r)+shifty, 
				(xCoor + 10 * r)+shiftx,( yCoor - 3 * r)+shifty, 
				(xCoor + 10 * r)+shiftx, (yCoor - 1 * r)+shifty);
			lines.getChildren().addAll(
				new Line(xCoor, yCoor - r, (xCoor)+shiftx, (yCoor - r)+shifty),
				new Line(xCoor, yCoor - 3 * r, (xCoor)+shiftx, (yCoor - 3 * r)+shifty),
				new Line(xCoor + 3 * r, yCoor - 3 * r, (xCoor + 3 * r)+shiftx,( yCoor - 3 * r)+shifty),
				new Line(xCoor + 3.4 * r, yCoor - 4 * r, (xCoor + 3.4 * r)+shiftx,( yCoor - 4 * r)+shifty),
				new Line(xCoor + 3.8 * r, yCoor - 5 * r, (xCoor + 3.8 * r)+shiftx,( yCoor - 5 * r)+shifty),
				new Line(xCoor + 4.2 * r, yCoor - 5 * r, (xCoor + 4.2 * r)+shiftx,( yCoor - 5 * r)+shifty),
				new Line(xCoor + 4.6 * r, yCoor - 4 * r, (xCoor + 4.6 * r)+shiftx,( yCoor - 4 * r)+shifty),
				
				new Line(xCoor + 5 * r, yCoor - 3 * r, (xCoor + 5 * r)+shiftx,( yCoor - 3 * r)+shifty),
				new Line(xCoor + 10 * r, yCoor - 3 * r, (xCoor + 10 * r)+shiftx,( yCoor - 3 * r)+shifty),
				new Line(xCoor + 10 * r, yCoor - 1 * r, (xCoor + 10 * r)+shiftx, (yCoor - 1 * r)+shifty)
					);
		}break;
		default:{
			polygon = new Polygon(xCoor, yCoor - r, xCoor, yCoor - 4 * r, xCoor + 2 * r, yCoor - 4 * r, xCoor + 4 * r,
					yCoor - 6 * r, xCoor + 6 * r, yCoor - 6 * r, xCoor + 8 * r, yCoor - 4 * r, xCoor + 10 * r,
					yCoor - 4 * r, xCoor + 10 * r, yCoor - r);
		}break;
		
		
		
		}
		
		
		// Draw the car
		polygon.setFill(color);
		polygon.setStroke(Color.BLACK);
		polygon1.setFill(color);
		polygon1.setStroke(Color.BLACK);
		
		// Draw the wheels
		//Circle wheel1 = new Circle(xCoor + r * 3, yCoor - r, r, Color.BLACK);
		//Circle wheel2 = new Circle(xCoor + r * 7, yCoor - r, r, Color.BLACK);
		PhongMaterial grayMaterial = new PhongMaterial();
		grayMaterial.setDiffuseColor(Color.DARKGREY);
		grayMaterial.setSpecularColor(Color.GRAY);
		
		Sphere wheel1 = new Sphere(r*1.5);
		Sphere wheel2 = new Sphere(r*1.5);
		Sphere wheel3 = new Sphere(r*1.5);
		Sphere wheel4 = new Sphere(r*1.5);
		
		 
		wheel1.setTranslateX(xCoor + r * 3);
		wheel1.setTranslateY(yCoor - r);
		wheel1.setTranslateZ(1);
		wheel1.setMaterial(grayMaterial);
		
		wheel2.setTranslateX(xCoor + r * 7);
		wheel2.setTranslateY(yCoor - r);
		wheel2.setTranslateZ(1);
		wheel2.setMaterial(grayMaterial);
		
		wheel3.setTranslateX((xCoor + r * 3)+shiftx);
		wheel3.setTranslateY((yCoor - r)+shifty);
		wheel3.setTranslateZ(-10);
		wheel3.setMaterial(grayMaterial);
		
		wheel4.setTranslateX((xCoor + r * 7)+shiftx);
		wheel4.setTranslateY((yCoor - r)+shifty);
		wheel4.setTranslateZ(-10);
		wheel4.setMaterial(grayMaterial);
		
		getChildren().clear();
		getChildren().addAll(wheel3, wheel4,polygon1,polygon, wheel1, wheel2,lines);
	}

	public void createTimeline() {
		EventHandler<ActionEvent> eventHandler = e -> {
			moveCar(MOVE); // move car pane according to limits
		};
		tl = new Timeline();
		tl.setCycleCount(Timeline.INDEFINITE);
		KeyFrame kf = new KeyFrame(Duration.millis(50), eventHandler);
		tl.getKeyFrames().add(kf);
		//tl.play();
		//tl.stop();
	}

	public Timeline getTimeline() {
		return tl;
	}

	public void setColor(Color color) {
		this.color = color;
		if (car.getSpeed() == STOP)
			moveCar(STOP);
	}

	public void setRadius(int r) {
		this.r = r;
		if (car.getSpeed() == STOP)
			moveCar(STOP);
	}

	public void setSpeed(double speed) {
		if (speed == STOP) {
			tl.stop();
		} else {
			tl.setRate(speed);
			tl.play();
		}
	}

	public double getX() {
		return xCoor;
	}

	public double getY() {
		return yCoor;
	}
}
