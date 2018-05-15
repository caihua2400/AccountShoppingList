package com.example.owner.accountshoppinglist;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {
    public static SQLiteDatabase db;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SHARE_IMAGE = 2;
    String mCurrentPhotoPath;
    String tag="";
    private static ArrayList<String> nameList;
    private static ArrayList<String> findNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ShoppingItemDatabase dbConnection = new ShoppingItemDatabase(this);
        db = dbConnection.openDatabase();
        nameList=DatabaseUtility.selectAllName(db);
        findNameList=new ArrayList<String>();
        final EditText mName = findViewById(R.id.mName);
        final EditText mPrice = findViewById(R.id.mPrice);
        final EditText mQuantity = findViewById(R.id.mQuantity);
        Button button_create = findViewById(R.id.button_create);
        final ListView name_list_view=findViewById(R.id.name_list);
        final SearchView name_search=findViewById(R.id.searchView_nameList);
        final Spinner spinner_tag=findViewById(R.id.spinner_tag);
        final ArrayAdapter<String> spinner_adapter=
                new ArrayAdapter<String>(AddItemActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.tag_list));
        spinner_tag.setAdapter(spinner_adapter);
        spinner_tag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 String[] spin_items=getResources().getStringArray(R.array.tag_list);
                 tag=spin_items[i];
                 Toast.makeText(AddItemActivity.this,"tagged successful",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final ArrayAdapter<String> name_adapter=new ArrayAdapter<String>(AddItemActivity.this,android.R.layout.simple_list_item_1,nameList);
        name_list_view.setAdapter(name_adapter);

        Button button_Take_photo=findViewById(R.id.button_take_photo);




        button_Take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestToTakeAPicture();
            }
        });

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(AddItemActivity.this);
                builder.setTitle("Add new Item");
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ShoppingItem p=new ShoppingItem();
                        p.setName(mName.getText().toString());
                        p.setTag(tag);
                        p.setPath(mCurrentPhotoPath);
                        p.setPrice(Integer.parseInt(mPrice.getText().toString()) );
                        p.setQuantity(Integer.parseInt(mQuantity.getText().toString()));
                        DatabaseUtility.insert(db,p);
                        Toast.makeText(AddItemActivity.this,"create success",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AddItemActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                /*
                ShoppingItem p=new ShoppingItem();
                p.setName(mName.getText().toString());
                p.setTag(tag);
                p.setPath(mCurrentPhotoPath);
                p.setPrice(Integer.parseInt(mPrice.getText().toString()) );
                p.setQuantity(Integer.parseInt(mQuantity.getText().toString()));
                DatabaseUtility.insert(db,p);
                Toast.makeText(AddItemActivity.this,"create success",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddItemActivity.this,MainActivity.class);
                startActivity(intent);
                */
            }
        });

        name_search.setSubmitButtonEnabled(true);
        name_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(TextUtils.isEmpty(s)){
                    Toast.makeText(AddItemActivity.this,"please input name to filter",Toast.LENGTH_SHORT).show();
                    name_list_view.setAdapter(name_adapter);
                }else{
                    findNameList.clear();
                    for(int i=0;i<nameList.size();i++){
                        String oldName=nameList.get(i);
                        if(oldName.toUpperCase().equals(s)){
                            findNameList.add(s);
                            break;
                        }
                    }
                    if(findNameList.size()==0){
                        Toast.makeText(AddItemActivity.this,"the item you search does not exist",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AddItemActivity.this,"search successful",Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> findNameAdapter=new ArrayAdapter<String>(AddItemActivity.this,android.R.layout.simple_list_item_1,findNameList);
                        name_list_view.setAdapter(findNameAdapter);
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)){
                    name_list_view.setAdapter(name_adapter);
                }else{
                    findNameList.clear();
                    for(int i=0;i<nameList.size();i++){
                        String oldName=nameList.get(i);
                        if(oldName.toUpperCase().contains(s.toUpperCase())){
                            findNameList.add(oldName);
                        }
                    }
                    ArrayAdapter<String> findNameAdapter=new ArrayAdapter<String>(AddItemActivity.this,android.R.layout.simple_list_item_1,findNameList);
                    name_list_view.setAdapter(findNameAdapter);
                }
                return false;
            }
        });

        name_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(findNameList.size()>0){
                    String name= findNameList.get(i);
                    mName.setText(name);
                }else{
                    String name=nameList.get(i);
                    mName.setText(name);
                }

            }
        });


    }
    private void requestToTakeAPicture()
    {
        ActivityCompat.requestPermissions(AddItemActivity.this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_IMAGE_CAPTURE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay!
                    takeAPicture();
                } else {
                    // permission denied, boo!
                }
                return;


        }
    }
    private void takeAPicture()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //takePictureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);

        // Ensure that theres a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            // Create the File where the photo should go
            try
            {
                File photoFile = createImageFile();
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.owner.accountshoppinglist", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            catch (IOException ex)
            {
                // Error occurred while creating the File
            }
        }
    }
    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            //to get just the thumbnail:
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView myImageView = findViewById(R.id.myImageView);
            setPic(myImageView, mCurrentPhotoPath);
        }
    }
    private void setPic(ImageView myImageView, String path)
    {
        // Get the dimensions of the View
        int targetW = myImageView.getWidth();
        int targetH = myImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        myImageView.setImageBitmap(bitmap);



    }

}