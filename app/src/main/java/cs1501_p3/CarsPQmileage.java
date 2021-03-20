/**
 * CarsPQ specification class for CS1501 Project 3
 * @author	David Roberts
 */
package cs1501_p3;

import java.util.*;
import java.io.*;

public class CarsPQmileage
{
	/**
	 * Array representation of the heap
	 */
	private Car[] carArray;

	/**
	 * Number of nodes in CarsPQ
	 */
	private int size;

	public CarsPQmileage()
	{
		carArray = new Car[15];
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
		if (size == carArray.length)
			carArray = resizeArr();

		for (int i=0; i<size; i++)
		{
			if (carArray[i].getVIN().compareTo(c.getVIN()) == 0)
			{
				throw new IllegalStateException("This VIN already here");
			}
		}

		if (carArray[0] == null)
		{
			carArray[0] = c;
		}
		else
		{
			int index = size;

			while (index > 0)
			{
				//System.out.println("index: "+index);
				if (index % 2 == 1)
				{
					index = (index-1)/2;
					//System.out.println("New index: "+index);
					Car parent = carArray[index];
					//System.out.println("Parent Price: "+parent.getCar().getMileage())''

					if (c.getMileage() < parent.getMileage())
					{
						//System.out.println("Needs Swapping");
						carArray[(2*index)+1] = parent;
						//System.out.println("New Left Price: "+parent.getLeft().getCar().getMileage());
					}
					else
					{
						//System.out.println("No Swapping Needed");
						carArray[(2*index)+1] = c;
						//System.out.println("New Left Price: "+parent.getLeft().getCar().getMileage());
						break;
					}
				}
				else if (index % 2 == 0)
				{
					index = (index-2)/2;
					//System.out.println("New index: "+index);
					Car parent = carArray[index];
					//System.out.println("Parent Price: "+parent.getCar().getMileage());

					if (c.getMileage() < parent.getMileage())
					{
						//System.out.println("Needs Swapping");
						carArray[(2*index)+2] = parent;
						//System.out.println("New Right Price: "+parent.getRight().getCar().getMileage());
					}
					else
					{
						//System.out.println("No Swapping Needed");
						carArray[(2*index)+2] = c;
						//System.out.println("New Right Price: "+parent.getRight().getCar().getMileage());
						break;
					}
				}
				if (index == 0)
				{
					//System.out.println("Changed Root");
					carArray[index] = c;
					//System.out.println("New Root Price: "+root.getCar().getMileage());
				}
			}
		}
		size++;
		//System.out.println("Done Adding");
		//System.out.println();
	}

	/**
	 * Retrieve a new Car from the data structure
	 * Should throw a `NoSuchElementException` if there is no car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be updated
	 */
	public int get(String vin) throws NoSuchElementException
	{
		for (int i=0; i<size; i++)
		{
			if (carArray[i].getVIN().compareTo(vin) == 0)
			{
				return i;
			}
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
		Car car = carArray[get(vin)];

		car.setPrice(newPrice);
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
		int index = get(vin);
		Car carToUpdate = carArray[index];

		carToUpdate.setMileage(newMileage);

		while (index > 0)
		{
			if (index % 2 == 1)
			{
				index = (index-1)/2;
				Car parent = carArray[index];
				if (carToUpdate.getMileage() < parent.getMileage())
				{	
					swapLeft(parent, index);
					carToUpdate = carArray[index];
				}
				else
					break;
			}
			else if (index % 2 == 0)
			{
				index = (index-2)/2;
				Car parent = carArray[index];
				if (carToUpdate.getMileage() < parent.getMileage())
				{	
					swapRight(parent, index);
					carToUpdate = carArray[index];
				}
				else
					break;
			}
		}

		try
		{
			while (carArray[(2*index)+1] != null)
			{
				if (carArray[(2*index)+2] != null)
				{
					if (carArray[index].getMileage() < carArray[(2*index)+1].getMileage() && carArray[index].getMileage() < carArray[(2*index)+2].getMileage())
						break;

					if (carArray[(2*index)+1].getMileage() < carArray[(2*index)+2].getMileage())
					{
						swapLeft(carToUpdate, index);

						index = (2*index)+1;
						carToUpdate = carArray[index];					
					}
					else
					{
						swapRight(carToUpdate, index);

						index = (2*index)+2;
						carToUpdate = carArray[index];	
					}
				}
				else
				{
					if (carArray[index].getMileage() > carArray[(2*index)+1].getMileage())
					{
						swapLeft(carToUpdate, index);

						index = (2*index)+1;
						carToUpdate = carArray[index];
					}
					else
						break;
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException ignored) {}
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
		Car car = carArray[get(vin)];

		car.setColor(newColor);
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
		Car last = carArray[size-1];
		carArray[size-1] = null;

		int index = get(vin);

		if (index != size-1)
		{
			carArray[index] = last;
		}

		Car carToRemove = carArray[index];

		try
		{
			while (carArray[(2*index)+1] != null)
			{
				if (carArray[(2*index)+2] != null)
				{
					if (carArray[index].getMileage() < carArray[(2*index)+1].getMileage() && carArray[index].getMileage() < carArray[(2*index)+2].getMileage())
						break;

					if (carArray[(2*index)+1].getMileage() < carArray[(2*index)+2].getMileage())
					{
						swapLeft(carToRemove, index);

						index = (2*index)+1;
						carToRemove = carArray[index];					
					}
					else
					{
						swapRight(carToRemove, index);

						index = (2*index)+2;
						carToRemove = carArray[index];	
					}
				}
				else
				{
					if (carArray[index].getMileage() > carArray[(2*index)+1].getMileage())
					{
						swapLeft(carToRemove, index);

						index = (2*index)+1;
						carToRemove = carArray[index];
					}
					else
						break;
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException ignored) {}
		size--;
	}

	/**
	 * Returns root of current CarsPQ
	 * 
	 * @return root of CarsPQ
	 */
	public Car getRoot()
	{
		return carArray[0];
	}

	private void swapLeft(Car nodeToSwap, int index)
	{
		Car tempCar = carArray[(2*index)+1];
		carArray[(2*index)+1] = nodeToSwap;
		carArray[index] = tempCar;
	}

	private void swapRight(Car nodeToSwap, int index)
	{
		Car tempCar = carArray[(2*index)+2];
		carArray[(2*index)+2] = nodeToSwap;
		carArray[index] = tempCar;
	}

	public Car getAtIndex(int i)
	{
		return carArray[i];
	}

	public void print()
	{
		for (int i=0; i<carArray.length; i++)
		{
			if (carArray[i] != null)
				carArray[i].print();
		}
	}

	private Car[] resizeArr()
	{
		Car[] newArr = new Car[carArray.length*2];

		for (int i=0; i<carArray.length; i++)
		{
			if (carArray[i] != null)
				newArr[i] = carArray[i];
		}
		return newArr;
	}
}