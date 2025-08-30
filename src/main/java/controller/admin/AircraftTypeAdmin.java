package controller.admin;

import DAO.Admin.AircraftSeatConfigDAO;
import DAO.Admin.AircraftTypeDAO;
import DAO.Admin.AirlineDAO;
import DAO.Admin.SeatClassDAO;
import model.AircraftSeatConfig;
import model.AircraftType;
import model.Airline;
import model.SeatClass;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "AircraftTypeAdmin", urlPatterns = {"/AircraftTypeAdmin"})
public class AircraftTypeAdmin extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AircraftTypeAdmin.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "listAircraftTypes";
        }
        AircraftTypeDAO dao = new AircraftTypeDAO();
        SeatClassDAO seatClassDAO = new SeatClassDAO();
        AirlineDAO airlineDAO = new AirlineDAO();
        List<SeatClass> seatClasses;

        try {
            seatClasses = seatClassDAO.getAllSeatClasses();
            request.setAttribute("seatClasses", seatClasses);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving seat classes", e);
            response.sendRedirect("AircraftTypeAdmin?error=Failed to retrieve seat classes: " + e.getMessage());
            return;
        }

        switch (action) {
            case "listAircraftTypes":
                List<AircraftType> aircraftTypes = dao.getAllAircraftTypes();
                request.setAttribute("aircraftTypes", aircraftTypes);
                request.getRequestDispatcher("/WEB-INF/admin/aircraftTypeList.jsp").forward(request, response);
                break;
            case "showAddForm":
                try {
                    List<Airline> airlines = airlineDAO.getAllAirlines();
                    request.setAttribute("airlines", airlines);
                    request.setAttribute("action", "add");
                    request.setAttribute("seatCountMap", new HashMap<Integer, Integer>());
                    request.getRequestDispatcher("/WEB-INF/admin/addEditAircraftType.jsp").forward(request, response);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error retrieving airlines", e);
                    response.sendRedirect("AircraftTypeAdmin?error=Failed to retrieve airlines: " + e.getMessage());
                }
                break;
            case "edit":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    AircraftType aircraftType = dao.getAircraftTypeById(id);
                    List<Airline> airlines = airlineDAO.getAllAirlines();
                    request.setAttribute("aircraftType", aircraftType);
                    request.setAttribute("airlines", airlines);
                    request.setAttribute("action", "update");
                    AircraftSeatConfigDAO configDAO = new AircraftSeatConfigDAO();
                    List<AircraftSeatConfig> seatConfigs = configDAO.getSeatConfigsByAircraftTypeID(id);
                    Map<Integer, Integer> seatCountMap = new HashMap<>();
                    for (AircraftSeatConfig config : seatConfigs) {
                        seatCountMap.put(config.getSeatClassID(), config.getSeatCount());
                    }
                    request.setAttribute("seatCountMap", seatCountMap);
                    request.getRequestDispatcher("/WEB-INF/admin/addEditAircraftType.jsp").forward(request, response);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error retrieving airlines", e);
                    response.sendRedirect("AircraftTypeAdmin?error=Failed to retrieve airlines: " + e.getMessage());
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.SEVERE, "Invalid ID format", e);
                    response.sendRedirect("AircraftTypeAdmin?error=Invalid ID format");
                }
                break;
            default:
                response.sendRedirect("AircraftTypeAdmin?error=Invalid action");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        AircraftTypeDAO dao = new AircraftTypeDAO();
        AircraftSeatConfigDAO configDAO = new AircraftSeatConfigDAO();
        SeatClassDAO seatClassDAO = new SeatClassDAO();
        List<SeatClass> seatClasses;

        try {
            seatClasses = seatClassDAO.getAllSeatClasses();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving seat classes", e);
            response.sendRedirect("AircraftTypeAdmin?error=Failed to retrieve seat classes: " + e.getMessage());
            return;
        }

        try {
            AircraftType aircraftType = new AircraftType();
            aircraftType.setAircraftTypeCode(request.getParameter("aircraftTypeCode"));
            aircraftType.setAircraftTypeName(request.getParameter("aircraftTypeName"));
            aircraftType.setStatus(request.getParameter("status"));
            Airline airline = new Airline();
            airline.setAirlineId(Integer.parseInt(request.getParameter("airlineId")));
            aircraftType.setAirlineId(airline);

            if ("add".equals(action)) {
                int newId = dao.insertAircraftType(aircraftType);
                if (newId != -1) {
                    for (SeatClass sc : seatClasses) {
                        String countStr = request.getParameter("seatCount_" + sc.getSeatClassID());
                        if (countStr != null && !countStr.isEmpty()) {
                            int count = Integer.parseInt(countStr);
                            if (count > 0) {
                                AircraftSeatConfig config = new AircraftSeatConfig();
                                config.setAircraftTypeID(newId);
                                config.setSeatClassID(sc.getSeatClassID());
                                config.setSeatCount(count);
                                configDAO.insertAircraftSeatConfig(config);
                            }
                        }
                    }
                    response.sendRedirect("AircraftTypeAdmin?successcreate=1");
                } else {
                    response.sendRedirect("AircraftTypeAdmin?successcreate=0");
                }
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                aircraftType.setAircraftTypeId(id);
                boolean success = dao.updateAircraftType(aircraftType);
                if (success) {
                    configDAO.deleteByAircraftTypeID(id);
                    for (SeatClass sc : seatClasses) {
                        String countStr = request.getParameter("seatCount_" + sc.getSeatClassID());
                        if (countStr != null && !countStr.isEmpty()) {
                            int count = Integer.parseInt(countStr);
                            if (count > 0) {
                                AircraftSeatConfig config = new AircraftSeatConfig();
                                config.setAircraftTypeID(id);
                                config.setSeatClassID(sc.getSeatClassID());
                                config.setSeatCount(count);
                                configDAO.insertAircraftSeatConfig(config);
                            }
                        }
                    }
                    response.sendRedirect("AircraftTypeAdmin?successedit=1");
                } else {
                    response.sendRedirect("AircraftTypeAdmin?successedit=0");
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                if (dao.isAircraftTypeInUse(id)) {
                    response.sendRedirect("AircraftTypeAdmin?successdelete=0");
                } else {
                    configDAO.deleteByAircraftTypeID(id);
                    boolean success = dao.deleteAircraftType(id);
                    response.sendRedirect("AircraftTypeAdmin?successdelete=" + (success ? "1" : "0"));
                }
            } else {
                response.sendRedirect("AircraftTypeAdmin?error=Invalid action");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid input format", e);
            response.sendRedirect("AircraftTypeAdmin?error=Invalid input format: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            response.sendRedirect("AircraftTypeAdmin?error=Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "AircraftTypeAdmin Servlet for managing aircraft types";
    }
}