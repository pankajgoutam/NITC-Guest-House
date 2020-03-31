package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminViewBooking extends AppCompatActivity {


    private DatabaseReference ref, reff;
    private ListView listView;

    private TextView T9, T10, T11, T12, T13, T14, T15, T16;
    private Button B1, B2;
    int check = 0;
    String child;
    GuestHouse G1 = new GuestHouse();
    int i = 0;
    int stop = 0;
    String[] arr1, arr2;
    int day1, day2, year1, year2, month1, month2;
    ArrayList<GuestHouse> List = new ArrayList<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_booking);


        T9 = findViewById(R.id.T9);
        T10 = findViewById(R.id.T10);
        T11 = findViewById(R.id.T11);
        T12 = findViewById(R.id.T12);
        T13 = findViewById(R.id.T13);
        T14 = findViewById(R.id.T14);
        T15 = findViewById(R.id.T15);
        T16 = findViewById(R.id.T16);
        B1 = findViewById(R.id.B3);
        B2 = findViewById(R.id.B2);


        final String[] BookingDetails;
        BookingDetails = getIntent().getStringArrayExtra("Booking");


        T9.setText(BookingDetails[0]);
        T10.setText(BookingDetails[1]);
        T11.setText(BookingDetails[2]);
        T12.setText(BookingDetails[3]);
        T13.setText(BookingDetails[4]);
        T14.setText(BookingDetails[5]);
        T15.setText(BookingDetails[6]);
        T16.setText(BookingDetails[7]);


        final Booking booking = new Booking();
        booking.setBookingId(Integer.parseInt(BookingDetails[0]));
        booking.setUserId(BookingDetails[1]);
        booking.setBookingDate(BookingDetails[3]);
        booking.setCheckInDate(BookingDetails[4]);
        booking.setCheckOutDate(BookingDetails[5]);


        if (BookingDetails[6].equals("True"))
            booking.setFoodServices(true);
        else
            booking.setFoodServices(false);

        if(BookingDetails[7].equals("Pending"))
            B2.setText("REJECT");
        else if(BookingDetails[7].equals("Confirmed"))
            B2.setText("CANCEL");


        if (BookingDetails[7].equals("Completed") || BookingDetails[7].equals("CheckedIn" ) || BookingDetails[7].equals("Rejected") ) {
            B1.setVisibility(View.GONE);
            B2.setVisibility(View.GONE);
        }
        else {

            B1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewBooking.this);
                    builder.setTitle("Are You Sure ?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String checkInDate = BookingDetails[3].split(" ")[0];
                            String checkOutDate = BookingDetails[4].split(" ")[0];


                            arr1 = checkInDate.split("-");
                            arr2 = checkOutDate.split("-");
                            day1 = Integer.parseInt(arr1[0]);
                            day2 = Integer.parseInt(arr2[0]);

                            final int rooms = Integer.parseInt(BookingDetails[5]);


                            ref = FirebaseDatabase.getInstance().getReference("GuestHouse");

                            if(Integer.parseInt(arr1[1]) - Integer.parseInt(arr2[1]) == 0)
                            {
                                    i = day1;
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            child = Integer.toString(i) + "-" + arr1[1] + "-" + arr1[2];


                                            if(dataSnapshot.hasChild(child) && check == 0)
                                            {

                                                G1 = dataSnapshot.child(child).getValue(GuestHouse.class);
                                                int vacant = G1.getVacantRooms();
                                                int occupied = G1.getOccupiedRooms();
                                                G1.setOccupiedRooms(occupied + Integer.parseInt(BookingDetails[5]));
                                                G1.setVacantRooms(vacant - Integer.parseInt(BookingDetails[5]));
                                                ref.child(child).setValue(G1);
                                                if(i == day2 - 1)
                                                    check = 1;
                                                i = i + 1;
                                               // Toast.makeText(AdminViewBooking.this, "Normal First : " , Toast.LENGTH_SHORT).show();

                                            }
                                            else if(check == 0)
                                            {
                                                G1.setVacantRooms(G1.getTotalRooms() - rooms);
                                                G1.setOccupiedRooms(rooms);
                                                G1.setGuestHouseId(1);
                                                ref.child(child).setValue(G1);
                                                if(i == day2 - 1)
                                                    check = 1;
                                                i = i + 1;
                                                //Toast.makeText(AdminViewBooking.this, "Normal Second: " + child, Toast.LENGTH_SHORT).show();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                            }
                            else
                            {
                                i = day1;
                                do{
                                    check = 0;
                                    month1 = Integer.parseInt(arr1[1]);
                                    month2 = Integer.parseInt(arr1[1]);
                                    year1 =  Integer.parseInt(arr1[2]);
                                    year2 = Integer.parseInt(arr2[2]);
                                    child = Integer.toString(i) + "-" + arr1[1] + "-" + arr1[2];
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(child) && check == 0)
                                            {
                                                G1 = dataSnapshot.child(child).getValue(GuestHouse.class);
                                                int vacant = G1.getVacantRooms();
                                                int occupied = G1.getOccupiedRooms();
                                                G1.setOccupiedRooms(occupied + Integer.parseInt(BookingDetails[5]));
                                                G1.setVacantRooms(vacant - Integer.parseInt(BookingDetails[5]));
                                                ref.child(child).setValue(G1);
                                                check = 1;
                                                Toast.makeText(AdminViewBooking.this, "Normal First : " , Toast.LENGTH_SHORT).show();

                                            }
                                            else if(check == 0)
                                            {
                                                G1.setVacantRooms(G1.getTotalRooms() - rooms);
                                                G1.setOccupiedRooms(rooms);
                                                G1.setGuestHouseId(1);
                                                ref.child(child).setValue(G1);
                                                check = 1;
                                                Toast.makeText(AdminViewBooking.this, "Normal Second", Toast.LENGTH_SHORT).show();

                                            }

                                            i = i + 1;
                                            if(i == day2)
                                                stop = 1;
                                            if(i + 1 == 32) {
                                                i = 0;
                                                arr1[1] = arr2[1];
                                            }
                                            if (i + 1 == 31 && !((month1 == 1) || (month1 == 3) || (month1 == 5) || (month1 == 7) || (month1 == 8) || (month1 == 10) || (month1 == 12)))
                                                i = 0;
                                            if(year1 != year2 && i == 31) {
                                                arr1[2] = arr2[2];
                                                arr1[1] = arr2[1];
                                                i = 0;
                                            }
                                            if(year1 % 4 != 0 && month1 == 2 && i == 28) {
                                                arr1[1] = arr2[1];
                                                i = 0;
                                            }
                                            if(year1 % 4 == 0 && i == 29 && month1 == 2) {
                                                arr1[1] = arr2[1];
                                                i = 0;
                                            }

                                            i = i + 1;
                                            if(i == day2)
                                                stop = 1;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } while (stop == 0);

                            }
                            changeBookingStatus(BookingDetails[0]);

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });

            B2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewBooking.this);
                    builder.setTitle("Are You Sure ?");

                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if(BookingDetails[7].equals("Pending"))
                                booking.setBookingStatus("Rejected");
                            else
                                booking.setBookingStatus("Cancelled");

                            ref = FirebaseDatabase.getInstance().getReference("Booking");
                            ref.child(BookingDetails[0]).setValue(booking);
                            if(BookingDetails[7].equals("Pending"))
                                Toast.makeText(AdminViewBooking.this, "Booking Rejected", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(AdminViewBooking.this, "Booking Cancelled", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AdminViewBooking.this, ManageBookings.class);
                            startActivity(intent);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });



        }

    }

   /* void checkBookingHistory(final String str)
    {


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(AdminViewBooking.this, "First This..", Toast.LENGTH_SHORT).show();



            }
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Don't ignore errors!
            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }*/

    void changeBookingStatus(final String bookId)
    {
        reff = FirebaseDatabase.getInstance().getReference("Booking");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Booking booking = dataSnapshot.child(bookId).getValue(Booking.class);
                booking.setBookingStatus("Confirmed");
                reff.child(bookId).setValue(booking);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(AdminViewBooking.this, "Booking Confirmed..", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AdminViewBooking.this, ManageBookings.class);
        startActivity(intent);
    }
}