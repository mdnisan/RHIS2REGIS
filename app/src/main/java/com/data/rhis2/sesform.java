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

public class sesform extends Activity {
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
        AlertDialog.Builder adb = new AlertDialog.Builder(sesform.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
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
    LinearLayout secQ1;
    TextView VlblQ1;
    Spinner spnQ1;
    LinearLayout secQ11;
    TextView VlblQ11;
    EditText txtQ11;
    LinearLayout secQ2;
    TextView VlblQ2;
    Spinner spnQ2;
    LinearLayout secQ21;
    TextView VlblQ21;
    EditText txtQ21;
    LinearLayout seclbl3;
    LinearLayout secQ3a;
    TextView VlblQ3a;
    RadioGroup rdogrpQ3a;

    RadioButton rdoQ3a1;
    RadioButton rdoQ3a2;
    LinearLayout secQ3b;
    TextView VlblQ3b;
    RadioGroup rdogrpQ3b;

    RadioButton rdoQ3b1;
    RadioButton rdoQ3b2;
    LinearLayout secQ3c;
    TextView VlblQ3c;
    RadioGroup rdogrpQ3c;

    RadioButton rdoQ3c1;
    RadioButton rdoQ3c2;
    LinearLayout secQ3d;
    TextView VlblQ3d;
    RadioGroup rdogrpQ3d;

    RadioButton rdoQ3d1;
    RadioButton rdoQ3d2;
    LinearLayout secQ3e;
    TextView VlblQ3e;
    RadioGroup rdogrpQ3e;

    RadioButton rdoQ3e1;
    RadioButton rdoQ3e2;
    //RadioButton rdoQ3e3;
    LinearLayout secQ3f;
    TextView VlblQ3f;
    RadioGroup rdogrpQ3f;

    RadioButton rdoQ3f1;
    RadioButton rdoQ3f2;
    LinearLayout secQ3g;
    TextView VlblQ3g;
    RadioGroup rdogrpQ3g;

    RadioButton rdoQ3g1;
    RadioButton rdoQ3g2;
    LinearLayout secQ3h;
    TextView VlblQ3h;
    RadioGroup rdogrpQ3h;

    RadioButton rdoQ3h1;
    RadioButton rdoQ3h2;
    LinearLayout secQ3i;
    TextView VlblQ3i;
    RadioGroup rdogrpQ3i;

    RadioButton rdoQ3i1;
    RadioButton rdoQ3i2;
    LinearLayout secQ3j;
    TextView VlblQ3j;
    RadioGroup rdogrpQ3j;

    RadioButton rdoQ3j1;
    RadioButton rdoQ3j2;
    LinearLayout secQ3k;
    TextView VlblQ3k;
    RadioGroup rdogrpQ3k;

    RadioButton rdoQ3k1;
    RadioButton rdoQ3k2;
    LinearLayout secQ3l;
    TextView VlblQ3l;
    RadioGroup rdogrpQ3l;

    RadioButton rdoQ3l1;
    RadioButton rdoQ3l2;
    LinearLayout secQ3m;
    TextView VlblQ3m;
    RadioGroup rdogrpQ3m;

    RadioButton rdoQ3m1;
    RadioButton rdoQ3m2;
    LinearLayout secQ3n;
    TextView VlblQ3n;
    RadioGroup rdogrpQ3n;

    RadioButton rdoQ3n1;
    RadioButton rdoQ3n2;
    LinearLayout secQ3o;
    TextView VlblQ3o;
    RadioGroup rdogrpQ3o;

    RadioButton rdoQ3o1;
    RadioButton rdoQ3o2;
    LinearLayout secQ3p;
    TextView VlblQ3p;
    RadioGroup rdogrpQ3p;

    RadioButton rdoQ3p1;
    RadioButton rdoQ3p2;
    LinearLayout secQ4;
    TextView VlblQ4;
    Spinner spnQ4;
    LinearLayout secQ41;
    TextView VlblQ41;
    EditText txtQ41;
    LinearLayout secQ5;
    TextView VlblQ5;
    Spinner spnQ5;
    LinearLayout secQ51;
    TextView VlblQ51;
    EditText txtQ51;
    LinearLayout secQ6;
    TextView VlblQ6;
    Spinner spnQ6;
    LinearLayout secQ61;
    TextView VlblQ61;
    EditText txtQ61;
    LinearLayout secQ7;
    TextView VlblQ7;
    Spinner spnQ7;
    LinearLayout secQ71;
    TextView VlblQ71;
    EditText txtQ71;
    LinearLayout seclbl8;
    LinearLayout secQ8a;
    TextView VlblQ8a;
    RadioGroup rdogrpQ8a;

    RadioButton rdoQ8a1;
    RadioButton rdoQ8a2;
    LinearLayout secQ8b;
    TextView VlblQ8b;
    RadioGroup rdogrpQ8b;

    RadioButton rdoQ8b1;
    RadioButton rdoQ8b2;
    LinearLayout secQ8c;
    TextView VlblQ8c;
    RadioGroup rdogrpQ8c;

    RadioButton rdoQ8c1;
    RadioButton rdoQ8c2;
    LinearLayout secQ8d;
    TextView VlblQ8d;
    RadioGroup rdogrpQ8d;

    RadioButton rdoQ8d1;
    RadioButton rdoQ8d2;
    LinearLayout secQ8e;
    TextView VlblQ8e;
    RadioGroup rdogrpQ8e;

    RadioButton rdoQ8e1;
    RadioButton rdoQ8e2;

    String StartTime;

    String Dist = "";
    String Upz = "";
    String UN = "";
    String Mouza = "";
    String Vill = "";
    String ProvType = "";
    String ProvCode = "";

    LinearLayout secAll;


    RadioButton rdoQ8f1;
    RadioButton rdoQ8f2;
    LinearLayout secQ8f;
    TextView VlblQ8f;
    RadioGroup rdogrpQ8f;

    RadioButton rdoQ8g1;
    RadioButton rdoQ8g2;
    LinearLayout secQ8g;
    TextView VlblQ8g;
    RadioGroup rdogrpQ8g;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.sesform);
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

            secAll = (LinearLayout) findViewById(R.id.secAll);

            seclblSS = (LinearLayout) findViewById(R.id.seclblSS);

            VlblHHNo = (TextView) findViewById(R.id.VlblHHNo);
            txtHHNo = (EditText) findViewById(R.id.txtHHNo);
            txtHHNo.setText(g.getHouseholdNo());
            txtHHNo.setEnabled(false);


            secstatus = (LinearLayout) findViewById(R.id.secstatus);
            Vlblstatus = (TextView) findViewById(R.id.Vlblstatus);
            spnstatus = (Spinner) findViewById(R.id.spnstatus);
            List<String> liststatus = new ArrayList<String>();

            liststatus.add("");
            liststatus.add("1-সম্মত");
            liststatus.add("2-তথ্য প্রদানে অসম্মত");
            liststatus.add("7-অন্যান্য");
            ArrayAdapter<String> adptrstatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liststatus);
            spnstatus.setAdapter(adptrstatus);

            spnstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnstatus.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnstatus.getSelectedItem().toString(), 1);
                    if (spnData.equalsIgnoreCase("2") | spnData.equalsIgnoreCase("7")) {
                        secAll.setVisibility(View.GONE);
                        secQ1.setVisibility(View.GONE);
                        secQ11.setVisibility(View.GONE);
                        secQ2.setVisibility(View.GONE);
                        secQ21.setVisibility(View.GONE);
                        secQ3a.setVisibility(View.GONE);
                        secQ3b.setVisibility(View.GONE);
                        secQ3c.setVisibility(View.GONE);
                        secQ3d.setVisibility(View.GONE);
                        secQ3e.setVisibility(View.GONE);
                        secQ3f.setVisibility(View.GONE);
                        secQ3g.setVisibility(View.GONE);
                        secQ3h.setVisibility(View.GONE);
                        secQ3i.setVisibility(View.GONE);
                        secQ3j.setVisibility(View.GONE);
                        secQ3k.setVisibility(View.GONE);
                        secQ3l.setVisibility(View.GONE);
                        secQ3m.setVisibility(View.GONE);
                        secQ3n.setVisibility(View.GONE);
                        secQ3o.setVisibility(View.GONE);
                        secQ3p.setVisibility(View.GONE);
                        secQ4.setVisibility(View.GONE);
                        secQ41.setVisibility(View.GONE);
                        secQ5.setVisibility(View.GONE);
                        secQ51.setVisibility(View.GONE);
                        secQ6.setVisibility(View.GONE);
                        secQ61.setVisibility(View.GONE);
                        secQ7.setVisibility(View.GONE);
                        secQ71.setVisibility(View.GONE);
                        secQ8a.setVisibility(View.GONE);
                        secQ8b.setVisibility(View.GONE);
                        secQ8c.setVisibility(View.GONE);
                        secQ8d.setVisibility(View.GONE);
                        secQ8f.setVisibility(View.GONE);
                        secQ8g.setVisibility(View.GONE);
                    } else {
                        secAll.setVisibility(View.VISIBLE);
                        secQ1.setVisibility(View.VISIBLE);
                        secQ11.setVisibility(View.GONE);
                        secQ2.setVisibility(View.VISIBLE);
                        secQ21.setVisibility(View.GONE);
                        secQ3a.setVisibility(View.VISIBLE);
                        secQ3b.setVisibility(View.VISIBLE);
                        secQ3c.setVisibility(View.VISIBLE);
                        secQ3d.setVisibility(View.VISIBLE);
                        secQ3e.setVisibility(View.VISIBLE);
                        secQ3f.setVisibility(View.VISIBLE);
                        secQ3g.setVisibility(View.VISIBLE);
                        secQ3h.setVisibility(View.VISIBLE);
                        secQ3i.setVisibility(View.VISIBLE);
                        secQ3j.setVisibility(View.VISIBLE);
                        secQ3k.setVisibility(View.VISIBLE);
                        secQ3l.setVisibility(View.VISIBLE);
                        secQ3m.setVisibility(View.VISIBLE);
                        secQ3n.setVisibility(View.VISIBLE);
                        secQ3o.setVisibility(View.VISIBLE);
                        secQ3p.setVisibility(View.VISIBLE);
                        secQ4.setVisibility(View.VISIBLE);
                        secQ41.setVisibility(View.GONE);
                        secQ5.setVisibility(View.VISIBLE);
                        secQ51.setVisibility(View.GONE);
                        secQ6.setVisibility(View.VISIBLE);
                        secQ61.setVisibility(View.GONE);
                        secQ7.setVisibility(View.VISIBLE);
                        secQ71.setVisibility(View.GONE);
                        secQ8a.setVisibility(View.VISIBLE);
                        secQ8b.setVisibility(View.VISIBLE);
                        secQ8c.setVisibility(View.VISIBLE);
                        secQ8d.setVisibility(View.VISIBLE);
                        secQ8f.setVisibility(View.VISIBLE);
                        secQ8g.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ1 = (LinearLayout) findViewById(R.id.secQ1);
            VlblQ1 = (TextView) findViewById(R.id.VlblQ1);
            spnQ1 = (Spinner) findViewById(R.id.spnQ1);
            List<String> listQ1 = new ArrayList<String>();

            listQ1.add("");
            listQ1.add("11-ঘরের মধ্যে পাইপের পানি ");
            listQ1.add("12-বাড়ির চত্বরে /আঙ্গিনায় পাইপ");
            listQ1.add("13-সরকারি(পাবলিক ট্যাপ/স্থায়ী ট্যাপ)");
            listQ1.add("21-টিউবওয়েল (নলকুপ)");
            listQ1.add("31-সংরক্ষিত কূপ/ইন্দারা");
            listQ1.add("32-অসংরক্ষিত কূপ/ইন্দারা");
            listQ1.add("41-সংরক্ষিত ঝর্নার পানি");
            listQ1.add("42-অসংরক্ষিত ঝর্নার পানি");
            listQ1.add("51-সংগ্রহীত বৃষ্টির পানি");
            listQ1.add("61- ট্যাংকার ট্রাক");
            listQ1.add("71-ছোট ট্যাঙ্ক");
            listQ1.add("81- ভ-ূপৃষ্ঠের পানি(নদী, খাল, পুকুর, লেক, সেচের পানির নালা ইত্যাদি)    ");
            listQ1.add("91-বোতলের পানি");
            listQ1.add("96- অন্যান্য");
            ArrayAdapter<String> adptrQ1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ1);
            spnQ1.setAdapter(adptrQ1);

            spnQ1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ1.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ1.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ11.setVisibility(View.GONE);
                    } else {
                        secQ11.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ11 = (LinearLayout) findViewById(R.id.secQ11);
            VlblQ11 = (TextView) findViewById(R.id.VlblQ11);
            txtQ11 = (EditText) findViewById(R.id.txtQ11);
            secQ2 = (LinearLayout) findViewById(R.id.secQ2);
            VlblQ2 = (TextView) findViewById(R.id.VlblQ2);
            spnQ2 = (Spinner) findViewById(R.id.spnQ2);
            List<String> listQ2 = new ArrayList<String>();

            listQ2.add("");
            listQ2.add("11-ফ্লাশ করে পাইপের মাধ্যমে অপসারণ ");
            listQ2.add("12-ফ্লাশ করে ট্যাংকে ধারণ");
            listQ2.add("13-ফ্লাশ করে গর্তে ধারণ");
            listQ2.add("14-ফ্লাশ করে অন্য কোথাও অপসারণ");
            listQ2.add("15-ফ্লাশ করে কোথায় অপসারিত হয় তা জানিনা");
            listQ2.add("21-বায়ু চলাচলের ব্যবস্থাসহ উন্নত মানের পিট ল্যাট্রিন");
            listQ2.add("22-পিট ল্যাট্রিন (স্ল্যাব সহ)");
            listQ2.add("23-পিট ল্যাট্রিন (স্ল্যাব বিহীন)/খোলা গর্ত");
            listQ2.add("31-কম্পোস্টিং   ল্যাট্রিন");
            listQ2.add("41-বাকেট ল্যাট্রিন");
            listQ2.add("51-খোলা/ঝুলন্ত ল্যাট্রিন");
            listQ2.add("61-ল্যাট্রিন নাই/ঝোপ-ঝাড়/মাঠ");
            listQ2.add("96-অন্যান্য (নির্দিষ্ট করুন)");
            listQ2.add("");
            ArrayAdapter<String> adptrQ2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ2);
            spnQ2.setAdapter(adptrQ2);

            spnQ2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ2.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ2.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ21.setVisibility(View.GONE);
                    } else {
                        secQ21.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ21 = (LinearLayout) findViewById(R.id.secQ21);
            VlblQ21 = (TextView) findViewById(R.id.VlblQ21);
            txtQ21 = (EditText) findViewById(R.id.txtQ21);
            seclbl3 = (LinearLayout) findViewById(R.id.seclbl3);
            secQ3a = (LinearLayout) findViewById(R.id.secQ3a);
            VlblQ3a = (TextView) findViewById(R.id.VlblQ3a);
            rdogrpQ3a = (RadioGroup) findViewById(R.id.rdogrpQ3a);

            rdoQ3a1 = (RadioButton) findViewById(R.id.rdoQ3a1);
            rdoQ3a2 = (RadioButton) findViewById(R.id.rdoQ3a2);
            secQ3b = (LinearLayout) findViewById(R.id.secQ3b);
            VlblQ3b = (TextView) findViewById(R.id.VlblQ3b);
            rdogrpQ3b = (RadioGroup) findViewById(R.id.rdogrpQ3b);

            rdoQ3b1 = (RadioButton) findViewById(R.id.rdoQ3b1);
            rdoQ3b2 = (RadioButton) findViewById(R.id.rdoQ3b2);
            secQ3c = (LinearLayout) findViewById(R.id.secQ3c);
            VlblQ3c = (TextView) findViewById(R.id.VlblQ3c);
            rdogrpQ3c = (RadioGroup) findViewById(R.id.rdogrpQ3c);

            rdoQ3c1 = (RadioButton) findViewById(R.id.rdoQ3c1);
            rdoQ3c2 = (RadioButton) findViewById(R.id.rdoQ3c2);
            secQ3d = (LinearLayout) findViewById(R.id.secQ3d);
            VlblQ3d = (TextView) findViewById(R.id.VlblQ3d);
            rdogrpQ3d = (RadioGroup) findViewById(R.id.rdogrpQ3d);

            rdoQ3d1 = (RadioButton) findViewById(R.id.rdoQ3d1);
            rdoQ3d2 = (RadioButton) findViewById(R.id.rdoQ3d2);
            secQ3e = (LinearLayout) findViewById(R.id.secQ3e);
            VlblQ3e = (TextView) findViewById(R.id.VlblQ3e);
            rdogrpQ3e = (RadioGroup) findViewById(R.id.rdogrpQ3e);

            rdoQ3e1 = (RadioButton) findViewById(R.id.rdoQ3e1);
            rdoQ3e2 = (RadioButton) findViewById(R.id.rdoQ3e2);
            //rdoQ3e3 = (RadioButton) findViewById(R.id.rdoQ3e3);
            secQ3f = (LinearLayout) findViewById(R.id.secQ3f);
            VlblQ3f = (TextView) findViewById(R.id.VlblQ3f);
            rdogrpQ3f = (RadioGroup) findViewById(R.id.rdogrpQ3f);

            rdoQ3f1 = (RadioButton) findViewById(R.id.rdoQ3f1);
            rdoQ3f2 = (RadioButton) findViewById(R.id.rdoQ3f2);
            secQ3g = (LinearLayout) findViewById(R.id.secQ3g);
            VlblQ3g = (TextView) findViewById(R.id.VlblQ3g);
            rdogrpQ3g = (RadioGroup) findViewById(R.id.rdogrpQ3g);

            rdoQ3g1 = (RadioButton) findViewById(R.id.rdoQ3g1);
            rdoQ3g2 = (RadioButton) findViewById(R.id.rdoQ3g2);
            secQ3h = (LinearLayout) findViewById(R.id.secQ3h);
            VlblQ3h = (TextView) findViewById(R.id.VlblQ3h);
            rdogrpQ3h = (RadioGroup) findViewById(R.id.rdogrpQ3h);

            rdoQ3h1 = (RadioButton) findViewById(R.id.rdoQ3h1);
            rdoQ3h2 = (RadioButton) findViewById(R.id.rdoQ3h2);
            secQ3i = (LinearLayout) findViewById(R.id.secQ3i);
            VlblQ3i = (TextView) findViewById(R.id.VlblQ3i);
            rdogrpQ3i = (RadioGroup) findViewById(R.id.rdogrpQ3i);

            rdoQ3i1 = (RadioButton) findViewById(R.id.rdoQ3i1);
            rdoQ3i2 = (RadioButton) findViewById(R.id.rdoQ3i2);
            secQ3j = (LinearLayout) findViewById(R.id.secQ3j);
            VlblQ3j = (TextView) findViewById(R.id.VlblQ3j);
            rdogrpQ3j = (RadioGroup) findViewById(R.id.rdogrpQ3j);

            rdoQ3j1 = (RadioButton) findViewById(R.id.rdoQ3j1);
            rdoQ3j2 = (RadioButton) findViewById(R.id.rdoQ3j2);
            secQ3k = (LinearLayout) findViewById(R.id.secQ3k);
            VlblQ3k = (TextView) findViewById(R.id.VlblQ3k);
            rdogrpQ3k = (RadioGroup) findViewById(R.id.rdogrpQ3k);

            rdoQ3k1 = (RadioButton) findViewById(R.id.rdoQ3k1);
            rdoQ3k2 = (RadioButton) findViewById(R.id.rdoQ3k2);
            secQ3l = (LinearLayout) findViewById(R.id.secQ3l);
            VlblQ3l = (TextView) findViewById(R.id.VlblQ3l);
            rdogrpQ3l = (RadioGroup) findViewById(R.id.rdogrpQ3l);

            rdoQ3l1 = (RadioButton) findViewById(R.id.rdoQ3l1);
            rdoQ3l2 = (RadioButton) findViewById(R.id.rdoQ3l2);
            secQ3m = (LinearLayout) findViewById(R.id.secQ3m);
            VlblQ3m = (TextView) findViewById(R.id.VlblQ3m);
            rdogrpQ3m = (RadioGroup) findViewById(R.id.rdogrpQ3m);

            rdoQ3m1 = (RadioButton) findViewById(R.id.rdoQ3m1);
            rdoQ3m2 = (RadioButton) findViewById(R.id.rdoQ3m2);
            secQ3n = (LinearLayout) findViewById(R.id.secQ3n);
            VlblQ3n = (TextView) findViewById(R.id.VlblQ3n);
            rdogrpQ3n = (RadioGroup) findViewById(R.id.rdogrpQ3n);

            rdoQ3n1 = (RadioButton) findViewById(R.id.rdoQ3n1);
            rdoQ3n2 = (RadioButton) findViewById(R.id.rdoQ3n2);
            secQ3o = (LinearLayout) findViewById(R.id.secQ3o);
            VlblQ3o = (TextView) findViewById(R.id.VlblQ3o);
            rdogrpQ3o = (RadioGroup) findViewById(R.id.rdogrpQ3o);

            rdoQ3o1 = (RadioButton) findViewById(R.id.rdoQ3o1);
            rdoQ3o2 = (RadioButton) findViewById(R.id.rdoQ3o2);
            secQ3p = (LinearLayout) findViewById(R.id.secQ3p);
            VlblQ3p = (TextView) findViewById(R.id.VlblQ3p);
            rdogrpQ3p = (RadioGroup) findViewById(R.id.rdogrpQ3p);

            rdoQ3p1 = (RadioButton) findViewById(R.id.rdoQ3p1);
            rdoQ3p2 = (RadioButton) findViewById(R.id.rdoQ3p2);
            secQ4 = (LinearLayout) findViewById(R.id.secQ4);
            VlblQ4 = (TextView) findViewById(R.id.VlblQ4);
            spnQ4 = (Spinner) findViewById(R.id.spnQ4);
            List<String> listQ4 = new ArrayList<String>();

            listQ4.add("");
            listQ4.add("01-বিদ্যুৎ ");
            listQ4.add("02-তরল গ্যাস");
            listQ4.add("03-প্রাকৃতিক গ্যাস");
            listQ4.add("04-বায়ো গ্যাস");
            listQ4.add("05-কেরোসিন");
            listQ4.add("06-কয়লা /লিগনাইট");
            listQ4.add("07- চারকোল");
            listQ4.add("08-কাঠ");
            listQ4.add("09-ভূষি/তুষ/ঘাস ");
            listQ4.add("10-ফসলের অবশিষ্টাংশ(খড়/কুটা/পাতা)");
            listQ4.add("11-গোবর");
            listQ4.add("95-খানায় রান্না হয় না");
            listQ4.add("96-অন্যান্য");
            ArrayAdapter<String> adptrQ4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ4);
            spnQ4.setAdapter(adptrQ4);

            spnQ4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ4.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ4.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ41.setVisibility(View.GONE);
                    } else {
                        secQ41.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ41 = (LinearLayout) findViewById(R.id.secQ41);
            VlblQ41 = (TextView) findViewById(R.id.VlblQ41);
            txtQ41 = (EditText) findViewById(R.id.txtQ41);
            secQ5 = (LinearLayout) findViewById(R.id.secQ5);
            VlblQ5 = (TextView) findViewById(R.id.VlblQ5);
            spnQ5 = (Spinner) findViewById(R.id.spnQ5);
            List<String> listQ5 = new ArrayList<String>();

            listQ5.add("");
            listQ5.add("11-মাটি/বালু ");
            listQ5.add("21-কাঠের তক্তা");
            listQ5.add("22-তাল গাছ/ বাঁশ");
            listQ5.add("31-নকশা করা কাঠের পাটাতন/পালিশকৃত কাঠ");
            listQ5.add("33-সিরামিক টাইলস/মোজাইক");
            listQ5.add("34-সিমেন্ট ");
            listQ5.add("35-কার্পেট");
            listQ5.add("96-অন্যান্য");
            listQ5.add("");
            ArrayAdapter<String> adptrQ5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ5);
            spnQ5.setAdapter(adptrQ5);

            spnQ5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ5.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ5.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ51.setVisibility(View.GONE);
                    } else {
                        secQ51.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ51 = (LinearLayout) findViewById(R.id.secQ51);
            VlblQ51 = (TextView) findViewById(R.id.VlblQ51);
            txtQ51 = (EditText) findViewById(R.id.txtQ51);
            secQ6 = (LinearLayout) findViewById(R.id.secQ6);
            VlblQ6 = (TextView) findViewById(R.id.VlblQ6);
            spnQ6 = (Spinner) findViewById(R.id.spnQ6);
            List<String> listQ6 = new ArrayList<String>();

            listQ6.add("");
            listQ6.add("11-ছাদ নেই");
            listQ6.add("12-খড়/ছন/তালপাতা");
            listQ6.add("22-বাঁশ/তালগাছ");
            listQ6.add("23-কাঠের তক্তা");
            listQ6.add("24-কার্ডবোর্ড(কাগজের তৈরি বোর্ড)");
            listQ6.add("31-টিন");
            listQ6.add("32-কাঠ");
            listQ6.add("34-সিরামিক  টাইলস");
            listQ6.add("35-সিমেন্ট");
            listQ6.add("36-কাষ্ঠ খন্ড(টালি বা শ্লেট)");
            listQ6.add("96-অন্যান্য");
            ArrayAdapter<String> adptrQ6 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ6);
            spnQ6.setAdapter(adptrQ6);

            spnQ6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ6.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ6.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ61.setVisibility(View.GONE);
                    } else {
                        secQ61.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ61 = (LinearLayout) findViewById(R.id.secQ61);
            VlblQ61 = (TextView) findViewById(R.id.VlblQ61);
            txtQ61 = (EditText) findViewById(R.id.txtQ61);
            secQ7 = (LinearLayout) findViewById(R.id.secQ7);
            VlblQ7 = (TextView) findViewById(R.id.VlblQ7);
            spnQ7 = (Spinner) findViewById(R.id.spnQ7);
            List<String> listQ7 = new ArrayList<String>();

            listQ7.add("");
            listQ7.add("11-দেয়াল নাই");
            listQ7.add("12-পাট কাঠি/বেত/তাল গাছ//গাছের গুড়ি");
            listQ7.add("13-মাটি");
            listQ7.add("21-মাটিসহ বাঁশ");
            listQ7.add("22-মাটিসহ পাথর");
            listQ7.add("24-প্লাই  উড");
            listQ7.add("25-কার্ডবোর্ড");
            listQ7.add("31-টিন");
            listQ7.add("32-সিমেন্ট (প্লাস্টার সহ)");
            listQ7.add("33-চুনাপাথর/ সিমেন্ট");
            listQ7.add("34-ইট(প্লাস্টার ছাড়া)");
            listQ7.add("36-কাঠের তক্তা");
            listQ7.add("96-অন্যান্য");
            ArrayAdapter<String> adptrQ7 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listQ7);
            spnQ7.setAdapter(adptrQ7);

            spnQ7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnQ7.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnQ7.getSelectedItem().toString(), 2);
                    if (!spnData.equalsIgnoreCase("96")) {
                        secQ71.setVisibility(View.GONE);
                    } else {
                        secQ71.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secQ71 = (LinearLayout) findViewById(R.id.secQ71);
            VlblQ71 = (TextView) findViewById(R.id.VlblQ71);
            txtQ71 = (EditText) findViewById(R.id.txtQ71);
            seclbl8 = (LinearLayout) findViewById(R.id.seclbl8);
            secQ8a = (LinearLayout) findViewById(R.id.secQ8a);
            VlblQ8a = (TextView) findViewById(R.id.VlblQ8a);
            rdogrpQ8a = (RadioGroup) findViewById(R.id.rdogrpQ8a);

            rdoQ8a1 = (RadioButton) findViewById(R.id.rdoQ8a1);
            rdoQ8a2 = (RadioButton) findViewById(R.id.rdoQ8a2);
            secQ8b = (LinearLayout) findViewById(R.id.secQ8b);
            VlblQ8b = (TextView) findViewById(R.id.VlblQ8b);
            rdogrpQ8b = (RadioGroup) findViewById(R.id.rdogrpQ8b);

            rdoQ8b1 = (RadioButton) findViewById(R.id.rdoQ8b1);
            rdoQ8b2 = (RadioButton) findViewById(R.id.rdoQ8b2);
            secQ8c = (LinearLayout) findViewById(R.id.secQ8c);
            VlblQ8c = (TextView) findViewById(R.id.VlblQ8c);
            rdogrpQ8c = (RadioGroup) findViewById(R.id.rdogrpQ8c);

            rdoQ8c1 = (RadioButton) findViewById(R.id.rdoQ8c1);
            rdoQ8c2 = (RadioButton) findViewById(R.id.rdoQ8c2);
            secQ8d = (LinearLayout) findViewById(R.id.secQ8d);
            VlblQ8d = (TextView) findViewById(R.id.VlblQ8d);
            rdogrpQ8d = (RadioGroup) findViewById(R.id.rdogrpQ8d);

            rdoQ8d1 = (RadioButton) findViewById(R.id.rdoQ8d1);
            rdoQ8d2 = (RadioButton) findViewById(R.id.rdoQ8d2);
            secQ8e = (LinearLayout) findViewById(R.id.secQ8e);
            VlblQ8e = (TextView) findViewById(R.id.VlblQ8e);
            rdogrpQ8e = (RadioGroup) findViewById(R.id.rdogrpQ8e);

            rdoQ8e1 = (RadioButton) findViewById(R.id.rdoQ8e1);
            rdoQ8e2 = (RadioButton) findViewById(R.id.rdoQ8e2);


            rdoQ8f1 = (RadioButton) findViewById(R.id.rdoQ8f1);
            rdoQ8f2 = (RadioButton) findViewById(R.id.rdoQ8f2);
            rdogrpQ8f = (RadioGroup) findViewById(R.id.rdogrpQ8f);
            secQ8f = (LinearLayout) findViewById(R.id.secQ8f);

            rdoQ8g1 = (RadioButton) findViewById(R.id.rdoQ8g1);
            rdoQ8g2 = (RadioButton) findViewById(R.id.rdoQ8g2);
            rdogrpQ8g = (RadioGroup) findViewById(R.id.rdogrpQ8g);
            secQ8g = (LinearLayout) findViewById(R.id.secQ8g);


          /*  rdogrpQ8f.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(rdoQ8f1.isChecked())
                    {
                        secQ8g.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        secQ8g.setVisibility(View.VISIBLE);
                    }

                }
            });*/
            secAll.setVisibility(View.GONE);

            DataSearch(Dist, Upz, UN, Mouza, Vill, ProvType, ProvCode, txtHHNo.getText().toString());

            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(sesform.this, e.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {

            String DV = "";

            if (txtHHNo.getText().toString().length() == 0) {
                Connection.MessageBox(sesform.this, "Required field:HHNo.");
                txtHHNo.requestFocus();
                return;
            } else if (spnstatus.getSelectedItemPosition() == 0 & secstatus.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:অর্থ সামাজিক তথ্যাদি.");
                spnstatus.requestFocus();
                return;
            } else if (spnQ1.getSelectedItemPosition() == 0 & secQ1.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:আপনার খানার সদস্যদের খাবার পানি পান করার প্রধান উৎস কি ? .");
                spnQ1.requestFocus();
                return;
            } else if (txtQ11.getText().toString().length() == 0 & secQ11.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:অন্যান্য.");
                txtQ11.requestFocus();
                return;
            } else if (spnQ2.getSelectedItemPosition() == 0 & secQ2.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:এ খানার সদস্যরা সাধারনত কোন ধরনের পায়খানা ব্যবহার করে ? .");
                spnQ2.requestFocus();
                return;
            } else if (txtQ21.getText().toString().length() == 0 & secQ21.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:অন্যান্য.");
                txtQ21.requestFocus();
                return;
            } else if (!rdoQ3a1.isChecked() & !rdoQ3a2.isChecked() & secQ3a.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3a.");
                rdoQ3a1.requestFocus();
                return;
            } else if (!rdoQ3b1.isChecked() & !rdoQ3b2.isChecked() & secQ3b.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3b.");
                rdoQ3b1.requestFocus();
                return;
            } else if (!rdoQ3c1.isChecked() & !rdoQ3c2.isChecked() & secQ3c.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3c.");
                rdoQ3c1.requestFocus();
                return;
            } else if (!rdoQ3d1.isChecked() & !rdoQ3d2.isChecked() & secQ3d.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3d.");
                rdoQ3d1.requestFocus();
                return;
            } else if (!rdoQ3e1.isChecked() & !rdoQ3e2.isChecked() & secQ3e.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3e.");
                rdoQ3e1.requestFocus();
                return;
            } else if (!rdoQ3f1.isChecked() & !rdoQ3f2.isChecked() & secQ3f.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3f.");
                rdoQ3f1.requestFocus();
                return;
            } else if (!rdoQ3g1.isChecked() & !rdoQ3g2.isChecked() & secQ3g.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3g.");
                rdoQ3g1.requestFocus();
                return;
            } else if (!rdoQ3h1.isChecked() & !rdoQ3h2.isChecked() & secQ3h.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3h.");
                rdoQ3h1.requestFocus();
                return;
            } else if (!rdoQ3i1.isChecked() & !rdoQ3i2.isChecked() & secQ3i.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3i.");
                rdoQ3i1.requestFocus();
                return;
            } else if (!rdoQ3j1.isChecked() & !rdoQ3j2.isChecked() & secQ3j.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3j.");
                rdoQ3j1.requestFocus();
                return;
            } else if (!rdoQ3k1.isChecked() & !rdoQ3k2.isChecked() & secQ3k.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3k.");
                rdoQ3k1.requestFocus();
                return;
            } else if (!rdoQ3l1.isChecked() & !rdoQ3l2.isChecked() & secQ3l.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3l.");
                rdoQ3l1.requestFocus();
                return;
            } else if (!rdoQ3m1.isChecked() & !rdoQ3m2.isChecked() & secQ3m.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3m.");
                rdoQ3m1.requestFocus();
                return;
            } else if (!rdoQ3n1.isChecked() & !rdoQ3n2.isChecked() & secQ3n.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3n.");
                rdoQ3n1.requestFocus();
                return;
            } else if (!rdoQ3o1.isChecked() & !rdoQ3o2.isChecked() & secQ3o.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3o.");
                rdoQ3o1.requestFocus();
                return;
            } else if (!rdoQ3p1.isChecked() & !rdoQ3p2.isChecked() & secQ3p.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q3p.");
                rdoQ3p1.requestFocus();
                return;
            } else if (spnQ4.getSelectedItemPosition() == 0 & secQ4.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:আপনার খানায় রান্নার জন্য প্রধানত কী ধরণের জ্বালানী ব্যবহার করা হয় ? .");
                spnQ4.requestFocus();
                return;
            } else if (txtQ41.getText().toString().length() == 0 & secQ41.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:অন্যান্য.");
                txtQ41.requestFocus();
                return;
            } else if (spnQ5.getSelectedItemPosition() == 0 & secQ5.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:বসত ঘরের মেঝের প্রধান নির্মাণ সামগ্রী ? .");
                spnQ5.requestFocus();
                return;
            } else if (txtQ51.getText().toString().length() == 0 & secQ51.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:অন্যান্য.");
                txtQ51.requestFocus();
                return;
            } else if (spnQ6.getSelectedItemPosition() == 0 & secQ6.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:বসত ঘরের ছাদের প্রধান নির্মাণ সামগ্রী ?.");
                spnQ6.requestFocus();
                return;
            } else if (txtQ61.getText().toString().length() == 0 & secQ61.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:অন্যান্য.");
                txtQ61.requestFocus();
                return;
            } else if (spnQ7.getSelectedItemPosition() == 0 & secQ7.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:বসত ঘরের দেয়ালের প্রধান নির্মাণ সামগ্রী ?.");
                spnQ7.requestFocus();
                return;
            } else if (txtQ71.getText().toString().length() == 0 & secQ71.isShown()) {
                Connection.MessageBox(sesform.this, "Required field:অন্যান্য.");
                txtQ71.requestFocus();
                return;
            } else if (!rdoQ8a1.isChecked() & !rdoQ8a2.isChecked() & secQ8a.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q8a.");
                rdoQ8a1.requestFocus();
                return;
            } else if (!rdoQ8b1.isChecked() & !rdoQ8b2.isChecked() & secQ8b.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q8b.");
                rdoQ8b1.requestFocus();
                return;
            } else if (!rdoQ8c1.isChecked() & !rdoQ8c2.isChecked() & secQ8c.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q8c.");
                rdoQ8c1.requestFocus();
                return;
            } else if (!rdoQ8d1.isChecked() & !rdoQ8d2.isChecked() & secQ8d.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q8d.");
                rdoQ8d1.requestFocus();
                return;
            } else if (!rdoQ8e1.isChecked() & !rdoQ8e2.isChecked() & secQ8e.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q8e.");
                rdoQ8e1.requestFocus();
                return;
            } else if (!rdoQ8f1.isChecked() & !rdoQ8f2.isChecked() & secQ8f.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q8f.");
                rdoQ8e1.requestFocus();
                return;
            } else if (!rdoQ8g1.isChecked() & !rdoQ8g2.isChecked() & secQ8g.isShown()) {
                Connection.MessageBox(sesform.this, "Select anyone options from Q8g.");
                rdoQ8e1.requestFocus();
                return;
            }


            String SQL = "";

            if (!C.Existence("Select Dist ,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,StartTime,EndTime,UserId,EnDt,Upload from " + TableName + "  Where Dist ='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + txtHHNo.getText().toString() + "'")) {
                SQL = "Insert into " + TableName + "(Dist ,Upz,UN,Mouza,Vill,ProvType,ProvCode,HHNo,StartTime,EndTime,UserId,EnDt,Upload)Values('" + Dist + "','" + Upz + "','" + UN + "','" + Mouza + "','" + Vill + "','" + ProvType + "','" + ProvCode + "','" + txtHHNo.getText() + "','" + StartTime + "','" + g.CurrentTime24() + "','" + g.getUserID() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set upload='2',";
            SQL += "status = '" + (spnstatus.getSelectedItemPosition() == 0 ? "" : Global.Left(spnstatus.getSelectedItem().toString(), 1)) + "',";
            SQL += "Q1 = '" + (spnQ1.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ1.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q11 = '" + txtQ11.getText().toString() + "',";
            SQL += "Q2 = '" + (spnQ2.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ2.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q21 = '" + txtQ21.getText().toString() + "',";
            RadioButton rbQ3a = (RadioButton) findViewById(rdogrpQ3a.getCheckedRadioButtonId());
            SQL += "Q3a = '" + (rbQ3a == null ? "" : (Global.Left(rbQ3a.getText().toString(), 1))) + "',";
            RadioButton rbQ3b = (RadioButton) findViewById(rdogrpQ3b.getCheckedRadioButtonId());
            SQL += "Q3b = '" + (rbQ3b == null ? "" : (Global.Left(rbQ3b.getText().toString(), 1))) + "',";
            RadioButton rbQ3c = (RadioButton) findViewById(rdogrpQ3c.getCheckedRadioButtonId());
            SQL += "Q3c = '" + (rbQ3c == null ? "" : (Global.Left(rbQ3c.getText().toString(), 1))) + "',";
            RadioButton rbQ3d = (RadioButton) findViewById(rdogrpQ3d.getCheckedRadioButtonId());
            SQL += "Q3d = '" + (rbQ3d == null ? "" : (Global.Left(rbQ3d.getText().toString(), 1))) + "',";
            RadioButton rbQ3e = (RadioButton) findViewById(rdogrpQ3e.getCheckedRadioButtonId());
            SQL += "Q3e = '" + (rbQ3e == null ? "" : (Global.Left(rbQ3e.getText().toString(), 1))) + "',";
            RadioButton rbQ3f = (RadioButton) findViewById(rdogrpQ3f.getCheckedRadioButtonId());
            SQL += "Q3f = '" + (rbQ3f == null ? "" : (Global.Left(rbQ3f.getText().toString(), 1))) + "',";
            RadioButton rbQ3g = (RadioButton) findViewById(rdogrpQ3g.getCheckedRadioButtonId());
            SQL += "Q3g = '" + (rbQ3g == null ? "" : (Global.Left(rbQ3g.getText().toString(), 1))) + "',";
            RadioButton rbQ3h = (RadioButton) findViewById(rdogrpQ3h.getCheckedRadioButtonId());
            SQL += "Q3h = '" + (rbQ3h == null ? "" : (Global.Left(rbQ3h.getText().toString(), 1))) + "',";
            RadioButton rbQ3i = (RadioButton) findViewById(rdogrpQ3i.getCheckedRadioButtonId());
            SQL += "Q3i = '" + (rbQ3i == null ? "" : (Global.Left(rbQ3i.getText().toString(), 1))) + "',";
            RadioButton rbQ3j = (RadioButton) findViewById(rdogrpQ3j.getCheckedRadioButtonId());
            SQL += "Q3j = '" + (rbQ3j == null ? "" : (Global.Left(rbQ3j.getText().toString(), 1))) + "',";
            RadioButton rbQ3k = (RadioButton) findViewById(rdogrpQ3k.getCheckedRadioButtonId());
            SQL += "Q3k = '" + (rbQ3k == null ? "" : (Global.Left(rbQ3k.getText().toString(), 1))) + "',";
            RadioButton rbQ3l = (RadioButton) findViewById(rdogrpQ3l.getCheckedRadioButtonId());
            SQL += "Q3l = '" + (rbQ3l == null ? "" : (Global.Left(rbQ3l.getText().toString(), 1))) + "',";
            RadioButton rbQ3m = (RadioButton) findViewById(rdogrpQ3m.getCheckedRadioButtonId());
            SQL += "Q3m = '" + (rbQ3m == null ? "" : (Global.Left(rbQ3m.getText().toString(), 1))) + "',";
            RadioButton rbQ3n = (RadioButton) findViewById(rdogrpQ3n.getCheckedRadioButtonId());
            SQL += "Q3n = '" + (rbQ3n == null ? "" : (Global.Left(rbQ3n.getText().toString(), 1))) + "',";
            RadioButton rbQ3o = (RadioButton) findViewById(rdogrpQ3o.getCheckedRadioButtonId());
            SQL += "Q3o = '" + (rbQ3o == null ? "" : (Global.Left(rbQ3o.getText().toString(), 1))) + "',";
            RadioButton rbQ3p = (RadioButton) findViewById(rdogrpQ3p.getCheckedRadioButtonId());
            SQL += "Q3p = '" + (rbQ3p == null ? "" : (Global.Left(rbQ3p.getText().toString(), 1))) + "',";
            SQL += "Q4 = '" + (spnQ4.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ4.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q41 = '" + txtQ41.getText().toString() + "',";
            SQL += "Q5 = '" + (spnQ5.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ5.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q51 = '" + txtQ51.getText().toString() + "',";
            SQL += "Q6 = '" + (spnQ6.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ6.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q61 = '" + txtQ61.getText().toString() + "',";
            SQL += "Q7 = '" + (spnQ7.getSelectedItemPosition() == 0 ? "" : Global.Left(spnQ7.getSelectedItem().toString(), 2)) + "',";
            SQL += "Q71 = '" + txtQ71.getText().toString() + "',";
            RadioButton rbQ8a = (RadioButton) findViewById(rdogrpQ8a.getCheckedRadioButtonId());
            SQL += "Q8a = '" + (rbQ8a == null ? "" : (Global.Left(rbQ8a.getText().toString(), 1))) + "',";
            RadioButton rbQ8b = (RadioButton) findViewById(rdogrpQ8b.getCheckedRadioButtonId());
            SQL += "Q8b = '" + (rbQ8b == null ? "" : (Global.Left(rbQ8b.getText().toString(), 1))) + "',";
            RadioButton rbQ8c = (RadioButton) findViewById(rdogrpQ8c.getCheckedRadioButtonId());
            SQL += "Q8c = '" + (rbQ8c == null ? "" : (Global.Left(rbQ8c.getText().toString(), 1))) + "',";
            RadioButton rbQ8d = (RadioButton) findViewById(rdogrpQ8d.getCheckedRadioButtonId());
            SQL += "Q8d = '" + (rbQ8d == null ? "" : (Global.Left(rbQ8d.getText().toString(), 1))) + "',";
            RadioButton rbQ8e = (RadioButton) findViewById(rdogrpQ8e.getCheckedRadioButtonId());
            SQL += "Q8e = '" + (rbQ8e == null ? "" : (Global.Left(rbQ8e.getText().toString(), 1))) + "',";

            RadioButton rbQ8f = (RadioButton) findViewById(rdogrpQ8f.getCheckedRadioButtonId());
            SQL += "Q9 = '" + (rbQ8f == null ? "" : (Global.Left(rbQ8f.getText().toString(), 1))) + "',";

            RadioButton rbQ8g = (RadioButton) findViewById(rdogrpQ8g.getCheckedRadioButtonId());
            SQL += "Q10 = '" + (rbQ8g == null ? "" : (Global.Left(rbQ8g.getText().toString(), 1))) + "'";


            SQL += "  Where Dist ='" + Dist + "' and Upz='" + Upz + "' and UN='" + UN + "' and Mouza='" + Mouza + "' and Vill='" + Vill + "' and HHNo='" + txtHHNo.getText().toString() + "'";
            C.Save(SQL);
            Connection.MessageBox(sesform.this, "Saved Successfully");

            finish();
        } catch (Exception e) {
            Connection.MessageBox(sesform.this, e.getMessage());
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

                //txtHHNo.setText(cur.getString(cur.getColumnIndex("HHNo")));
                spnstatus.setSelection(Global.SpinnerItemPosition(spnstatus, 1, cur.getString(cur.getColumnIndex("status"))));
                spnQ1.setSelection(Global.SpinnerItemPosition(spnQ1, 2, cur.getString(cur.getColumnIndex("Q1"))));
                txtQ11.setText(cur.getString(cur.getColumnIndex("Q11")));
                spnQ2.setSelection(Global.SpinnerItemPosition(spnQ2, 2, cur.getString(cur.getColumnIndex("Q2"))));
                txtQ21.setText(cur.getString(cur.getColumnIndex("Q21")));
                for (int i = 0; i < rdogrpQ3a.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3a.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3a"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3b.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3b.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3b"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3c.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3c.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3c"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3d.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3d.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3d"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3e.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3e.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3e"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3f.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3f.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3f"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3g.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3g.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3g"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3h.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3h.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3h"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3i.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3i.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3i"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3j.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3j.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3j"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3k.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3k.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3k"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3l.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3l.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3l"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3m.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3m.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3m"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3n.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3n.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3n"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3o.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3o.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3o"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ3p.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ3p.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q3p"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                spnQ4.setSelection(Global.SpinnerItemPosition(spnQ4, 2, cur.getString(cur.getColumnIndex("Q4"))));
                txtQ41.setText(cur.getString(cur.getColumnIndex("Q41")));
                spnQ5.setSelection(Global.SpinnerItemPosition(spnQ5, 2, cur.getString(cur.getColumnIndex("Q5"))));
                txtQ51.setText(cur.getString(cur.getColumnIndex("Q51")));
                spnQ6.setSelection(Global.SpinnerItemPosition(spnQ6, 2, cur.getString(cur.getColumnIndex("Q6"))));
                txtQ61.setText(cur.getString(cur.getColumnIndex("Q61")));
                spnQ7.setSelection(Global.SpinnerItemPosition(spnQ7, 2, cur.getString(cur.getColumnIndex("Q7"))));
                txtQ71.setText(cur.getString(cur.getColumnIndex("Q71")));
                for (int i = 0; i < rdogrpQ8a.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ8a.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q8a"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ8b.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ8b.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q8b"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ8c.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ8c.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q8c"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ8d.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ8d.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q8d"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpQ8e.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ8e.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q8e"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }

                for (int i = 0; i < rdogrpQ8f.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ8f.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q9"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }

                for (int i = 0; i < rdogrpQ8g.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpQ8g.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Q10"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }


                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(sesform.this, e.getMessage());
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