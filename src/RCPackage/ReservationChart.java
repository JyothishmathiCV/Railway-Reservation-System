package RCPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class ReservationChart {
	int gen,tat,prtat,ladies,pd,staff,rac;
	ReservationChart(int gen,int tat,int prtat,int ladies,int pd,int staff,int rac)
	{
		this.gen=gen;
		this.tat=tat;
		this.prtat=prtat;
		this.ladies=ladies;
		this.pd=pd;
		this.staff=staff;
		this.rac=rac;
	}
	synchronized public static void append(int trainNo,String date,int pnr, String name,String fromst, String tost,String status,String coach,String tkttype,int fr_id,int to_id)
	{
		try
		{
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwyrev","root","PasswordSQL3");
			String query = " insert into res_chart"+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setInt (1,pnr);
			preparedStmt.setString (2, name);
			preparedStmt.setInt (3,trainNo);
			SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
			java.util.Date d1 =(java.util.Date)format.parse(date);
			java.sql.Date datej = new java.sql.Date(d1.getTime());
			preparedStmt.setDate (4,datej);
			preparedStmt.setString (5, fromst);
			preparedStmt.setString (6, tost);
			preparedStmt.setString (7,status);
			preparedStmt.setString (8,coach);
			preparedStmt.setString (9,tkttype);
			preparedStmt.setInt (10,fr_id);
			preparedStmt.setInt (11,to_id);
			preparedStmt.execute();
			con.close();
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}
	}
	synchronized public static void cancel(int pnr,String status)
	{
		try
		{
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
			String query = "delete from res_chart where pnr="+pnr;
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.execute();
			con.close();
			
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}
		if(status.contains("Wl"))
		{
			try
			{
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
				Statement stmt=con.createStatement(); 
				ResultSet rs=stmt.executeQuery("select status from res_chart where status="+"WL"+";");
				while(rs.next())
				{
					int now=Integer.parseInt(rs.getString("status"));
					now=now-1;
					String currstatus=(now==0)?"CNF":"WL"+now;
					String query = "update res_chart set status = "+currstatus+";";
					PreparedStatement preparedStmt = con.prepareStatement(query);
					preparedStmt.execute();	
				}
				con.close();
				
			}
			catch(Exception e)
			{ 
				System.out.println(e);
			}
		}
		if(status.contains("RAC"))
		{
			try
			{
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
				Statement stmt=con.createStatement(); 
				ResultSet rs=stmt.executeQuery("select status from res_chart where status="+"WL"+";");
				while(rs.next())
				{
					int racno=Integer.parseInt(rs.getString("status"));
					racno-=1;
					String currstatus=(racno==0)?"CNF":"RAC"+racno;
					String query = "update res_chart set status = "+currstatus+";";
					PreparedStatement preparedStmt = con.prepareStatement(query);
					preparedStmt.execute();	
				}
				con.close();
				
			}
			catch(Exception e)
			{ 
				System.out.println(e);
			}
		}
	}
	synchronized public static void seatAllocation(int nos,int tots)
	{
		int tick[][]=new int[tots][nos];
		int nosb=0;
		for(int i=0;i<tots;i++)
		{
			for(int j=0;j<nos;j++)
			{
				tick[i][j]=0;
			}
		}
		try
		{
			int outerid=nos-1;
			int innerid=0;
			boolean exact_fit=true;
			boolean booked=true;
			boolean should_continue=true;
			String coach="";//To be initialized
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayrev","root","PasswordSQL3");
			Statement stmt=con.createStatement(); 
			ResultSet rs;
			do
			{ 
				rs=stmt.executeQuery("Select * from res_chart where status=\"CNF\" and coach=\'"+coach+"\' and from_st= "+outerid+" to_st= "+innerid+ ";");
				int pnr=rs.getInt("pnr");
				exact_fit=false;
				for(int i=0;i<nosb;i++)
				{
					exact_fit=true;
					int j;
					for(j=innerid;j<nos && tick[i][j]==1;j++);
					if(j==nos)
						exact_fit=false;
					else 
					{
						for(;j<outerid;j++)
						{
							if(tick[i][j]!=0)
								exact_fit=false;
						}
					}
					if(exact_fit==true)
					{
						//book the ticket and update the status in the data base and break the loop
						for(j=innerid;j<outerid;j++)
						{
							tick[i][j]=1;
						}
						String query = "update res_chart set status = "+i+" where pnr = "+pnr+";";
						PreparedStatement preparedStmt = con.prepareStatement(query);
						preparedStmt.execute();	
						booked=true;
						break;						
					}
				}
				//if the booked is false book a new ticket and increment nosb and update
				//outerid ++ and continue
				if(!booked)
				{
					
					for( int j=innerid;j<outerid;j++)
					{
						tick[nosb][j]=1;
					}
					nosb+=1;
					String query = "update res_chart set status = "+nosb+" where pnr = "+"pnr"+";";
					PreparedStatement preparedStmt = con.prepareStatement(query);
					preparedStmt.execute();	
					booked=true;
				}
				if(rs.next())
					should_continue=true;
				else
				{
					rs=stmt.executeQuery("Select * from res_chart where status=\"CNF\" and coach=\'"+coach+"\' "+ ";");
					if(rs.next())
					{
						innerid+=1;
						should_continue=true;
					}
					else
						should_continue=false;
				}
			}while(should_continue);
			con.close();
			
		}
		catch(Exception e)
		{ 
			System.out.println(e);
		}
	}

}
