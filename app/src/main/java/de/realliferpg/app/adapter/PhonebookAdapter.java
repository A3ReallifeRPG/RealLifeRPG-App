package de.realliferpg.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FractionMappingHelper;
import de.realliferpg.app.interfaces.FractionEnum;
import de.realliferpg.app.objects.PhoneNumbers;
import de.realliferpg.app.objects.PhonebookEntry;
import de.realliferpg.app.objects.PlayerInfo;


public class PhonebookAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final PlayerInfo playerInfo;

    public PhonebookAdapter(Context _context, PlayerInfo _playerInfo) {
        this.context = _context;
        this.playerInfo = _playerInfo;
    }

    @Override
    public int getGroupCount() {
        return playerInfo.phonebooks.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return playerInfo.phonebooks[i].phonebook.length;
    }

    @Override
    public Object getGroup(int i) {
        return playerInfo.phonebooks[i];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        PhonebookEntry[] phoneBook = playerInfo.phonebooks[groupPosition].phonebook;
        return phoneBook[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_group_phonebook, null);

            viewHolder = new ViewHolder();
            viewHolder.position = groupPosition;

            viewHolder.tvSide = convertView.findViewById(R.id.tv_phonebook_side);
            viewHolder.tvOwnNumbers = convertView.findViewById(R.id.tv_phonebook_own_numbers);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = groupPosition;

        FractionEnum fractionEnum = FractionMappingHelper.getFractionFromSide(playerInfo.phonebooks[groupPosition].side, Integer.valueOf(playerInfo.coplevel));
        viewHolder.tvSide.setText(FractionMappingHelper.getFractionNameFromEnum(this.context, fractionEnum));

        String defaultNumber = getDefaultNumber(playerInfo.phones);

        if (playerHasMultipleOwnNumbers(playerInfo.phones, fractionEnum)) {
            viewHolder.tvOwnNumbers.setVisibility(View.INVISIBLE);
        } else {
            String textOwnPhoneNumber = context.getResources().getString(R.string.str_own_phonenumber) + " " + getNumberFormSide(playerInfo.phones, fractionEnum);
            viewHolder.tvOwnNumbers.setText(textOwnPhoneNumber);
        }

        convertView.setTag(viewHolder);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PhonebookEntry phoneBookEntry = playerInfo.phonebooks[groupPosition].phonebook[childPosition];

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_phonebook, null);
        }

        TextView tvName = convertView.findViewById(R.id.tv_phonebook_name);
        TextView tvNumber = convertView.findViewById(R.id.tv_phonebook_number);
        TextView tvIban = convertView.findViewById(R.id.tv_phonebook_iban);

        tvName.setText(context.getResources().getString(R.string.str_name) + " " + phoneBookEntry.name);
        tvNumber.setText(context.getResources().getString(R.string.str_number) + " " + phoneBookEntry.number);
        tvIban.setText(context.getResources().getString(R.string.str_iban) + " " + phoneBookEntry.iban);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private boolean playerHasMultipleOwnNumbers(PhoneNumbers[] phones, FractionEnum fractionEnum) {
        int count = 0;
        for (PhoneNumbers phone : phones) {
            if (phone.disabled != 0) {
                continue;
            }
            if (phone.note.matches("default")) {
                continue;
            }
            if (phone.side.matches(FractionMappingHelper.getSideFromFractionEnum(fractionEnum))) {
                continue;
            }
            count++;
        }

        return count > 1;
    }

    private String getDefaultNumber(PhoneNumbers[] phones) {
        for (PhoneNumbers phone : phones) {
            if (phone.note.matches("default")) {
                return phone.phone;
            }
        }
        return "0";
    }

    private String getNumberFormSide(PhoneNumbers[] phones, FractionEnum fractionEnum) {
        for (PhoneNumbers phone : phones) {
            if (phone.disabled != 0) {
                continue;
            }
            if (phone.note.matches("default")) {
                continue;
            }
            if (phone.side.matches(FractionMappingHelper.getSideFromFractionEnum(fractionEnum))) {
                return phone.phone;
            }
        }
        return "0";
    }

    private static class ViewHolder {
        TextView tvSide;
        TextView tvOwnNumbers;
        int position;
    }
}
