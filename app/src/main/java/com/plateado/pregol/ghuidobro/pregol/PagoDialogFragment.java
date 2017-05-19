package com.plateado.pregol.ghuidobro.pregol;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.DecorationPreference;

/**
 * Created by ghuidobro on 5/18/17.
 */

public class PagoDialogFragment extends DialogFragment {

    private static TextView tv;
    Button buttonPagar,buttonCancelar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.pago_custom_dialog);

        buttonPagar = (Button) d.findViewById(R.id.buttonPagar);
        buttonCancelar = (Button) d.findViewById(R.id.buttonCancelar);

        tv = (TextView) d.findViewById(R.id.monto);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(3);
        np.setMinValue(1);
        np.setDisplayedValues( new String[] { "10", "50", "100" } );
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Integer pick = null;
                switch (np.getValue()) {
                    case 1:
                        pick = 10;
                        break;
                    case 2:
                        pick = 50;
                        break;
                    case 3:
                        pick = 100;
                        break;
                    default:
                }
                tv.setText("$" + String.valueOf(pick) + " Pesos seran enviados y recibiras una confirmacion pronto");
            }
        });

        buttonPagar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                submit(v);
            }
        });
        buttonCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();

        return d;
    }

    // Método ejecutado al hacer clic en el botón
    public void submit(View view) {
        //Puedes cambiar los colores
        DecorationPreference decorationPreference = new DecorationPreference();
        //Puedes obtener el color desde un recurso de tu colors.xml
        decorationPreference.setBaseColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        //O con un código de color hexadecimal
        decorationPreference.setBaseColor("#77B2D2");
        //También puedes habilitar la fuente oscura
        decorationPreference.enableDarkFont();

        // Iniciar el checkout de MercadoPago
        new MercadoPago.StartActivityBuilder()
                .setActivity(getActivity())
                .setPublicKey("TEST-ad365c37-8012-4014-84f5-6c895b3f8e0a")
                .setCheckoutPreferenceId("150216849-ceed1ee4-8ab9-4449-869f-f4a8565d386f")
                .setDecorationPreference(decorationPreference)
                .startCheckoutActivity();

    }
}