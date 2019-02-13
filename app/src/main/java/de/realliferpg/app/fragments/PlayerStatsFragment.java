package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.PlayersFractionListAdapter;
import de.realliferpg.app.adapter.PlayersListAdapter;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.FractionInfo;
import de.realliferpg.app.objects.PlayerInfo;

public class PlayerStatsFragment extends Fragment {

    private View view;
    private FragmentInteractionInterface mListener;
    private FractionInfo fractionInfo;

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

        if(Singleton.getInstance().getPlayerInfo() == null){
            Singleton.getInstance().setErrorMsg("PlayerStatsFragment Error Code #1");
            mListener.onFragmentInteraction(PlayerStatsFragment.class,Uri.parse("open_error"));
        }else {
            showPlayerInfo();
        }

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

        final ExpandableListView listViewFractions = view.findViewById(R.id.lv_fractionen);
        this.fractionInfo = new FractionInfo(Integer.parseInt(playerInfo.coplevel), Integer.parseInt(playerInfo.mediclevel), Integer.parseInt(playerInfo.adaclevel));
        PlayersFractionListAdapter listAdapter = new PlayersFractionListAdapter(this.getContext(), this.fractionInfo);
        listViewFractions.setAdapter(listAdapter);

        listViewFractions.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem)
                    listViewFractions.collapseGroup(previousItem);
                previousItem = groupPosition;
            }
        });
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
