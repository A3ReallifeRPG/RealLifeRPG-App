package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.objects.PlayerInfo;

public class PlayerStatsFragment extends Fragment {

    private View view;
    private OnFragmentInteractionListener mListener;

    public PlayerStatsFragment() {
        // Required empty public constructor
    }

    public static PlayerStatsFragment newInstance() {
        PlayerStatsFragment fragment = new PlayerStatsFragment();
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
        view = inflater.inflate(R.layout.fragment_player_stats, container, false);

        showPlayerInfo();

        return view;
    }

    public void showPlayerInfo(){
        FormatHelper formatHelper = new FormatHelper();
        PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();

        TextView tvCash = view.findViewById(R.id.tv_p_stats_cash);
        TextView tvBank = view.findViewById(R.id.tv_p_stats_bank);

        TextView tvLevelCur = view.findViewById(R.id.tv_p_stats_level_cur);
        TextView tvLevelNext = view.findViewById(R.id.tv_p_stats_level_next);
        ProgressBar pbLevel = view.findViewById(R.id.pb_p_stats_level);

        TextView tvSkillpoint = view.findViewById(R.id.tv_p_stats_skillpoint);
        TextView tvLastSeen = view.findViewById(R.id.tv_p_stats_last_seen);

        tvCash.setText(formatHelper.formatCurrency(playerInfo.cash));
        tvBank.setText(formatHelper.formatCurrency(playerInfo.bankacc));

        tvLevelCur.setText(String.valueOf(playerInfo.level));
        tvLevelNext.setText(String.valueOf(playerInfo.level + 1));
        pbLevel.setProgress(playerInfo.level_progress);

        tvLastSeen.setText(formatHelper.toDateTime(formatHelper.getApiDate(playerInfo.last_seen.date)));
        tvSkillpoint.setText(String.valueOf(playerInfo.skillpoint));
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
        void onFragmentInteraction(Uri uri);
    }

}
