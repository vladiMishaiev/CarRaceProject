package vladi_yaki_project.raceMVC;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class CarRace implements Serializable {
	private int raceCounter;
	private Car c1;
	private Car c2;
	private Car c3;
	private Car c4;
	private Car c5;
	private double[] raceResults = new double[5];

	public CarRace(int raceCounter) {
		this.raceCounter = raceCounter;
		c1 = new Car(0, raceCounter);
		c2 = new Car(1, raceCounter);
		c3 = new Car(2, raceCounter);
		c4 = new Car(3, raceCounter);
		c5 = new Car(4, raceCounter);
	}

	public void changeColor(int id, Color color) {
		getCarById(id).setColor(color);
	}

	public void changeRadius(int id, int radius) {
		getCarById(id).setRadius(radius);
	}

	public void changeSpeed(int id, double speed) {
		getCarById(id).setSpeed(speed);
		System.out.println("Speed changed "+id+"  ;"+speed);
	}

	public Car getCarById(int id) {
		switch (id) {
		case 0:
			return c1;
		case 1:
			return c2;
		case 2:
			return c3;
		case 3:
			return c4;
		case 4:
			return c5;
		}
		return null;
	}

	public int getRaceCounter() {
		return raceCounter;
	}
	public void updateResult(int carID,double speed, long interval){
		raceResults[carID]+=(speed*interval);
	}
	public void updateResults(long counter){
		updateResult(c1.getId(),c1.getSpeed(),counter);
		updateResult(c2.getId(),c2.getSpeed(),counter);
		updateResult(c3.getId(),c3.getSpeed(),counter);
		updateResult(c4.getId(),c4.getSpeed(),counter);
		updateResult(c5.getId(),c5.getSpeed(),counter);
	}
	public double[] getRaceResults() {
		return raceResults;
	}

}
