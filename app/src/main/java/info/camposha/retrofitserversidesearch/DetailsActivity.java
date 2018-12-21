package info.camposha.retrofitserversidesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    TextView nameDetailTextView,propellantDetailTextView,dateDetailTextView,destinationDetailTextView;
    CheckBox techExistsDetailCheckBox;
    ImageView teacherDetailImageView;

    private void initializeWidgets(){
        nameDetailTextView= findViewById(R.id.nameDetailTextView);
        propellantDetailTextView= findViewById(R.id.propellantDetailTextView);
        dateDetailTextView= findViewById(R.id.dateDetailTextView);
        destinationDetailTextView=findViewById(R.id.destinationDetailTextView);
        techExistsDetailCheckBox= findViewById(R.id.techExistsDetailCheckBox);
        teacherDetailImageView=findViewById(R.id.teacherDetailImageView);
    }
    private String getDateToday(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String today= dateFormat.format(date);
        return today;
    }
    private void receiveAndShowData(){
        //RECEIVE DATA FROM ITEMS ACTIVITY VIA INTENT
        Intent i=this.getIntent();
        String name=i.getExtras().getString("NAME_KEY");
        String propellant=i.getExtras().getString("PROPELLANT_KEY");
        String destination=i.getExtras().getString("DESTINATION_KEY");
        String technologyExists=i.getExtras().getString("TECHNOLOGY_EXISTS_KEY");
        String imageURL=i.getExtras().getString("IMAGE_KEY");

        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        nameDetailTextView.setText(name);
        propellantDetailTextView.setText(propellant);
        dateDetailTextView.setText(getDateToday());
        destinationDetailTextView.setText(destination);
        techExistsDetailCheckBox.setChecked(technologyExists.equalsIgnoreCase("YES"));
        techExistsDetailCheckBox.setEnabled(false);
        Picasso.get().load(imageURL).placeholder(R.drawable.placeholder).into(teacherDetailImageView);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeWidgets();
        receiveAndShowData();
    }
}