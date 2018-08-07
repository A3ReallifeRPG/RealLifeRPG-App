package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;


public class ErrorFragment extends Fragment {

    private FragmentInteractionInterface mListener;
    View view;

    public ErrorFragment() {
        // Required empty public constructor
    }

    public static ErrorFragment newInstance() {
        return new ErrorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_error, container, false);

        Button btnSettings = view.findViewById(R.id.btn_error_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(ErrorFragment.class,Uri.parse("open_settings"));
            }
        });

        TextView tvErrorMsg = view.findViewById(R.id.tv_error_msg);
        tvErrorMsg.setText(Singleton.getInstance().getErrorMsg());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionInterface) {
            mListener = (FragmentInteractionInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

}
