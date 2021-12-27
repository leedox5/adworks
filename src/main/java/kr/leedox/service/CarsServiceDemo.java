package kr.leedox.service;

import java.util.ArrayList;
import java.util.List;

import kr.leedox.bean.Car;

public class CarsServiceDemo implements ICarsService {

	@Override
	public List<Car> findAllCars() {
		List<Car> cars = new ArrayList<Car>();
		
		cars.add(new Car(1L, "Sonata", 3000));
		cars.add(new Car(2L, "Audi", 4000));
		cars.add(new Car(3L, "Volvo", 2000));
		
		return cars;
	}

	@Override
	public Car findCar(Long carId) {
		// TODO Auto-generated method stub
		return null;
	}

}
