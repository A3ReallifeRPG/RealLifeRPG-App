package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.Changelog;
import de.realliferpg.app.objects.Server;


public class MainFragment extends Fragment implements RequestCallbackInterface {

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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
        apiHelper.getServers();

        return inflater.inflate(R.layout.fragment_main, container, false);
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

    @Override
    public void onResponse(JSONObject response, Class type) {
        if (type.equals(Server.Wrapper.class)) {
            Gson gson = new Gson();

            Server.Wrapper value = gson.fromJson(response.toString(), Server.Wrapper.class);

            ArrayList<Server> servers = new ArrayList<>(Arrays.asList(value.data));

            TextView tv = this.getActivity().findViewById(R.id.tv_main_test);
            tv.setText(servers.get(0).toString());

            Server server1 = servers.get(0); //TODO actually search for s1

            TextView tvS1Head = this.getActivity().findViewById(R.id.tv_main_s1_head);
            ProgressBar pbS1Player = this.getActivity().findViewById(R.id.pb_main_s1_players);


            String title = server1.Servername + " - " +  server1.Playercount + "/" + server1.Slots;
            tvS1Head.setText(title);

            double progress = ((double) server1.Playercount / (double) server1.Slots)*100;

            pbS1Player.setProgress( (int) progress);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
