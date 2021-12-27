package kr.leedox.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import kr.leedox.bean.Car;

public class CarsServiceTest {

	@Test
	public void finAllCarsTest() {
		ICarsService service = new CarsServiceDemo();
		List<Car> cars = service.findAllCars();
		assertNotNull(cars);
	}

}
