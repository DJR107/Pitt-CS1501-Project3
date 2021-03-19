/**
 * CarsPQ specification class for CS1501 Project 3
 * @author	David Roberts
 */
package cs1501_p3;

import java.util.*;
import java.io.*;

public class CarsPQ implements CarsPQ_Inter
{
	/**
	 * Top-most node and start of the CarsPQ
	 */
	private CarNode root;

	/**
	 * Array representation of the heap
	 */
	private ArrayList<CarNode> carArray;

	/**
	 * Number of nodes in CarsPQ
	 */
	private int size;

	public CarsPQ(String fileName)
	{
		root = new CarNode(null);
		carArray = new ArrayList<CarNode>();
		size = 0;

		try
		{
			File file = new File(fileName);
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine())
			{
				String newCar = scan.nextLine();
				String[] newCarData = newCar.split(":");
				if (newCarData[0].compareTo("# VIN") != 0)
				{
					int price = Integer.parseInt(newCarData[3]);
					int mileage = Integer.parseInt(newCarData[4]);
					Car car = new Car(newCarData[0], newCarData[1], newCarData[2], price, mileage, newCarData[5]);
					try
					{
						add(car);
					}
					catch (IllegalStateException e)
					{
						System.out.println("Could not add car: "+car.getVIN());
						e.printStackTrace();
					}
				}
			}
			scan.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public CarsPQ()
	{
		root = new CarNode(null);
		carArray = new ArrayList<CarNode>();
		size = 0;
	}

	/**
	 * Add a new Car to the data structure
	 * Should throw an `IllegalStateException` if there is already car with the
	 * same VIN in the datastructure.
	 *
	 * @param 	c Car to be added to the data structure
	 */
	public void add(Car c) throws IllegalStateException
	{
		for (CarNode cn : carArray)
		{
			if (cn.getCar().getVIN().compareTo(c.getVIN()) == 0)
				throw new IllegalStateException("This VIN already here");
		}

		if (root.getCar() == null)
		{
			root = new CarNode(c);
			carArray.add(root);
			//System.out.println("Added to top: "+c.getVIN());
		}
		else
		{
			CarNode cn = new CarNode(c);
			int index = size;

			if (index % 2 == 1)
			{
				index = (index-1)/2;
				CarNode parentOfNew = carArray.get(index);
				parentOfNew.setLeft(cn);
				carArray.add(cn);
				//System.out.println("Added: "+cn.getCar().getVIN()+" to left of: "+parentOfNew.getCar().getVIN());
			}
			else if (index % 2 == 0)
			{
				index = (index-2)/2;
				CarNode parentOfNew = carArray.get(index);
				parentOfNew.setRight(cn);
				carArray.add(cn);
				//System.out.println("Added: "+cn.getCar().getVIN()+" to right of: "+parentOfNew.getCar().getVIN());
			}
		}
		size++;
	}

	/**
	 * Ensures that heap property is maintained by swapping necessary nodes, according to min value
	 * specified in function call
	 *
	 * @param curr 	current node of search
	 * @param what	minimum value that dictates the resulting order (ex: price or mileage)
	 */
	public void ensureHeapProperty(CarNode curr, String what)
	{
		int currLeftVal = 0;
		int currVal = 0;

		if (curr.getLeft() != null)
		{
			ensureHeapProperty(curr.getLeft(), what);
			if (what.compareTo("price") == 0)
			{
				currLeftVal = curr.getLeft().getCar().getPrice();
				currVal = curr.getCar().getPrice();
			}
			else if (what.compareTo("mileage") == 0)
			{
				currLeftVal = curr.getLeft().getCar().getMileage();
				currVal = curr.getCar().getMileage();
			}
			//System.out.println("Curr Value: "+curr.getCar().getPrice());
			//System.out.println("Left Value: "+curr.getLeft().getCar().getPrice());

			if (currLeftVal < currVal)
			{
				//System.out.println("Swapped");
				Car tempCar = curr.getCar();
				curr.setCar(curr.getLeft().getCar());
				curr.getLeft().setCar(tempCar);
			}
		}
		if (curr.getRight() != null)
		{
			ensureHeapProperty(curr.getRight(), what);
			//System.out.println("Curr Value: "+curr.getCar().getPrice());
			//System.out.println("Right Value: "+curr.getRight().getCar().getPrice());
			if (what.compareTo("price") == 0)
			{
				currLeftVal = curr.getRight().getCar().getPrice();
				currVal = curr.getCar().getPrice();
			}
			else if (what.compareTo("mileage") == 0)
			{
				currLeftVal = curr.getRight().getCar().getMileage();
				currVal = curr.getCar().getMileage();
			}

			if (currLeftVal < currVal)
			{
				//System.out.println("Swapped");
				Car tempCar = curr.getCar();
				curr.setCar(curr.getRight().getCar());
				curr.getRight().setCar(tempCar);
			}
		}
	}

	/**
	 * Preorder Traversal of the CarsPQ, printing all data for each car and their position in the tree
	 */
	public void preOrderTraversal()
	{
		if (root.getCar() == null)
		{
			System.out.println("CarsPQ is empty");
		}
		else
		{
			System.out.println("Root:");
			root.getCar().print();

			getKeysPOT(root);
		}
	}

	/**
	 * Recursively goes to each node checking left first and the right, adding 
	 * type of node and value to StringBuilder
	 *
	 * @param curr 	current node of search
	 */
	private void getKeysPOT(CarNode curr)
	{
		//System.out.println("Curr: "+curr.getKey());
		if (curr.getLeft() != null)
		{
			if (curr.getLeft().getLeft() == null && curr.getLeft().getRight() == null)
			{
				System.out.println("Leaf:");
				curr.getLeft().getCar().print();
			}
			else if (curr.getLeft().getLeft() != null || curr.getLeft().getRight() != null)
			{
				System.out.println("Interior:");
				curr.getLeft().getCar().print();
			}
			getKeysPOT(curr.getLeft());
		}
		if (curr.getRight() != null)
		{
			if (curr.getLeft() == null)
			{
				System.out.println("Null leaf");
				System.out.println();
			}
			if (curr.getRight().getLeft() == null && curr.getRight().getRight() == null)
			{
				System.out.println("Leaf:");
				curr.getRight().getCar().print();
			}
			else if (curr.getRight().getLeft() != null || curr.getRight().getRight() != null)
			{
				System.out.println("Interior:");
				curr.getRight().getCar().print();
			}
			getKeysPOT(curr.getRight());
		}
		if (curr.getRight() == null && curr.getLeft() != null)
		{
			System.out.println("Null leaf");
			System.out.println();
		}
	}

	/**
	 * Retrieve a new Car from the data structure
	 * Should throw a `NoSuchElementException` if there is no car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be updated
	 */
	public Car get(String vin) throws NoSuchElementException
	{
		for (CarNode cn : carArray)
		{
			if (cn.getCar().getVIN().compareTo(vin) == 0)
				return cn.getCar();
		}
		throw new NoSuchElementException("This VIN not here");
	}

	/**
	 * Update the price attribute of a given car
	 * Should throw a `NoSuchElementException` if there is no car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be updated
	 * @param	newPrice The updated price value
	 */
	public void updatePrice(String vin, int newPrice) throws NoSuchElementException
	{
		boolean notFound = true;

		for (CarNode cn : carArray)
		{
			if (cn.getCar().getVIN().compareTo(vin) == 0)
			{
				cn.getCar().setPrice(newPrice);
				notFound = false;
				break;
			}
		}

		if (notFound)
			throw new NoSuchElementException("This VIN not here");
	}

	/**
	 * Update the mileage attribute of a given car
	 * Should throw a `NoSuchElementException` if there is not car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be updated
	 * @param	newMileage The updated mileage value
	 */
	public void updateMileage(String vin, int newMileage) throws NoSuchElementException
	{
		boolean notFound = true;

		for (CarNode cn : carArray)
		{
			if (cn.getCar().getVIN().compareTo(vin) == 0)
			{
				cn.getCar().setMileage(newMileage);
				notFound = false;
				break;
			}
		}

		if (notFound)
			throw new NoSuchElementException("This VIN not here");
	}

	/**
	 * Update the color attribute of a given car
	 * Should throw a `NoSuchElementException` if there is not car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be updated
	 * @param	newColor The updated color value
	 */
	public void updateColor(String vin, String newColor) throws NoSuchElementException
	{
		boolean notFound = true;

		for (CarNode cn : carArray)
		{
			if (cn.getCar().getVIN().compareTo(vin) == 0)
			{
				cn.getCar().setColor(newColor);
				notFound = false;
				break;
			}
		}

		if (notFound)
			throw new NoSuchElementException("This VIN not here");
	}

	/**
	 * Remove a car from the data structure
	 * Should throw a `NoSuchElementException` if there is not car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be removed
	 */
	public void remove(String vin) throws NoSuchElementException
	{
		boolean notFound = true;

		for (CarNode cn : carArray)
		{
			if (cn.getCar().getVIN().compareTo(vin) == 0)
			{
				int index = size-1;
				
				CarNode carToReplace = carArray.get(index);
				cn.setCar(carToReplace.getCar());

				if (index % 2 == 1)
				{
					index = (index-1)/2;
					CarNode parent = carArray.get(index);
					parent.setLeft(null);
				}
				else if (index % 2 == 0)
				{
					index = (index-2)/2;
					CarNode parent = carArray.get(index);
					parent.setRight(null);
				}

				carArray.remove(cn);
				notFound = false;
				break;
			}
		}
		if (notFound)
			throw new NoSuchElementException("This VIN not here");
	}

	/**
	 * Get the lowest priced car (across all makes and models)
	 * Should return `null` if the data structure is empty
	 *
	 * @return	Car object representing the lowest priced car
	 */
	public Car getLowPrice()
	{
		if (root.getCar() == null)
		{
			return null;
		}
		else
		{
			ensureHeapProperty(root, "price");
			return root.getCar();
		}
	}

	/**
	 * Get the lowest priced car of a given make and model
	 * Should return `null` if the data structure is empty
	 *
	 * @param	make The specified make
	 * @param	model The specified model
	 * 
	 * @return	Car object representing the lowest priced car
	 */
	public Car getLowPrice(String make, String model)
	{
		if (root.getCar() == null)
		{
			return null;
		}
		else
		{
			CarsPQ newPQ = new CarsPQ();
			for (CarNode cn : carArray)
			{
				if (cn.getCar().getMake().compareTo(make) == 0 && cn.getCar().getModel().compareTo(model) == 0)
				{
					newPQ.add(cn.getCar());
				}
			}
			newPQ.ensureHeapProperty(newPQ.getRoot(), "price");
			return newPQ.getRoot().getCar();
		}
	}

	/**
	 * Get the car with the lowest mileage (across all makes and models)
	 * Should return `null` if the data structure is empty
	 *
	 * @return	Car object representing the lowest mileage car
	 */
	public Car getLowMileage()
	{
		if (root.getCar() == null)
		{
			return null;
		}
		else
		{
			ensureHeapProperty(root, "mileage");
			return root.getCar();
		}
	}

	/**
	 * Get the car with the lowest mileage of a given make and model
	 * Should return `null` if the data structure is empty
	 *
	 * @param	make The specified make
	 * @param	model The specified model
	 *
	 * @return	Car object representing the lowest mileage car
	 */
	public Car getLowMileage(String make, String model)
	{
		if (root.getCar() == null)
		{
			return null;
		}
		else
		{
			CarsPQ newPQ = new CarsPQ();
			for (CarNode cn : carArray)
			{
				if (cn.getCar().getMake().compareTo(make) == 0 && cn.getCar().getModel().compareTo(model) == 0)
				{
					newPQ.add(cn.getCar());
				}
			}
			newPQ.ensureHeapProperty(newPQ.getRoot(), "mileage");
			return newPQ.getRoot().getCar();
		}
	}

	/**
	 * Returns root of current CarsPQ
	 * 
	 * @return root of CarsPQ
	 */
	public CarNode getRoot()
	{
		return root;
	}
}