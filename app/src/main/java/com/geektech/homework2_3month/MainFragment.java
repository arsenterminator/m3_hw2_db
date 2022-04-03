package com.geektech.homework2_3month;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

   private RecyclerView rvNotes;
   private NotesAdapter adapter;
   private FloatingActionButton btnOpenAddNoteFragment;



    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rvNotes = view.findViewById(R.id.rv_notes);
        btnOpenAddNoteFragment = view.findViewById(R.id.btm_open_add_note);
        rvNotes.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new NotesAdapter(getActivity());
        rvNotes.setAdapter(adapter);


        btnOpenAddNoteFragment.setOnClickListener(view1 -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new AddNoteFragment());
            transaction.addToBackStack("AddNoteFragment");
            transaction.commit();
        });

        listenNoteDate();
        listenGetDate();
        return view;
    }

    private void listenNoteDate() {
        adapter.setNotesList(App.appDataBase.baseDao().getAll());
    }

    private void listenGetDate() {
        getActivity().getSupportFragmentManager().setFragmentResultListener("getNote", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals("getNote")){
                    String title = result.getString("title");
                    String description = result.getString("description");
                    String date = result.getString("date");
                    int position = result.getInt("position");
                    adapter.addGetNote(new NoteModel(title, description, date), position);
                    Toast.makeText(requireContext(), "Успешно изменено!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}