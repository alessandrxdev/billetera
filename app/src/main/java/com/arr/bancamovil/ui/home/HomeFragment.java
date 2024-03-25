package com.arr.bancamovil.ui.home;

import android.content.Intent;
import androidx.fragment.app.DialogFragment;
import static com.arr.bancamovil.R.array.entries_value_default;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.text.HtmlCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.arr.bancamovil.MainActivity;
import com.arr.bancamovil.R;
import com.arr.bancamovil.Test;
import com.arr.bancamovil.adapter.DataCardAdapter;
import com.arr.bancamovil.adapter.TasasAdapter;
import com.arr.bancamovil.databinding.FragmentHomeBinding;
import com.arr.bancamovil.model.DataCards;
import com.arr.bancamovil.model.Header;
import com.arr.bancamovil.model.Items;
import com.arr.bancamovil.model.Tasas;
import com.arr.bancamovil.utils.asynck.ExecuteTask;
import com.arr.bancamovil.utils.card.DataUtils;
import com.arr.bancamovil.utils.dialog.ConvertDialog;
import com.arr.bancamovil.utils.tasas.TasasUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class HomeFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentHomeBinding binding;
    private SharedPreferences sp;

    // data cards
    private List<DataCards> listCards;
    private DataUtils data;
    private DataCardAdapter cardAdapter;

    // tasas
    private final List<Items> listTasas = new ArrayList<>();
    private TasasUtils tasas;
    private TasasAdapter tasasAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arg2) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        addOptionMenu(); // --> Options Menu

        sp = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        sp.registerOnSharedPreferenceChangeListener(this);

        // logica para mostrar la lista de tarjetas
        data = new DataUtils(requireContext());
        listCards = data.loadData();
        cardAdapter =
                new DataCardAdapter(
                        requireContext(), listCards, this::toInfoCard, this::copyNumber);
        binding.viewpagerMain.setAdapter(cardAdapter);
        binding.viewpagerMain.setOffscreenPageLimit(3);
        binding.viewpagerMain.setClipToPadding(false);
        binding.viewpagerMain.setClipChildren(false);
        binding.viewpagerMain.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        compositePageTransformer(binding.viewpagerMain);

        // mostrar u ocultar el dots
        if (data.loadData().isEmpty()) {
            binding.dotsViewPager.setVisibility(View.GONE);
        } else {
            binding.dotsViewPager.setVisibility(View.VISIBLE);
            binding.dotsViewPager.attachTo(binding.viewpagerMain);
        }

        // about tasas
        binding.aboutTasaa.setOnClickListener(this::aboutTasas);

        // logica para mostrar laa tasas de cambio
        tasas = new TasasUtils(requireContext());
        tasasAdapter = new TasasAdapter(requireContext(), listTasas, position -> onClick(position));

        binding.recycler.setHasFixedSize(true);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setAdapter(tasasAdapter);
        binding.recycler.setNestedScrollingEnabled(false);

        // comprobar si las tasas de cambio se han actualizado en las últimas 6 horas
        if (isUpdateTasas()) {
            new ExecuteTask<Void, Void, JSONObject>(requireActivity()) {
                @Override
                protected JSONObject doInBackground(Void... params) {
                    try {
                        return tasas.obtainsTasas();
                    } catch (Exception err) {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(JSONObject result) {
                    if (result != null) {
                        tasas.saveTasas(result);
                    }
                    loadTasasToFile();
                }
            }.execute();

            saveUpdate();
        } else {
            listTasas.clear();
            loadTasasToFile();
        }

        // botones para acceder a Calcular y Gastos
        binding.buttons.calculate.setOnClickListener(this::onClickCalculate);
        binding.buttons.compras.setOnClickListener(this::onClickMarket);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // comprobar si se han actualizado las tasas de cambio
    private boolean isUpdateTasas() {
        SharedPreferences preferences =
                requireContext().getSharedPreferences("Update", Context.MODE_PRIVATE);
        long lastUpdateTime = preferences.getLong("lastUpdateTime", 0);
        Calendar now = Calendar.getInstance();
        long currentTime = now.getTimeInMillis();
        return currentTime - lastUpdateTime >= 6 * 60 * 60 * 1000;
    }

    // guardar la ultima actualizacion en un preference
    private void saveUpdate() {
        SharedPreferences preferences =
                requireContext().getSharedPreferences("Update", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("lastUpdateTime", Calendar.getInstance().getTimeInMillis());
        editor.apply();
    }

    // main options menu
    private void addOptionMenu() {
        requireActivity()
                .addMenuProvider(
                        new MenuProvider() {
                            @Override
                            public void onCreateMenu(
                                    @NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                                menuInflater.inflate(R.menu.main_menu, menu);
                                searchCard(menu); // search

                                MenuItem saveMenuItem = menu.findItem(R.id.menu_add);
                                View actionView = saveMenuItem.getActionView();

                                // add cards
                                ImageView add = actionView.findViewById(R.id.add);
                                add.setOnClickListener(
                                        view ->
                                                navigateTo()
                                                        .navigate(
                                                                R.id.navigation_add_card,
                                                                null,
                                                                options()));
                            }

                            @Override
                            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                                return true;
                            }
                        },
                        getViewLifecycleOwner(),
                        Lifecycle.State.RESUMED);
    }

    // navigate to fragment
    private NavController navigateTo() {
        return Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment_activity_main);
    }

    private NavOptions options() {
        return new NavOptions.Builder().setLaunchSingleTop(true).build();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        assert key != null;
        if (key.equals("monedas")) {
            Set<String> select = sharedPreferences.getStringSet("monedas", new HashSet<>());
            Log.e("PREFFF ", " " + select);
        }
    }

    // copiar el numero de la tarjeta
    private void copyNumber(int position) {
        String numberCard = data.loadInfo(position, "card");
        String number = groupNumber(numberCard);
        ClipData.Item item = new ClipData.Item(number);
        String[] mimeType = new String[1];
        mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;
        ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), item);
        ClipboardManager cm =
                (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        PersistableBundle extras = new PersistableBundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras.putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true);
            }
        } else {
            extras.putBoolean("android.content.extra.IS_SENSITIVE", true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cd.getDescription().setExtras(extras);
        }
        cm.setPrimaryClip(cd);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S_V2) {
            Toast.makeText(requireContext(), "Se copió al portapapeles.", Toast.LENGTH_LONG).show();
        }
    }

    private String groupNumber(String number) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < number.length(); i += 4) {
            if (i + 4 < number.length()) {
                resultado.append(number, i, i + 4).append(" ");
            } else {
                resultado.append(number, i, number.length());
            }
        }
        return resultado.toString();
    }

    // cargar la informacion de la tarjeta seleccionada
    private void toInfoCard(int position) {
        String numero = data.loadInfo(position, "card");
        String moneda = data.loadInfo(position, "currency");
        String titular = data.loadInfo(position, "user");
        String phone = data.loadInfo(position, "phone");
        String expire = data.loadInfo(position, "expire");

        Bundle bundle = new Bundle();
        bundle.putString("tarjeta", numero);
        bundle.putString("moneda", moneda);
        bundle.putString("user", titular);
        bundle.putString("phone", phone);
        bundle.putInt("id", position);
        bundle.putString("expire", expire);
        navigateTo().navigate(R.id.navigation_info, bundle);
    }

    // vista de animación transicion de cada tarjeta
    private void compositePageTransformer(ViewPager2 viewpager) {
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(
                (page, position) -> {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.5f + r * 0.5f);
                });
        viewpager.setPageTransformer(compositePageTransformer);
    }

    // mostrar info de las tasas
    private void aboutTasas(View view) {
        String message = getString(R.string.message_tasas);
        String styledMessage =
                message.replace("→", "<font color='green'>verde</font>")
                        .replace("←", "<font color='red'>rojo</font>");
        CharSequence styledText =
                HtmlCompat.fromHtml(styledMessage, HtmlCompat.FROM_HTML_MODE_LEGACY);

        new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle("Tasas")
                .setIcon(R.drawable.ic_about_24px)
                .setMessage(styledText)
                .setPositiveButton("Aceptar", null)
                .show();
    }

    // search view
    @SuppressLint("ResourceAsColor")
    private void searchCard(Menu menu) {
        SearchView search =
                new SearchView(
                        Objects.requireNonNull(
                                        ((MainActivity) requireContext()).getSupportActionBar())
                                .getThemedContext());
        MenuItem itemSearch = menu.findItem(R.id.menu_search);
        search.setQueryHint(getString(R.string.hint_titular));
        View searchPlate = search.findViewById(androidx.appcompat.R.id.search_plate);
        searchPlate.setBackgroundColor(android.R.color.transparent);
        search.setIconifiedByDefault(true);
        itemSearch.setShowAsAction(
                MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemSearch.setActionView(search);
        search.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        String lowCaseQuery = query.toLowerCase();
                        List<DataCards> filteredList = new ArrayList<>();
                        for (DataCards card : listCards) {
                            String lowCaseCard = card.getTarjeta().toLowerCase();
                            String lowCaseTitular = card.getUsuario().toLowerCase();
                            if (lowCaseCard.contains(lowCaseQuery)
                                    || lowCaseTitular.contains(lowCaseQuery)) {
                                filteredList.add(card);
                            }
                        }
                        if (lowCaseQuery.isEmpty()) {
                            cardAdapter.filterList(listCards);
                            Toast.makeText(
                                            requireContext(),
                                            "Los datos insertados son incorrectos",
                                            Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            cardAdapter.filterList(filteredList);
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (newText.isEmpty()) {
                            cardAdapter.filterList(listCards);
                        }
                        return false;
                    }
                });
    }

    // cargar las tasas desde el archivo almacenado
    private void loadTasasToFile() {
        Set<String> select =
                sp.getStringSet(
                        "monedas",
                        new HashSet<>(
                                Arrays.asList(
                                        getResources().getStringArray(entries_value_default))));
        try {
            JSONObject informal = new JSONObject(tasas.getFileJson().getString("informal"));
            JSONObject formal = new JSONObject(tasas.getFileJson().getString("formal"));

            if (select.contains("0") && select.contains("1")) {
                if (informal.length() > 0 || formal.length() > 0) {
                    HashMap<String, String> map = new HashMap<>();
                    Log.e("Pref ", " " + select);
                    viewAnimation();
                    map.put("USD", "USD");
                    map.put("EUR", "EUR");
                    map.put("MLC", "MLC");
                    for (String currency : map.keySet()) {
                        if (informal.has(currency)) {
                            JSONObject object = informal.getJSONObject(currency);
                            double buy = object.getDouble("buy");
                            double sell = object.getDouble("sell");
                            list(map.get(currency), Double.toString(buy), Double.toString(sell));
                        }
                    }

                    map.put("USD", "USD");
                    map.put("EUR", "EUR");
                    map.put("CAD", "CAD");
                    map.put("CHF", "CHF");
                    map.put("GBP", "GBP");
                    map.put("JPY", "JPY");
                    map.put("MXN", "MXN");
                    headerList();
                    for (String currency : map.keySet()) {
                        if (formal.has(currency)) {
                            JSONObject object = formal.getJSONObject(currency);
                            double buy = object.getDouble("buy");
                            double sell = object.getDouble("sell");
                            list(map.get(currency), Double.toString(buy), Double.toString(sell));
                        }
                    }
                }
            } else {
                if (select.contains("0")) {
                    if (informal.length() > 0) {
                        HashMap<String, String> map = new HashMap<>();
                        Log.e("Pref ", " " + select);
                        viewAnimation();
                        map.put("USD", "USD");
                        map.put("EUR", "EUR");
                        map.put("MLC", "MLC");
                        for (String currency : map.keySet()) {
                            if (informal.has(currency)) {
                                JSONObject object = informal.getJSONObject(currency);
                                double buy = object.getDouble("buy");
                                double sell = object.getDouble("sell");
                                list(
                                        map.get(currency),
                                        Double.toString(buy),
                                        Double.toString(sell));
                            }
                        }
                    }
                }
                if (select.contains("1")) {
                    if (formal.length() > 0) {
                        HashMap<String, String> map = new HashMap<>();
                        viewAnimation();
                        Log.e("Pref ", " " + select);
                        map.put("USD", "USD");
                        map.put("EUR", "EUR");
                        map.put("CAD", "CAD");
                        map.put("CHF", "CHF");
                        map.put("GBP", "GBP");
                        map.put("JPY", "JPY");
                        map.put("MXN", "MXN");
                        for (String currency : map.keySet()) {
                            if (formal.has(currency)) {
                                JSONObject object = formal.getJSONObject(currency);
                                double buy = object.getDouble("buy");
                                double sell = object.getDouble("sell");
                                list(
                                        map.get(currency),
                                        Double.toString(buy),
                                        Double.toString(sell));
                            }
                        }
                    }
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
            Log.e("Load Tasas: ", Objects.requireNonNull(err.getMessage()));
        }
    }

    private void list(String courrency, String buy, String sell) {
        listTasas.add(new Tasas(courrency, buy, sell));
    }

    private void headerList() {
        listTasas.add(new Header(getString(R.string.title_tasa_oficial)));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void viewAnimation() {
        binding.recycler.setVisibility(View.VISIBLE);
        tasasAdapter.notifyDataSetChanged();
        binding.shimmer.stopShimmer();
        binding.shimmer.setVisibility(View.GONE);
    }

    private void onClickMarket(View view) {
        navigateTo().navigate(R.id.navigation_market, null, options());
    }

    private void onClickCalculate(View view) {
        navigateTo().navigate(R.id.navigation_calculate, null, options());
    }

    private void onClick(int position) {
        DialogFragment dialog = new ConvertDialog();
        dialog.show(requireActivity().getSupportFragmentManager(), null);
    }
}
