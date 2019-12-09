package com.minhtzy.moneytracker.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.EventDAOImpl;
import com.minhtzy.moneytracker.dataaccess.IEventDAO;
import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.event.adapter.EventRecyclerViewAdapter;
import com.minhtzy.moneytracker.model.EventStatus;

import org.parceler.Parcels;

import java.util.List;

public class EventListFragment extends Fragment implements OnEventItemInteractionListener {

    List<EventEntity> mListEvents;

    EventStatus eventStatus;

    public static final String BUNDLE_EVENT_STATUS =  "EventListFragment.status";
    public EventListFragment() {
    }

    public static EventListFragment newInstance(EventStatus eventStatus) {
        EventListFragment fragment = new EventListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_EVENT_STATUS,eventStatus);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            eventStatus = (EventStatus) getArguments().getSerializable(BUNDLE_EVENT_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IEventDAO iEventDAO = new EventDAOImpl(getContext());
        mListEvents = iEventDAO.getAllAvailableEvent(eventStatus);
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new EventRecyclerViewAdapter(mListEvents, this));
            recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        }
        return view;
    }

    @Override
    public void onEventItemClicked(EventEntity entity) {
        Intent intent = new Intent(getContext(),EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EXTRA_EVENT,Parcels.wrap(entity));
        startActivity(intent);
    }
}
