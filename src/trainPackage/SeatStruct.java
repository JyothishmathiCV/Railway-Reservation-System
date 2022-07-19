package trainPackage;

public class SeatStruct {
	int gen,tat,prtat,ladies,pd,staff,rac;
	SeatStruct()
	{
		gen=tat=prtat=ladies=pd=staff=rac=0;
	}
	SeatStruct(int gen,int tat,int prtat,int ladies,int pd,int staff,int rac)
	{
		this.gen=gen;
		this.tat=tat;
		this.prtat=prtat;
		this.ladies=ladies;
		this.pd=pd;
		this.staff=staff;
		this.rac=rac;
	}
	boolean Take(String tkttype)
	{
		switch(tkttype)
		{
		case "GEN":
			if(gen==0)
				return false;
			else
				gen=gen-1;
			break;
		case "TAT":
			if(tat==0)
				return false;
			else
				tat-=1;
			break;
		case "PRTAT":
			if(prtat==0)
				return false;
			else
				prtat-=1;
			break;
		case "LADIES":
			if(ladies==0)
				return false;
			else
				ladies-=1;
			break;
		case "PD":
			if(pd==0)
				return false;
			else
				pd-=1;
			break;
		case "STAFF":
			if(staff==0)
				return false;
			else
				staff-=1;
			break;
		case "RAC":
			if(rac==0)
				return false;
			else
				rac-=1;
			break;
		}
		return true;
	}
	void Free(String tkttype,int n)
	{
		switch(tkttype)
		{
		case "GEN":gen+=n;break;
		case "TAT":tat+=n;break;
		case "PRTAT":prtat+=n;break;
		case "LADIES":ladies+=n;break;
		case "PD":pd+=n;break;
		case "STAFF":staff+=n;break;
		case "RAC":rac+=n;break;
		}
	}
}
