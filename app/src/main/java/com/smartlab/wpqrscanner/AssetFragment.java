package com.smartlab.wpqrscanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class AssetFragment extends Fragment {
    // String name = getArguments().getString("AssetInfo", "");
    TextView txt3, txt1, txt2, txt4, txt5, txt6,txt7, txt8, txt9, txt10, txt11, txt12;
    TableLayout tl;
    TableRow tr2,tr3;
    Button buttonHide;
    Boolean visible = true;
    DatabaseHelper db;

    @Override
    public void onSaveInstanceState(Bundle outState) {


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assetlist, parent, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String data = bundle.getString("AssetInfo");
            String[] separated = data.split("\\|");
            TextView textView = view.findViewById(R.id.txtAssetInfo);
            textView.setText("");
            for (String words : separated) {
                textView.append(words + "\n");
            }
            Toast.makeText(getContext(),separated[0].substring(separated[0].indexOf(":") + 1)+"\n"+
                    separated[1].substring(separated[1].indexOf(":") + 1)+"\n"+
                    separated[2].substring(separated[2].indexOf(":") + 1)+"\n"+
                    separated[3].substring(separated[3].indexOf(":") + 1)+"\n"+
                    separated[4].substring(separated[4].indexOf(":") + 1)+"\n"+
                    separated[5].substring(separated[5].indexOf(":") + 1),Toast.LENGTH_SHORT).show();
            db = new DatabaseHelper(getContext());
            Boolean add = db.addData(separated[0].substring(separated[0].indexOf(":") + 1),
                    separated[1].substring(separated[1].indexOf(":") + 1),
                    separated[2].substring(separated[2].indexOf(":") + 1),
                    separated[3].substring(separated[3].indexOf(":") + 1),
                    separated[4].substring(separated[4].indexOf(":") + 1),
                    separated[5].substring(separated[5].indexOf(":") + 1));
            if (add == true) {
                Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            DatabaseHelper db = new DatabaseHelper(getContext());
            Cursor cursor=db.getData();
            deleteButton();
            addTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void deleteButton(){
        buttonHide =(Button) getView().findViewById(R.id.buttonHide);
        final DatabaseHelper db = new DatabaseHelper(getContext());
        buttonHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(getContext())
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you want to delete all log?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                db.deleteData();
                            }
                        }).create().show();


                FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.contain_main, new AssetFragment());
                    fragmentTransaction.commit();
                }
        });
    }


    public void addTable() {

        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.getData();
        tl = getView().findViewById(R.id.tl);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_SHORT).show();
        } else {

            int TextSize = 14;

                    txt1 = new TextView(getContext());
                    txt1.setText(cursor.getString(1));
                    txt1.setGravity(Gravity.CENTER);
                    txt1.setTextSize(TextSize);
                    txt2 = new TextView(getContext());
                    txt2.setText(cursor.getString(2));
                    txt2.setGravity(Gravity.CENTER);
                    txt2.setTextSize(TextSize);
                    txt3 = new TextView(getContext());
                    txt3.setText(cursor.getString(3));
                    txt3.setGravity(Gravity.CENTER);
                    txt3.setTextSize(TextSize);
                    txt4 = new TextView(getContext());
                    txt4.setText(cursor.getString(4));
                    txt4.setGravity(Gravity.CENTER);
                    txt4.setTextSize(TextSize);
                    txt5 = new TextView(getContext());
                    txt5.setText(cursor.getString(5));
                    txt5.setGravity(Gravity.CENTER);
                    txt5.setTextSize(TextSize);
                    txt6 = new TextView(getContext());
                    txt6.setText(cursor.getString(6));
                    txt6.setGravity(Gravity.CENTER);
                    txt6.setTextSize(TextSize);

                    tr2 = new TableRow(getContext());
                    tr2.addView(txt1, lp);
                    tr2.addView(txt2, lp);
                    tr2.addView(txt3, lp);
                    tr2.addView(txt4, lp);
                    tr2.addView(txt5, lp);
                    tr2.addView(txt6, lp);
                    tl.addView(tr2);
                 while (cursor.moveToNext()){
                     txt1 = new TextView(getContext());
                     txt1.setText(cursor.getString(1));
                     txt1.setGravity(Gravity.CENTER);
                     txt1.setTextSize(TextSize);
                     txt2 = new TextView(getContext());
                     txt2.setText(cursor.getString(2));
                     txt2.setGravity(Gravity.CENTER);
                     txt2.setTextSize(TextSize);
                     txt3 = new TextView(getContext());
                     txt3.setText(cursor.getString(3));
                     txt3.setGravity(Gravity.CENTER);
                     txt3.setTextSize(TextSize);
                     txt4 = new TextView(getContext());
                     txt4.setText(cursor.getString(4));
                     txt4.setGravity(Gravity.CENTER);
                     txt4.setTextSize(TextSize);
                     txt5 = new TextView(getContext());
                     txt5.setText(cursor.getString(5));
                     txt5.setGravity(Gravity.CENTER);
                     txt5.setTextSize(TextSize);
                     txt6 = new TextView(getContext());
                     txt6.setText(cursor.getString(6));
                     txt6.setGravity(Gravity.CENTER);
                     txt6.setTextSize(TextSize);

                     tr2 = new TableRow(getContext());
                     tr2.addView(txt1, lp);
                     tr2.addView(txt2, lp);
                     tr2.addView(txt3, lp);
                     tr2.addView(txt4, lp);
                     tr2.addView(txt5, lp);
                     tr2.addView(txt6, lp);
                     tl.addView(tr2);
                 }
            }
    }
}