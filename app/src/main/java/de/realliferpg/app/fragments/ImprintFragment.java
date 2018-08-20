package de.realliferpg.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import de.realliferpg.app.BuildConfig;
import de.realliferpg.app.R;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.Changelog;

public class ImprintFragment extends Fragment {

    private FragmentInteractionInterface mListener;

    public ImprintFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_imprint, container, false);

        WebView webView = view.findViewById(R.id.wv_imprint_main);
        webView.loadUrl("file:///android_asset/imprint.html");
        webView.setBackgroundColor(getResources().getColor(R.color.secondaryColor));

        TextView tv = view.findViewById(R.id.tv_imprint_debug);
        if(BuildConfig.DEBUG){
            tv.setText("DEBUG");
        }else{
            tv.setText("RELEASE");
        }

        Button btnLicense = view.findViewById(R.id.btn_imprint_licenseInfo);
        btnLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OssLicensesMenuActivity.class));
            }
        });

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
