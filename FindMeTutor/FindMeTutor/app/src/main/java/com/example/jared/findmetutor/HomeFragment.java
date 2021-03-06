package com.example.jared.findmetutor;

/**
 * Created by Jadon on 09-Aug-16.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements AsyncResponse{

    SharedPreferences myprefs;

    getSessions connect2server;
    List<Session> list = new ArrayList<>();

    RecyclerView rv;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    //Fragments work differently than activitues
    //you put your code in the onCreate View instead of the inCreate as with Activities
    //and refrence the rootView object
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        myprefs= getContext().getSharedPreferences("user", MODE_PRIVATE);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //get user info from sharedSettings

                String balance= myprefs.getString("student_current_balance", null);

                if(Integer.parseInt(balance)<=5)
                {
                    Toast.makeText(getContext(), "Insufficient Funds", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent requestTutor = new Intent(getActivity(),RequestActivity.class); //this is how to start an activity from a fragment
                    startActivity(requestTutor);
                }

            }
        });


        //Handle the card and recycle view

        rv = (RecyclerView) rootView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        String id = myprefs.getString("student_id",null);
        Toast.makeText(getContext(), "On post " + id , Toast.LENGTH_SHORT).show();

        connect2server = new getSessions(this.getActivity(), id, list);

        connect2server.delegate = this;
        connect2server.execute();




        //Session event = new Session("Complex Analysis","Flower Hall","1 October","8am", R.drawable.session);
        //event.initializeData();





        // Inflate the layout for this fragment
        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void processFinish(String output) {
        String temp = output.substring(0,2);


        if(temp.equals("[]"))
        {
            Toast.makeText(getContext(), "No subjects Ass some", Toast.LENGTH_SHORT).show();
        }
        else{
            list = connect2server.getList();

           // Toast.makeText(getContext(), "On post LIST : "+list.get(1).SubjectName, Toast.LENGTH_SHORT).show();


            CardViewAdapter adapter = new CardViewAdapter(list, this.getContext());
            rv.setAdapter(adapter);
        }



    }

    @Override
    public void processFinish2(String out) {

    }
}
