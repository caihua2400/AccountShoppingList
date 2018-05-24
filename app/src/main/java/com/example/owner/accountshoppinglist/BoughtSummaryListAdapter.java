package com.example.owner.accountshoppinglist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Owner on 16/04/2018.
 */

public class BoughtSummaryListAdapter extends BaseAdapter implements Filterable {
    private int mLayoutResourceId;
    private SQLiteDatabase databaseHandler;

    private ArrayList<ShoppingItem> mFilterList;
    public ArrayList<ShoppingItem> boughtList;
    private Context context;
    ValueFilter valueFilter;

    public BoughtSummaryListAdapter(Context context,int resource,ArrayList<ShoppingItem> shoppingItemArrayList,SQLiteDatabase databaseHandler){
        this.context=context;
        this.mLayoutResourceId=resource;
        mFilterList=new ArrayList<ShoppingItem>(shoppingItemArrayList);
        boughtList=shoppingItemArrayList;
        this.databaseHandler=databaseHandler;



    }
    @Override
    public int getCount() {
        return boughtList.size();
    }

    @Override
    public Object getItem(int i) {
        return boughtList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(mLayoutResourceId,viewGroup,false);
        final ShoppingItem shoppingItem=(ShoppingItem) getItem(i);
        TextView bought_name_text= row.findViewById(R.id.text_bought_name_summary);
        bought_name_text.setText(shoppingItem.getName());
        TextView bought_price_text=row.findViewById(R.id.text_bought_price_summary);
        bought_price_text.setText(shoppingItem.getPrice()+"");
        TextView bought_quantity_text=row.findViewById(R.id.text_bought_quantity_summary);
        bought_quantity_text.setText(shoppingItem.getQuantity()+"");
        TextView bought_tag_text=row.findViewById(R.id.text_bought_tag_summary);
        bought_tag_text.setText(shoppingItem.getTag());
        ImageView bought_image=row.findViewById(R.id.image_summary_item);
        setPic(bought_image,shoppingItem.getPath());

        Button button_delete_bought=row.findViewById(R.id.button_delete_bought_summary);
        button_delete_bought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("delete bought item");
                builder.setNegativeButton("cancel delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("confirm delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseUtility.delete_bought(databaseHandler,shoppingItem);
                        remove(shoppingItem);
                        notifyDataSetChanged();

                        Toast.makeText(context,"delete successful",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                */
                DatabaseUtility.delete_bought(databaseHandler,shoppingItem);
                remove(shoppingItem);
                notifyDataSetChanged();

                Toast.makeText(context,"delete successful",Toast.LENGTH_SHORT).show();

            }
        });

        return row;
    }
    private void setPic(ImageView myImageView, String path)
    {
        // Get the dimensions of the View
        int targetW = myImageView.getWidth();
        int targetH = myImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        //   bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        myImageView.setImageBitmap(bitmap);



    }
    private void remove(ShoppingItem shoppingItem){
        boughtList.remove(shoppingItem);
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }
    private class ValueFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint !=null || constraint.length()>0){
                ArrayList<ShoppingItem> filterList=new ArrayList<ShoppingItem>();
                for(int i=0;i<mFilterList.size();i++){
                    if(mFilterList.get(i).getTag().contains(constraint.toString())){
                        ShoppingItem s=new ShoppingItem();
                        s.setName(mFilterList.get(i).getName());
                        s.setQuantity(mFilterList.get(i).getQuantity());
                        s.setPrice(mFilterList.get(i).getPrice());
                        s.setPath(mFilterList.get(i).getPath());
                        s.setTag(mFilterList.get(i).getTag());
                        filterList.add(s);
                    }
                }
                results.count=filterList.size();
                results.values=filterList;
            }else{
                results.count=mFilterList.size();
                results.values=mFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                boughtList=(ArrayList<ShoppingItem>) filterResults.values;
                notifyDataSetChanged();
        }
    }
}
