package com.material.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.material.R;

import java.text.Collator;
import java.util.*;

/**
 * Created by mojingtian on 14/11/19.
 */
public class ObScrollviewFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String CATEGORY_SAMPLES = "ObScrollview";
    private static final String TAG_CLASS_NAME = "className";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_INTENT = "intent";

    private static final Comparator<Map<String, Object>> DISPLAY_NAME_COMPARATOR = new Comparator<Map<String, Object>>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            return collator.compare(lhs.get("className"), rhs.get("className"));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_ob_scrollview, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView listView = (ListView) getView().findViewById(android.R.id.list);
        listView.setAdapter(new SimpleAdapter(getActivity(), getData(),
                R.layout.item_obs,
                new String[]{TAG_CLASS_NAME, TAG_DESCRIPTION,},
                new int[]{R.id.className, R.id.description,}));
        listView.setOnItemClickListener(this);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(CATEGORY_SAMPLES);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
        Log.i("JT","list size = "+list.size());

        if (list == null) {
            return data;
        }

        for (ResolveInfo info : list) {
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null
                    ? labelSeq.toString()
                    : info.activityInfo.name;

            String[] labelPath = label.split("/");

            String nextLabel = labelPath[0];

            if (labelPath.length == 1) {
                String nameLabel = info.activityInfo.name.replace(info.activityInfo.packageName + "", "");
                // Remove package and get simple class name
                if (nameLabel.contains(".")) {
                    nameLabel = nameLabel.replaceAll("[^.]*\\.", "");
                }
                addItem(data,
                        nameLabel,
                        nextLabel,
                        activityIntent(
                                info.activityInfo.applicationInfo.packageName,
                                info.activityInfo.name));
            }
        }

        Collections.sort(data, DISPLAY_NAME_COMPARATOR);

        return data;
    }

    protected Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    protected void addItem(List<Map<String, Object>> data, String className, String description,
                           Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put(TAG_CLASS_NAME, className);
        temp.put(TAG_DESCRIPTION, description);
        temp.put(TAG_INTENT, intent);
        data.add(temp);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);

        Intent intent = (Intent) map.get(TAG_INTENT);
        startActivity(intent);
    }
}
