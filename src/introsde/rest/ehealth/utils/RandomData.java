package introsde.rest.ehealth.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class RandomData {
	
	private String[] firstname = {"Nam", "Bac", "Thu", "Phuong", "Tien", "Tho", "Diep", "Long", "Linh", "Hung", "Thai", "Ba", "Giang"}; 
	private String[] lastname = {"Nguyen", "Pham", "Phan", "Vu", "Dinh", "Le", "Dao", "Ly", "Trinh"}; 
	
	public String getTimestamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String created = dateFormat.format(new Date());
		return created.toString();
	}
	
	public String getRandomUsername(String firstname, String lastname) {
		String username = firstname.toLowerCase() + "." + lastname.toLowerCase();
		return username;
	}
	
	public String getRandomEmail(String username) {
		return username + "@gmail.com";
	}
	
	public double getRandomHeight() {
		double minX = 1.60;
		double maxX = 2.20;
		Random rand = new Random();
		double finalX = rand.nextDouble() * (maxX - minX) + minX;
		finalX = Double.parseDouble(new DecimalFormat("##.##").format(finalX));
		return finalX;
	}
	
	public double getRandomWeight() {
		double minX = 50;
		double maxX = 150;
		Random rand = new Random();
		double finalX = rand.nextDouble() * (maxX - minX) + minX;
		finalX = Double.parseDouble(new DecimalFormat("##.##").format(finalX));
		return finalX;
	}
	
	public String getRandomFirstName() {
		int fnNumber = randBetween(0, (firstname.length - 1));
		return firstname[fnNumber];
	}
	
	public String getRandomLastName() {
		int lnNumber = randBetween(0, (lastname.length - 1));
		return lastname[lnNumber];
	}
	
	public String getRandomTimestamp() {
        GregorianCalendar gc = new GregorianCalendar();
        
        int year = randBetween(2000, 2015);
        gc.set(gc.YEAR, year);
        
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        
        return gc.get(gc.YEAR) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.DAY_OF_MONTH);
    }
	
	public String getRandomBirthdate() {
        GregorianCalendar gc = new GregorianCalendar();
        
        int year = randBetween(1960, 2000);
        gc.set(gc.YEAR, year);
        
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        
        return gc.get(gc.YEAR) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.DAY_OF_MONTH);
    }

    public int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}
