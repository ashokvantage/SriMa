package yoga.in.co.srima;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yoga.in.co.srima.R;

/**
 * Created by Belal on 2/3/2016.
 */
//Our class extending fragment
public class Tab_Edit_Update extends Fragment {
    LinearLayout layoutedittext_name, layoutedittext_date, layoutbutton_add, layoutsubmit, layoutbutton_remove;
    LinearLayout.LayoutParams paramsedittext, paramsbutton, paramssubmit;
    EditText edttext_name[] = new EditText[100], edttext_date[] = new EditText[100];
    Button btn_add, btn_remove, btn_submit;
    List<EditText> allEds_name = new ArrayList<EditText>();
    List<EditText> allEds_date = new ArrayList<EditText>();
    DatePickerDialog emp_joindate;
    SimpleDateFormat dateFormatter;
    int id = 0;
    int edt_id = 2;
    int length;
    boolean allready_added = false;
    boolean all_entry_fill = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_edit_update, container, false);


        layoutedittext_name = (LinearLayout) view.findViewById(R.id.linearLayoutedittext);
        layoutedittext_date = (LinearLayout) view.findViewById(R.id.linearLayoutedittext_date);
//        layout_remove = (LinearLayout) view.findViewById(R.id.linearLayoutremove);
        paramsedittext = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        layoutbutton_add = (LinearLayout) view.findViewById(R.id.linearLayoutbtnadd);
        layoutbutton_remove = (LinearLayout) view.findViewById(R.id.linearLayoutbtnremove);
        paramsbutton = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        paramsbutton.gravity = Gravity.RIGHT;

        layoutsubmit = (LinearLayout) view.findViewById(R.id.linearLayoutsubmit);
        paramssubmit = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramssubmit.gravity = Gravity.CENTER_HORIZONTAL;
        allEds_name.clear();
        allEds_date.clear();
        layoutedittext_name.removeAllViews();
        layoutedittext_date.removeAllViews();
//        Execute();


        btn_add = new Button(getActivity());
        btn_add.setLayoutParams(paramsbutton);
        btn_add.setText("+");
        layoutbutton_add.addView(btn_add);

        btn_remove = new Button(getActivity());
        btn_remove.setLayoutParams(paramsbutton);
        btn_remove.setText("-");
        layoutbutton_remove.addView(btn_remove);

        btn_submit = new Button(getActivity());
        btn_submit.setLayoutParams(paramssubmit);
        btn_submit.setText("UPDATE");
        layoutsubmit.addView(btn_submit);
        for (int i = 0; i < 6; i++) {
            edt_id = edt_id + 1;
            edttext_name[edt_id] = new EditText(getActivity());
            allEds_name.add(edttext_name[edt_id]);
            edttext_name[edt_id].setLayoutParams(paramsedittext);
            layoutedittext_name.addView(edttext_name[edt_id]);

            edttext_date[edt_id] = new EditText(getActivity());
            allEds_date.add(edttext_date[edt_id]);
            edttext_date[edt_id].setId(edt_id + 0);
            edttext_date[edt_id].setInputType(InputType.TYPE_NULL);
            edttext_date[edt_id].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    int pos1 = (Integer) v.getId();//getTag();
                    id = edttext_date[pos1].getId();
                    emp_joindate.show();
                }
            });
            edttext_date[edt_id].setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        int pos1 = (Integer) v.getId();
                        id = edttext_date[pos1].getId();
                        emp_joindate.show();
                    }
                }
            });

            edttext_date[edt_id].setLayoutParams(paramsedittext);
            layoutedittext_date.addView(edttext_date[edt_id]);


            if (allEds_name.size() > 0) {
                btn_remove.setVisibility(View.VISIBLE);
            }
        }
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_id = edt_id + 1;
                edttext_name[edt_id] = new EditText(getActivity());
                allEds_name.add(edttext_name[edt_id]);
                edttext_name[edt_id].setLayoutParams(paramsedittext);
                layoutedittext_name.addView(edttext_name[edt_id]);

                edttext_date[edt_id] = new EditText(getActivity());
                allEds_date.add(edttext_date[edt_id]);
                edttext_date[edt_id].setId(edt_id + 0);
                edttext_date[edt_id].setInputType(InputType.TYPE_NULL);
                edttext_date[edt_id].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        int pos1 = (Integer) v.getId();//getTag();
                        id = edttext_date[pos1].getId();
                        emp_joindate.show();
                    }
                });
                edttext_date[edt_id].setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            int pos1 = (Integer) v.getId();
                            id = edttext_date[pos1].getId();
                            emp_joindate.show();
                        }
                    }
                });

                edttext_date[edt_id].setLayoutParams(paramsedittext);
                layoutedittext_date.addView(edttext_date[edt_id]);


                if (allEds_name.size() > 0) {
                    btn_remove.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutedittext_name.removeView(edttext_name[edt_id]);
                allEds_name.remove(edttext_name[edt_id]);

                layoutedittext_date.removeView(edttext_date[edt_id]);
                allEds_date.remove(edttext_date[edt_id]);

                if (allEds_name.size() == 0) {
                    btn_remove.setVisibility(View.GONE);
                }
                edt_id = edt_id - 1;
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_entry_fill = true;
                String strings_name;// = new String[allEds_name.size()];
                String strings_date;// = new String[allEds_date.size()];

                for (int i = 0; i < allEds_name.size(); i++) {
                    strings_name = allEds_name.get(i).getText().toString();

                    strings_date = allEds_date.get(i).getText().toString();

                    if (strings_name.equalsIgnoreCase("")) {
                        allEds_name.get(i).requestFocus();
                        allEds_name.get(i).setText("");
                        allEds_name.get(i).setError("fill festival name before submit.");

                        all_entry_fill = false;
                        break;
                    } else if (strings_date.equalsIgnoreCase("")) {
                        allEds_date.get(i).requestFocus();
                        allEds_date.get(i).setText("");
                        allEds_date.get(i).setError("fill festival date before submit.");
                        all_entry_fill = false;
                        break;
                    }
                }
                if (all_entry_fill) {
                    for (int i = 0; i < allEds_name.size(); i++) {
                        strings_name = allEds_name.get(i).getText().toString();

                        strings_date = allEds_date.get(i).getText().toString();
                    }
                }


            }
        });


        Calendar newCalendar = Calendar.getInstance();
        emp_joindate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if (view.isShown()) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String date = "" + dayOfMonth;
                    if ((date.endsWith("1")) && (!date.endsWith("11")))
                        dateFormatter = new SimpleDateFormat("EE MMM d'st', yyyy");
                    else if ((date.endsWith("2")) && (!date.endsWith("12")))
                        dateFormatter = new SimpleDateFormat("EE MMM d'nd', yyyy");
                    else if ((date.endsWith("3")) && (!date.endsWith("13")))
                        dateFormatter = new SimpleDateFormat("EE MMM d'rd', yyyy");
                    else
                        dateFormatter = new SimpleDateFormat("EE MMM d'th', yyyy");


                    String[] strings_date = new String[allEds_date.size()];

                    for (int i = 0; i < allEds_name.size(); i++) {
                        strings_date[i] = allEds_date.get(i).getText().toString();
                        if (strings_date[i].equalsIgnoreCase(dateFormatter.format(newDate.getTime()))) {
                            allready_added = true;
                            break;
                        }
                    }
                    if (allready_added) {
                        allEds_date.get(id).requestFocus();
                        allEds_date.get(id).setError("This dates allready added.");
                        allready_added = false;
                        allEds_date.get(id).setFocusable(true);
                    } else {
                        allEds_date.get(id).setText(dateFormatter.format(newDate.getTime()));
                        allEds_date.get(id).setError(null);
                        allEds_date.get(id).setFocusable(false);
                    }
                }


            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        allEds_name.clear();
        allEds_date.clear();
        layoutedittext_name.removeAllViews();
        layoutedittext_date.removeAllViews();
    }
}
