/**
 * A driver for CS1501 Project 3
 * @author	Dr. Farnan
 */
package cs1501_p3;

public class App {
	public static void main(String[] args) {
		CarsPQ cpq = new CarsPQ("src/test/resources/cars.txt");
		Car c = new Car("5", "Ford", "Fiesta", 20, 200000, "White");
		cpq.add(c);

		Car car = cpq.getLowMileage("Hyundai", "Elantra");
		car.print();

		//cpq.preOrderTraversal();
	}
}
