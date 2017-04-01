package com.cg.assetMgmt.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cg.assetMgmt.dtos.Asset;
import com.cg.assetMgmt.dtos.AssetReport;
import com.cg.assetMgmt.dtos.Request;
import com.cg.assetMgmt.exception.AssetException;
import com.cg.assetMgmt.service.AssetServiceImpl;
import com.cg.assetMgmt.service.IAssetService;

@WebServlet("*.do")
public class ControllerMain extends HttpServlet {

	private static final long serialVersionUID = 1L;
	IAssetService service;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws SQLException,
			ServletException, IOException {

		service = new AssetServiceImpl();

		String path = request.getServletPath();
		System.out.println(path);

		if (path.equals("/login.do")) {
			RequestDispatcher req = request.getRequestDispatcher("/login.jsp");
			req.forward(request, response);
		}
		// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (path.equals("/authenticate.do")) {
			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
			System.out.println(userName);
			System.out.println(password);

			RequestDispatcher dispatch = null;

			String nextJsp = null;
			String message = null;

			try {
				int isAuthenticated = service.authenticate(userName, password);
				System.out.println(isAuthenticated);
				if (isAuthenticated == 1) {
					System.out.println(" admin authenticated successfully");
					request.setAttribute("userName", userName);
					nextJsp = "adminHomePage.jsp";

				} else if (isAuthenticated == 2) {
					System.out.println(" Manager authenticated successfully");
					int mgrId = service.getEmployeeNoFromUserName(userName);
					
					HttpSession session= request.getSession(true);
					session.setAttribute("mgrId" , mgrId);
					System.out.println(mgrId);
					request.setAttribute("userName", userName);
					nextJsp = "managerHomePage.jsp";
					// Bill bill=service.getBillDetails(userName);
					// request.setAttribute("user",userName);
					// request.setAttribute("bill",bill);
				} else {

					nextJsp = "login.jsp";
					message = "Wrong Credentials. Enter Again.";
					request.setAttribute("errorMsg", message); // used to pass
																// data to jsp

				}
			} catch (Exception e) {
				// e.printStackTrace();
				message = "Username does not exist";
				request.setAttribute("errorMsg", message);
				nextJsp = "error1.jsp";
			}

			dispatch = request.getRequestDispatcher(nextJsp);
			dispatch.forward(request, response);

		}

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (path.equals("/showAssets.do")) {

			List<Asset> myList = service.getAssetDetailsListAdmin();
			RequestDispatcher req = request
					.getRequestDispatcher("/showAssets.jsp");
			request.setAttribute("asset", myList);
			try {
				req.forward(request, response);

			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		///////////////////////////////////////////////////////////////////////////////////
		if (path.equals("/showReport.do")) {
			List<AssetReport> myList=service.getAssetReport();
			List<Asset> list=service.getAssetDetailsListAvailable();
			RequestDispatcher req = request
					.getRequestDispatcher("/report.jsp");
			request.setAttribute("report", myList);
			request.setAttribute("avail",list );
			req.forward(request, response);
			
			
		}
		
		if (path.equals("/generateReport.do")) {
			List<AssetReport> myList=service.getAssetReport();
			List<Asset> list=service.getAssetDetailsListAvailable();
			RequestDispatcher req = request
					.getRequestDispatcher("/assetreport.jsp");
			request.setAttribute("report", myList);
			request.setAttribute("avail",list );
			req.forward(request, response);
			
			
		}
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (path.equals("/adminModify.do")) { // admin add/modify form request
			String data = request.getQueryString();
			System.out.println(data + "  before");
			String data1 = data.substring(3, data.length());
			int assetId = Integer.parseInt(data1);
			System.out.println("assetId  " + assetId);
			Asset asset = service.getAssetDetails(assetId);
			System.out.println(asset);
			RequestDispatcher req = request
					.getRequestDispatcher("/modifyAssetForm.jsp");
			request.setAttribute("asset", asset);
			req.forward(request, response);
		}

		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		if (path.equals("/adminUpdateConfirm.do")) { // //admin add/modify
														// confirm change
														// request
			String data = request.getQueryString();
			System.out.println(data + "  before");
			String data1 = data.substring(3, data.length());
			int assetId = Integer.parseInt(data1);

			String addedqty = request.getParameter("addedquantity");
			System.out.println(addedqty);
			int quantityToAdd = Integer.parseInt(addedqty);
			boolean boo = service.updateAssetAdd(assetId, quantityToAdd);
			if (boo == true) {
				System.out.println("Asset quantity updated successfully");
				RequestDispatcher req = request
						.getRequestDispatcher("/showAssets.do");
				req.forward(request, response);

			}
		}

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		if (path.equals("/delete.do")) {

			String data = request.getQueryString();
			System.out.println(data + "  before");
			String data1 = data.substring(3, data.length());
			int assetId = Integer.parseInt(data1);
			boolean boo = service.removeAsset(assetId);
			if (boo == true) {
				System.out.println("Asset deleted successfully");
				RequestDispatcher req = request
						.getRequestDispatcher("/showAssets.do");
				req.forward(request, response);
			} else {
				System.out.println("deletion failed");
			}

		}

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (path.equals("/showPendingRequests.do")) {		 // request list seen by admin
			List<Request> pendingRequestList =  service.getRequestsPendingList();
			System.out.println("here");
			System.out.println(pendingRequestList);
			RequestDispatcher req = request.getRequestDispatcher("/pendingRequestList.jsp");
			request.setAttribute("requests", pendingRequestList);
			req.forward(request, response);
		
		}

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		if (path.equals("/addNewAsset.do")) {

			String assetName = request.getParameter("astname");
			String assetDesc = request.getParameter("astdesc");
			String qty = request.getParameter("aqty");
			int quantity = Integer.parseInt(qty);
			// System.out.println(quantity);

			Asset asset = new Asset(0, assetName, assetDesc, quantity, "status");
			boolean boo = service.addAsset(asset);
			if (boo == true) {
				System.out.println("New asset added successfully");
				RequestDispatcher req = request
						.getRequestDispatcher("/showAssets.do");
				req.forward(request, response);
			} else {
				System.out.println("Asset addition failed");
			}
		}

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		if (path.equals("/rejectRequest.do")){     //admin rejects request
			
			String data = request.getQueryString();
			System.out.println(data + "  before");
			String data1 = data.substring(3, data.length());
			int requestId = Integer.parseInt(data1);
			System.out.println("after substring "+requestId);
			boolean boo = service.rejectRequest(requestId);
			if (boo == true){
				System.out.println("Request reject successfully");
			}
			else{
				System.out.println("Failed to deny request ");
			}
		
			RequestDispatcher req = request.getRequestDispatcher("/showPendingRequests.do");
			req.forward(request, response);	
		}

		
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

			if (path.equals("/approveRequest.do")){   //admin approves request
				
				String data = request.getQueryString();
				System.out.println(data + "  before");
				String data1 = data.substring(3, data.length());
				int requestId = Integer.parseInt(data1);
				System.out.println("after substring "+requestId);
				boolean boo = service.approveRequest(requestId);
				if (boo == true){
					System.out.println("Request approved successfully");
				}
				else{
					System.out.println("Failed to approve request ");
				}
			
				RequestDispatcher req = request.getRequestDispatcher("/showPendingRequests.do");
				req.forward(request, response);	
			}
		
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		if (path.equals("/addRequest.do")) {
			
			try {
				int mgrId= Integer.parseInt(request.getParameter("mgrId"));
				String empName= request.getParameter("empName");
				int empId= service.getEmpNo(empName);
				String assetName= request.getParameter("assetName");
				int assetId= service.getAssetId(assetName);
				System.out.println(assetId +"  assetId generate while adding request");
				String reqDate= request.getParameter("reqDate");
				
				java.util.Date date;
				
					date = new SimpleDateFormat("dd-MM-yyyy").parse(reqDate);
					java.sql.Date sqlDate = new java.sql.Date(date.getTime());
					int reqDays=Integer.parseInt(request.getParameter("reqDays"));
					
					Request req= new Request();
					req.setMgrId(mgrId);
					req.setEmpNo(empId);
					req.setAssetId(assetId);
					req.setRequestDate(sqlDate);
					req.setRequestForDays(reqDays);
					req.setStatus("Pending");
					//System.out.println(service.addRequest(req));
					if(service.addRequest(req) == 1){
					System.out.println("Request successful");
					request.setAttribute("requestStatus", "Request successful");

					
					}
					else{
						System.out.println("Request Failed");
						request.setAttribute("requestStatus", "Request Failed");

					}
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AssetException e) {
			 	// TODO Auto-generated catch block
			 	System.out.println(e.getMessage());
			  }	
			RequestDispatcher dispatch = request.getRequestDispatcher("managerHomePage.jsp");
			dispatch.forward(request, response);
		}
		
	}

}
