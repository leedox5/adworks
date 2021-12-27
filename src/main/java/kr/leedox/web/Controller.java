package kr.leedox.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.leedox.service.CarsService;
import kr.leedox.service.ICarsService;
import kr.leedox.util.ValidateParameter;

@WebServlet(name = "Controller", urlPatterns = {"/controller"})
public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String ACTION_KEY = "action";

	private static final String LIST_CARS_ACTION = "listcars";
	private static final String READ_CAR_BY_ID_ACTION = "readbyid";
	private static final String VIEW_CAR_ACTION = "viewcar";
	
	private static final String UNKNOWN_VIEW = "unknown.jsp";

	private static final String ALL_CARS_VIEW = "allCars.jsp";
	private static final String READ_CAR_BY_ID_VIEW = "readCarId.jsp";
	private static final String SHOW_CAR_VIEW = "showCar.jsp";
	private static final String READ_CAR_VIEW = "readCar.jsp";



	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String actionName = req.getParameter(ACTION_KEY);
		String page = UNKNOWN_VIEW;
		
		if (LIST_CARS_ACTION.equals(actionName )) {
			ICarsService service = new CarsService();
			req.setAttribute("carList", service.findAllCars());
			page = ALL_CARS_VIEW;
		}
		
		if (READ_CAR_BY_ID_ACTION.equals(actionName)) {
			page = READ_CAR_BY_ID_VIEW;
		}
		
		if (VIEW_CAR_ACTION.equals(actionName)) {
			String sid = req.getParameter("carId");
			
			if (ValidateParameter.validateId(sid)) {
				ICarsService service = new CarsService();
				Long carId = Long.valueOf(sid);
				req.setAttribute("returnedCar", service.findCar(carId));
			}
			page = SHOW_CAR_VIEW;
		}
		
		RequestDispatcher disp = getServletContext().getRequestDispatcher("/" + page);
		disp.forward(req, resp);
	}
	
}
