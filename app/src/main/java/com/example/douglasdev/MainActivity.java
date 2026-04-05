package com.example.douglasdev;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import com.google.android.material.button.MaterialButton;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import android.content.Intent;
import android.net.Uri;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay, tvAssinatura, tvAssinaturaWelcome, tvAssinaturaConv, tvHistoryContent, navCalc, navConv, tvMoedaInfo, tvMoedaBrl, tvMoedaUsd, tvMoedaEur, tvImcGenLabel, tvResImcVal, tvResImcBadge, tvResAlt, tvResPesoSug, tvResTMB;
    private EditText etImcIdade, etImcAltura, etImcPeso;
    private LinearLayout layoutWelcome, layoutMainApp, layoutCalcContent, layoutConversorContent, layoutMoedaContent, layoutImcContent, layoutImcResult, topNav, rowBrl, rowUsd, rowEur;
    private ScrollView layoutConvScroll;
    private CardView cardHistory, cardDisplay;
    private MaterialButton btnToggleHistory, btnImcMale, btnImcFemale, btnImcCalc;
    private View rootLayout;
    private List<MaterialButton> botoesOperadores = new ArrayList<>();
    private List<MaterialButton> numberButtons = new ArrayList<>();
    private List<String> historyItems = new ArrayList<>();
    private SharedPreferences prefs;
    private String corAtual = "#4CAF50";
    private boolean isLightTheme = false;
    private int moedaSelecionada = 1;
    private double rateUsdBrl = 5.1786, rateEurBrl = 5.5000, rateUsdEur = 0.8652;
    private ObjectAnimator animAssinatura, animWelcome, animConv;

    private enum InputState { START, NUMBER, OPERATOR, DECIMAL }
    private InputState currentState = InputState.START;
    private boolean isMale = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tv_display);
        tvAssinatura = findViewById(R.id.tv_assinatura);
        tvAssinaturaWelcome = findViewById(R.id.tv_assinatura_welcome);
        tvAssinaturaConv = findViewById(R.id.tv_assinatura_conv);
        tvHistoryContent = findViewById(R.id.tv_history_content);
        layoutWelcome = findViewById(R.id.layout_welcome);
        layoutMainApp = findViewById(R.id.layout_main_app);
        layoutCalcContent = findViewById(R.id.layout_calculator_content);
        layoutConversorContent = findViewById(R.id.layout_conversor_content);
        layoutConvScroll = findViewById(R.id.layout_conversor_scroll);
        layoutMoedaContent = findViewById(R.id.layout_moeda_content);
        layoutImcContent = findViewById(R.id.layout_imc_content);
        layoutImcResult = findViewById(R.id.layout_imc_result);
        topNav = findViewById(R.id.top_nav_bar);
        navCalc = findViewById(R.id.nav_calculadora);
        navConv = findViewById(R.id.nav_conversor);
        cardHistory = findViewById(R.id.card_history);
        cardDisplay = findViewById(R.id.card_display);
        btnToggleHistory = findViewById(R.id.btn_toggle_history);
        rootLayout = findViewById(R.id.root_layout);
        tvMoedaBrl = findViewById(R.id.tv_moeda_brl);
        tvMoedaUsd = findViewById(R.id.tv_moeda_usd);
        tvMoedaEur = findViewById(R.id.tv_moeda_eur);
        rowBrl = findViewById(R.id.row_brl);
        rowUsd = findViewById(R.id.row_usd);
        rowEur = findViewById(R.id.row_eur);
        tvMoedaInfo = findViewById(R.id.tv_moeda_info);
        etImcIdade = findViewById(R.id.et_imc_idade);
        etImcAltura = findViewById(R.id.et_imc_altura);
        etImcPeso = findViewById(R.id.et_imc_peso);
        tvImcGenLabel = findViewById(R.id.tv_imc_gen_label);
        btnImcMale = findViewById(R.id.btn_gender_male);
        btnImcFemale = findViewById(R.id.btn_gender_female);
        btnImcCalc = findViewById(R.id.btn_imc_final_calc);
        tvResImcVal = findViewById(R.id.tv_res_imc_value);
        tvResImcBadge = findViewById(R.id.tv_res_imc_cat_badge);
        tvResAlt = findViewById(R.id.tv_res_altura);
        tvResPesoSug = findViewById(R.id.tv_res_peso_sug);
        tvResTMB = findViewById(R.id.tv_res_tmb);

        prefs = getSharedPreferences("CalculadoraPrefs", MODE_PRIVATE);

        botoesOperadores.add((MaterialButton) findViewById(R.id.btn_div_op));
        botoesOperadores.add((MaterialButton) findViewById(R.id.btn_mult_op));
        botoesOperadores.add((MaterialButton) findViewById(R.id.btn_sub_op));
        botoesOperadores.add((MaterialButton) findViewById(R.id.btn_soma_op));
        botoesOperadores.add((MaterialButton) findViewById(R.id.btn_igual));
        botoesOperadores.add((MaterialButton) findViewById(R.id.mbtn_mc));
        botoesOperadores.add((MaterialButton) findViewById(R.id.mbtn_mback));
        botoesOperadores.add((MaterialButton) findViewById(R.id.mbtn_mdiv));
        botoesOperadores.add((MaterialButton) findViewById(R.id.mbtn_mmult));
        botoesOperadores.add((MaterialButton) findViewById(R.id.mbtn_msub));
        botoesOperadores.add((MaterialButton) findViewById(R.id.mbtn_msoma));
        botoesOperadores.add((MaterialButton) findViewById(R.id.mbtn_migual));
        botoesOperadores.add(btnImcCalc);

        numberButtons.add((MaterialButton) findViewById(R.id.btn_0));
        numberButtons.add((MaterialButton) findViewById(R.id.btn_1));
        numberButtons.add((MaterialButton) findViewById(R.id.btn_2));
        numberButtons.add((MaterialButton) findViewById(R.id.btn_3));
        numberButtons.add((MaterialButton) findViewById(R.id.btn_4));
        numberButtons.add((MaterialButton) findViewById(R.id.btn_5));
        numberButtons.add((MaterialButton) findViewById(R.id.btn_6));
        numberButtons.add((MaterialButton) findViewById(R.id.btn_7));
        numberButtons.add((MaterialButton) findViewById(R.id.btn_8));
        numberButtons.add((MaterialButton) findViewById(R.id.btn_9));
        numberButtons.add((MaterialButton) findViewById(R.id.btn_dot));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_m0));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_m1));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_m2));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_m3));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_m4));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_m5));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_m6));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_m7));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_m8));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_m9));
        numberButtons.add((MaterialButton) findViewById(R.id.mbtn_mdot));

        loadPersistentData();
        fetchRealTimeRates();

        findViewById(R.id.btn_entrar).setOnClickListener(v -> {
            layoutWelcome.animate().alpha(0f).scaleX(0.9f).scaleY(0.9f).setDuration(500).setInterpolator(new DecelerateInterpolator()).withEndAction(() -> {
                layoutWelcome.setVisibility(View.GONE);
                layoutMainApp.setAlpha(0f);
                layoutMainApp.setVisibility(View.VISIBLE);
                layoutMainApp.animate().alpha(1f).setDuration(500).start();
            }).start();
        });

        navCalc.setOnClickListener(v -> { layoutCalcContent.setVisibility(View.VISIBLE); layoutConversorContent.setVisibility(View.GONE); navCalc.setAlpha(1f); navConv.setAlpha(0.4f); });
        navConv.setOnClickListener(v -> { layoutCalcContent.setVisibility(View.GONE); layoutConversorContent.setVisibility(View.VISIBLE); navCalc.setAlpha(0.4f); navConv.setAlpha(1f); });

        findViewById(R.id.item_moeda).setOnClickListener(v -> { topNav.setVisibility(View.GONE); layoutConversorContent.setVisibility(View.GONE); layoutMoedaContent.setVisibility(View.VISIBLE); selectMoeda(1); });
        findViewById(R.id.btn_moeda_back).setOnClickListener(v -> { topNav.setVisibility(View.VISIBLE); layoutConversorContent.setVisibility(View.VISIBLE); layoutMoedaContent.setVisibility(View.GONE); });
        findViewById(R.id.btn_imc_back).setOnClickListener(v -> { hideKeyboard(); topNav.setVisibility(View.VISIBLE); layoutConversorContent.setVisibility(View.VISIBLE); layoutImcContent.setVisibility(View.GONE); });
        findViewById(R.id.item_imc).setOnClickListener(v -> { topNav.setVisibility(View.GONE); layoutConversorContent.setVisibility(View.GONE); layoutImcContent.setVisibility(View.VISIBLE); });
        findViewById(R.id.btn_imc_result_back).setOnClickListener(v -> { layoutImcResult.setVisibility(View.GONE); layoutImcContent.setVisibility(View.VISIBLE); });

        btnImcMale.setOnClickListener(v -> { isMale = true; tvImcGenLabel.setText("Gênero: Masculino"); btnImcMale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(corAtual))); btnImcFemale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2C2C2C"))); });
        btnImcFemale.setOnClickListener(v -> { isMale = false; tvImcGenLabel.setText("Gênero: Feminino"); btnImcFemale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(corAtual))); btnImcMale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2C2C2C"))); });
        btnImcCalc.setOnClickListener(v -> calculateFinalIMC());

        rowBrl.setOnClickListener(v -> selectMoeda(0));
        rowUsd.setOnClickListener(v -> selectMoeda(1));
        rowEur.setOnClickListener(v -> selectMoeda(2));

        setupKeypadMoeda();
        btnToggleHistory.setOnClickListener(v -> cardHistory.setVisibility(cardHistory.getVisibility() == View.GONE ? View.VISIBLE : View.GONE));
        findViewById(R.id.btn_clear_history).setOnClickListener(v -> { historyItems.clear(); saveHistoryToPrefs(); tvHistoryContent.setText(""); });

        iniciarFlutuacao(tvAssinatura);
        iniciarFlutuacao(tvAssinaturaWelcome);
        iniciarFlutuacao(tvAssinaturaConv);

        findViewById(R.id.btn_red).setOnClickListener(v -> mudarTema("#440000", "#FF5252"));
        findViewById(R.id.btn_blue).setOnClickListener(v -> mudarTema("#001144", "#448AFF"));
        findViewById(R.id.btn_green).setOnClickListener(v -> mudarTema("#004411", "#4CAF50"));
        findViewById(R.id.btn_pink).setOnClickListener(v -> mudarTema("#440022", "#FF4081"));
        findViewById(R.id.btn_dark).setOnClickListener(v -> mudarTema("#050505", "#00FFCC"));
        findViewById(R.id.btn_white).setOnClickListener(v -> mudarTema("#FAFAFA", "#1976D2"));

        int[] numIds = {R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9};
        for (int id : numIds) findViewById(id).setOnClickListener(v -> handleNumberInput(((Button)v).getText().toString()));

        findViewById(R.id.btn_dot).setOnClickListener(v -> handleDecimalInput());
        findViewById(R.id.btn_soma_op).setOnClickListener(v -> handleOperatorInput("+"));
        findViewById(R.id.btn_sub_op).setOnClickListener(v -> handleOperatorInput("-"));
        findViewById(R.id.btn_mult_op).setOnClickListener(v -> handleOperatorInput("×"));
        findViewById(R.id.btn_div_op).setOnClickListener(v -> handleOperatorInput("÷"));
        findViewById(R.id.btn_c).setOnClickListener(v -> handleClear());
        findViewById(R.id.btn_backspace).setOnClickListener(v -> handleBackspace());
        findViewById(R.id.btn_igual).setOnClickListener(v -> handleEquals());

        findViewById(R.id.tv_telegram_link).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/MadKiller_1"));
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animAssinatura != null && animAssinatura.isRunning()) animAssinatura.pause();
        if (animWelcome != null && animWelcome.isRunning()) animWelcome.pause();
        if (animConv != null && animConv.isRunning()) animConv.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animAssinatura != null && !animAssinatura.isRunning()) animAssinatura.resume();
        if (animWelcome != null && !animWelcome.isRunning()) animWelcome.resume();
        if (animConv != null && !animConv.isRunning()) animConv.resume();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void handleNumberInput(String digit) {
        if (currentState == InputState.START) {
            tvDisplay.setText("");
        }
        String text = tvDisplay.getText().toString();
        if (text.equals("0")) {
            tvDisplay.setText(digit);
        } else {
            tvDisplay.append(digit);
        }
        currentState = InputState.NUMBER;
    }

    private void handleDecimalInput() {
        if (currentState == InputState.DECIMAL) return;
        if (currentState == InputState.START || tvDisplay.getText().toString().isEmpty() || tvDisplay.getText().toString().equals("0")) {
            tvDisplay.setText("0.");
            currentState = InputState.DECIMAL;
            return;
        }
        String text = tvDisplay.getText().toString();
        int lastSpace = text.lastIndexOf(' ');
        String currentNum = (lastSpace == -1) ? text : text.substring(lastSpace + 1).trim();
        if (currentNum.contains(".")) return;
        tvDisplay.append(".");
        currentState = InputState.DECIMAL;
    }

    private void handleOperatorInput(String op) {
        String text = tvDisplay.getText().toString().trim();
        if (text.isEmpty() || text.equals("0")) {
            if (op.equals("-")) {
                tvDisplay.setText("- ");
                currentState = InputState.OPERATOR;
            }
            return;
        }
        char lastChar = text.charAt(text.length() - 1);
        if ("+-×÷".indexOf(lastChar) != -1) {
            tvDisplay.setText(text.substring(0, text.length() - 3) + " " + op + " ");
        } else {
            tvDisplay.append(" " + op + " ");
        }
        currentState = InputState.OPERATOR;
    }

    private void handleBackspace() {
        String text = tvDisplay.getText().toString();
        if (text.equals("0") || text.isEmpty()) {
            tvDisplay.setText("0");
            currentState = InputState.START;
            return;
        }
        text = text.trim();
        if (text.length() <= 1) {
            tvDisplay.setText("0");
            currentState = InputState.START;
            return;
        }
        String newText = text.substring(0, text.length() - 1).trim();
        tvDisplay.setText(newText.isEmpty() ? "0" : newText);
        updateStateFromDisplay();
    }

    private void handleClear() {
        tvDisplay.setText("0");
        currentState = InputState.START;
    }

    private void handleEquals() {
        try {
            String expr = normalizarExpressao(tvDisplay.getText().toString());
            double resultado = avaliarExpressao(expr);
            String resultadoStr = formatarNumero(resultado);
            historyItems.add(0, expr + " = " + resultadoStr);
            if (historyItems.size() > 50) {
                historyItems = new ArrayList<>(historyItems.subList(0, 50));
            }
            saveHistoryToPrefs();
            updateHistoryView();
            tvDisplay.setText(resultadoStr);
            currentState = InputState.START;
        } catch (Exception e) {
            tvDisplay.setText("Erro");
            currentState = InputState.START;
        }
    }

    private void updateStateFromDisplay() {
        String text = tvDisplay.getText().toString().trim();
        if (text.isEmpty() || text.equals("0")) {
            currentState = InputState.START;
            return;
        }
        char lastChar = text.charAt(text.length() - 1);
        if ("+-×÷".indexOf(lastChar) != -1) {
            currentState = InputState.OPERATOR;
        } else if (lastChar == '.') {
            currentState = InputState.DECIMAL;
        } else {
            currentState = InputState.NUMBER;
        }
    }

    private void calculateFinalIMC() {
        hideKeyboard();
        try {
            String sPeso = etImcPeso.getText().toString().trim();
            String sAltura = etImcAltura.getText().toString().trim();
            String sIdade = etImcIdade.getText().toString().trim();

            if (sPeso.isEmpty() || sAltura.isEmpty() || sIdade.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            double p = Double.parseDouble(sPeso);
            double aCm = Double.parseDouble(sAltura);
            int idade = Integer.parseInt(sIdade);

            if (p <= 0 || aCm <= 0 || aCm > 300 || idade <= 0 || idade > 120) {
                Toast.makeText(this, "Valores inválidos!", Toast.LENGTH_SHORT).show();
                return;
            }

            double aM = aCm / 100;
            double imc = p / (aM * aM);
            tvResImcVal.setText(String.format("%.1f", imc));
            tvResAlt.setText(String.format("%.1f", aCm));
            double sMin = 18.5 * aM * aM;
            double sMax = 24.9 * aM * aM;
            tvResPesoSug.setText(String.format("%.1f ~ %.1f", sMin, sMax));

            double tmb;
            if (isMale) {
                tmb = 88.362 + (13.397 * p) + (4.799 * aCm) - (5.677 * idade);
            } else {
                tmb = 447.593 + (9.247 * p) + (3.098 * aCm) - (4.330 * idade);
            }
            tvResTMB.setText(String.format("%.0f kcal/dia", tmb));

            if (imc < 18.5) { tvResImcBadge.setText("Subpeso"); tvResImcBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2196F3"))); }
            else if (imc < 25) { tvResImcBadge.setText("Normal"); tvResImcBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); }
            else if (imc < 30) { tvResImcBadge.setText("Sobrepeso"); tvResImcBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEB3B"))); }
            else { tvResImcBadge.setText("Obeso"); tvResImcBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9800"))); }
            layoutImcContent.setVisibility(View.GONE);
            layoutImcResult.setVisibility(View.VISIBLE);
        } catch (Exception e) { Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show(); }
    }

    private void fetchRealTimeRates() {
        new Thread(() -> {
            try {
                URL url = new URL("https://economia.awesomeapi.com.br/last/USD-BRL,EUR-BRL,USD-EUR");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder res = new StringBuilder();
                String l;
                while ((l = r.readLine()) != null) res.append(l);
                r.close();
                conn.disconnect();
                JSONObject json = new JSONObject(res.toString());
                double newUsdBrl = json.getJSONObject("USDBRL").getDouble("bid");
                double newEurBrl = json.getJSONObject("EURBRL").getDouble("bid");
                double newUsdEur = json.getJSONObject("USDEUR").getDouble("bid");

                runOnUiThread(() -> {
                    rateUsdBrl = newUsdBrl;
                    rateEurBrl = newEurBrl;
                    rateUsdEur = newUsdEur;
                    tvMoedaInfo.setText("Taxas atualizadas agora via AwesomeAPI");
                    convertMoeda();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    tvMoedaInfo.setText("Falha na rede. Usando taxas offline.");
                    convertMoeda();
                });
            }
        }).start();
    }

    private void selectMoeda(int i) {
        moedaSelecionada = i;
        rowBrl.setBackgroundColor(i == 0 ? Color.parseColor("#1E1E1E") : Color.TRANSPARENT);
        rowUsd.setBackgroundColor(i == 1 ? Color.parseColor("#1E1E1E") : Color.TRANSPARENT);
        rowEur.setBackgroundColor(i == 2 ? Color.parseColor("#1E1E1E") : Color.TRANSPARENT);
        tvMoedaBrl.setTextColor(i == 0 ? Color.parseColor(corAtual) : Color.WHITE);
        tvMoedaUsd.setTextColor(i == 1 ? Color.parseColor(corAtual) : Color.WHITE);
        tvMoedaEur.setTextColor(i == 2 ? Color.parseColor(corAtual) : Color.WHITE);
    }

    private void setupKeypadMoeda() {
        int[] ids = {R.id.mbtn_mc, R.id.mbtn_mback, R.id.mbtn_m7, R.id.mbtn_m8, R.id.mbtn_m9, R.id.mbtn_m4, R.id.mbtn_m5, R.id.mbtn_m6, R.id.mbtn_m1, R.id.mbtn_m2, R.id.mbtn_m3, R.id.mbtn_m0, R.id.mbtn_mdot};
        for (int id : ids) findViewById(id).setOnClickListener(v -> {
            String val = ((Button)v).getText().toString();
            TextView t = (moedaSelecionada == 0) ? tvMoedaBrl : (moedaSelecionada == 1) ? tvMoedaUsd : tvMoedaEur;
            if (val.equals("C")) { tvMoedaBrl.setText("0"); tvMoedaUsd.setText("0"); tvMoedaEur.setText("0"); }
            else if (val.equals("⌫")) { String cur = t.getText().toString(); if (cur.length() > 1) t.setText(cur.substring(0, cur.length() - 1)); else t.setText("0"); convertMoeda(); }
            else { if (t.getText().toString().equals("0") && !val.equals(".")) t.setText(val); else t.append(val); convertMoeda(); }
        });
    }

    private void convertMoeda() {
        try {
            TextView target = (moedaSelecionada == 0) ? tvMoedaBrl : (moedaSelecionada == 1) ? tvMoedaUsd : tvMoedaEur;
            String raw = target.getText().toString().trim();
            if (raw.isEmpty() || raw.equals("0")) return;

            double v = Double.parseDouble(raw.replace(",", "."));
            if (moedaSelecionada == 0) {
                tvMoedaUsd.setText(String.format("%.4f", v / rateUsdBrl));
                tvMoedaEur.setText(String.format("%.4f", v / rateEurBrl));
            } else if (moedaSelecionada == 1) {
                tvMoedaBrl.setText(String.format("%.4f", v * rateUsdBrl));
                tvMoedaEur.setText(String.format("%.4f", v * rateUsdEur));
            } else {
                double usd = v / rateUsdEur;
                tvMoedaUsd.setText(String.format("%.4f", usd));
                tvMoedaBrl.setText(String.format("%.4f", usd * rateUsdBrl));
            }
        } catch (Exception ignored) {}
    }

    private String normalizarExpressao(String expr) {
        return expr.replaceAll("\\s+", " ")
                .trim()
                .replaceAll("\\s*([+\\-×÷])\\s*", " $1 ");
    }

    private double avaliarExpressao(String expr) throws Exception {
        expr = expr.replace(" ", "");
        expr = expr.replace("×", "*");
        expr = expr.replace("÷", "/");
        return avaliar(expr);
    }

    private double avaliar(String expr) throws Exception {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expr.length()) throw new RuntimeException("Caractere inesperado: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expr.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Caractere inesperado: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }

    private String formatarNumero(double valor) {
        if (Double.isNaN(valor) || Double.isInfinite(valor)) return "Erro";
        BigDecimal bd = new BigDecimal(valor)
                .setScale(8, RoundingMode.HALF_UP)
                .stripTrailingZeros();
        return bd.toPlainString();
    }

    private void updateHistoryView() { StringBuilder sb = new StringBuilder(); for (String s : historyItems) sb.append(s).append("\n"); tvHistoryContent.setText(sb.toString()); }

    private void saveHistoryToPrefs() {
        try {
            JSONArray jsonArr = new JSONArray(historyItems);
            prefs.edit().putString("history_data", jsonArr.toString()).apply();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (String s : historyItems) sb.append(s.replace("@@@", "␟")).append("@@@");
            prefs.edit().putString("history_data", sb.toString()).apply();
        }
    }

    private void loadPersistentData() {
        mudarTema(prefs.getString("fundo", "#121212"), prefs.getString("destaque", "#4CAF50"));
        String h = prefs.getString("history_data", "");
        if (!h.isEmpty()) {
            if (h.startsWith("[")) {
                try {
                    JSONArray jsonArr = new JSONArray(h);
                    historyItems.clear();
                    for (int i = 0; i < jsonArr.length(); i++) {
                        historyItems.add(jsonArr.getString(i));
                    }
                } catch (Exception e) {
                    for (String p : h.split("@@@")) {
                        if (!p.trim().isEmpty()) historyItems.add(p.replace("␟", "@@@"));
                    }
                }
            } else {
                for (String p : h.split("@@@")) {
                    if (!p.trim().isEmpty()) historyItems.add(p.replace("␟", "@@@"));
                }
            }
            updateHistoryView();
        }
    }

    private void mudarTema(String f, String d) {
        corAtual = d;
        isLightTheme = Color.parseColor(f) > 0xFF888888;
        rootLayout.setBackgroundColor(Color.parseColor(f));
        ColorStateList csl = ColorStateList.valueOf(Color.parseColor(d));
        for (MaterialButton b : botoesOperadores) b.setBackgroundTintList(csl);
        int textMain = isLightTheme ? Color.parseColor("#1E1E1E") : Color.parseColor("#FFFFFF");
        int textSecondary = isLightTheme ? Color.parseColor("#666666") : Color.parseColor("#888888");
        int cardBg = isLightTheme ? Color.parseColor("#FFFFFF") : Color.parseColor("#1E1E1E");
        int numberBg = isLightTheme ? Color.parseColor("#F0F0F0") : Color.parseColor("#2C2C2C");
        tvDisplay.setTextColor(textMain);
        navCalc.setTextColor(textMain);
        navConv.setTextColor(textMain);
        tvHistoryContent.setTextColor(isLightTheme ? Color.parseColor("#333333") : Color.parseColor("#CCCCCC"));
        tvMoedaInfo.setTextColor(textSecondary);
        cardDisplay.setCardBackgroundColor(cardBg);
        cardHistory.setCardBackgroundColor(cardBg);
        for (MaterialButton b : numberButtons) {
            b.setBackgroundTintList(ColorStateList.valueOf(numberBg));
        }
        btnToggleHistory.setTextColor(Color.parseColor(d));
        tvAssinatura.setShadowLayer(20, 0, 0, Color.parseColor(d));
        tvAssinaturaWelcome.setShadowLayer(15, 0, 0, Color.parseColor(d));
        tvAssinaturaConv.setShadowLayer(20, 0, 0, Color.parseColor(d));
        tvMoedaBrl.setTextColor(moedaSelecionada == 0 ? Color.parseColor(d) : textMain);
        tvMoedaUsd.setTextColor(moedaSelecionada == 1 ? Color.parseColor(d) : textMain);
        tvMoedaEur.setTextColor(moedaSelecionada == 2 ? Color.parseColor(d) : textMain);
        prefs.edit().putString("fundo", f).putString("destaque", d).apply();
    }

    private void iniciarFlutuacao(View v) {
        ObjectAnimator f = ObjectAnimator.ofFloat(v, "translationY", -30f, 30f);
        f.setDuration(2200); f.setInterpolator(new AccelerateDecelerateInterpolator());
        f.setRepeatMode(ValueAnimator.REVERSE); f.setRepeatCount(ValueAnimator.INFINITE);
        if (v == tvAssinatura) animAssinatura = f;
        else if (v == tvAssinaturaWelcome) animWelcome = f;
        else if (v == tvAssinaturaConv) animConv = f;
        f.start();
    }
}