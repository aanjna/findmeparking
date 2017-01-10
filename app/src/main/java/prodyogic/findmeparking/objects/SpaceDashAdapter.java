package prodyogic.findmeparking.objects;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpaceDashAdapter extends ArrayAdapter {
    Context context;
    ArrayList<SpaceDash> list = new ArrayList();

    public SpaceDashAdapter(Context context, ArrayList<SpaceDash> parkings) {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1);
        this.context = context;
        this.list = parkings;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = super.getView(i, view, viewGroup);
        SpaceDash item = list.get(i);
        TextView text1 = (TextView) row.findViewById(android.R.id.text1);
        TextView text2 = (TextView) row.findViewById(android.R.id.text2);

        text1.setText(item.getName());
        text2.setText("Your Rating:" + item.getRating());
        return row;
    }
}
