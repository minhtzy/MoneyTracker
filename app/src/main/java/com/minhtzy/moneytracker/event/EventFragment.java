package com.minhtzy.moneytracker.event;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.EventDAOImpl;
import com.minhtzy.moneytracker.dataaccess.IEventDAO;
import com.minhtzy.moneytracker.entity.EventEntity;

import java.util.List;

public class EventFragment extends Fragment implements OnEventItemInteractionListener {

    OnEventItemInteractionListener mListener;

    List<EventEntity> mListEvents;

    public EventFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventFragment newInstance(int columnCount) {
        EventFragment fragment = new EventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IEventDAO iEventDAO = new EventDAOImpl(getContext());
        mListEvents = iEventDAO.getAllAvaialbleEvent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyEventRecyclerViewAdapter(mListEvents, mListener));
        }
        return view;
    }

    @Override
    public void onEventItemClicked(EventEntity entity) {

    }
}
