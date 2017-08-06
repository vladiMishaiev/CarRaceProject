package vladi_yaki_project.domain;
import java.io.Serializable;

public class Gambler implements Serializable {
	private int ID;
	private String name;
	private String password;
	private double credit;
	private double balance;
	
	public Gambler(int iD, String name, String password, double credit) {
		ID = iD;
		this.name = name;
		this.password = password;
		this.credit = credit;
		this.setBalance(0);
	}

	public void depoit(){
		//future
	}
	public void withdraw(){
		//future
	}
	/**
	 * Place a new bet - gamblers balance is updated
	 * @param double sum - bet sum amount
	 * @return true if success
	 */
	public boolean placeBet(double betSum){
		setCredit(getCredit()-betSum);
		this.setBalance(this.getBalance() + betSum);
		return true;
	}
	
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}
	@Override
	public String toString() {
		return "Gambler [ID=" + ID + ", name=" + name + ", password=" + password + ", credit=" + credit + "]";
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	
}
