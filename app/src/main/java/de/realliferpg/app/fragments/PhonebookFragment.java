package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.PhonebookAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.Phone;
import de.realliferpg.app.objects.PlayerInfo;

public class PhonebookFragment extends Fragment implements CallbackNotifyInterface {

    private View view;
    private FragmentInteractionInterface mListener;

    public PhonebookFragment() {
        // Required empty public constructor
    }

    public static PhonebookFragment newInstance() {
        return new PhonebookFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phonebooks, container, false);

        final ProgressBar pbLoadphonebook = view.findViewById(R.id.pb_phonebook);
        pbLoadphonebook.setVisibility(View.VISIBLE);
        final ExpandableListView listPhonebooks = view.findViewById(R.id.lv_phonebook);

        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
        apiHelper.getPlayerStats();

        PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();

        final TextView tvDefaultPhoneNumber = view.findViewById(R.id.tv_default_phonenumber);
        tvDefaultPhoneNumber.setText(getContext().getResources().getString(R.string.str_default_phonenumber) + " " + getDefaultNumber(playerInfo.phones));
        final TextView tvKeineDaten = view.findViewById(R.id.tv_no_data_phonebooks);
        final ExpandableListView lvPhonebooks = view.findViewById(R.id.lv_phonebook);

        final SwipeRefreshLayout sc = view.findViewById(R.id.srl_main_phonebook);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getPlayerStats();

                pbLoadphonebook.setVisibility(View.VISIBLE);

                listPhonebooks.setAdapter((BaseExpandableListAdapter) null);
            }
        });

        listPhonebooks.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRow = (listPhonebooks == null || listPhonebooks.getChildCount() == 0) ?
                        0 : listPhonebooks.getChildAt(0).getTop();
                sc.setEnabled(firstVisibleItem == 0 && topRow >= 0);
            }
        });

        if (playerInfo.phonebooks == null || playerInfo.phonebooks.length == 0){
            tvKeineDaten.setVisibility(View.VISIBLE);
            lvPhonebooks.setVisibility(View.INVISIBLE);
        } else {
            tvKeineDaten.setVisibility(View.INVISIBLE);
            lvPhonebooks.setVisibility(View.VISIBLE);
        }

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

    @Override
    public void onCallback(RequestTypeEnum type) {
        ProgressBar pblistPhonebooks = view.findViewById(R.id.pb_phonebook);
        SwipeRefreshLayout sc = view.findViewById(R.id.srl_main_phonebook);
        sc.setRefreshing(false);

        switch (type) {
            case PLAYER:
                final ExpandableListView listPhoneBooks = view.findViewById(R.id.lv_phonebook);

                PhonebookAdapter listAdapter = new PhonebookAdapter(this.getContext(), Singleton.getInstance().getPlayerInfo());

                listPhoneBooks.setAdapter(listAdapter);

                pblistPhonebooks.setVisibility(View.GONE);

                // collapse all but selected item
                listPhoneBooks.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousItem = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousItem)
                            listPhoneBooks.collapseGroup(previousItem);
                        previousItem = groupPosition;
                    }
                });
                break;
            case NETWORK_ERROR:
                CustomNetworkError error = Singleton.getInstance().getNetworkError();

                pblistPhonebooks.setVisibility(View.GONE);

                Singleton.getInstance().setErrorMsg(error.toString());

                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_main_phonebook), R.string.str_error_occurred, Constants.ERROR_SNACKBAR_DURATION);

                snackbar.setAction(R.string.str_view, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onFragmentInteraction(ChangelogFragment.class, Uri.parse("open_error"));
                    }
                });

                snackbar.show();
                Singleton.getInstance().setCurrentSnackbar(snackbar);
                break;
        }
    }

    private String getDefaultNumber(Phone[] phones) {
        for (Phone phone : phones) {
            if (phone.note.matches("default")) {
                return phone.phone;
            }
        }
        return "0";
    }
}
