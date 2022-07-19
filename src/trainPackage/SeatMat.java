package trainPackage;

public class SeatMat {
	SeatStruct s[][]=new SeatStruct[30][30];
	SeatMat(int gen,int tat,int prtat,int ladies,int pd,int staff,int rac,int nos,int nob)
	{
		for(int i=0;i<nos;i++)
			for(int j=0;j<nos;j++)
				if(j>i)
					s[i][j]=new SeatStruct(gen*nob,tat*nob,prtat*nob,ladies*nob,pd*nob,staff*nob,rac*nob);
	}
	int getNoOfSeats(String tkttype,int i,int j)
	{
		switch(tkttype)
		{
		case "GEN":return s[i][j].gen;
		case "TAT":return s[i][j].tat;
		case "PRTAT":return s[i][j].prtat;
		case "LADIES":return s[i][j].ladies;
		case "PD":return s[i][j].pd;
		case "STAFF":return s[i][j].staff;
		case "RAC":return s[i][j].rac;
		}
		return 0;
	}
	boolean seatBook(int fr, int to,String tkttype,int nos)
	{
		boolean pr=false;
		for(int i=0;i<nos;i++)
			for(int j=0;j<nos;j++)
				if(i<j)
				{
					if(i<fr && j<=to);
					else if(i>=to);
					else
						pr=s[i][j].Take(tkttype);
				}
		return pr;
	}
	void seatCancel(int n,int fr, int to,String tkttype,int nos)
	{
		for(int i=0;i<nos;i++)
			for(int j=0;j<nos;j++)
				if(i<j)
				{
					if(i<fr && j<=to);
					else if(i>=to);
					else
						s[i][j].Free(tkttype,n);
				}
	}

}
