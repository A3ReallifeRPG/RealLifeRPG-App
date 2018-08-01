package de.realliferpg.app.interfaces;

import android.net.Uri;

public interface FragmentInteractionInterface<T> {
    void onFragmentInteraction(Class<T> type,Uri uri);
}
