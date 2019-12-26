package com.minhtzy.moneytracker.view.budget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.view.budget.adapter.BudgetsAdapter;
import com.minhtzy.moneytracker.dataaccess.BudgetDAOImpl;
import com.minhtzy.moneytracker.entity.BudgetEntity;

import org.parceler.Parcels;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class BudgetFragment extends Fragment implements OnBudgetItemClickListener {

    private static final int REQUEST_ADD_BUDGET = 1;
    FloatingActionButton mAddBudget;
    RecyclerView mListBudget;
    BudgetsAdapter budgetsAdapter;
    List<BudgetEntity> mBudgets;


    public BudgetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BudgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetFragment newInstance() {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        mAddBudget = view.findViewById(R.id.fab_add_budget);
        mListBudget = view.findViewById(R.id.list_budget);
        addEvents();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_BUDGET) {
                BudgetEntity budget = (BudgetEntity) Parcels.unwrap(data.getParcelableExtra(AddBudgetActivity.EXTRA_BUDGET));
                mBudgets.add(budget);
                budgetsAdapter.notifyItemInserted(mBudgets.size());
            }
        }
    }

    private void addEvents() {
        mAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddBudget(v);
            }
        });

        mBudgets = new BudgetDAOImpl(this.getContext()).getAllBudget();
        budgetsAdapter = new BudgetsAdapter(mBudgets);
        budgetsAdapter.setListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mListBudget.setLayoutManager(mLayoutManager);
        mListBudget.setItemAnimator(new DefaultItemAnimator());
        mListBudget.setAdapter(budgetsAdapter);
        mListBudget.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    private void onClickAddBudget(View v) {
        Intent intent = new Intent(BudgetFragment.this.getContext(), AddBudgetActivity.class);
        startActivityForResult(intent, REQUEST_ADD_BUDGET);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position >= 0 && position < mBudgets.size()) {
            Intent intent = new Intent(this.getActivity(), DetailBudgetActivity.class);
            intent.putExtra(DetailBudgetActivity.EXTRA_BUDGET, Parcels.wrap(mBudgets.get(position)));
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

}