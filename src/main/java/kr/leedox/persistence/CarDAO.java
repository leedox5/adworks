package kr.leedox.persistence;

import java.util.List;

import kr.leedox.bean.Car;

public interface CarDAO {

	List<Car> findAll();

	Car findCar(Long id);

}
