package com.data.rhis2;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;

public class ses extends Activity {
    boolean netwoekAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;

    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event) {
        if (iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        } else {
            return true;
        }
    }

    //Top menu
    //--------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(ses.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                adb.show();
                return true;
        }
        return false;
    }

    String VariableID;
    private int hour;
    private int minute;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;

    Connection C;
    Global g;
    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private String TableName;

    LinearLayout seclblSS;
    LinearLayout secHHNo;
    TextView VlblHHNo;
    EditText txtHHNo;
    LinearLayout secstatus;
    TextView Vlblstatus;
    Spinner spnstatus;
    LinearLayout secQ102;
    TextView VlblQ102;
    Spinner spnQ102;
    LinearLayout secQ1021;
    TextView VlblQ1021;
    EditText txtQ1021;
    LinearLayout secQ107;
    TextView VlblQ107;
    Spinner spnQ107;
    LinearLayout secQ1071;
    TextView VlblQ1071;
    EditText txtQ1071;
    LinearLayout seclbl110;
    LinearLayout secQ110a;
    TextView VlblQ110a;
    RadioGroup rdogrpQ110a;

    RadioButton rdoQ110a1;
    RadioButton rdoQ110a2;
    LinearLayout secQ110b;
    TextView VlblQ110b;
    RadioGroup rdogrpQ110b;

    RadioButton rdoQ110b1;
    RadioButton rdoQ110b2;
    LinearLayout secQ110c;
    TextView VlblQ110c;
    RadioGroup rdogrpQ110c;

    RadioButton rdoQ110c1;
    RadioButton rdoQ110c2;
    LinearLayout secQ110d;
    TextView VlblQ110d;
    RadioGroup rdogrpQ110d;

    RadioButton rdoQ110d1;
    RadioButton rdoQ110d2;
    LinearLayout secQ110e;
    TextView VlblQ110e;
    RadioGroup rdogrpQ110e;

    RadioButton rdoQ110e1;
    RadioButton rdoQ110e2;
    RadioButton rdoQ110e3;
    LinearLayout secQ110f;
    TextView VlblQ110f;
    RadioGroup rdogrpQ110f;

    RadioButton rdoQ110f1;
    RadioButton rdoQ110f2;
    LinearLayout secQ110g;
    TextView VlblQ110g;
    RadioGroup rdogrpQ110g;

    RadioButton rdoQ110g1;
    RadioButton rdoQ110g2;
    LinearLayout secQ110h;
    TextView VlblQ110h;
    RadioGroup rdogrpQ110h;

    RadioButton rdoQ110h1;
    RadioButton rdoQ110h2;
    LinearLayout secQ110i;
    TextView VlblQ110i;
    RadioGroup rdogrpQ110i;

    RadioButton rdoQ110i1;
    RadioButton rdoQ110i2;
    LinearLayout secQ110j;
    TextView VlblQ110j;
    RadioGroup rdogrpQ110j;

    RadioButton rdoQ110j1;
    RadioButton rdoQ110j2;
    LinearLayout secQ110k;
    TextView VlblQ110k;
    RadioGroup rdogrpQ110k;

    RadioButton rdoQ110k1;
    RadioButton rdoQ110k2;
    LinearLayout secQ110l;
    TextView VlblQ110l;
    RadioGroup rdogrpQ110l;

    RadioButton rdoQ110l1;
    RadioButton rdoQ110l2;
    LinearLayout secQ110m;
    TextView VlblQ110m;
    RadioGroup rdogrpQ110m;

    RadioButton rdoQ110m1;
    RadioButton rdoQ110m2;
    LinearLayout secQ110n;
    TextView VlblQ110n;
    RadioGroup rdogrpQ110n;

    RadioButton rdoQ110n1;
    RadioButton rdoQ110n2;
    LinearLayout secQ110o;
    TextView VlblQ110o;
    RadioGroup rdogrpQ110o;

    RadioButton rdoQ110o1;
    RadioButton rdoQ110o2;
    LinearLayout secQ110p;
    TextView VlblQ110p;
    RadioGroup rdogrpQ110p;

    RadioButton rdoQ110p1;
    RadioButton rdoQ110p2;
    LinearLayout secQ111;
    TextView VlblQ111;
    Spinner spnQ111;
    LinearLayout secQ1111;
    TextView VlblQ1111;
    EditText txtQ1111;
    LinearLayout secQ114;
    TextView VlblQ114;
    Spinner spnQ114;
    LinearLayout secQ1141;
    TextView VlblQ1141;
    EditText txtQ1141;
    LinearLayout secQ115;
    TextView VlblQ115;
    Spinner spnQ115;
    LinearLayout secQ1151;
    TextView VlblQ1151;
    EditText txtQ1151;
    LinearLayout secQ116;
    TextView VlblQ116;
    Spinner spnQ116;
    LinearLayout secQ1161;
    TextView VlblQ1161;
    EditText txtQ1161;
    LinearLayout seclbl118;
    LinearLayout secQ118a;
    TextView VlblQ118a;
    RadioGroup rdogrpQ118a;

    RadioButton rdoQ118a1;
    RadioButton rdoQ118a2;
    LinearLayout secQ118b;
    TextView VlblQ118b;
    RadioGroup rdogrpQ118b;

    RadioButton rdoQ118b1;
    RadioButton rdoQ118b2;
    LinearLayout secQ118c;
    TextView VlblQ118c;
    RadioGroup rdogrpQ118c;

    RadioButton rdoQ118c1;
    RadioButton rdoQ118c2;
    LinearLayout secQ118d;
    TextView VlblQ118d;
    RadioGroup rdogrpQ118d;

    RadioButton rdoQ118d1;
    RadioButton rdoQ118d2;
    LinearLayout secQ118e;
    TextView VlblQ118e;
    RadioGroup rdogrpQ118e;

    RadioButton rdoQ118e1;
    RadioButton rdoQ118e2;

    String StartTime;

    String Dist = "";
    String Upz = "";
    String UN = "";
    String Mouza = "";
    String Vill = "";
    String ProvType = "";
    String ProvCode = "";
    //String HHNo = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.ses);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "ses";

            Dist = g.getDistrict();
            Upz = g.getUpazila();
            UN = g.getUnion();
            Mouza = g.getMouza();
            Vill = g.getVillage();
            ProvType = g.getProvType();
            ProvCode = g.getProvCode();

            seclblSS = (LinearLayout) findViewById(R.id.seclblSS);

            //secHHNo=(LinearLayout)findViewById(R.id.secHHNo);
            //VlblHHNo=(TextView) findViewById(R.id.VlblHHNo);
            txtHHNo = (EditText) findViewById(R.id.txtHHNo);
            txtHHNo.setText(g.getHouseholdNo());
            txtHHNo.setEnabled(false);

            secstatus = (LinearLayout) findViewById(R.id.secstatus);
            Vlblstatus = (TextView) findViewById(R.id.Vlblstatus);
            spnstatus = (Spinner) findViewById(R.id.spnstatus);
            List<String> liststatus = new ArrayList<String>();

            liststatus.add("");
            liststatus.add("1-সম্পূর্ণ");
            liststatus.add("2-তথ্য প্রদানে অসন্মতি");
            liststatus.add("7-অন্যান্য");
            ArrayAdapter<String> adptrstatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liststatus);
            spnstatus.setAdapter(adptrstatus);

            spnstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnstatus.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnstatus.getSelectedItem().toString(), 1);
                    secQ1021.setVisibility(View.GONE);
                    secQ1071.setVisibility(View.GONE);
                    secQ1111.setVisibility(View.GONE);
                    secQ1141.setVisibility(View.GONE);
                    secQ1151.setVisibility(View.GONE);
                    secQ1161.setVisibility(View.GONE);

                    if (spnData.equalsIgnoreCase("2") | spnData.equalsIgnoreCase("7")) {
                        secQ102.setVisibility(View.GONE);
                        secQ1021.setVisibility(View.GONE);
                        secQ107.setVisibility(View.GONE);
                        secQ1071.setVisibility(View.GONE);

                        seclbl110.setVisibility(View.GONE);
                        secQ110a.setVisibility(View.GONE);
                        secQ110b.setVisibility(View.GONE);
                        secQ110c.setVisibility(View.GONE);
                        secQ110d.setVisibility(View.GONE);
                        secQ110e.setVisibility(View.GONE);
                        secQ110f.setVisibility(View.GONE);
                        secQ110g.setVisibility(View.GONE);
                        secQ110h.setVisibility(View.GONE);
                        secQ110i.setVisibility(View.GONE);
                        secQ110j.setVisibility(View.GONE);
                        secQ110k.setVisibility(View.GONE);
                        secQ110l.setVisibility(View.GONE);
                        secQ110m.setVisibility(View.GONE);
                        secQ110n.setVisibility(View.GONE);
                        secQ110o.setVisibility(View.GONE);
                        secQ110p.setVisibility(View.GONE);
                        secQ111.setVisibility(View.GONE);
                        secQ1111.setVisibility(View.GONE);
                        secQ114.setVisibility(View.GONE);
                        secQ1141.setVisibility(View.GONE);
                        secQ115.setVisibility(View.GONE);
                        secQ1151.setVisibility(View.GONE);
                        secQ116.setVisibility(View.GONE);
                        secQ1161.setVisibility(View.GONE);

                        seclbl118.setVisibility(View.GONE);
                        secQ118a.setVisibility(View.GONE);
                        secQ118b.setVisibility(View.GONE);
                        secQ118c.setVisibility(View.GONE);
                        secQ118d.setVisibility(View.GONE);
                        secQ118e.setVisibility(View.GONE);
                    } else {
                        secQ102.setVisibility(View.VISIBLE);
                        secQ1021.setVisibility(View.VISIBLE);
                        secQ107.setVisibility(View.VISIBLE);
                        secQ1071.setVisibility(View.VISIBLE);

                        seclbl110.setVisibility(View.VISIBLE);
                        secQ110a.setVisibility(View.VISIBLE);
                        secQ110b.setVisibility(View.VISIBLE);
                        secQ110c.setVisibility(View.VISIBLE);
                        secQ110d.setVisibility(View.VISIBLE);
                        secQ110e.setVisibility(View.VISIBLE);
                        secQ110f.setVisibility(View.VISIBLE);
                        secQ110g.setVisibility(View.VISIBLE);
                        secQ110h.setVisibility(View.VISIBLE);
                        secQ110i.setVisibility(View.VISIBLE);
                        secQ110j.setVisibility(View.VISIBLE);
                        secQ110k.setVisibility(View.VISIBLE);
                        secQ110l.setVisibility(View.VISIBLE);
                        secQ110m.setVisibility(View.VISIBLE);
                        secQ110n.setVisibility(View.VISIBLE);
                        secQ110o.setVisibility(View.VISIBLE);
                        secQ110p.setVisibility(View.VISIBLE);
                        secQ111.setVisibility(View.VISIBLE);
                        secQ1111.setVisibility(View.VISIBLE);
                        secQ114.setVisibility(View.VISIBLE);
                        secQ1141.setVisibility(View.VISIBLE);
                        secQ115.setVisibility(View.VISIBLE);
                        secQ1151.setVisibility(View.VISIBLE);
                        secQ116.setVisibility(View.VISIBLE);
                        secQ1161.setVisibility(View.VISIBLE);

                        seclbl118.setVisibility(View.VISIBLE);
                        secQ118a.setVisibility(View.VISIBLE);
                        secQ118b.setVisibility(View.VISIBLE);
                        secQ118c.setVisibility(View.VISIBLE);
                        secQ118d.setVisibility(View.VISIBLE);
                        secQ118e.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ102 = (LinearLayout) findViewById(R.id.secQ102);
            VlblQ102 = (TextView) findViewById(R.id.VlblQ102);
            spnQ102 = (Spinner) findViewById(R.id.spnQ102);
            List<String> listQ102 = new ArrayList<String>();

            listQ102.add("");
            listQ102.add("11-ঘরের মধ্যে পাইপের পানি ");
            listQ102.add("12-বাড়ির চত্তরে /আঙ্গিনায় পাইপ");
            listQ102.add("13-সরকারি(পাবলিক ট্যাপ/স্থায়ী ট্যাপ)");
            listQ102.add("21- টিউবওয়েল (নলকুপ)");
            listQ102.add("31-সংরক্ষিত কুপ/ইন্দিরা");
            listQ102.add("32-অসংরক্ষিত কুপ/ইন্দিরা");
            listQ102.add("41-সংরক্ষিত ঝর্নার পানি");
            listQ102.add("42-অসংরক্ষিত ঝর্নার পানি");
            listQ102.add("51-সংগ্রহী্ত বৃষ্টির পানি");
            listQ102.add("61- ট্যাংকার ট্রাক");
            listQ102.add("71-ছোট ট্যাঙ্ক");
            listQ102.add("81- ভু-পৃষ্ঠের পানি(নদী, খাল, পুকুর, লেক, সেচের পানির নালা ইত্যাদি)    ");
            listQ102.add("91-বোতলের পানি");
            listQ102.add("96- অন্যান্য ");
            ArrayAdapter<String> adptrQ102 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ102);
            spnQ102.setAdapter(adptrQ102);

            spnQ102.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ102.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ102.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ1021.setVisibility(View.GONE);
                    } else {
                        secQ1021.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ1021 = (LinearLayout) findViewById(R.id.secQ1021);
            VlblQ1021 = (TextView) findViewById(R.id.VlblQ1021);
            txtQ1021 = (EditText) findViewById(R.id.txtQ1021);
            secQ107 = (LinearLayout) findViewById(R.id.secQ107);
            VlblQ107 = (TextView) findViewById(R.id.VlblQ107);
            spnQ107 = (Spinner) findViewById(R.id.spnQ107);
            List<String> listQ107 = new ArrayList<String>();

            listQ107.add("");
            listQ107.add("11-ফ্লাশ করে পাইপের মাধ্যমে অপসারণ ");
            listQ107.add("12-ফ্লাশ করে ট্যাংকে ধারণ");
            listQ107.add("13-ফ্লাশ করে গর্তে ধারণ");
            listQ107.add("14-ফ্লাশ করে অন্য কোথায় ও অপসারণ");
            listQ107.add("15-ফ্লাশ করে কোথায় অপসারিত হয় তা জানিনা");
            listQ107.add("21-বায়ু চলাচলের ব্যবস্থাসহ উন্নতমানের পিট ল্যাট্রিন");
            listQ107.add("22-পিট ল্যাট্রিন (স্ন্যাবসহ)");
            listQ107.add("23-পিট ল্যাট্রিন (স্ন্যাবসহবিহীন)/খোলা গর্ত");
            listQ107.add("31-কম্পোস্টিং   ল্যাট্রিন");
            listQ107.add("41-বাকেট ল্যাট্রিন");
            listQ107.add("51-খোলা/ঝুলন্ত ল্যাট্রিন");
            listQ107.add("61-ল্যাট্রিন নাই/ঝোপ-ঝাড়/মাঠ");
            listQ107.add("96-অন্যান্য ");
            ArrayAdapter<String> adptrQ107 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ107);
            spnQ107.setAdapter(adptrQ107);

            spnQ107.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ107.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ107.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ1071.setVisibility(View.GONE);
                    } else {
                        secQ1071.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ1071 = (LinearLayout) findViewById(R.id.secQ1071);
            VlblQ1071 = (TextView) findViewById(R.id.VlblQ1071);
            txtQ1071 = (EditText) findViewById(R.id.txtQ1071);
            seclbl110 = (LinearLayout) findViewById(R.id.seclbl110);
            secQ110a = (LinearLayout) findViewById(R.id.secQ110a);
            VlblQ110a = (TextView) findViewById(R.id.VlblQ110a);
            rdogrpQ110a = (RadioGroup) findViewById(R.id.rdogrpQ110a);

            rdoQ110a1 = (RadioButton) findViewById(R.id.rdoQ110a1);
            rdoQ110a2 = (RadioButton) findViewById(R.id.rdoQ110a2);
            secQ110b = (LinearLayout) findViewById(R.id.secQ110b);
            VlblQ110b = (TextView) findViewById(R.id.VlblQ110b);
            rdogrpQ110b = (RadioGroup) findViewById(R.id.rdogrpQ110b);

            rdoQ110b1 = (RadioButton) findViewById(R.id.rdoQ110b1);
            rdoQ110b2 = (RadioButton) findViewById(R.id.rdoQ110b2);
            secQ110c = (LinearLayout) findViewById(R.id.secQ110c);
            VlblQ110c = (TextView) findViewById(R.id.VlblQ110c);
            rdogrpQ110c = (RadioGroup) findViewById(R.id.rdogrpQ110c);

            rdoQ110c1 = (RadioButton) findViewById(R.id.rdoQ110c1);
            rdoQ110c2 = (RadioButton) findViewById(R.id.rdoQ110c2);
            secQ110d = (LinearLayout) findViewById(R.id.secQ110d);
            VlblQ110d = (TextView) findViewById(R.id.VlblQ110d);
            rdogrpQ110d = (RadioGroup) findViewById(R.id.rdogrpQ110d);

            rdoQ110d1 = (RadioButton) findViewById(R.id.rdoQ110d1);
            rdoQ110d2 = (RadioButton) findViewById(R.id.rdoQ110d2);
            secQ110e = (LinearLayout) findViewById(R.id.secQ110e);
            VlblQ110e = (TextView) findViewById(R.id.VlblQ110e);
            rdogrpQ110e = (RadioGroup) findViewById(R.id.rdogrpQ110e);

            rdoQ110e1 = (RadioButton) findViewById(R.id.rdoQ110e1);
            rdoQ110e2 = (RadioButton) findViewById(R.id.rdoQ110e2);
            secQ110f = (LinearLayout) findViewById(R.id.secQ110f);
            VlblQ110f = (TextView) findViewById(R.id.VlblQ110f);
            rdogrpQ110f = (RadioGroup) findViewById(R.id.rdogrpQ110f);

            rdoQ110f1 = (RadioButton) findViewById(R.id.rdoQ110f1);
            rdoQ110f2 = (RadioButton) findViewById(R.id.rdoQ110f2);
            secQ110g = (LinearLayout) findViewById(R.id.secQ110g);
            VlblQ110g = (TextView) findViewById(R.id.VlblQ110g);
            rdogrpQ110g = (RadioGroup) findViewById(R.id.rdogrpQ110g);

            rdoQ110g1 = (RadioButton) findViewById(R.id.rdoQ110g1);
            rdoQ110g2 = (RadioButton) findViewById(R.id.rdoQ110g2);
            secQ110h = (LinearLayout) findViewById(R.id.secQ110h);
            VlblQ110h = (TextView) findViewById(R.id.VlblQ110h);
            rdogrpQ110h = (RadioGroup) findViewById(R.id.rdogrpQ110h);

            rdoQ110h1 = (RadioButton) findViewById(R.id.rdoQ110h1);
            rdoQ110h2 = (RadioButton) findViewById(R.id.rdoQ110h2);
            secQ110i = (LinearLayout) findViewById(R.id.secQ110i);
            VlblQ110i = (TextView) findViewById(R.id.VlblQ110i);
            rdogrpQ110i = (RadioGroup) findViewById(R.id.rdogrpQ110i);

            rdoQ110i1 = (RadioButton) findViewById(R.id.rdoQ110i1);
            rdoQ110i2 = (RadioButton) findViewById(R.id.rdoQ110i2);
            secQ110j = (LinearLayout) findViewById(R.id.secQ110j);
            VlblQ110j = (TextView) findViewById(R.id.VlblQ110j);
            rdogrpQ110j = (RadioGroup) findViewById(R.id.rdogrpQ110j);

            rdoQ110j1 = (RadioButton) findViewById(R.id.rdoQ110j1);
            rdoQ110j2 = (RadioButton) findViewById(R.id.rdoQ110j2);
            secQ110k = (LinearLayout) findViewById(R.id.secQ110k);
            VlblQ110k = (TextView) findViewById(R.id.VlblQ110k);
            rdogrpQ110k = (RadioGroup) findViewById(R.id.rdogrpQ110k);

            rdoQ110k1 = (RadioButton) findViewById(R.id.rdoQ110k1);
            rdoQ110k2 = (RadioButton) findViewById(R.id.rdoQ110k2);
            secQ110l = (LinearLayout) findViewById(R.id.secQ110l);
            VlblQ110l = (TextView) findViewById(R.id.VlblQ110l);
            rdogrpQ110l = (RadioGroup) findViewById(R.id.rdogrpQ110l);

            rdoQ110l1 = (RadioButton) findViewById(R.id.rdoQ110l1);
            rdoQ110l2 = (RadioButton) findViewById(R.id.rdoQ110l2);
            secQ110m = (LinearLayout) findViewById(R.id.secQ110m);
            VlblQ110m = (TextView) findViewById(R.id.VlblQ110m);
            rdogrpQ110m = (RadioGroup) findViewById(R.id.rdogrpQ110m);

            rdoQ110m1 = (RadioButton) findViewById(R.id.rdoQ110m1);
            rdoQ110m2 = (RadioButton) findViewById(R.id.rdoQ110m2);
            secQ110n = (LinearLayout) findViewById(R.id.secQ110n);
            VlblQ110n = (TextView) findViewById(R.id.VlblQ110n);
            rdogrpQ110n = (RadioGroup) findViewById(R.id.rdogrpQ110n);

            rdoQ110n1 = (RadioButton) findViewById(R.id.rdoQ110n1);
            rdoQ110n2 = (RadioButton) findViewById(R.id.rdoQ110n2);
            secQ110o = (LinearLayout) findViewById(R.id.secQ110o);
            VlblQ110o = (TextView) findViewById(R.id.VlblQ110o);
            rdogrpQ110o = (RadioGroup) findViewById(R.id.rdogrpQ110o);

            rdoQ110o1 = (RadioButton) findViewById(R.id.rdoQ110o1);
            rdoQ110o2 = (RadioButton) findViewById(R.id.rdoQ110o2);
            secQ110p = (LinearLayout) findViewById(R.id.secQ110p);
            VlblQ110p = (TextView) findViewById(R.id.VlblQ110p);
            rdogrpQ110p = (RadioGroup) findViewById(R.id.rdogrpQ110p);

            rdoQ110p1 = (RadioButton) findViewById(R.id.rdoQ110p1);
            rdoQ110p2 = (RadioButton) findViewById(R.id.rdoQ110p2);
            secQ111 = (LinearLayout) findViewById(R.id.secQ111);
            VlblQ111 = (TextView) findViewById(R.id.VlblQ111);
            spnQ111 = (Spinner) findViewById(R.id.spnQ111);
            List<String> listQ111 = new ArrayList<String>();

            listQ111.add("");
            listQ111.add("01-বিদ্যুৎ ");
            listQ111.add("02-তরল গ্যাস");
            listQ111.add("03-প্রকৃতিক গ্যাস");
            listQ111.add("04-বায়ো গ্যাস");
            listQ111.add("05-কেরোসিন");
            listQ111.add("06-কয়লা /লিগনাইট");
            listQ111.add("07- চারকৌ্ল");
            listQ111.add("08-কাঠ");
            listQ111.add("09-ভুষি/তুষ/ঘাস ");
            listQ111.add("10-ফসলের অবশিষ্টাংস (খড়/কুটা/পাতা)");
            listQ111.add("11-গোবর");
            listQ111.add("95-খানায় রান্না হয় না");
            listQ111.add("96-অন্যান্য");
            ArrayAdapter<String> adptrQ111 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ111);
            spnQ111.setAdapter(adptrQ111);

            spnQ111.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ111.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ111.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ1111.setVisibility(View.GONE);
                    } else {
                        secQ1111.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ1111 = (LinearLayout) findViewById(R.id.secQ1111);
            VlblQ1111 = (TextView) findViewById(R.id.VlblQ1111);
            txtQ1111 = (EditText) findViewById(R.id.txtQ1111);
            secQ114 = (LinearLayout) findViewById(R.id.secQ114);
            VlblQ114 = (TextView) findViewById(R.id.VlblQ114);
            spnQ114 = (Spinner) findViewById(R.id.spnQ114);
            List<String> listQ114 = new ArrayList<String>();

            listQ114.add("");
            listQ114.add("11-মাটি/বালু ");
            listQ114.add("21-কাঠের তক্তা");
            listQ114.add("22-তাল গাছ/ বাঁশ");
            listQ114.add("31-নকশা করা কাঠের পাটাতন/পালিশকৃত কাঠ");
            listQ114.add("33-সিরামিক টাইলস/মোজাইক");
            listQ114.add("34-সিমেন্ট ");
            listQ114.add("35-কারপেট");
            listQ114.add("96-অন্যান্য");

            ArrayAdapter<String> adptrQ114 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ114);
            spnQ114.setAdapter(adptrQ114);

            spnQ114.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ114.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ114.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ1141.setVisibility(View.GONE);
                    } else {
                        secQ1141.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ1141 = (LinearLayout) findViewById(R.id.secQ1141);
            VlblQ1141 = (TextView) findViewById(R.id.VlblQ1141);
            txtQ1141 = (EditText) findViewById(R.id.txtQ1141);
            secQ115 = (LinearLayout) findViewById(R.id.secQ115);
            VlblQ115 = (TextView) findViewById(R.id.VlblQ115);
            spnQ115 = (Spinner) findViewById(R.id.spnQ115);
            List<String> listQ115 = new ArrayList<String>();

            listQ115.add("");
            listQ115.add("11-ছাদ নেই");
            listQ115.add("12-খড়/ছন/তাল্পাতা");
            listQ115.add("22-বাঁশ/তালগাছ");
            listQ115.add("23-কাঠের তক্তা");
            listQ115.add("24-কার্ডবোর্ড(কাগজের তৈরি বোর্ড)");
            listQ115.add("31-টিন");
            listQ115.add("32-কাঠ");
            listQ115.add("34-সিরামিক  টাইল্স");
            listQ115.add("35-সিমেন্ট ");
            listQ115.add("36-কাষ্ঠ খন্ড(টালি বা শ্লেট)");
            listQ115.add("96-অন্যান্য");

            ArrayAdapter<String> adptrQ115 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ115);
            spnQ115.setAdapter(adptrQ115);
            spnQ115.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ115.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ115.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ1151.setVisibility(View.GONE);
                    } else {
                        secQ1151.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });


            secQ1151 = (LinearLayout) findViewById(R.id.secQ1151);
            VlblQ1151 = (TextView) findViewById(R.id.VlblQ1151);
            txtQ1151 = (EditText) findViewById(R.id.txtQ1151);
            secQ116 = (LinearLayout) findViewById(R.id.secQ116);
            VlblQ116 = (TextView) findViewById(R.id.VlblQ116);
            spnQ116 = (Spinner) findViewById(R.id.spnQ116);
            List<String> listQ116 = new ArrayList<String>();

            listQ116.add("");
            listQ116.add("11-দেয়াল নাই");
            listQ116.add("12-পাট কাঠি/বেত/তাল গাছ//গাছের গুড়ি");
            listQ116.add("13-মাটি");
            listQ116.add("21-মাটিসহ বাঁশ");
            listQ116.add("22-মাটিসহ পাথর");
            listQ116.add("24-প্লাই  উড");
            listQ116.add("25-কার্ডবোর্ড");
            listQ116.add("31-টিন");
            listQ116.add("32-সিমেন্ট (প্লাস্টার সহ)");
            listQ116.add("33-চুনাপাথর/ সিমেন্ট");
            listQ116.add("34-ইট(প্লাস্টার ছাড়া)");
            listQ116.add("36-কাঠের তক্তা");
            listQ116.add("96-অন্যান্য");
            ArrayAdapter<String> adptrQ116 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ116);
            spnQ116.setAdapter(adptrQ116);
            spnQ116.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ116.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ116.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ1161.setVisibility(View.GONE);
                    } else {
                        secQ1161.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });


            secQ1161 = (LinearLayout) findViewById(R.id.secQ1161);
            VlblQ1161 = (TextView) findViewById(R.id.VlblQ1161);
            txtQ1161 = (EditText) findViewById(R.id.txtQ1161);
            seclbl118 = (LinearLayout) findViewById(R.id.seclbl118);
            secQ118a = (LinearLayout) findViewById(R.id.secQ118a);
            VlblQ118a = (TextView) findViewById(R.id.VlblQ118a);
            rdogrpQ118a = (RadioGroup) findViewById(R.id.rdogrpQ118a);

            rdoQ118a1 = (RadioButton) findViewById(R.id.rdoQ118a1);
            rdoQ118a2 = (RadioButton) findViewById(R.id.rdoQ118a2);
            secQ118b = (LinearLayout) findViewById(R.id.secQ118b);
            VlblQ118b = (TextView) findViewById(R.id.VlblQ118b);
            rdogrpQ118b = (RadioGroup) findViewById(R.id.rdogrpQ118b);

            rdoQ118b1 = (RadioButton) findViewById(R.id.rdoQ118b1);
            rdoQ118b2 = (RadioButton) findViewById(R.id.rdoQ118b2);
            secQ118c = (LinearLayout) findViewById(R.id.secQ118c);
            VlblQ118c = (TextView) findViewById(R.id.VlblQ118c);
            rdogrpQ118c = (RadioGroup) findViewById(R.id.rdogrpQ118c);

            rdoQ118c1 = (RadioButton) findViewById(R.id.rdoQ118c1);
            rdoQ118c2 = (RadioButton) findViewById(R.id.rdoQ118c2);
            secQ118d = (LinearLayout) findViewById(R.id.secQ118d);
            VlblQ118d = (TextView) findViewById(R.id.VlblQ118d);
            rdogrpQ118d = (RadioGroup) findViewById(R.id.rdogrpQ118d);

            rdoQ118d1 = (RadioButton) findViewById(R.id.rdoQ118d1);
            rdoQ118d2 = (RadioButton) findViewById(R.id.rdoQ118d2);
            secQ118e = (LinearLayout) findViewById(R.id.secQ118e);
            VlblQ118e = (TextView) findViewById(R.id.VlblQ118e);
            rdogrpQ118e = (RadioGroup) findViewById(R.id.rdogrpQ118e);

            rdoQ118e1 = (RadioButton) findViewById(R.id.rdoQ118e1);
            rdoQ118e2 = (RadioButton) findViewById(R.id.rdoQ118e2);


            secQ102.setVisibility(View.GONE);
            secQ1021.setVisibility(View.GONE);
            secQ107.setVisibility(View.GONE);
            secQ1071.setVisibility(View.GONE);
            seclbl110.setVisibility(View.GONE);
            secQ110a.setVisibility(View.GONE);
            secQ110b.setVisibility(View.GONE);
            secQ110c.setVisibility(View.GONE);
            secQ110d.setVisibility(View.GONE);
            secQ110e.setVisibility(View.GONE);
            secQ110f.setVisibility(View.GONE);
            secQ110g.setVisibility(View.GONE);
            secQ110h.setVisibility(View.GONE);
            secQ110i.setVisibility(View.GONE);
            secQ110j.setVisibility(View.GONE);
            secQ110k.setVisibility(View.GONE);
            secQ110l.setVisibility(View.GONE);
            secQ110m.setVisibility(View.GONE);
            secQ110n.setVisibility(View.GONE);
            secQ110o.setVisibility(View.GONE);
            secQ110p.setVisibility(View.GONE);
            secQ111.setVisibility(View.GONE);
            secQ1111.setVisibility(View.GONE);
            secQ114.setVisibility(View.GONE);
            secQ1141.setVisibility(View.GONE);
            secQ115.setVisibility(View.GONE);
            secQ1151.setVisibility(View.GONE);
            secQ116.setVisibility(View.GONE);
            secQ1161.setVisibility(View.GONE);
            seclbl118.setVisibility(View.GONE);
            secQ118a.setVisibility(View.GONE);
            secQ118b.setVisibility(View.GONE);
            secQ118c.setVisibility(View.GONE);
            secQ118d.setVisibility(View.GONE);
            secQ118e.setVisibility(View.GONE);

            secQ1021.setVisibility(View.GONE);
            secQ1071.setVisibility(View.GONE);
            secQ1111.setVisibility(View.GONE);
            secQ1141.setVisibility(View.GONE);
            secQ1151.setVisibility(View.GONE);
            secQ1161.setVisibility(View.GONE);


            DataSearch(Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, txtHHNo.getText().toString());

            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(ses.this, e.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {

            String DV = "";

            if (txtHHNo.getText().toString().length() == 0) {
                Connection.MessageBox(ses.this, "Required field:HHNo.");
                txtHHNo.requestFocus();
                return;
            } else if (spnstatus.getSelectedItemPosition() == 0 & secstatus.isShown()) {
                Connection.MessageBox(ses.this, "Required field:ভিজিটের ফলাফল.");
                spnstatus.requestFocus();
                return;
            } else if (spnQ102.getSelectedItemPosition() == 0 & secQ102.isShown()) {
                Connection.MessageBox(ses.this, "Required field:আপনার খানার সদস্যদের খাবার পানি পান করার প্রধান উৎস কি ? .");
                spnQ102.requestFocus();
                return;
            } else if (txtQ1021.getText().toString().length() == 0 & secQ1021.isShown()) {
                Connection.MessageBox(ses.this, "Required field:অন্যান্য.");
                txtQ1021.requestFocus();
                return;
            } else if (spnQ107.getSelectedItemPosition() == 0 & secQ107.isShown()) {
                Connection.MessageBox(ses.this, "Required field:এ খানার সদস্যরা সাধারনত কোন ধরনের পায়খানা ব্যবহার করে ? .");
                spnQ107.requestFocus();
                return;
            } else if (txtQ1071.getText().toString().length() == 0 & secQ1071.isShown()) {
                Connection.MessageBox(ses.this, "Required field:অন্যান্য.");
                txtQ1071.requestFocus();
                return;
            } else if (!rdoQ110a1.isChecked() & !rdoQ110a2.isChecked() & secQ110a.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110a.");
                rdoQ110a1.requestFocus();
                return;
            } else if (!rdoQ110b1.isChecked() & !rdoQ110b2.isChecked() & secQ110b.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110b.");
                rdoQ110b1.requestFocus();
                return;
            } else if (!rdoQ110c1.isChecked() & !rdoQ110c2.isChecked() & secQ110c.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110c.");
                rdoQ110c1.requestFocus();
                return;
            } else if (!rdoQ110d1.isChecked() & !rdoQ110d2.isChecked() & secQ110d.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110d.");
                rdoQ110d1.requestFocus();
                return;
            } else if (!rdoQ110e1.isChecked() & !rdoQ110e2.isChecked() & secQ110e.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110e.");
                rdoQ110e1.requestFocus();
                return;
            } else if (!rdoQ110f1.isChecked() & !rdoQ110f2.isChecked() & secQ110f.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110f.");
                rdoQ110f1.requestFocus();
                return;
            } else if (!rdoQ110g1.isChecked() & !rdoQ110g2.isChecked() & secQ110g.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110g.");
                rdoQ110g1.requestFocus();
                return;
            } else if (!rdoQ110h1.isChecked() & !rdoQ110h2.isChecked() & secQ110h.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110h.");
                rdoQ110h1.requestFocus();
                return;
            } else if (!rdoQ110i1.isChecked() & !rdoQ110i2.isChecked() & secQ110i.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110i.");
                rdoQ110i1.requestFocus();
                return;
            } else if (!rdoQ110j1.isChecked() & !rdoQ110j2.isChecked() & secQ110j.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110j.");
                rdoQ110j1.requestFocus();
                return;
            } else if (!rdoQ110k1.isChecked() & !rdoQ110k2.isChecked() & secQ110k.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110k.");
                rdoQ110k1.requestFocus();
                return;
            } else if (!rdoQ110l1.isChecked() & !rdoQ110l2.isChecked() & secQ110l.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110l.");
                rdoQ110l1.requestFocus();
                return;
            } else if (!rdoQ110m1.isChecked() & !rdoQ110m2.isChecked() & secQ110m.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110m.");
                rdoQ110m1.requestFocus();
                return;
            } else if (!rdoQ110n1.isChecked() & !rdoQ110n2.isChecked() & secQ110n.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110n.");
                rdoQ110n1.requestFocus();
                return;
            } else if (!rdoQ110o1.isChecked() & !rdoQ110o2.isChecked() & secQ110o.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110o.");
                rdoQ110o1.requestFocus();
                return;
            } else if (!rdoQ110p1.isChecked() & !rdoQ110p2.isChecked() & secQ110p.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q110p.");
                rdoQ110p1.requestFocus();
                return;
            } else if (spnQ111.getSelectedItemPosition() == 0 & secQ111.isShown()) {
                Connection.MessageBox(ses.this, "Required field:আপনার খানায় রান্নার জন্য প্রধানত কী ধরণের জ্বালানী ব্যবহার করা হয় ? .");
                spnQ111.requestFocus();
                return;
            } else if (txtQ1111.getText().toString().length() == 0 & secQ1111.isShown()) {
                Connection.MessageBox(ses.this, "Required field:অন্যান্য.");
                txtQ1111.requestFocus();
                return;
            } else if (spnQ114.getSelectedItemPosition() == 0 & secQ114.isShown()) {
                Connection.MessageBox(ses.this, "Required field:বসত ঘরের মাঝের প্রধান নির্মাণ সামগ্রী ? .");
                spnQ114.requestFocus();
                return;
            } else if (txtQ1141.getText().toString().length() == 0 & secQ1141.isShown()) {
                Connection.MessageBox(ses.this, "Required field:অন্যান্য.");
                txtQ1141.requestFocus();
                return;
            } else if (spnQ115.getSelectedItemPosition() == 0 & secQ115.isShown()) {
                Connection.MessageBox(ses.this, "Required field:বসত ঘরের ছাদের প্রধান নির্মাণ সামগ্রী ?.");
                spnQ115.requestFocus();
                return;
            } else if (txtQ1151.getText().toString().length() == 0 & secQ1151.isShown()) {
                Connection.MessageBox(ses.this, "Required field:অন্যান্য.");
                txtQ1151.requestFocus();
                return;
            } else if (spnQ116.getSelectedItemPosition() == 0 & secQ116.isShown()) {
                Connection.MessageBox(ses.this, "Required field:বসত ঘরের দেয়ালের প্রধান নির্মাণ সামগ্রী ?.");
                spnQ116.requestFocus();
                return;
            } else if (txtQ1161.getText().toString().length() == 0 & secQ1161.isShown()) {
                Connection.MessageBox(ses.this, "Required field:অন্যান্য.");
                txtQ1161.requestFocus();
                return;
            } else if (!rdoQ118a1.isChecked() & !rdoQ118a2.isChecked() & secQ118a.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q118a.");
                rdoQ118a1.requestFocus();
                return;
            } else if (!rdoQ118b1.isChecked() & !rdoQ118b2.isChecked() & secQ118b.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q118b.");
                rdoQ118b1.requestFocus();
                return;
            } else if (!rdoQ118c1.isChecked() & !rdoQ118c2.isChecked() & secQ118c.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q118c.");
                rdoQ118c1.requestFocus();
                return;
            } else if (!rdoQ118d1.isChecked() & !rdoQ118d2.isChecked() & secQ118d.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q118d.");
                rdoQ118d1.requestFocus();
                return;
            } else if (!rdoQ118e1.isChecked() & !rdoQ118e2.isChecked() & secQ118e.isShown()) {
                Connection.MessageBox(ses.this, "Select anyone options from Q118e.");
                rdoQ118e1.requestFocus();
                return;
            }

            String SQL = "";

           /* if(!C.Existence("Select Dist ,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,StartTime,EndTime,UserId,EnDt,Upload from " + TableName + "  Where Dist ='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and ProvType='"+ ProvType +"' and ProvCode='"+ ProvCode +"' and HHNo='"+ txtHHNo.getText().toString() +"'"))*/
            if (!C.Existence("Select Dist ,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,StartTime,EndTime,UserId,EnDt,Upload from " + TableName + "  Where Dist ='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + txtHHNo.getText().toString() + "'")) {
                SQL = "Insert into " + TableName + "(Dist ,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,StartTime,EndTime,UserId,EnDt,Upload)Values('" + Dist + "','" + Upz + "','" + UN + "','" + Mouza + "','" + Vill + "','" + ProvType + "','" + ProvCode + "','" + txtHHNo.getText() + "','" + StartTime + "','" + g.CurrentTime24() + "','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set Upload='2',";
            SQL += "status = '" + (spnstatus.getSelectedItemPosition() == 0 ? "" : Global.Left(spnstatus.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q102 = '" + (spnQ102.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ102.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q1021 = '" + txtQ1021.getText().toString() + "',";
            SQL += "Q107 = '" + (spnQ107.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ107.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q1071 = '" + txtQ1071.getText().toString() + "',";
            RadioButton rbQ110a = (RadioButton) findViewById(rdogrpQ110a.getCheckedRadioButtonId());
            SQL += "Q110a = '" + (rbQ110a == null ? "" : (Global.Left(rbQ110a.getText().toString(), 1))) + "',";
            RadioButton rbQ110b = (RadioButton) findViewById(rdogrpQ110b.getCheckedRadioButtonId());
            SQL += "Q110b = '" + (rbQ110b == null ? "" : (Global.Left(rbQ110b.getText().toString(), 1))) + "',";
            RadioButton rbQ110c = (RadioButton) findViewById(rdogrpQ110c.getCheckedRadioButtonId());
            SQL += "Q110c = '" + (rbQ110c == null ? "" : (Global.Left(rbQ110c.getText().toString(), 1))) + "',";
            RadioButton rbQ110d = (RadioButton) findViewById(rdogrpQ110d.getCheckedRadioButtonId());
            SQL += "Q110d = '" + (rbQ110d == null ? "" : (Global.Left(rbQ110d.getText().toString(), 1))) + "',";
            RadioButton rbQ110e = (RadioButton) findViewById(rdogrpQ110e.getCheckedRadioButtonId());
            SQL += "Q110e = '" + (rbQ110e == null ? "" : (Global.Left(rbQ110e.getText().toString(), 1))) + "',";
            RadioButton rbQ110f = (RadioButton) findViewById(rdogrpQ110f.getCheckedRadioButtonId());
            SQL += "Q110f = '" + (rbQ110f == null ? "" : (Global.Left(rbQ110f.getText().toString(), 1))) + "',";
            RadioButton rbQ110g = (RadioButton) findViewById(rdogrpQ110g.getCheckedRadioButtonId());
            SQL += "Q110g = '" + (rbQ110g == null ? "" : (Global.Left(rbQ110g.getText().toString(), 1))) + "',";
            RadioButton rbQ110h = (RadioButton) findViewById(rdogrpQ110h.getCheckedRadioButtonId());
            SQL += "Q110h = '" + (rbQ110h == null ? "" : (Global.Left(rbQ110h.getText().toString(), 1))) + "',";
            RadioButton rbQ110i = (RadioButton) findViewById(rdogrpQ110i.getCheckedRadioButtonId());
            SQL += "Q110i = '" + (rbQ110i == null ? "" : (Global.Left(rbQ110i.getText().toString(), 1))) + "',";
            RadioButton rbQ110j = (RadioButton) findViewById(rdogrpQ110j.getCheckedRadioButtonId());
            SQL += "Q110j = '" + (rbQ110j == null ? "" : (Global.Left(rbQ110j.getText().toString(), 1))) + "',";
            RadioButton rbQ110k = (RadioButton) findViewById(rdogrpQ110k.getCheckedRadioButtonId());
            SQL += "Q110k = '" + (rbQ110k == null ? "" : (Global.Left(rbQ110k.getText().toString(), 1))) + "',";
            RadioButton rbQ110l = (RadioButton) findViewById(rdogrpQ110l.getCheckedRadioButtonId());
            SQL += "Q110l = '" + (rbQ110l == null ? "" : (Global.Left(rbQ110l.getText().toString(), 1))) + "',";
            RadioButton rbQ110m = (RadioButton) findViewById(rdogrpQ110m.getCheckedRadioButtonId());
            SQL += "Q110m = '" + (rbQ110m == null ? "" : (Global.Left(rbQ110m.getText().toString(), 1))) + "',";
            RadioButton rbQ110n = (RadioButton) findViewById(rdogrpQ110n.getCheckedRadioButtonId());
            SQL += "Q110n = '" + (rbQ110n == null ? "" : (Global.Left(rbQ110n.getText().toString(), 1))) + "',";
            RadioButton rbQ110o = (RadioButton) findViewById(rdogrpQ110o.getCheckedRadioButtonId());
            SQL += "Q110o = '" + (rbQ110o == null ? "" : (Global.Left(rbQ110o.getText().toString(), 1))) + "',";
            RadioButton rbQ110p = (RadioButton) findViewById(rdogrpQ110p.getCheckedRadioButtonId());
            SQL += "Q110p = '" + (rbQ110p == null ? "" : (Global.Left(rbQ110p.getText().toString(), 1))) + "',";
            SQL += "Q111 = '" + (spnQ111.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ111.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q1111 = '" + txtQ1111.getText().toString() + "',";
            SQL += "Q114 = '" + (spnQ114.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ114.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q1141 = '" + txtQ1141.getText().toString() + "',";
            SQL += "Q115 = '" + (spnQ115.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ115.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q1151 = '" + txtQ1151.getText().toString() + "',";
            SQL += "Q116 = '" + (spnQ116.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ116.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q1161 = '" + txtQ1161.getText().toString() + "',";
            RadioButton rbQ118a = (RadioButton) findViewById(rdogrpQ118a.getCheckedRadioButtonId());
            SQL += "Q118a = '" + (rbQ118a == null ? "" : (Global.Left(rbQ118a.getText().toString(), 1))) + "',";
            RadioButton rbQ118b = (RadioButton) findViewById(rdogrpQ118b.getCheckedRadioButtonId());
            SQL += "Q118b = '" + (rbQ118b == null ? "" : (Global.Left(rbQ118b.getText().toString(), 1))) + "',";
            RadioButton rbQ118c = (RadioButton) findViewById(rdogrpQ118c.getCheckedRadioButtonId());
            SQL += "Q118c = '" + (rbQ118c == null ? "" : (Global.Left(rbQ118c.getText().toString(), 1))) + "',";
            RadioButton rbQ118d = (RadioButton) findViewById(rdogrpQ118d.getCheckedRadioButtonId());
            SQL += "Q118d = '" + (rbQ118d == null ? "" : (Global.Left(rbQ118d.getText().toString(), 1))) + "',";
            RadioButton rbQ118e = (RadioButton) findViewById(rdogrpQ118e.getCheckedRadioButtonId());
            SQL += "Q118e = '" + (rbQ118e == null ? "" : (Global.Left(rbQ118e.getText().toString(), 1))) + "'";
           /* SQL+="  Where Dist ='"+ Dist +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and ProvType='"+ ProvType +"' and ProvCode='"+ ProvCode +"' and HHNo='"+ txtHHNo.getText().toString() +"'";*/
            SQL += "  Where Dist ='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + txtHHNo.getText().toString() + "'";
            C.Save(SQL);
            Connection.MessageBox(ses.this, "Saved Successfully");
            finish();
        } catch (Exception e) {
            Connection.MessageBox(ses.this, e.getMessage());
            return;
        }
    }

    private void DataSearch(String Dist, String Upz, String UN, String Mouza, String Vill, String ProvType, String ProvCode, String HHNo) {
        try {

            RadioButton rb;
            /*Cursor cur = C.ReadData("Select * from "+ TableName +"  Where Dist ='"+ Dist  +"' and Upz='"+ Upz +"' and UN='"+ UN +"' and Mouza='"+ Mouza +"' and Vill='"+ Vill +"' and ProvType='"+ ProvType +"' and ProvCode='"+ ProvCode +"' and HHNo='"+ HHNo +"'");*/
            Cursor cur = C.ReadData("Select * from " + TableName + "  Where Dist ='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + HHNo + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                spnstatus.setSelection(Global.SpinnerItemPosition(spnstatus, 2, cur.getString(cur.getColumnIndex("status"))));
                spnQ102.setSelection(Global.SpinnerItemPosition(spnQ102, 2, cur.getString(cur.getColumnIndex("Q102"))));
                txtQ1021.setText(cur.getString(cur.getColumnIndex("Q1021")));
                spnQ107.setSelection(Global.SpinnerItemPosition(spnQ107, 2, cur.getString(cur.getColumnIndex("Q107"))));
                txtQ1071.setText(cur.getString(cur.getColumnIndex("Q1071")));
                for (int i = 0; i < rdogrpQ110a.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110a.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110a"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110b.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110b.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110b"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110c.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110c.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110c"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110d.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110d.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110d"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110e.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110e.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110e"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110f.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110f.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110f"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110g.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110g.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110g"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110h.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110h.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110h"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110i.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110i.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110i"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110j.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110j.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110j"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110k.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110k.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110k"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110l.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110l.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110l"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110m.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110m.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110m"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110n.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110n.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110n"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110o.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110o.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110o"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ110p.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ110p.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q110p"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                spnQ111.setSelection(Global.SpinnerItemPosition(spnQ111, 2, cur.getString(cur.getColumnIndex("Q111"))));
                txtQ1111.setText(cur.getString(cur.getColumnIndex("Q1111")));
                spnQ114.setSelection(Global.SpinnerItemPosition(spnQ114, 2, cur.getString(cur.getColumnIndex("Q114"))));
                txtQ1141.setText(cur.getString(cur.getColumnIndex("Q1141")));
                spnQ115.setSelection(Global.SpinnerItemPosition(spnQ115, 2, cur.getString(cur.getColumnIndex("Q115"))));
                txtQ1151.setText(cur.getString(cur.getColumnIndex("Q1151")));
                spnQ116.setSelection(Global.SpinnerItemPosition(spnQ116, 2, cur.getString(cur.getColumnIndex("Q116"))));
                txtQ1161.setText(cur.getString(cur.getColumnIndex("Q1161")));
                for (int i = 0; i < rdogrpQ118a.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ118a.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q118a"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ118b.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ118b.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q118b"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ118c.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ118c.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q118c"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ118d.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ118d.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q118d"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ118e.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ118e.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q118e"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(ses.this, e.getMessage());
            return;
        }
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, g.mYear, g.mMonth - 1, g.mDay);
            case TIME_DIALOG:
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            EditText dtpDate;

            /*dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00"+mDay,2)).append("/")
                    .append(Global.Right("00"+mMonth,2)).append("/")
                    .append(mYear));*/
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;

            //tpTime.setText(new StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));

        }
    };
}
