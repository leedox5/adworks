package kr.leedox.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import kr.leedox.bean.Car;
import kr.leedox.util.DBUtils;
import kr.leedox.util.ServiceLocator;

public class JdbcDAO implements CarDAO {

	private static final String DATA_SOURCE = "jdbc/cams";
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;

	@Override
	public List<Car> findAll() {
		
		List<Car> carList = new ArrayList<>();

		execute(() -> {

			DataSource ds = ServiceLocator.getDataSource(DATA_SOURCE);
			con = ds.getConnection();
			pst = con.prepareStatement("SELECT * FROM CARS");
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				Car car = new Car();
				car.setId(rs.getLong(1));
				car.setName(rs.getString(2));
				car.setPrice(rs.getInt(3));
				carList.add(car);
			}
		});
		
		return carList;
	}

	
	private void execute(Executable executable) {
		try {
			executable.exec();
		} catch (Exception e) {
			Logger lgr = Logger.getLogger(JdbcDAO.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(pst);
			DBUtils.closeConnection(con);
		}
	}


	@Override
	public Car findCar(Long id) {
		Car car = new Car();
		
		execute(() -> {
			DataSource ds = ServiceLocator.getDataSource(DATA_SOURCE);
			con = ds.getConnection();
			pst = con.prepareStatement("SELECT * FROM CARS WHERE ID = (?) ");
			pst.setLong(1,  id);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				car.setId(rs.getLong(1));
				car.setName(rs.getString(2));
				car.setPrice(rs.getInt(3));
			}
		});
		
		return car;
	}
}
