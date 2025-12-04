package Back_End;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeCheckoutService {

    private static final String SECRET_KEY = "sk_test_51SW8xkRv7Hl7GkW3DkdHbUUdQJVNPwClNuVfUofHbjdrcI2Q0wJKI944Cu0qDo9vzI47LTOYm3SID8StSdpsLoNi00TxK1Wsj3";

    public StripeCheckoutService() {
        Stripe.apiKey = SECRET_KEY;
    }

    public String crearSesionPago(int idUsuario) throws Exception {

        SessionCreateParams params =
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setClientReferenceId(String.valueOf(idUsuario))
                .setSuccessUrl("http://localhost:4242/premium/exito?id=" + idUsuario)
                .setCancelUrl("http://localhost:4242/premium/cancelado")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                            .setPrice("price_1SWA81Rv7Hl7GkW3y2Qf2qQU")   // ⚠️ <-- tu PRICE real de Stripe
                            .setQuantity(1L)
                            .build()
                )
                .setClientReferenceId(String.valueOf(idUsuario))
                .build();

        Session session = Session.create(params);

        return session.getUrl(); // URL real del checkout con redirección funcional
    }
}
