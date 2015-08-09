package Common;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.widget.Spinner;

public class Global {
    private static Global instance;
    public static synchronized Global getInstance()
    {
        if(instance==null){
            instance=new Global();
        }
        return instance;
    }

	//Web services: PostgreSQL
	public static String Namespace       = "http://203.190.254.42/"; 	
	public static String Soap_Address    = "http://203.190.254.42/rhisdata/rhis.asmx";
	public static char VariableSeperator = '^';

	public static String Organization 	 = "ICDDRB";

	//Database
	public static String DatabaseFolder = "RHISDatabase";
	public static String DatabaseName   = "RHISDatabase.db";

    //New Version
    public static String NewVersionName   = "RHISUpdate";
    public static String UpdatedSystem   = "http://203.190.254.42/rhisdata/Update/"+ Global.NewVersionName +".txt";


	//Global Variables
	private String _UserID;
	public void setUserID(String UserID){this._UserID = UserID;}
	public String getUserID(){ return this._UserID;}

    private String _LoginProvType; public void setLoginProvType(String LoginProvType){this._LoginProvType = LoginProvType;} public String getLoginProvType(){ return this._LoginProvType;}
    private String _LoginProvCode; public void setLoginProvCode(String LoginProvCode){this._LoginProvCode = LoginProvCode;} public String getLoginProvCode(){ return this._LoginProvCode;}

    //Global Variable
	//-------------------------------------------------------------------------
	 private String _UserId;
	 public void setUserId(String UserId){this._UserId = UserId;}
	 public String getUserId(){ return this._UserId;}
	//-------------------------------------------------------------------------
	 private String _DeviceNo;
	 public void setDeviceNo(String DeviceNo){this._DeviceNo = DeviceNo;}
	 public String getDeviceNo(){ return this._DeviceNo;}
	//-------------------------------------------------------------------------
	private String _HealthID;
	public void setHealthID(String HealthID){this._HealthID = HealthID;}
	public String getHealthID(){ return this._HealthID;}

	//-------------------------------------------------------------------------
	 private String _IdNo;
	 public void setIdNo(String IdNo){this._IdNo = IdNo;}
	 public String getIdNo(){ return this._IdNo;}
	 //-------------------------------------------------------------------------
	 private String _RegistrationDate;
	 public void setRegistrationDate(String RegistrationDate){this._RegistrationDate = RegistrationDate;}
	 public String getRegistrationDate(){ return this._RegistrationDate;}
	//-------------------------------------------------------------------------
	 private String _CallFrom;
	 public void setCallFrom(String CallFrom){this._CallFrom = CallFrom;}
	 public String getCallFrom(){ return this._CallFrom;}
	 //-------------------------------------------------------------------------

	 //Area Variable
	 private String _Div; public void setDivision(String Div){this._Div = Div;} public String getDivision(){ return this._Div;}
	 private String _Dist; public void setDistrict(String Dist){this._Dist = Dist;} public String getDistrict(){ return this._Dist;}
	 private String _Upz; public void setUpazila(String Upz){this._Upz = Upz;} public String getUpazila(){ return this._Upz;}
	 private String _UN; public void setUnion(String UN){this._UN = UN;} public String getUnion(){ return this._UN;}
	 private String _ProvType; public void setProvType(String ProvType){this._ProvType = ProvType;} public String getProvType(){ return this._ProvType;}
	 private String _ProvCode; public void setProvCode(String ProvCode){this._ProvCode = ProvCode;} public String getProvCode(){ return this._ProvCode;}
	 private String _Mouza; public void setMouza(String Mouza){this._Mouza = Mouza;} public String getMouza(){ return this._Mouza;}
	 private String _Vill; public void setVillage(String Vill){this._Vill = Vill;} public String getVillage(){ return this._Vill;}
	 private String _VillName; public void setVillageName(String VillName){this._VillName = VillName;} public String getVillageName(){ return this._VillName;}
	 private String _WardNew; public void setWardNew(String WardNew){this._WardNew = WardNew;} public String getWardNew(){ return this._WardNew;}
	 private String _WardOld; public void setWardOld(String WardOld){this._WardOld = WardOld;} public String getWardOld(){ return this._WardOld;}
	 private String _FWAUnit; public void setFWAUnit(String FWAUnit){this._FWAUnit = FWAUnit;} public String getFWAUnit(){ return this._FWAUnit;}
	 private String _EPIBlock; public void setEPIBlock(String EPIBlock){this._EPIBlock = EPIBlock;} public String getEPIBlock(){ return this._EPIBlock;}

	 private String _HHNo; public void setHouseholdNo(String HHNo){this._HHNo = HHNo;} public String getHouseholdNo(){ return this._HHNo;}
	 private String _SNo; public void setSerialNo(String SNo){this._SNo = SNo;} public String getSerialNo(){ return this._SNo;}

	 //Immunization
	 private String _imuCode; public void setImuCode(String imuCode){this._imuCode = imuCode;} public String getImuCode(){ return this._imuCode;}

	 //String Function
	 //...........................................................................................................
     public static String Right(String text, int length) {
  	   return text.substring(text.length() - length, text.length());
     }
     public static String Left(String text, int length){
          return text.substring(0, length);
    }
     public static String Mid(String text, int start, int end){
          return text.substring(start, end);
     }
     public static String Mid(String text, int start){
          return text.substring(start, text.length() - start);
     }



    Calendar c = Calendar.getInstance();
    public int mYear = c.get(Calendar.YEAR);
    public int mMonth = c.get(Calendar.MONTH)+1;
    public int mDay = c.get(Calendar.DAY_OF_MONTH);


    //Year, Month
    public static String CurrentYear()
    {
    	Calendar c = Calendar.getInstance();
        String Y = String.valueOf(c.get(Calendar.YEAR));
        return Y;
    }

    public static String CurrentMonth()
    {
    	Calendar c = Calendar.getInstance();
        int mMonth = c.get(Calendar.MONTH)+1;

        String M = Right("00"+String.valueOf(mMonth),2);

        return M;
    }

    //Date now
    //...........................................................................................................
    //Format: YYYY-MM-DD
    public static String DateNowYMD()
    {
        Calendar c = Calendar.getInstance();
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay   = c.get(Calendar.DAY_OF_MONTH);

        String M = Right("00"+String.valueOf(mMonth),2);
        String Y = String.valueOf(mYear);
        String D = Right("00"+String.valueOf(mDay),2);

        String CurrentDateYYYYMMDD = String.valueOf(Y)+"-"+String.valueOf(M)+"-"+String.valueOf(D);

    	return CurrentDateYYYYMMDD;
    }

    //Format: DD/MM/YYYY
    public static String DateNowDMY()
    {
        Calendar c = Calendar.getInstance();
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay   = c.get(Calendar.DAY_OF_MONTH);

        String M = Right("00"+String.valueOf(mMonth),2);
        String Y = String.valueOf(mYear);
        String D = Right("00"+String.valueOf(mDay),2);

        String CurrentDateDDMMYYYY = String.valueOf(D)+"/"+String.valueOf(M)+"/"+String.valueOf(Y);

    	return CurrentDateDDMMYYYY;
    }

    public static String DateTimeNowYMDHMS()
    {
        Calendar c = Calendar.getInstance();
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay   = c.get(Calendar.DAY_OF_MONTH);

        String M = Right("00"+String.valueOf(mMonth),2);
        String Y = String.valueOf(mYear);
        String D = Right("00"+String.valueOf(mDay),2);

        String second = Right("00"+String.valueOf(c.get(Calendar.SECOND)),2);
        String minute = Right("00"+String.valueOf(c.get(Calendar.MINUTE)),2);
        String hour   = Right("00"+String.valueOf(c.get(Calendar.HOUR_OF_DAY)),2);  //24 hour format

        String CurrentDateTimeYMD = String.valueOf(Y)+"-"+String.valueOf(M)+"-"+String.valueOf(D)+" "+ String.valueOf(hour)+":"+ String.valueOf(minute)+":"+ String.valueOf(second);

    	return CurrentDateTimeYMD;
    }

    public static String DateTimeNowDMYHMS()
    {
        Calendar c = Calendar.getInstance();
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay   = c.get(Calendar.DAY_OF_MONTH);

        String M = Right("00"+String.valueOf(mMonth),2);
        String Y = String.valueOf(mYear);
        String D = Right("00"+String.valueOf(mDay),2);

        String second = Right("00"+String.valueOf(c.get(Calendar.SECOND)),2);
        String minute = Right("00"+String.valueOf(c.get(Calendar.MINUTE)),2);
        String hour   = Right("00"+String.valueOf(c.get(Calendar.HOUR_OF_DAY)),2);  //24 hour format

        String CurrentDateTimeDMY = String.valueOf(D)+"-"+String.valueOf(M)+"-"+String.valueOf(Y)+" "+ String.valueOf(hour)+":"+ String.valueOf(minute)+":"+ String.valueOf(second);

    	return CurrentDateTimeDMY;
    }

    //Time Now
    //...........................................................................................................
    public String CurrentTime24()
    {
    	Calendar TM = Calendar.getInstance();
    	//return Right("00"+String.valueOf(TM.get(Calendar.HOUR_OF_DAY)),2)+":"+ Right("00"+String.valueOf(TM.get(Calendar.MINUTE)),2)+":"+ Right("00"+String.valueOf(TM.get(Calendar.SECOND)),2);
    	return Right("00"+String.valueOf(TM.get(Calendar.HOUR_OF_DAY)),2)+":"+ Right("00"+String.valueOf(TM.get(Calendar.MINUTE)),2);
    }


    //Date Converter: dd/mm/yyyy to yyyy-mm-dd
    public static String DateConvertYMD(String DateString)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); // Make sure user insert date into edittext in this format.
        Date dateObject;
        String date="";
	    try{
		    dateObject = formatter.parse(DateString);
		    date = new SimpleDateFormat("yyyy-MM-dd").format(dateObject);
	    }

	    catch (java.text.ParseException e)
	        {
	            e.printStackTrace();
	        }
	    return date;
    }

  //Date Converter: yyyy-mm-dd to dd/mm/yyyy
    public static String DateConvertDMY(String DateString)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Make sure user insert date into edittext in this format.
        Date dateObject;
        String date="";
	    try{
		    dateObject = formatter.parse(DateString);
		    date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);
	    }

	    catch (java.text.ParseException e)
	        {
	            e.printStackTrace();
	        }
	    return date;
    }

    //Add days: format: YYYY-MM-DD
    public static String addDaysYMD(String date, int days)
    {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
			cal.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return sdf.format(cal.getTime());
    }


    //Add days: format: DD/MM/YYYY
    public static String addDays(String date, int days)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return sdf.format(cal.getTime());
    }

    //Add days: format: DD-MM-YYYY
	public static String addDaysDMY(String date, int days)
    {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        try {
			cal.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return sdf.format(cal.getTime());
    }

	//difference between two data
	//End date  : dd/mm/yyyy
	//Start date: dd/mm/yyyy
	//...........................................................................................................
	public static int DateDifferenceDays(String EndDateDDMMYYYY,String StartDateDDMMYYYY)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			int age = 0;
			int diffInDays = 0;
			Date VD;
			Date BD;
			try {
				VD = sdf.parse(Global.DateConvertYMD(EndDateDDMMYYYY));
				BD = sdf.parse(Global.DateConvertYMD(StartDateDDMMYYYY));
				diffInDays = (int) ((VD.getTime() - BD.getTime())/ (1000 * 60 * 60 * 24));
				//age = (int)(diffInDays/365.25);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return diffInDays;
		}

	public static int DateDifferenceMonth(String EndDateDDMMYYYY,String StartDateDDMMYYYY)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			int age = 0;
			int diffInDays = 0;
			Date VD;
			Date BD;
			try {
				VD = sdf.parse(Global.DateConvertYMD(EndDateDDMMYYYY));
				BD = sdf.parse(Global.DateConvertYMD(StartDateDDMMYYYY));
				diffInDays = (int) ((VD.getTime() - BD.getTime())/ (1000 * 60 * 60 * 24));
				age = (int)(diffInDays/30.40);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return age;
		}

	public static int DateDifferenceYears(String EndDateDDMMYYYY,String StartDateDDMMYYYY)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			int age = 0;
			int diffInDays = 0;
			Date VD;
			Date BD;
			try {
				VD = sdf.parse(Global.DateConvertYMD(EndDateDDMMYYYY));
				BD = sdf.parse(Global.DateConvertYMD(StartDateDDMMYYYY));
				diffInDays = (int) ((VD.getTime() - BD.getTime())/ (1000 * 60 * 60 * 24));
				age = (int)(diffInDays/365.25);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return age;
		}

    //Date field validate
    //...........................................................................................................
	public static String DateValidate(String DateString)
	{
		String DT=DateString;
		String Message = "";

		if(DT.length()!=10)
		{
			Message = "তারিখ ১০ digit হতে হবে [dd/mm/yyyy].";
		}
		else if(!DT.substring(2, 3).equals("/") | !DT.substring(5, 6).equals("/"))
		{
			Message = "তারিখ সঠিক নয়.";
		}
		else
		{
			String D=DT.substring(0,2);
			String M=DT.substring(3,5);
			Calendar c = Calendar.getInstance();
			int currentYear = c.get(Calendar.YEAR);

			int Y= Integer.parseInt(DT.substring(6,10));


			//Date format check
			//-------------------------------------------------------------------------------------------
			if(Integer.parseInt(M)<1 | Integer.parseInt(M)>12)
			{
				Message = "মাসের সংখ্যা 01 - 12 হতে হবে.";
			}
			else if(Integer.parseInt(D)<1 | Integer.parseInt(D)>31)
			{
				Message = "দিনের সংখ্যা 01 - 31 হতে হবে.";
			}
			else if(Y > currentYear | Y < 1900)
			{
				Message = "বছরের সংখ্যা 1900 - "+ String.valueOf(currentYear)+" হতে হবে.";
			}

			// only 1,3,5,7,8,10,12 has 31 days
			else if (D.equals("31") &&
	        		 (M.equals("4") || M .equals("6") || M.equals("9") ||
	                  M.equals("11") || M.equals("04") || M .equals("06") ||
	                  M.equals("09"))) {
				Message = "তারিখ সঠিক নয়.";
	         }
	         else if (M.equals("2") || M.equals("02")) {
	              //leap year
	             if(Y % 4==0){
	                  if(D.equals("30") || D.equals("31")){
	                	  Message = "তারিখ সঠিক নয়.";
	                  }
	                  else{
	                	  //valid=true;
	                  }
	             }
	             else{
	                 if(D.equals("29")||D.equals("30")||D.equals("31")){
	                	 Message = "তারিখ সঠিক নয়.";
	                 }
	                 else{
	                	 //valid=true;
	                 }
	             }
	         }

	         else{
	        	 //valid=true;
	         }

			//Validation check
			//-------------------------------------------------------------------------------------------
	        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	        Date dateObject;
	        try {
	        	Global g=new Global();
				dateObject = formatter.parse(DateString);
	            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	            String formattedDate = sdf.format(c.getTime());
	            Date currentDate = sdf.parse(DateNowDMY());
	            Date DateValue = sdf.parse(DateString);

	            if(DateValue.after(currentDate))
		          {
	                int mYear = c.get(Calendar.YEAR);
	                int mMonth = c.get(Calendar.MONTH)+1;
	                int mDay = c.get(Calendar.DAY_OF_MONTH);

	            	String MM   = Right("00"+String.valueOf(c.get(Calendar.MONTH)+1),2);
	                String YYYY = String.valueOf(c.get(Calendar.YEAR));
	                String DD   = Right("00"+String.valueOf(c.get(Calendar.DAY_OF_MONTH)),2);

	            	Message = "তারিখ আজকের তারিখের["+ (String.valueOf(DD)+"/"+String.valueOf(MM)+"/"+String.valueOf(YYYY)) +"]  সমান অথবা কম হতে হবে।";
		          }
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		return Message;

	}

	//System date check
    public static String TodaysDateforCheck()
    {
        Calendar c = Calendar.getInstance();
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay   = c.get(Calendar.DAY_OF_MONTH);

        String CurrentDateDDMMYYYY = String.valueOf(mYear)+""+String.valueOf(mMonth)+""+String.valueOf(mDay);

    	return CurrentDateDDMMYYYY;
    }


    //Getting spinner item position with code
	public static int SpinnerItemPosition(Spinner spn,int CodeLength, String Value)
	{
		int pos = 0;
		if(Value.length()!=0)
		{
		    for(int i=0;i<spn.getCount();i++)
		    {
		    	if(spn.getItemAtPosition(i).toString().length()!=0)
		    	{
			    	if(Global.Left(spn.getItemAtPosition(i).toString(),CodeLength).equalsIgnoreCase(Value))
			    	{
			    		pos = i;
			    		i   = spn.getCount();
			    	}
		    	}
		    }
		}
	    return pos;
	}

    /*
     Pass Value to spinner to set spinner item
      */
    public static void SetSpinnerItem(Spinner spn,String Value)
    {
        int pos = 0;
        if(Value.length()!=0)
        {
            for(int i=0;i<spn.getCount();i++)
            {
                if(spn.getItemAtPosition(i).toString().length()!=0)
                {
                    if(Global.Left(spn.getItemAtPosition(i).toString(),Value.length()).equalsIgnoreCase(Value))
                    {
                        spn.setSelection(i);
                        break;
                        /*pos = i;
                        i   = spn.getCount();*/
                    }
                }
            }
        }

    }

	//...........................................................................................................
	// GPS Coordinates
	//...........................................................................................................
	public static String decimalToDMS(double coord) {
        String output, degrees, minutes, seconds;

        // gets the modulus the coordinate divided by one (MOD1).
        // in other words gets all the numbers after the decimal point.
        // e.g. mod = 87.728056 % 1 == 0.728056
        //
        // next get the integer part of the coord. On other words the whole number part.
        // e.g. intPart = 87

        double mod = coord % 1;
        int intPart = (int)coord;

        //set degrees to the value of intPart
        //e.g. degrees = "87"

        degrees = String.valueOf(intPart);

        // next times the MOD1 of degrees by 60 so we can find the integer part for minutes.
        // get the MOD1 of the new coord to find the numbers after the decimal point.
        // e.g. coord = 0.728056 * 60 == 43.68336
        //      mod = 43.68336 % 1 == 0.68336
        //
        // next get the value of the integer part of the coord.
        // e.g. intPart = 43

        coord = mod * 60;
        mod = coord % 1;
        intPart = (int)coord;
        if (intPart < 0) {
           // Convert number to positive if it's negative.
           intPart *= -1;
        }

        // set minutes to the value of intPart.
        // e.g. minutes = "43"
        minutes = String.valueOf(intPart);

        //do the same again for minutes
        //e.g. coord = 0.68336 * 60 == 41.0016
        //e.g. intPart = 41
        coord = mod * 60;
        intPart = (int)coord;
        if (intPart < 0) {
           // Convert number to positive if it's negative.
           intPart *= -1;
        }

        // set seconds to the value of intPart.
        // e.g. seconds = "41"
        seconds = String.valueOf(intPart);

        // I used this format for android but you can change it
        // to return in whatever format you like
        // e.g. output = "87/1,43/1,41/1"
        //output = degrees + "/1," + minutes + "/1," + seconds + "/1";

        //Standard output of DÃ‚Â°MÃ¢â‚¬Â²SÃ¢â‚¬Â³
        //output = degrees + "Ã‚Â°" + minutes + "'" + seconds + "\"";
        	output = degrees + "," + minutes + "," + seconds;

        return output;
		}

       /*
        * Conversion DMS to decimal
        *
        * Input: latitude or longitude in the DMS format ( example: N 43Ã‚Â° 36' 15.894")
        * Return: latitude or longitude in decimal format
        * hemisphereOUmeridien => {W,E,S,N}
        *
        */
        public double DMSToDecimal(String hemisphereOUmeridien,double degres,double minutes,double secondes)
        {
                double LatOrLon=0;
                double signe=1.0;
 
                if((hemisphereOUmeridien.equals("W"))||(hemisphereOUmeridien.equals("S"))) {signe=-1.0;}                
                LatOrLon = signe*(Math.floor(degres) + Math.floor(minutes)/60.0 + secondes/3600.0);
 
                return(LatOrLon);               
        }

    //pregNo
    private String _PregNo;
    public void setPregNo(String PregNo){this._PregNo = PregNo;}
    public String getPregNo(){ return this._PregNo;}

    //ChildNo
    private String _ChildNo;
    public void setChildNo(String ChildNo){this._ChildNo = ChildNo;}
    public String getChildNo(){ return this._ChildNo;}
        
}

