/**
 * CarsPQ specification class for CS1501 Project 3
 * @author	David Roberts
 */
package cs1501_p3;

import java.util.*;
import java.io.*;

public class CarsPQprice
{
	/**
	 * Array representation of the heap
	 */
	private Car[] carArray;

	/**
	 * Number of nodes in CarsPQ
	 */
	private int size;

	/**
	 * HashTable for VINs of cars
	 */
	private Hashtable<String, Integer> hashed;

	public CarsPQprice()
	{
		carArray = new Car[15];
		size = 0;
		hashed = new Hashtable<String, Integer>();
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

		try
		{
			int a = get(c.getVIN());
			if (a < -1)
				throw new IllegalStateException("This VIN already here");
		}
		catch (NoSuchElementException ignored) {}

		hashed.put(c.getVIN(), size);
		carArray[size] = c;

		int index = size;
		while (index > 0)
		{
			if (index % 2 == 1)
			{
				index = (index-1)/2;
				Car parent = carArray[index];
				if (c.getPrice() < parent.getPrice())
				{
					carArray[(2*index)+1] = parent;
					hashed.put(parent.getVIN(), (2*index)+1);
				}
				else
				{
					carArray[(2*index)+1] = c;
					hashed.put(c.getVIN(), (2*index)+1);
					break;
				}
			}
			else if (index % 2 == 0)
			{
				index = (index-2)/2;
				Car parent = carArray[index];
				if (c.getPrice() < parent.getPrice())
				{
					carArray[(2*index)+2] = parent;
					hashed.put(parent.getVIN(), (2*index)+2);
				}
				else
				{
					carArray[(2*index)+2] = c;
					hashed.put(c.getVIN(), (2*index)+2);
					break;
				}
			}
			if (index == 0)
			{
				carArray[index] = c;
				hashed.put(c.getVIN(), index);
			}
		}
		size++;
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
		try
		{
			int a = hashed.get(vin);
			return a;
		}
		catch (NullPointerException e)
		{
			throw new NoSuchElementException("This VIN not here");		
		}
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
		int index = get(vin);
		Car carToUpdate = carArray[index];

		carToUpdate.setPrice(newPrice);

		while (index > 0)
		{
			if (index % 2 == 1)
			{
				index = (index-1)/2;
				Car parent = carArray[index];
				if (carToUpdate.getPrice() < parent.getPrice())
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
				if (carToUpdate.getPrice() < parent.getPrice())
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
					if (carArray[index].getPrice() < carArray[(2*index)+1].getPrice() && carArray[index].getPrice() < carArray[(2*index)+2].getPrice())
						break;

					if (carArray[(2*index)+1].getPrice() < carArray[(2*index)+2].getPrice())
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
					if (carArray[index].getPrice() > carArray[(2*index)+1].getPrice())
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
	 * Update the mileage attribute of a given car
	 * Should throw a `NoSuchElementException` if there is not car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be updated
	 * @param	newMileage The updated mileage value
	 */
	public void updateMileage(String vin, int newMileage) throws NoSuchElementException
	{
		Car car = carArray[get(vin)];

		car.setMileage(newMileage);
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
		hashed.remove(vin);

		try
		{
			while (carArray[(2*index)+1] != null)
			{
				if (carArray[(2*index)+2] != null)
				{
					if (carArray[index].getPrice() < carArray[(2*index)+1].getPrice() && carArray[index].getPrice() < carArray[(2*index)+2].getPrice())
						break;

					if (carArray[(2*index)+1].getPrice() < carArray[(2*index)+2].getPrice())
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
					if (carArray[index].getPrice() > carArray[(2*index)+1].getPrice())
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
		hashed.put(nodeToSwap.getVIN(), (2*index)+1);
		carArray[index] = tempCar;
		hashed.put(tempCar.getVIN(), index);
	}

	private void swapRight(Car nodeToSwap, int index)
	{
		Car tempCar = carArray[(2*index)+2];
		carArray[(2*index)+2] = nodeToSwap;
		hashed.put(nodeToSwap.getVIN(), (2*index)+2);
		carArray[index] = tempCar;
		hashed.put(tempCar.getVIN(), index);
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
			else
				break;
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