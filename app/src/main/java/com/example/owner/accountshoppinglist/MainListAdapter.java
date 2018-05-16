package com.example.owner.accountshoppinglist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 16/04/2018.
 */

public class MainListAdapter extends BaseAdapter implements Filterable{
    private int mLayoutResourceId;
    private SQLiteDatabase databaseHandler;
    private MainListAdapter mainListAdapter;
    public ArrayList<ShoppingItem> mFilterList;
    public ArrayList<ShoppingItem> wishList;
    private Context context;
    ValueFilter valueFilter;

    public MainListAdapter(@NonNull Context context, int resource, @NonNull List<ShoppingItem> objects,SQLiteDatabase databaseHandler) {

        this.mLayoutResourceId=resource;
        this.databaseHandler=databaseHandler;
        mFilterList=new ArrayList<ShoppingItem>(objects);
        wishList=new ArrayList<ShoppingItem>(objects);
        this.context=context;


    }

    @Override
    public int getCount() {
        return wishList.size();
    }

    @Override
    public Object getItem(int i) {
        return wishList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

          LayoutInflater  inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


          View row= inflater.inflate(mLayoutResourceId, parent,false);
          if(position%2==0){
              //row.setBackgroundColor(Color.GRAY);
              LinearLayout linearLayout= row.findViewById(R.id.line1);
              linearLayout.setBackgroundColor(Color.GRAY);
          }
          final ShoppingItem s=(ShoppingItem) getItem(position);
          TextView text_name= row.findViewById(R.id.text_name_wishlist);
          text_name.setText(s.getName());
          TextView text_price=  row.findViewById(R.id.text_price_wishlist);
          text_price.setText(s.getPrice()+"$");
          TextView text_quantity=  row.findViewById(R.id.text_quantity_wishlist);
          text_quantity.setText(s.getQuantity()+"X");
          TextView text_tag_wishlist=  row.findViewById(R.id.text_tag_wishlist);
          text_tag_wishlist.setText(s.getTag());
          ImageView imageView_wishlist=row.findViewById(R.id.image_wishlist_item);

          setPic(imageView_wishlist,s.getPath());

          Button button_delete=row.findViewById(R.id.button_delete);

          button_delete.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("confirm delete");
                    builder.setNegativeButton("cancel delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.setPositiveButton("confirm delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseUtility.delete(databaseHandler,s);
                            remove(s);
                            notifyDataSetChanged();
                            Toast.makeText(context,"delete successful",Toast.LENGTH_SHORT).show();
                        }
                    });
                  AlertDialog dialog=builder.create();
                  dialog.show();
                    /*
                    DatabaseUtility.delete(databaseHandler,s);
                    remove(s);
                    notifyDataSetChanged();
                    Toast.makeText(context,"delete successful",Toast.LENGTH_SHORT).show();
                    */
              }
          });

          Button button_modify=row.findViewById(R.id.button_modify);
          button_modify.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent=new Intent(context,ModifyItemActivity.class);
                  intent.putExtra("shoppingItem",s);

                  context.startActivity(intent);
              }
          });

          Button button_purchase=row.findViewById(R.id.button_purchase);
          button_purchase.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  AlertDialog.Builder builder=new AlertDialog.Builder(context);
                  builder.setTitle("purchase item");
                  builder.setNegativeButton("cancel purchase", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.cancel();
                      }
                  });
                  builder.setPositiveButton("confirm purchase", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          DatabaseUtility.delete(databaseHandler,s);
                          DatabaseUtility.insert_bought(databaseHandler,s);
                          remove(s);
                          notifyDataSetChanged();
                          Toast.makeText(context,"purchase successful",Toast.LENGTH_SHORT).show();
                      }
                  });
                  AlertDialog dialog=builder.create();
                  dialog.show();
                  /*
                    DatabaseUtility.delete(databaseHandler,s);
                    DatabaseUtility.insert_bought(databaseHandler,s);
                    remove(s);
                    notifyDataSetChanged();
                    Toast.makeText(context,"purchase successful",Toast.LENGTH_SHORT).show();
                    */
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
        wishList.remove(shoppingItem);
    }
    @NonNull
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
                wishList=(ArrayList<ShoppingItem>) filterResults.values;
                notifyDataSetChanged();
        }
    }
}
