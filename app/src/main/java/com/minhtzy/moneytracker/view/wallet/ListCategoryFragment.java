package com.minhtzy.moneytracker.view.wallet;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.view.category.adapter.CategoryExpandableListAdapter;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.model.CategoryExpandableGroup;

import java.util.List;

public class ListCategoryFragment extends Fragment {

    private OnCategoryFragmentListener mListener;
    private ExpandableListAdapter mAdapter;
    private ExpandableListView mExpandableListView;

    private List<CategoryExpandableGroup> mCategories;

    public ListCategoryFragment() {

    }

    public static ListCategoryFragment newInstance(List<CategoryExpandableGroup> categories) {
        ListCategoryFragment fragment = new ListCategoryFragment();
        fragment.mCategories = categories;
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);
        mExpandableListView = view.findViewById(R.id.lvListCategory);
        addControls();
        return view;
    }

    private void addControls() {
        mAdapter = new CategoryExpandableListAdapter(this.getContext(),mCategories);
        mExpandableListView.setAdapter(mAdapter);
        //mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(parent.isGroupExpanded(groupPosition)) {
                    onItemPressed(groupPosition,-1);
                }
                else {
                    parent.expandGroup(groupPosition,true);

                }
                return true;
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                onItemPressed(groupPosition,childPosition);
                return false;
            }
        });
        expandAll();

    }

    private void expandAll() {
        for (int i = 0; i < mAdapter.getGroupCount();++i) {
            mExpandableListView.expandGroup(i);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onItemPressed(int groupPosition,int childPosition) {
        if (mListener != null) {
            CategoryEntity category;
            if(childPosition == -1) {
                category = mCategories.get(groupPosition).getCategory();
            }
            else {
                category = mCategories.get(groupPosition).getSubCategories().get(childPosition);
            }
            mListener.onItemClicked(category);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCategoryFragmentListener) {
            mListener = (OnCategoryFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCategoryFragmentListener {
        // TODO: Update argument type and name
        void onItemClicked(CategoryEntity category);
    }
}
