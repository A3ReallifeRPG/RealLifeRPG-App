package de.realliferpg.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FractionMappingHelper;
import de.realliferpg.app.interfaces.FractionEnum;
import de.realliferpg.app.objects.Phones;
import de.realliferpg.app.objects.PhonebookEntry;
import de.realliferpg.app.objects.Phonebooks;
import de.realliferpg.app.objects.PlayerInfo;


public class PhonebookAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final PlayerInfo playerInfo;
    private ArrayList<Phonebooks> filteredList;
    private ArrayList<Phonebooks> originalList;

    public PhonebookAdapter(Context _context, PlayerInfo _playerInfo) {
        this.context = _context;
        this.playerInfo = _playerInfo;
        this.filteredList = new ArrayList<>();
        this.originalList = new ArrayList<>();
        this.originalList.addAll(Arrays.asList(_playerInfo.phonebooks));
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
        int idNrPhone = this.playerInfo.phones[groupPosition].idNR;
        PhonebookEntry[] phoneBook = null;

        for (Phonebooks book : this.playerInfo.phonebooks) {
            if (book.idNR == idNrPhone)
            {
                phoneBook = book.phonebook;
            }
        }

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

            viewHolder.tvPhonebookToPhonenumber = convertView.findViewById(R.id.tv_phonebook_to_phonenumber);
            viewHolder.tvSide = convertView.findViewById(R.id.tv_phonebook_side);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = groupPosition;

        viewHolder.tvPhonebookToPhonenumber.setText("Telefonbuch zur Nr. " + this.playerInfo.phones[groupPosition].phone);

        FractionEnum fractionEnum = FractionMappingHelper.getFractionFromSide(this.playerInfo.phones[groupPosition].side, Integer.valueOf(playerInfo.coplevel));
        String textSidePlusMaybeDefault = FractionMappingHelper.getFractionNameFromEnum(this.context, fractionEnum);
        if (this.playerInfo.phones[groupPosition].note.toLowerCase().equals("default"))
        {
            textSidePlusMaybeDefault += " (default)";
        }

        viewHolder.tvSide.setText(textSidePlusMaybeDefault);

        convertView.setTag(viewHolder);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PhonebookEntry phoneBookEntry = this.playerInfo.phonebooks[groupPosition].phonebook[childPosition];

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

    public void filterData(String query){

        query = query.toLowerCase();
        filteredList.clear();

        if (query.isEmpty()){
            filteredList.addAll(originalList);
        }
        else {

            for (Phonebooks phonebook : originalList){
                ArrayList<PhonebookEntry> phonebookList = new ArrayList<PhonebookEntry>(Arrays.asList(phonebook.phonebook));
                ArrayList<PhonebookEntry> newPhonebookList = new ArrayList<PhonebookEntry>();
                for (PhonebookEntry entry: phonebookList){
                    if(entry.name.toLowerCase().contains(query)){
                        newPhonebookList.add(entry);
                    }
                }
                if (newPhonebookList.size() > 0){
                    Phonebooks newEntry = new Phonebooks(phonebook.idNR, newPhonebookList.toArray(new PhonebookEntry[newPhonebookList.size()]));
                    filteredList.add(newEntry);
                }
            }
        }

        this.playerInfo.phonebooks = filteredList.toArray(new Phonebooks[filteredList.size()]);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView tvSide;
        TextView tvPhonebookToPhonenumber;
        int position;
    }
}
