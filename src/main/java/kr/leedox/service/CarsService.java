package kr.leedox.service;

import java.util.List;

import kr.leedox.bean.Car;
import kr.leedox.persistence.CarDAO;
import kr.leedox.persistence.JdbcDAO;

public class CarsService implements ICarsService {

	@Override
	public List<Car> findAllCars() {
		CarDAO carDAO = new JdbcDAO();
		return carDAO .findAll();
	}

	@Override
	public Car findCar(Long id) {
		CarDAO carDAO = new JdbcDAO();
		return carDAO.findCar(id);
	}

}
