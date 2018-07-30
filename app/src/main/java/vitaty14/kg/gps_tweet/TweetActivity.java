package vitaty14.kg.gps_tweet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class TweetActivity extends AppCompatActivity {

    private String msg_Lat;
    private String msg_Lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_tweet);

        Intent intent = getIntent();
        msg_Lat = intent.getStringExtra("data1");
        msg_Lon = intent.getStringExtra("data2");

        EditText editText = (EditText)findViewById(R.id.editText);
        editText.setText(msg_Lat +","+ msg_Lon);
    }
}
