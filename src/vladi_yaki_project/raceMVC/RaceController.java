package vladi_yaki_project.raceMVC;
import java.util.concurrent.ThreadLocalRandom;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RaceController {
	private final int MAXSPEED = 50;
	private final int MINSPEED = 5;
	private final int CAR1_ID = 0;
	private final int CAR2_ID = 1;
	private final int CAR3_ID = 2;
	private final int CAR4_ID = 3;
	private final int CAR5_ID = 4;
	private Stage stg;
	private CarRace model;
	private RaceView view;
	private Color colors[] = { Color.RED, Color.AQUA, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK,
			Color.VIOLET, Color.WHITE, Color.TRANSPARENT };

	public RaceController(CarRace model, RaceView view) {
		this.model = model;
		this.view = view;
		speedHandlers();
		// handle radius change
		Slider radSlider = view.getRadSlider();
		radSlider.valueProperty().addListener(e -> {
			int car_index = view.getItemsCar().indexOf(view.getCarIdComBox().getValue());
			int oldRad = model.getCarById(car_index).getRadius();
			int newRad = (int) radSlider.getValue();
			if (oldRad != newRad)
				model.changeRadius(car_index, newRad);
		});
		// handle color change
		Button btn = view.getColorButton();
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				changeColorView();
			}
		});
		// make the slider's value suitable to the selected car in the combo box
		view.getCarIdComBox().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int car_index = view.getItemsCar().indexOf(view.getCarIdComBox().getValue());
				int r = model.getCarById(car_index).getRadius();
				view.getRadSlider().setValue(r);
			}
		});
	}

	public void changeColorView() {
		int car_index = view.getItemsCar().indexOf(view.getCarIdComBox().getValue());
		int color_index = view.getItemsColor().indexOf(view.getColorComBox().getValue());
		model.changeColor(car_index, colors[color_index]);
	}

	public void setSpeedModelView(TextField tf, int n) {
		System.out.println("Got here to change speed");
		String msg = null;
		try {
			if (!tf.getText().equals("")) {
				Double speed = Double.parseDouble(tf.getText());
				if (0 <= speed && speed <= MAXSPEED) {
					model.changeSpeed(n, speed);
				} else if (speed > MAXSPEED) {
					msg = "You're driving too fast!!! Speed above " + MAXSPEED + "!!!";
				} else {
					msg = "Only Numbers Great or Equals 0 ";
				}
			}
		} catch (Exception e) {
			msg = "Only Numbers Great or Equals 0 ";
		}
		if (msg != null)
			try {
				errorAlert(msg);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
	}
	public void setSpeedModelView(double speed, int n) {
		String msg = null;
		try {
			//if (!tf.getText().equals("")) {
				//Double speed = Double.parseDouble(tf.getText());
				//double speed = tf;
				if (0 <= speed && speed <= MAXSPEED) {
					model.changeSpeed(n, speed);
				} else if (speed > MAXSPEED) {
					msg = "You're driving too fast!!! Speed above " + MAXSPEED + "!!!";
				} else {
					msg = "Only Numbers Great or Equals 0 ";
				}
			//}
		} catch (Exception e) {
			msg = "Only Numbers Great or Equals 0 ";
		}
		if (msg != null)
			try {
				errorAlert(msg);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
	}
	public void generateCars(){
		//set type
		//set size - small(5) medium(7) large(10)
	
	}
	//generate speed
	public void generateCarsSpeed(){
		setSpeedModelView(ThreadLocalRandom.current().nextDouble(MINSPEED,MAXSPEED),0);
		setSpeedModelView(ThreadLocalRandom.current().nextDouble(MINSPEED,MAXSPEED),1);
		setSpeedModelView(ThreadLocalRandom.current().nextDouble(MINSPEED,MAXSPEED),2);
		setSpeedModelView(ThreadLocalRandom.current().nextDouble(MINSPEED,MAXSPEED),3);
		setSpeedModelView(ThreadLocalRandom.current().nextDouble(MINSPEED,MAXSPEED),4);
	}
	//stop cars
	public void stopRace(){
		
		setSpeedModelView(0,0);
		setSpeedModelView(0,1);
		setSpeedModelView(0,2);
		setSpeedModelView(0,3);
		setSpeedModelView(0,4);
	}
	
	public void speedHandlers() {
		TextField tf1 = view.getSpeedTxt1();
		tf1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setSpeedModelView(tf1, CAR1_ID);
			}
		});
		TextField tf2 = view.getSpeedTxt2();
		tf2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { 
													
				setSpeedModelView(tf2, CAR2_ID);
			}
		});
		TextField tf3 = view.getSpeedTxt3();
		tf3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { 
													
				setSpeedModelView(tf3, CAR3_ID);
			}
		});
		//add more
	}

	public void setOwnerStage(Stage stg) {
		this.stg = stg;
	}

	public void errorAlert(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(stg);
		alert.setTitle("Error");
		alert.setContentText(msg);
		alert.show();
	}
}
