package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.Changelog;

public class ImprintFragment extends Fragment implements RequestCallbackInterface {

    private OnFragmentInteractionListener mListener;

    public ImprintFragment() {
        // Required empty public constructor
    }

    public static ImprintFragment newInstance() {
        ImprintFragment fragment = new ImprintFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ApiHelper apiHelper = new ApiHelper(this);
        apiHelper.getChangelog();

        return inflater.inflate(R.layout.fragment_imprint, container, false);
    }

    @Override
    public void onResponse(JSONObject response, Class type) {
        if (type.equals(Changelog.Wrapper.class)) {

            Gson gson = new Gson();
            Changelog.Wrapper value = gson.fromJson(response.toString(), Changelog.Wrapper.class);

            ArrayList<Changelog> changelogs = new ArrayList<>(Arrays.asList(value.data));

            TextView tv = this.getActivity().findViewById(R.id.tv_imprint_main);
            tv.setText(changelogs.get(1).toString());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
