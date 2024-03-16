package com.arr.bancamovil.ui.settings;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.core.text.HtmlCompat;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.arr.bancamovil.R;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.arr.bancamovil.adapter.TipsAdapter;
import com.arr.bancamovil.databinding.FragmentHelpBinding;
import com.arr.bancamovil.model.Tips;
import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        FragmentHelpBinding binding = FragmentHelpBinding.inflate(inflater, container, false);
        addOptionMenu(); // back press

        List<Tips> list = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        TipsAdapter adapter = new TipsAdapter(list);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);

        list.add(
                new Tips(
                        getString(R.string.title_help1), getString(R.string.message_help1), true));
        list.add(
                new Tips(
                        getString(R.string.title_help2), getString(R.string.message_help2), false));
        list.add(
                new Tips(
                        getString(R.string.title_help3), getString(R.string.message_help3), false));
        list.add(
                new Tips(
                        getString(R.string.title_help4), getString(R.string.message_help4), false));
        list.add(
                new Tips(
                        getString(R.string.title_help5), getString(R.string.message_help5), false));

        return binding.getRoot();
    }

    private void addOptionMenu() {
        requireActivity()
                .addMenuProvider(
                        new MenuProvider() {
                            @Override
                            public void onCreateMenu(
                                    @NonNull Menu arg0, @NonNull MenuInflater arg1) {}

                            @Override
                            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                                int id = menuItem.getItemId();
                                if (id == android.R.id.home) {
                                    NavController nav = Navigation.findNavController(requireView());
                                    nav.navigateUp();
                                }
                                return true;
                            }
                        },
                        getViewLifecycleOwner(),
                        Lifecycle.State.RESUMED);
    }
}
