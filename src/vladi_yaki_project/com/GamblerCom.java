package vladi_yaki_project.com;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GamblerCom {
	private int ID;
	private ObjectOutputStream outPortA;
	private ObjectInputStream inPortA;
	private ObjectOutputStream outPortB;
	private ObjectInputStream inPortB;
	
	public GamblerCom (ObjectOutputStream outPortA, ObjectInputStream inPortA,
			ObjectOutputStream outPortB, ObjectInputStream inPortB){
		this.outPortA = outPortA;
		this.inPortA = inPortA;
		this.outPortB = outPortB;
		this.inPortB = inPortB;
	}

	public int getID() {
		return ID;
	}

	public void setID(int gamblerCounter) {
		ID = gamblerCounter;
	}

	public ObjectOutputStream getOutPortA() {
		return outPortA;
	}

	public void setOutPortA(ObjectOutputStream outPortA) {
		this.outPortA = outPortA;
	}

	public ObjectInputStream getInPortA() {
		return inPortA;
	}

	public void setInPortA(ObjectInputStream inPortA) {
		this.inPortA = inPortA;
	}

	public ObjectOutputStream getOutPortB() {
		return outPortB;
	}

	public void setOutPortB(ObjectOutputStream outPortB) {
		this.outPortB = outPortB;
	}

	public ObjectInputStream getInPortB() {
		return inPortB;
	}

	public void setInPortB(ObjectInputStream inPortB) {
		this.inPortB = inPortB;
	}
}
