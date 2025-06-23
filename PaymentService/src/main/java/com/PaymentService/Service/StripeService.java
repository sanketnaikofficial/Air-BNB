package com.PaymentService.Service;

import com.PaymentService.Service.*;
import com.PaymentService.Client.BookingClient;
import com.PaymentService.Client.PropertyClient;
import com.PaymentService.Dto.*;
import com.PaymentService.Dto.StripeResponseDto;
import com.PaymentService.Entity.Payment;
import com.PaymentService.Enums.PaymentMethod;
import com.PaymentService.Enums.PaymentStatus;
import com.PaymentService.Repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class StripeService {
	

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PropertyClient propertyClient;

    @Autowired
    private BookingClient bookingClient;
    
    @Autowired
    private PdfService pdfService;
    
    @Autowired
    private KafkaProducerService kafkaProducerService;

    public StripeResponseDto checkoutProducts(ProductRequestDto productRequest) {
        Stripe.apiKey = secretKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(productRequest.getName())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : "USD")
                        .setUnitAmountDecimal(BigDecimal.valueOf(productRequest.getAmount()))
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(productRequest.getQuantity())
                        .setPriceData(priceData)
                        .build();

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5556/paymentservice/product/v1/payment/success?session_id={CHECKOUT_SESSION_ID}&bookingId=" + productRequest.getBookingId())
                    .setCancelUrl("http://localhost:5556/paymentservice/product/v1/payment/cancel")
                    .addLineItem(lineItem)
                    .build();

            Session session = Session.create(params);

            // Save initial payment entry with PENDING status
            Payment payment = new Payment();
            payment.setBookingId(productRequest.getBookingId());
            payment.setPaymentStatus(PaymentStatus.PENDING);
            payment.setStripeSessionId(session.getId());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setAmountPaid(productRequest.getAmount() / 100.0); // converting cents to dollars
            payment.setPaymentMethod("UNKNOWN");

            paymentRepository.save(payment);

            StripeResponseDto response = StripeResponseDto.builder()
                    .status("SUCCESS")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

            return response;

        } catch (StripeException e) {
            throw new RuntimeException("Error while creating Stripe Session", e);
        }
    }

    public ResponseEntity<String> handleSuccess(String sessionId, Long bookingId) {
        Stripe.apiKey = secretKey;

        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();

            if ("paid".equalsIgnoreCase(paymentStatus)) {

                // Retrieve PaymentIntent to get payment method type
                String paymentIntentId = session.getPaymentIntent();
                PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

                String paymentMethodId = paymentIntent.getPaymentMethod();
                com.stripe.model.PaymentMethod stripePaymentMethod = com.stripe.model.PaymentMethod.retrieve(paymentMethodId);
                String paymentType = stripePaymentMethod.getType();

                // Update payment record
                Optional<Payment> optionalPayment = paymentRepository.findByStripeSessionId(sessionId);
                if (optionalPayment.isPresent()) {
                    Payment payment = optionalPayment.get();
                    payment.setPaymentStatus(PaymentStatus.SUCCESS);
                    payment.setPaymentMethod(paymentType.toUpperCase());
                    paymentRepository.save(payment);
                }
                
                // Call BookingService to confirm booking
                String res=bookingClient.updatePaymentStatus(bookingId);
                if(!res.contains("Booking confirmed and room availability updated")||!res.equals("Booking already processed")) {
                	 // 1. Generate PDF from booking data
                	APIResponse<BookingDto> bookingDetails=bookingClient.getBookingDetialsById(bookingId);
//            	    byte[] pdfBytes = pdfService.generatePdf(bookingDetails);

            	    // 2. Construct Kafka message
//            	    EmailKafkaMessageDto emailMessage = new EmailKafkaMessageDto(
//            	        bookingDetails.getData().getEmail(),
//            	        "Booking Confirmation - ID #" + bookingId,
//            	        "Your booking was successful. Please find attached confirmation.",
//            	        pdfBytes
//            	    );

            	    // 3. Send message
//            	    kafkaProducerService.sendBookingEmail(emailMessage);
                	
                	return ResponseEntity.ok(res);
                }
                return ResponseEntity.ok("Payment successful and booking confirmed");
            } else {
                return ResponseEntity.status(400).body("Payment not completed");
            }
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Stripe error occurred");
        }
    }
}
