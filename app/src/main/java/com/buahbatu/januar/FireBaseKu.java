package com.buahbatu.januar;

import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseKu {

    DatabaseReference databaseLamps;

    FireBaseKu()
    {
        databaseLamps = FirebaseDatabase.getInstance().getReference("dataLamp");
    }

    public void saveData(LampModel data){
        String id = databaseLamps.push().getKey();
        databaseLamps.child(id).setValue(data);

    }


}
