package trainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import RCPackage.ReservationChart;

public class TrainObj {
	int trainNo;
	int now=0;
	int racno=0;
	SeatMat a1,a2,a3,fc,cc,sl,un,s2;
	int nos; //no of stations
	synchronized void setDetails(String trainName,String daysRun,int trainNo,int nos,int stationIdInd[],String arrDeptSt[][],int trainCoaches[])
	{
		this.nos=nos;
		this.trainNo=trainNo;
		try
		{
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
			String query = " insert into train"+ " values (?, ?, ?, ?, ?, ?)";	
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setString (1,trainName);
			preparedStmt.setInt (2,trainNo);
			preparedStmt.setString (3,daysRun);
			preparedStmt.setTime (1,null);
			preparedStmt.setTime (1,null);
			preparedStmt.setTime (1,null);
			preparedStmt.execute();
			con.close();
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
			for(int i=0;i<nos;i++)
			{
				query = " insert into routes"+ " values (?, ?, ?, ?, ?)";
				preparedStmt = con.prepareStatement(query);
				preparedStmt.setInt (1,trainNo);
				preparedStmt.setInt (2,stationIdInd[i]);
				preparedStmt.setInt (3,i);
				SimpleDateFormat format = new SimpleDateFormat("hh:mm a");//"hh:mm a"
				java.util.Date d1 =(java.util.Date)format.parse(arrDeptSt[i][0]);
                java.sql.Time timej = new java.sql.Time(d1.getTime());
                preparedStmt.setTime (4,timej);
                d1 =(java.util.Date)format.parse(arrDeptSt[i][1]);
                timej = new java.sql.Time(d1.getTime());
				preparedStmt.setTime (5,timej);
				preparedStmt.execute();
			}		
			query = " insert into train_coaches"+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStmt = con.prepareStatement(query);
			for(int i=0;i<nos;i++)
			{
				preparedStmt.setInt (i+1,trainCoaches[i]);
			}	
			preparedStmt.execute();
			con.close();
			
			
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}
		constructSeatMatrix(trainCoaches);
		
	}
	TrainObj(String trainName,String daysRun,int trainNo,int nos,int stationIdInd[],String arrDeptSt[][],int trainCoaches[])
	{
		setDetails(trainName,daysRun,trainNo,nos,stationIdInd,arrDeptSt,trainCoaches);
	}
	void constructSeatMatrix(int trainCoaches[])
	{
		try
		{
			//Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
			//Statement stmt=con.createStatement(); 
			//ResultSet rs=stmt.executeQuery("select * from train_coaches where train_no="+trainNo);
			
			int a1no=trainCoaches[0];
			int a2no=trainCoaches[1];
			int a3no=trainCoaches[3];
			int fcno=trainCoaches[2];
			int ccno=trainCoaches[4];
			int slno=trainCoaches[5];
			int unno=trainCoaches[7];
			int s2no=trainCoaches[6];
			//con.close();
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
			Statement stmt=con.createStatement(); 
			ResultSet rs=stmt.executeQuery("select * from seats_per_coach");
			while(rs.next())
			{
					int gen=rs.getInt("GEN");
					int tat=rs.getInt("TAT");
					int prtat=rs.getInt("PRTAT");
					int ladies=rs.getInt("LADIES");
					int pd=rs.getInt("PD");
					int staff=rs.getInt("STAFF");
					int rac=rs.getInt("RAC");
					if(rs.getString("Coach_type")=="A1")
						a1=new SeatMat(gen,tat,prtat,ladies,pd,staff,rac,nos,a1no);
					else if(rs.getString("Coach_type")=="A2")
						a2=new SeatMat(gen,tat,prtat,ladies,pd,staff,rac,nos,a2no);
					else if(rs.getString("Coach_type")=="A3")
						a3=new SeatMat(gen,tat,prtat,ladies,pd,staff,rac,nos,a3no);
					else if(rs.getString("Coach_type")=="FC")
						fc=new SeatMat(gen,tat,prtat,ladies,pd,staff,rac,nos,fcno);
					else if(rs.getString("Coach_type")=="CC")
						cc=new SeatMat(gen,tat,prtat,ladies,pd,staff,rac,nos,ccno);
					else if(rs.getString("Coach_type")=="SL")
						sl=new SeatMat(gen,tat,prtat,ladies,pd,staff,rac,nos,slno);
					else if(rs.getString("Coach_type")=="UN")
						un=new SeatMat(gen,tat,prtat,ladies,pd,staff,rac,nos,unno);
					else if(rs.getString("Coach_type")=="S2")
						s2=new SeatMat(gen,tat,prtat,ladies,pd,staff,rac,nos,s2no);
			}
			con.close();
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}
	}
	synchronized int noOfSeatsAvailable(String coach,String tkttype,String fromst,String tost)
	{
		int i=getStInd(fromst);
		int j=getStInd(tost);
		switch(coach)
		{
		case "A1":return a1.getNoOfSeats(tkttype,i,j);
		case "A2":return a2.getNoOfSeats(tkttype,i,j);
		case "A3":return a3.getNoOfSeats(tkttype,i,j);
		case "FC":return fc.getNoOfSeats(tkttype,i,j);
		case "CC":return cc.getNoOfSeats(tkttype,i,j);
		case "SL":return sl.getNoOfSeats(tkttype,i,j);
		case "UN":return un.getNoOfSeats(tkttype,i,j);
		case "S2":return s2.getNoOfSeats(tkttype,i,j);
		}
		return 0;
			
	}
	static int getStId(String station)
	{
		try
		{
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
			Statement stmt=con.createStatement(); 
			ResultSet rs=stmt.executeQuery("select * from station_id where st_name="+station);	
			int stid= rs.getInt("st_id");
			con.close();
			return stid;
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}
		return 0;
	}
	int getStInd(String station)
	{
		try
		{
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
			Statement stmt=con.createStatement(); 
			ResultSet rs=stmt.executeQuery("select * from routes where train_no="+trainNo+"and st_id="+getStId(station));	
			int stind= rs.getInt("st_index");
			con.close();
			return stind;
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}
		return 0;
	}
	synchronized boolean book(String coach,String tkttype,String fromst,String tost,String date,String names[])
	{
		int i=getStInd(fromst);
		int j=getStInd(tost);
		boolean value=false;
		int n=names.length;
		while (n != 0) 
		{
			switch (coach) 
			{
			case "A1":
				value = a1.seatBook(i, j, tkttype, nos);
				break;
			case "A2":
				value = a2.seatBook(i, j, tkttype, nos);
				break;
			case "A3":
				value = a3.seatBook(i, j, tkttype, nos);
				break;
			case "FC":
				value = fc.seatBook(i, j, tkttype, nos);
				break;
			case "CC":
				value = cc.seatBook(i, j, tkttype, nos);
				break;
			case "SL":
				value = sl.seatBook(i, j, tkttype, nos);
				break;
			case "UN":
				value = un.seatBook(i, j, tkttype, nos);
				break;
			case "S2":
				value = s2.seatBook(i, j, tkttype, nos);
				break;
			}
			int pnr = trainNo;// To be changed............................
			String status = "";
			if (value == true) 
			{
				if (tkttype == "RAC")
				{
					racno+=1;
					status = "RAC"+racno;
				}
				else
					status = "CNF";
			}
			else
			{
				now+=1;
				status="WL"+now;
			}
			ReservationChart.append(trainNo, date, pnr, names[i], fromst, tost, status, coach, tkttype,i,j);
			n--;
		}
		return value;
	}
	synchronized void cancel(int pnr, String  date)
	{
		int n=0;
		String coach="";
		String tkttype="";
		String fr="";
		String to="";
		String status="";
		try
		{
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
			Statement stmt=con.createStatement(); 
			ResultSet rs=stmt.executeQuery("select count( * ) FROM res_chart WHERE pnr="+pnr+";");
			n=rs.getInt(1);
			rs=stmt.executeQuery("Select * from res_chart where pnr="+pnr+";");
			coach=rs.getString("Coach");
			tkttype=rs.getString("Type");
			fr=rs.getString("from_st");
			to=rs.getString("to_st");
			status=rs.getString("status");
			con.close();
			
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}
		ReservationChart.cancel(pnr,status);
		int i=getStInd(fr);
		int j=getStInd(to);
		switch(coach)
		{
		case "A1":a1.seatCancel(n, i, j, tkttype, nos);break;
		case "A2":a2.seatCancel(n, i, j, tkttype, nos);break;
		case "A3":a3.seatCancel(n, i, j, tkttype, nos);break;
		case "FC":fc.seatCancel(n, i, j, tkttype, nos);break;
		case "CC":cc.seatCancel(n, i, j, tkttype, nos);break;
		case "SL":sl.seatCancel(n, i, j, tkttype, nos);break;
		case "UN":un.seatCancel(n, i, j, tkttype, nos);break;
		case "S2":s2.seatCancel(n, i, j, tkttype, nos);break;
		}
	}
}
