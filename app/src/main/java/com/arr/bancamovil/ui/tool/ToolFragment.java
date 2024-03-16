package com.arr.bancamovil.ui.tool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.arr.bancamovil.adapter.ToolsAdapter;
import com.arr.bancamovil.databinding.FragmentToolBinding;
import com.arr.bancamovil.model.Tools;
import java.util.ArrayList;
import java.util.List;
import com.arr.bancamovil.R;

public class ToolFragment extends Fragment {

    private FragmentToolBinding binding;
    private ToolsAdapter adapter;
    private List<Tools> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arg2) {
        binding = FragmentToolBinding.inflate(inflater, container, false);

        adapter = new ToolsAdapter(list);
        binding.recycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recycler.setAdapter(adapter);
        list.add(new Tools(R.drawable.ic_menu_24px, "Calcular"));
        list.add(new Tools(R.drawable.ic_ticket_24px, "Compras"));

        return binding.getRoot();
    }
}
