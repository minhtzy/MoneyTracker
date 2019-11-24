package com.minhtzy.moneytracker.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.EventDAOImpl;
import com.minhtzy.moneytracker.dataaccess.IEventDAO;
import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.model.EventStatus;

import org.parceler.Parcels;

import java.util.List;

public class EventListFragment extends Fragment implements OnEventItemInteractionListener {

    private static final String EXTRA_EVENT_ENTITY = "EventEntity.value" ;
    OnEventItemInteractionListener mListener;

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
        IEventDAO iEventDAO = new EventDAOImpl(getContext());
        mListEvents = iEventDAO.getAllAvailableEvent(eventStatus);
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
            recyclerView.setAdapter(new EventRecyclerViewAdapter(mListEvents, mListener));
        }
        return view;
    }

    @Override
    public void onEventItemClicked(EventEntity entity) {
        Intent intent = new Intent(getContext(),EventDetailActivity.class);
        intent.putExtra(EXTRA_EVENT_ENTITY,Parcels.wrap(entity));
        startActivity(intent);
    }
}
