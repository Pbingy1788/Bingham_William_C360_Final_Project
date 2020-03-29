package com.cs360.williambingham.bingham_william_c360_final_project;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.cs360.williambingham.bingham_william_c360_final_project.locator.LocatrActivity;

//LinkedIn  Import Stuff
import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class DatabaseActivity extends AppCompatActivity
{
    TextView idView;
    EditText nameBox;
    EditText featureBox;
    EditText cityBox;
    EditText rateBox;
    ImageView imageView;
    Button btnTakePic;
    String pathToFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idView = (TextView) findViewById(R.id.parkID);
        nameBox = (EditText) findViewById(R.id.parkName);
        featureBox = (EditText) findViewById(R.id.parkFeature);
        cityBox = (EditText) findViewById(R.id.parkCity);
        rateBox = (EditText) findViewById(R.id.rateSite);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.camp_default);
        btnTakePic = findViewById(R.id.btnTakePic);

        //Logic to allow button for camera to be used
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchPictureTakeAction();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void dispatchPictureTakeAction()
    {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(DatabaseActivity.this, "com.cs360.williambingham.bingham_william_c360_final_project", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic, 1);
            }
        }
    }

    private File createPhotoFile()
    {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try
        {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("mylog", "Excep : " + e.toString());
        }
        return image;
    }

    // Moves to the AboutUs Screen Logic
    public void moveToAboutUs(View view)
    {
        Intent intent = new Intent(DatabaseActivity.this, aboutUs.class);
        startActivity(intent);
    }

    // Moves to the ContactUs Screen Logic
    public void moveToContactUs(View view)
    {
        Intent intent = new Intent(DatabaseActivity.this, ContactUs.class);
        startActivity(intent);
    }

    // Moves to the LinkedIn Screen Logic
    public void moveToLinkedIn(View view)
    {
        Intent intent = new Intent(DatabaseActivity.this, LinkedInn.class);
        startActivity(intent);
    }

    // Moves to the add park Screen Logic
    public void addPark(View view)
    {
        ParkDBHandler dbHandler = new ParkDBHandler(this, null, null, 1);
        int rate = Integer.parseInt(rateBox.getText().toString());
        Parks parks = new Parks(rate, nameBox.getText().toString(), featureBox.getText().toString(), cityBox.getText().toString());
        dbHandler.addPark(parks);
        nameBox.setText("");
        featureBox.setText("");
        cityBox.setText("");
        rateBox.setText("");
    }

    // Moves to the Locate park Screen Logic
    public void locateCampsite(View view)
    {
        Intent intent = new Intent(DatabaseActivity.this, LocatrActivity.class);
        startActivity(intent);
    }

    // Search park Screen
    public void searchPark(View view)
    {
        ParkDBHandler dbHandler = new ParkDBHandler(this, null, null, 1);
        Parks parks = dbHandler.searchPark(nameBox.getText().toString(), featureBox.getText().toString(), cityBox.getText().toString());

        if (parks != null)
        {
            idView.setText(String.valueOf(parks.getID()));
            nameBox.setText(String.valueOf(parks.getName()));
            featureBox.setText(String.valueOf(parks.getFeature()));
            cityBox.setText(String.valueOf(parks.getCity()));
            rateBox.setText(String.valueOf(parks.getRate()));

            // check for these local campsites in database
            if (parks.getName().equals("Glacier Park")) {
                imageView.setImageResource(R.drawable.glacier);
            } else if (parks.getName().equals("Gyoen Park")) {
                imageView.setImageResource(R.drawable.gyoen);
            } else if (parks.getName().equals("Meiji Park")) {
                imageView.setImageResource(R.drawable.meiji);
            } else if (parks.getName().equals("Olympic Park")) {
                imageView.setImageResource(R.drawable.olympic);
            } else if (parks.getName().equals("Redwood Park")) {
                imageView.setImageResource(R.drawable.redwood);
            } else if (parks.getName().equals("Shiba Park")) {
                imageView.setImageResource(R.drawable.shiba);
            } else if (parks.getName().equals("Sumida Park")) {
                imageView.setImageResource(R.drawable.sumida);
            } else if (parks.getName().equals("Yellowstone Park")) {
                imageView.setImageResource(R.drawable.yellowstone);
            } else if (parks.getName().equals("Yosemite Park")) {
                imageView.setImageResource(R.drawable.yosemite);
            } else if (parks.getName().equals("Yoyogi Park")) {
                imageView.setImageResource(R.drawable.yoyogi);
            } else {
                imageView.setImageResource(R.drawable.camp_default);
            }
        } else {
            idView.setText("Park not found.");
        }
    }

    // Update park logic
    public void updatePark(View view)
    {
        if (!(idView.getText().equals("Park Deleted") || idView.getText().length() == 0)) {
            ParkDBHandler dbHandler = new ParkDBHandler(this, null, null, 1);
            dbHandler.updatePark(Integer.parseInt(idView.getText().toString()), Integer.parseInt(rateBox.getText().toString()));
        }
    }

    // Delete park Logic
    public void deletePark(View view)
    {
        ParkDBHandler dbHandler = new ParkDBHandler(this, null, null, 1);
        boolean result = dbHandler.deletePark(nameBox.getText().toString());
        if (result) {
            idView.setText("Park Deleted");
            nameBox.setText("");
            featureBox.setText("");
            cityBox.setText("");
            rateBox.setText("");
        } else
            idView.setText("Park not found.");
    }
}