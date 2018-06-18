package edu.wit.karen.seniorprojectrev1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class TimeDialog extends AppCompatDialogFragment {

    private EditText hourText;
    private EditText minuteText;
    private CheckBox[] weekBoxes;
    private TimeDialogueListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialogue_alarm, null);

        hourText = view.findViewById(R.id.DiaHour);
        minuteText = view.findViewById(R.id.DiaMinute);
        weekBoxes = new CheckBox[]{view.findViewById(R.id.checkBoxDiaSu), view.findViewById(R.id.checkBoxDiaM), view.findViewById(R.id.checkBoxDiaTu),view.findViewById(R.id.checkBoxDiaW), view.findViewById(R.id.checkBoxDiaTh),view.findViewById(R.id.checkBoxDiaF), view.findViewById(R.id.checkBoxDiaSa)};
        builder.setView(view)
                .setTitle("Enter New Alarm")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                        int hour = Integer.parseInt(hourText.getText().toString());
                        int minute = Integer.parseInt(minuteText.getText().toString());
                        boolean[] boxes = new boolean[7];

                        for(int i = 0; i < 6; i++)
                        {

                            boxes[i] = weekBoxes[i].isChecked();
                        }

                      //  listener.applyInfo(hour,minute,boxes);
                      */
                    }
                });

        return builder.create();
    }

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (TimeDialogueListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement TimeDialogueListener");
        }
    } */

    public interface  TimeDialogueListener
    {
        void applyInfo(int hour, int minute, boolean[] boxes);
    }
}
