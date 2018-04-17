package com.example.owner.accountshoppinglist;

import android.Manifest;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {
    public static SQLiteDatabase db;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SHARE_IMAGE = 2;
    String mCurrentPhotoPath;
    String tag="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ShoppingItemDatabase dbConnection = new ShoppingItemDatabase(this);
        db = dbConnection.openDatabase();
        final EditText mName = findViewById(R.id.mName);
        final EditText mPrice = findViewById(R.id.mPrice);
        final EditText mQuantity = findViewById(R.id.mQuantity);
        Button button_create = findViewById(R.id.button_create);
        final TextView mTag=findViewById(R.id.text_tag);
        Spinner spinner = findViewById(R.id.spinner_tag);
        Button button_Take_photo=findViewById(R.id.button_take_photo);
        final String[] spinner_items = getResources().getStringArray(R.array.tag_list);
        ArrayAdapter<String> arrayAdapter_spinner = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_list_item_1, spinner_items);
        spinner.setAdapter(arrayAdapter_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    tag=spinner_items[i];
                    mTag.setText(tag);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button_Take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestToTakeAPicture();
            }
        });

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingItem p=new ShoppingItem();
                p.setName(mName.getText().toString());
                p.setTag(mTag.getText().toString());
                p.setPath(mCurrentPhotoPath);
                p.setPrice(Integer.parseInt(mPrice.getText().toString()) );
                p.setQuantity(Integer.parseInt(mQuantity.getText().toString()));
                DatabaseUtility.insert(db,p);
                Toast.makeText(AddItemActivity.this,"create success",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddItemActivity.this,MainActivity.class);
                startActivity(intent);
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