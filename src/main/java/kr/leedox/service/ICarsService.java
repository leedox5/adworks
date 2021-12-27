package kr.leedox.service;

import java.util.List;

import kr.leedox.bean.Car;

public interface ICarsService {

	public List<Car> findAllCars();

	public Car findCar(Long id);

}
