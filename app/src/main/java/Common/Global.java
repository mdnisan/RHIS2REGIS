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
    //Web api
  // public static final String URL_WebAPI      = "http://203.190.254.42:9080/CcahWebservice/apidata/DataSync";
//public static final String URL_WebAPI        = "http://172.16.12.44:9090/CcahWebservice/apidata/DataSync";

  //  public static final String USER_AGENT_WebAPI = "Mozilla/5.0";
	//Web services: PostgreSQL
	/*public static String Namespace       = "http://203.190.254.42/";
	public static String Soap_Address    = "http://203.190.254.42/rhis2/rhis.asmx";*/
    //http://203.190.254.42/rhisdata/rhis.asmx
	public static char VariableSeperator = '^';

	public static String Organization 	 = "ICDDRB";

	//Database
	public static String DatabaseFolder = "RHISDatabase";
	public static String DatabaseName   = "RHISDatabase.db";

    //New Version
    //public static String NewVersionName   = "RHISFPI";
    //icddrb ip
    //public static String UpdatedSystemFPI   = "http://203.190.254.42:9080/CcahWebservice/UpdateFPI/"+ Global.NewVersionName +".txt";
    //public static String UpdatedSystemAHI   = "http://203.190.254.42:9080/CcahWebservice/UpdateAHI/"+ Global.NewVersionName +".txt";
    //SCI IP
    //public static String UpdatedSystem   = "http://mamoni.net:8080/CcahWebservice/Update/"+ Global.NewVersionName +".txt";
   //linux server
    //private static final String Server_URL = "http://172.16.18.19:8080";

   // private static final String Server_URL = "http://mamoni.net:8080";          // Save the Children
   private static final String Server_URL = "http://mchdrhis.icddrb.org:8080"; // icddrb

    public static final String URL_WebAPI        = Server_URL + "/CcahWebservice/apidata/DataSync";
    public static final String USER_AGENT_WebAPI = "Mozilla/5.0";

 /*   private static final String Server_URL = "http://203.190.254.39:8080";

    public static final String URL_WebAPI  = Server_URL + "/CcahWebservice/apidata/DataSync";
    public static final String USER_AGENT_WebAPI = "Mozilla/5.0";*/

    //New Version
    public static String NewVersionName    = "RHISFPI";
    public static String NewVersionName1    = "RHISAHI";
    public static String NewVersionName2    = "RHISHI";
    public static String UpdatedSystemFPI  = Server_URL + "/CcahWebservice/UpdateFPI/"+ Global.NewVersionName +".txt";
    public static String UpdatedSystemAHI  = Server_URL + "/CcahWebservice/UpdateAHI/"+ Global.NewVersionName1 +".txt";
    public static String UpdatedSystemHI  = Server_URL + "/CcahWebservice/UpdateHI/"+ Global.NewVersionName2 +".txt";
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

    private String _FWAProvCode;
    public void setFWAProvCode(String FWAProvCode){this._FWAProvCode = FWAProvCode;}
    public String getFWAProvCode(){ return this._FWAProvCode;}
    //-------------------------------------------------------------------------

    private String _WPProvCode;
    public void setWPProvCode(String WPProvCode){this._WPProvCode = WPProvCode;}
    public String getWPProvCode(){ return this._WPProvCode;}

    private String _WPDate;
    public void setWPDate(String WPDate){this._WPDate = WPDate;}
    public String getWPDate(){ return this._WPDate;}

    private String _WPDes;
    public void setWPDes(String WPDes){this._WPDes = WPDes;}
    public String getWPDes(){ return this._WPDes;}

    //////////////////////////////////////////////

    private String _progressMessage;
    public void setProgressMessage(String progressMessage){this._progressMessage = progressMessage;}
    public String getProgressMessage(){ return this._progressMessage;}
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
    private String _HHHName; public void setHouseholdHName(String HHHName){this._HHHName = HHHName;} public String getHouseholdHName(){ return this._HHHName;}
    private String _SupType; public void setSupType(String SupType){this._SupType = SupType;} public String getSupType(){ return this._SupType;}
	 //Immunization
	 private String _imuCode; public void setImuCode(String imuCode){this._imuCode = imuCode;} public String getImuCode(){ return this._imuCode;}
    private String _AreaDate;
    public void setAreaDate(String AreaDate){this._AreaDate = AreaDate;}
    public String getAreaDate(){ return this._AreaDate;}
    private String _AreaDateM;
    public void setAreaDateM(String AreaDateM){this._AreaDateM = AreaDateM;}
    public String getAreaDateM(){ return this._AreaDateM;}
    private String _AreaUnitValue;
    public void setAreaUnitValue(String AreaUnitValue){this._AreaUnitValue = AreaUnitValue;}
    public String getAreaUnitValue(){ return this._AreaUnitValue;}
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

	private String _SLno;
	public void setSLno(String SLno){this._SLno = SLno;}
	public String getSLno(){ return this._SLno;}

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

        String M = Right("00" + String.valueOf(mMonth), 2);

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
        String D = Right("00" + String.valueOf(mDay), 2);

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

        String M = Right("00" + String.valueOf(mMonth), 2);
        String Y = String.valueOf(mYear);
        String D = Right("00" + String.valueOf(mDay), 2);

        String second = Right("00"+String.valueOf(c.get(Calendar.SECOND)),2);
        String minute = Right("00" + String.valueOf(c.get(Calendar.MINUTE)), 2);
        String hour   = Right("00"+String.valueOf(c.get(Calendar.HOUR_OF_DAY)),2);  //24 hour format

        String CurrentDateTimeYMD = String.valueOf(Y)+"-"+String.valueOf(M)+"-"+String.valueOf(D)+" "+ String.valueOf(hour)+":"+ String.valueOf(minute)+":"+ String.valueOf(second);

    	return CurrentDateTimeYMD;
    }

	public static String GenerateProviderId(String ProviderId)
	{

			Calendar c = Calendar.getInstance();
			int mYear  = c.get(Calendar.YEAR);
			int mMonth = c.get(Calendar.MONTH)+1;
			int mDay   = c.get(Calendar.DAY_OF_MONTH);

			String M = Right("00" + String.valueOf(mMonth), 2);
			String Y = String.valueOf(mYear);
			String D = Right("00" + String.valueOf(mDay), 2);

			String second = Right("00"+String.valueOf(c.get(Calendar.SECOND)),2);
			String minute = Right("00" + String.valueOf(c.get(Calendar.MINUTE)), 2);
			String hour   = Right("00"+String.valueOf(c.get(Calendar.HOUR_OF_DAY)),2);  //24 hour format

			String CurrentDateTimeYMD = String.valueOf(Y)+""+String.valueOf(M)+""+String.valueOf(D)+""+ String.valueOf(hour)+""+ String.valueOf(minute)+""+ String.valueOf(second);

			return CurrentDateTimeYMD;
        //return (ProviderId + CurrentDateTimeYMD);


	}

    public static String GenerateProviderIdForInjectable1(String ProviderId)
    {

        Calendar c = Calendar.getInstance();
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay   = c.get(Calendar.DAY_OF_MONTH);

        String M = Right("00" + String.valueOf(mMonth), 2);
        String Y = String.valueOf(mYear);
        String D = Right("00" + String.valueOf(mDay), 2);

        String second = Right("00"+String.valueOf(c.get(Calendar.SECOND)),2);
        String minute = Right("00" + String.valueOf(c.get(Calendar.MINUTE)), 2);
        String hour   = Right("00"+String.valueOf(c.get(Calendar.HOUR_OF_DAY)),2);  //24 hour format

        String CurrentDateTimeYMD = String.valueOf(Y)+""+String.valueOf(M)+""+String.valueOf(D)+""+ String.valueOf(hour);

        return (ProviderId + CurrentDateTimeYMD);


    }


    public static String GenerateProviderIdForInjectable2(String ProviderId)
    {

        Calendar c = Calendar.getInstance();
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay   = c.get(Calendar.DAY_OF_MONTH);

        String M = Right("00" + String.valueOf(mMonth), 2);
        String Y = String.valueOf(mYear);
        String D = Right("00" + String.valueOf(mDay), 2);

        String second = Right("00"+String.valueOf(c.get(Calendar.SECOND)),2);
        String minute = Right("00" + String.valueOf(c.get(Calendar.MINUTE)), 2);
        String hour   = Right("00"+String.valueOf(c.get(Calendar.HOUR_OF_DAY)),2);  //24 hour format

        String CurrentDateTime = String.valueOf(minute)+""+ String.valueOf(second);

        return CurrentDateTime;


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

	    catch (ParseException e)
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

	    catch (ParseException e)
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
	public static Integer GetCurrentStockOfItem(Connection c, String ProviderId, String ItemCode)
	{
		String SQL = "";

		SQL = "select '0'||(ifnull(max(cast(stockQty as int)),0))stockQty from currentStock";
		SQL += " WHERE providerId = '" + ProviderId + "' AND itemCode = '" + ItemCode +"'";
		String q = c.ReturnSingleValue(SQL);
		return Integer.parseInt(q);
	}
    public static String GetFatherName(Connection c,String HealthId)
    {
    String sql = "SELECT CASE WHEN cast(FNo as int) = 55 THEN ifnull(Father,'') WHEN cast(FNo as int) = 77 THEN ifnull(Father,'') " +
                "  WHEN cast(FNo as int) = 88 THEN ifnull(Father,'') ELSE (select  NameEng  from member " +
                "  where  ProvCode=(select  ProvCode  from member  Where  healthid ='"+ HealthId+ "') " +
                "  and HHNo=(select  HHNo  from member  Where  healthid  ='"+ HealthId+ "') and " +
                "  SNo=(select  FNo  from member  Where  healthid ='"+ HealthId+ "')) END AS  Father " +
                "  FROM member WHERE healthId ='"+ HealthId+ "'";

     /*   String sql = "Select CASE WHEN CAST ( E.FNo AS int ) = 55 THEN ifnull ( E.NameEng , '' )  " +
                "  WHEN CAST ( E.FNo AS int ) = 77 THEN ifnull ( E.NameEng , '' ) " +
                "  WHEN CAST ( E.FNo AS int ) = 88 THEN ifnull ( E.NameEng , '' ) " +
                "  ELSE ( SELECT NameEng FROM member A " +
                "  WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid ='"+ HealthId+ "') " +
                "  and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN and A.Mouza = B.Mouza and " +
                "  A.Vill =B.Vill and A.HHNo = B.HHNo ) AND " +
                "  A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid ='"+ HealthId+ "') " +
                "  AND A.SNo = ( SELECT D.FNo FROM member  D WHERE D.HealthID ='" +HealthId+ "')) END AS  Father "+
                "  FROM member E WHERE E.healthId='"+ HealthId+ "'";*/
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetHusName(Connection c,String HealthId)
    {
       String sql = "SELECT CASE WHEN cast(FNo as int) = 55 THEN ifnull(Father,'') WHEN cast(FNo as int) = 77 THEN ifnull(Father,'') " +
                "  WHEN cast(FNo as int) = 88 THEN ifnull(Father,'') ELSE (select  NameEng  from member " +
                "  where  ProvCode=(select  ProvCode  from member  Where  healthid ='"+ HealthId+ "') " +
                "  and HHNo=(select  HHNo  from member  Where  healthid  ='"+ HealthId+ "') and " +
                "  SNo=(select  SPNO1  from member  Where  healthid='" +HealthId+ "')) END AS  HusName" +
                "  FROM member WHERE healthId ='"+ HealthId+ "'";
  /*      String sql = "Select CASE WHEN CAST ( E.SPNO1 AS int ) = 55 THEN ifnull ( E.NameEng , '' )  " +
                "  WHEN CAST ( E.SPNO1 AS int ) = 77 THEN ifnull ( E.NameEng , '' ) " +
                "  WHEN CAST ( E.SPNO1 AS int ) = 88 THEN ifnull ( E.NameEng , '' ) " +
                "  ELSE ( SELECT NameEng FROM member A " +
                "  WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid ='"+ HealthId+ "') " +
                "  and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN and A.Mouza = B.Mouza and " +
                "  A.Vill =B.Vill and A.HHNo = B.HHNo ) AND " +
                "  A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid ='"+ HealthId+ "') " +
                "  AND A.SNo = ( SELECT D.SPNO1 FROM member  D WHERE D.HealthID ='" +HealthId+ "')) END AS  HusName "+
                "  FROM member E WHERE E.healthId='"+ HealthId+ "'";*/
         String result = c.ReturnSingleValue(sql);
        return result;

    }



    public static String GetImmunizationDateForChild(Connection c,String HealthId, String imuCode)
    {
        String sql = "select imuDate FROM immunizationHistory where Healthid = '" + HealthId +"' AND imuCode = '"+ imuCode +"'";
        String result = c.ReturnSingleValue(sql);
        return result;
    }

	public static String GetImmunizationDateForWoman(Connection c,String HealthId, String imuCode)
	{
		String sql = "select imuDate FROM immunizationHistory where Healthid = '" + HealthId +"' AND imuCode = '"+ imuCode +"'";
		String result = c.ReturnSingleValue(sql);
		return result;
	}

    public static String GetMotherName(Connection c,String HealthId)
    {
   String sql = "SELECT CASE WHEN cast(MNo as int) = 55 THEN ifnull(Mother,'') WHEN cast(MNo as int) = 77 THEN ifnull(Mother,'') " +
                "  WHEN cast(MNo as int) = 88 THEN ifnull(Mother,'') ELSE (select  NameEng  from member " +
                "  where  ProvCode=(select  ProvCode  from member  Where  healthid ='"+ HealthId+ "') " +
                "  and HHNo=(select  HHNo  from member  Where  healthid  ='"+ HealthId+ "') and " +
                "  SNo=(select  MNo  from member  Where  healthid ='"+ HealthId+ "')) END AS  Mother " +
                "  FROM member WHERE healthId ='"+ HealthId+ "'";

 /*       String sql = "Select CASE WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull ( E.NameEng , '' )  " +
                "  WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull ( E.NameEng , '' ) " +
                "  WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull ( E.NameEng , '' ) " +
                "  ELSE ( SELECT NameEng FROM member A " +
                "  WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid ='"+ HealthId+ "') " +
                "  and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN and A.Mouza = B.Mouza and " +
                "  A.Vill =B.Vill and A.HHNo = B.HHNo ) AND " +
                "  A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid ='"+ HealthId+ "') " +
                "  AND A.SNo = ( SELECT D.MNo FROM member  D WHERE D.HealthID ='" +HealthId+ "')) END AS  Mother "+
                "  FROM member E WHERE E.healthId='"+ HealthId+ "'";*/
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetFatherNameNrc(Connection c,String HealthId)
    {

        String sql = "SELECT CASE WHEN CAST ( E.FNo AS int ) = 55 THEN ifnull ( E.NameEng , '' ) " +
                "  WHEN CAST ( E.FNo AS int ) = 77 THEN ifnull ( E.NameEng , '' ) " +
                "  WHEN CAST ( E.FNo AS int ) = 88 THEN ifnull ( E.NameEng , '' ) " +
                "  ELSE ( SELECT NameEng FROM member A " +
                "  WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid ='"+ HealthId+ "'"+
                "  and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN and A.Mouza = B.Mouza and "+
                "  A.Vill =B.Vill and A.HHNo = B.HHNo ) AND "+
                "  A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid ='"+ HealthId+ "') " +
                "  AND A.SNo = ( SELECT D.FNo FROM member  D WHERE D.HealthID =='"+ HealthId+ "')) END AS Father " +
                "  FROM member E WHERE E.healthId ='"+ HealthId+ "'";

        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetHusNameNrc(Connection c,String HealthId)
    {

        String sql = "SELECT CASE WHEN CAST ( E.SPNO1 AS int ) = 55 THEN ifnull ( E.NameEng , '' ) " +
                "  WHEN CAST ( E.SPNO1 AS int ) = 77 THEN ifnull ( E.NameEng , '' ) " +
                "  WHEN CAST ( E.SPNO1 AS int ) = 88 THEN ifnull ( E.NameEng , '' ) " +
                "  ELSE ( SELECT NameEng FROM member A " +
                "  WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid ='"+ HealthId+ "'"+
                "  and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN and A.Mouza = B.Mouza and "+
                "  A.Vill =B.Vill and A.HHNo = B.HHNo ) AND "+
                "  A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid ='"+ HealthId+ "') " +
                "  AND A.SNo = ( SELECT D.SPNO1 FROM member  D WHERE D.HealthID =='"+ HealthId+ "')) END AS HusName " +
                "  FROM member E WHERE E.healthId ='"+ HealthId+ "'";

        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetMotherNameNrc(Connection c,String HealthId)
    {


        String sql = "SELECT CASE WHEN CAST ( E.MNo AS int ) = 55 THEN ifnull ( E.NameEng , '' ) " +
                "  WHEN CAST ( E.MNo AS int ) = 77 THEN ifnull ( E.NameEng , '' ) " +
                "  WHEN CAST ( E.MNo AS int ) = 88 THEN ifnull ( E.NameEng , '' ) " +
                "  ELSE ( SELECT NameEng FROM member A " +
                "  WHERE A.ProvCode = ( SELECT B.ProvCode FROM member B WHERE B.healthid ='"+ HealthId+ "'"+
                "  and A.Dist = B.Dist and A.Upz = B.Upz and A.UN = B.UN and A.Mouza = B.Mouza and "+
                "  A.Vill =B.Vill and A.HHNo = B.HHNo ) AND "+
                "  A.HHNo = ( SELECT C.HHNo FROM member C WHERE C.healthid ='"+ HealthId+ "') " +
                "  AND A.SNo = ( SELECT D.MNo FROM member  D WHERE D.HealthID =='"+ HealthId+ "')) END AS Mother " +
                "  FROM member E WHERE E.healthId ='"+ HealthId+ "'";

        String result = c.ReturnSingleValue(sql);
        return result;

    }
    public static String GetMotherHealthid(Connection c,String HealthId)
    {
        String sql = "SELECT CASE WHEN cast(MNo as int) = 55 THEN ifnull(healthid,'') WHEN cast(MNo as int) = 77 THEN ifnull(healthid,'') " +
                "  WHEN cast(MNo as int) = 88 THEN ifnull(healthid,'') ELSE (select  healthid  from member " +
                "  where  ProvCode=(select  ProvCode  from member  Where  healthid ='"+ HealthId+ "') " +
                "  and HHNo=(select  HHNo  from member  Where  healthid  ='"+ HealthId+ "') and " +
                "  SNo=(select  MNo  from member  Where  healthid ='"+ HealthId+ "')) END AS  Mhealthid " +
                "  FROM member WHERE healthId ='"+ HealthId+ "'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetElcoNo(Connection c,String Mhealthid)
    {
        String sql = "select ifnull(ElcoNo,'') FROM elco where healthid = '"+ Mhealthid +"'";
        String result = c.ReturnSingleValue(sql);
        return result;
    }
    public static String GetImmunizationDose(Connection c,String imuCode)
    {
        String sql = "select numOfDose FROM immunization where imuCode = '"+ imuCode +"'";
        String result = c.ReturnSingleValue(sql);
        return result;
    }

    public static String Getclassfication(Connection c,String classCode)
    {
        String sql = "select classficationName FROM classfication WHERE classficationCode = '" + classCode +"'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }


    public static String GetsymtomDes(Connection c,String symtomCode)
    {
        String sql = "select symtomDes FROM symtom WHERE symtomCode = '" + symtomCode +"'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetsymtomCode(Connection c,String symtomCode)
    {
        String sql = "select symtomCode FROM symtom WHERE symtomCode = '" + symtomCode +"'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GettreatmentDes(Connection c,String treatmentCode)
    {
        String sql = "select tretmentDes FROM treatment WHERE treatmentCode = '" + treatmentCode +"'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GettreatmentCode(Connection c,String treatmentCode)
    {
        String sql = "select treatmentCode FROM treatment WHERE treatmentCode = '" + treatmentCode +"'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetProblemCode(Connection c,String HealthId,String problemCode,String VDate)
    {
        String sql = "select problemCode FROM  under5ChildProblem WHERE HealthId = '" + HealthId + "' and problemCode= '"+ problemCode+ "' and visitDate= '"+ VDate+ "'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetAdviceCode(Connection c,String HealthId,String AdviceCode,String VDate)

    {
        String sql = "select adviceCode FROM  under5ChildAdvice WHERE HealthId = '" + HealthId + "' and adviceCode= '"+ AdviceCode+ "' and visitDate= '"+ VDate+ "'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetAdolescentProblemCode(Connection c,String problemCode,String VDate)
    {
        String sql = "select problemCode FROM  adolescentProblem WHERE problemCode = '" + problemCode + "' and visitDate= '"+VDate+ "'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetAdolescentDes(Connection c,String AdolescentCode)
    {
        String sql = "select problemDes FROM adoSymtom WHERE problemCode = '" + AdolescentCode +"'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }

    public static String GetAdolescentCode(Connection c,String AdolescentCode)
    {
        String sql = "select problemCode FROM adoSymtom WHERE problemCode = '" + AdolescentCode +"'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }
    public static String GetVisit(Connection c,String HealthId,String ProvCode) {
        String SQL = "";

        SQL = "select ifnull(Count(*),'') from under5Child";
        SQL += " where healthId='" + HealthId + "' and providerId='" +ProvCode + "'";
        String result = c.ReturnSingleValue(SQL);
        return result;
    }
	public static String GetVisitAd(Connection c,String HealthId,String ProvCode) {
		String SQL = "";

		SQL = "select ifnull(Count(*),'') from adolescent";
		SQL += " where healthId='" + HealthId + "' and providerId='" +ProvCode + "'";
		String result = c.ReturnSingleValue(SQL);
		return result;
	}

    public String getDay(String dateStr){

        Date date = null;
        String day=null;

        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

            day = simpleDateFormat.format(date).toUpperCase();
            if(day.equalsIgnoreCase("SUNDAY"))
            {
                day = "রবিবার";
            }
            if(day.equalsIgnoreCase("MONDAY"))
            {

                day = "সোমবার";
            }
            if(day.equalsIgnoreCase("TUESDAY"))
            {

                day = "মঙ্গলবার";
            }
            if(day.equalsIgnoreCase("WEDNESDAY"))
            {
                day = "বুধবার";
            }
            if(day.equalsIgnoreCase("THURSDAY"))
            {
                day = "বৃহস্পতিবার";
            }
            if(day.equalsIgnoreCase("FRIDAY"))
            {

                day = "শুক্রবার";
            }
            if(day.equalsIgnoreCase("SATURDAY"))
            {
                day = "শনিবার";
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return day;
    }


    public static String GetIfImmunizationisGivenForAParticularDate(Connection c,String HealthId,String imuDate)
    {
        String sql = "select systementrydate FROM immunizationHistory WHERE HealthId = '" + HealthId +"' AND imuDate='" + imuDate + "'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }

  /*  public static String Getclassfication(Connection c,String classCode)
    {
        String sql = "select classficationName FROM classfication WHERE classficationCode = '" + classCode +"'";
        String result = c.ReturnSingleValue(sql);
        return result;

    }*/
    //pregNo
    private String _PregNo;
    public void setPregNo(String PregNo){this._PregNo = PregNo;}
    public String getPregNo(){ return this._PregNo;}

    //ChildNo
    private String _ChildNo;
    public void setChildNo(String ChildNo){this._ChildNo = ChildNo;}
    public String getChildNo(){ return this._ChildNo;}

    private String _epiSchedulerId;
    public void setepiSchedulerId(String epiSchedulerId){this._epiSchedulerId = epiSchedulerId;}
    public String getepiSchedulerId(){ return this._epiSchedulerId;}

    private String _epiSubBlockId;
    public void setepiSubBlockId(String epiSubBlockId){this._epiSubBlockId = epiSubBlockId;}
    public String getepiSubBlockId(){ return this._epiSubBlockId;}

    private String _epiScheduleDate;
    public void setepiScheduleDate(String epiScheduleDate){this._epiScheduleDate = epiScheduleDate;}
    public String getepiScheduleDate(){ return this._epiScheduleDate;}

    private String _epiCenterName;
    public void setepiCenterName(String epiCenterName){this._epiCenterName = epiCenterName;}
    public String getepiCenterName(){ return this._epiCenterName;}

    private String _epiName;
    public void setepiName(String epiName){this._epiName = epiName;}
    public String getepiName(){ return this._epiName;}


    private String _epiSex;
    public void setepiSex(String epiSex){this._epiSex = epiSex;}
    public String getepiSex(){ return this._epiSex;}

    private String _epimName;
    public void setepimName(String epimName){this._epimName = epimName;}
    public String getepimName(){ return this._epimName;}

    private String _epifName;
    public void setepifName(String epiName){this._epifName = epiName;}
    public String getepifName(){ return this._epifName;}

    private String _DOB;
    public void setDOB(String DOB){this._DOB = DOB;}
    public String getDOB(){ return this._DOB;}

	private String _ELCONo;
	public void setELCONo(String ELCONo){this._ELCONo = ELCONo;}
	public String getELCONo(){ return this._ELCONo;}




    private String _epiCallForm;
    public void setepiCallForm(String epiCallForm){this._epiCallForm = epiCallForm;}
    public String getepiCallForm(){ return this._epiCallForm;}

    private String _AVDate;
    public void setAVDate(String AVDate){this._AVDate = AVDate;}
    public String getAVDate(){ return this._AVDate;}

    private String _AHHNO;
    public void setAHHNO(String AHHNO){this._AHHNO = AHHNO;}
    public String getHHNO(){ return this._AHHNO;}
    private String _AAge;
    public void setAAge(String AAge){this._AAge = AAge;}
    public String getAAge(){ return this._AAge;}

    private String _AgeY;
    public void setAgeY(String AgeY){this._AgeY = AgeY;}
    public String getAgeY(){ return this._AgeY;}

    private String _AgeM;
    public void setAgeM(String AgeM){this._AgeM = AgeM;}
    public String getAgeM(){ return this._AgeM;}

    private String _AgeD;
    public void setAgeD(String AgeD){this._AgeD = AgeD;}
    public String getAgeD(){ return this._AgeD;}

    private String _AName;
    public void setAName(String AName){this._AName = AName;}
    public String getAName(){ return this._AName;}
    private String _AAgeMDY;
    public void setAAgeMDY(String AAgeMDY){this._AAgeMDY = AAgeMDY;}
    public String getAAgeMDY(){ return this._AAgeMDY;}

    private String _ASex;
    public void setASex(String ASex){this._ASex = ASex;}
    public String getASex(){ return this._ASex;}

    private String _AMElco;
    public void setAMElco(String AMElco){this._AMElco = AMElco;}
    public String getAMElco(){ return this._AMElco;}


    private String _AGroup;
    public void setAGroup(String AGroup){this._AGroup = AGroup;}
    public String getAGroup(){ return this._AGroup;}

    private String _AAgeD;
    public void setAAgeD(String AAgeD){this._AAgeD = AAgeD;}
    public String getAAgeD(){ return this._AAgeD;}

    private String _AAgeM;
    public void setAAgeM(String AAgeM){this._AAgeM = AAgeM;}
    public String getAAgeM(){ return this._AAgeM;}

    private String _AAgeY;
    public void setAAgeY(String AAgeY){this._AAgeY = AAgeY;}
    public String getAAgeY(){ return this._AAgeM;}

    private String _GeneratedId;
    public void setGeneratedId(String GeneratedId){this._GeneratedId = GeneratedId;}
    public String getGeneratedId(){ return this._GeneratedId;}


    private String _MotherName;
    public void setMotherName(String MotherName){this._MotherName = MotherName;}
    public String getMotherName(){ return this._MotherName;}

    private String _FatherName;
    public void setFatherName(String FatherName){this._FatherName = FatherName;}
    public String getFatherName(){ return this._FatherName;}

    private String _HusName;
    public void setHusName(String HusName){this._HusName = HusName;}
    public String getHusName(){ return this._HusName;}

   // private String get_HHNo


}

