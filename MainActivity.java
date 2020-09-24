package com.example.android.coffebreak;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Обьявляем все виды кофе
    String[] coffeeTypes;

    boolean largePortion = false;

    boolean hasMilk = false, hasCream = false, hasWCream = false, hasHoney = false, hasSyrup = false, hasMarshmallow = false;

    int quantity = 1;

    String currentCoffeeType = "Americana";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Запрет альбомной ориентации
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Системный метод, срабатывает при старте программы
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coffeeTypes = new String[]{getResources().getString(R.string.americana), getResources().getString(R.string.cappuccino), getResources().getString(R.string.latte), getResources().getString(R.string.mocha)};

        //Создаем адаптер и слушатель для обьекта spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, coffeeTypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Меняем текущий тип кофе на новый, в соответствие с его названием в списке spinner
                currentCoffeeType = coffeeTypes[(int) adapterView.getSelectedItemId()];
                display();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        display();
    }

    private Editable getName() {
        //Возвращает имя
        EditText editText = (EditText) findViewById(R.id.name);
        return editText.getText();
    }

    @SuppressLint("WrongViewCast")
    public void updateCheckBoxes(View view) {
        //Обновляет global boolean'ы
        CheckBox milk = (CheckBox) findViewById(R.id.cb_milk);
        CheckBox cream = (CheckBox) findViewById(R.id.cb_cream);
        CheckBox wCream = (CheckBox) findViewById(R.id.cb_w_cream);
        CheckBox syrup = (CheckBox) findViewById(R.id.cb_syrup);
        CheckBox honey = (CheckBox) findViewById(R.id.cb_honey);
        CheckBox marshmallow = (CheckBox) findViewById(R.id.cb_marshmallow);

        hasMilk = milk.isChecked();
        hasCream = cream.isChecked();
        hasWCream = wCream.isChecked();
        hasSyrup = syrup.isChecked();
        hasHoney = honey.isChecked();
        hasMarshmallow = marshmallow.isChecked();

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch size = (Switch) findViewById(R.id.is_big);

        //CheckBox size = (CheckBox) findViewById(R.id.is_big);
        largePortion = size.isChecked();

        display();
    }

    public void quantityMinus(View view) {
        //Кол-во минус
        if (quantity > 1)
            quantity--;

        display();
    }

    public void quantityPlus(View view) {
        //Кол-во плюс
        if (quantity < 30)
            quantity++;

        display();
    }

    @SuppressLint("SetTextI18n")
    private void display() {
        //Отображает или обновляет все данные на экране
        TextView quantityTV = (TextView) findViewById(R.id.quantity);
        quantityTV.setText("" + quantity);

        TextView priceTV = (TextView) findViewById(R.id.price);
        priceTV.setText("₽" + calculatePrice() + ".00");
    }

    private int calculatePrice() {
        //Считаем цену

        int price = 0;

        //Рассчитываем цену в соответствие с видом кофе и "Большой порцией"
        if (currentCoffeeType.equals(coffeeTypes[0])) {
            if (largePortion)
                price = 99;
            else
                price = 65;
        }
        if (currentCoffeeType.equals(coffeeTypes[1]) || currentCoffeeType.equals(coffeeTypes[2])) {
            if (largePortion)
                price = 135;
            else
                price = 85;
        }
        if (currentCoffeeType.equals(coffeeTypes[3])) {
            if (largePortion)
                price = 175;
            else
                price = 110;
        }

        int toppingsPrice = 0;

        if (hasMilk)
            toppingsPrice += 10;
        if (hasCream)
            toppingsPrice += 20;
        if (hasWCream)
            toppingsPrice += 20;
        if (hasSyrup)
            toppingsPrice += 20;
        if (hasHoney)
            toppingsPrice += 20;
        if (hasMarshmallow)
            toppingsPrice += 20;

        //Финальная формула: (цена + цена добавок) * кол-во
        return (price + toppingsPrice) * quantity;
    }

    private String generateMessage() {
        String message = "ORDER #" + (int) (Math.random() * 10000);
        if (getName().length() > 0)
            message += "\n" + "Name: " + getName();
        String isLarge = "";
        if (largePortion)
            isLarge += " large";
        message += "\n" + quantity + isLarge + " cups of ";
        if (currentCoffeeType.equals(coffeeTypes[0]))
            message += "Americana";
        if (currentCoffeeType.equals(coffeeTypes[1]))
            message += "Cappuccino";
        if (currentCoffeeType.equals(coffeeTypes[2]))
            message += "Latte";
        if (currentCoffeeType.equals(coffeeTypes[3]))
            message += "Mocha";
        if (hasMilk || hasCream || hasWCream || hasHoney || hasSyrup || hasMarshmallow) {
            message += "\n" + "With: ";
            if (hasMilk)
                message += "\nMilk";
            if (hasCream)
                message += "\nCream";
            if (hasWCream)
                message += "\nWhipped Cream";
            if (hasHoney)
                message += "\nHoney";
            if (hasSyrup)
                message += "\nSyrup";
            if (hasMarshmallow)
                message += "\nMarshmallow";
        }
        return message;
    }

    @SuppressLint("IntentReset")
    public void submitOrder(View view) {
        String message = generateMessage();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:example@gmail.com")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hello");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}