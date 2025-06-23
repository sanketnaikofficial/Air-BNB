package com.PaymentService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PaymentService.Client.BookingClient;
import com.PaymentService.Dto.ProductRequestDto;
import com.PaymentService.Dto.StripeResponseDto;
import com.PaymentService.Service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import org.springframework.beans.factory.annotation.Value;



@RestController
@RequestMapping("/product/v1/payment")
public class ProductCheckoutController {
	
	
	@Autowired
    private StripeService stripeService;
	
	@Autowired
	private BookingClient bookingClient;

    public ProductCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponseDto> checkoutProducts(@RequestBody ProductRequestDto productRequest) {
        StripeResponseDto stripeResponse = stripeService.checkoutProducts(productRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(stripeResponse);
    }
    
    @GetMapping("/success")
    public ResponseEntity<String> handleSuccess(@RequestParam("session_id") String sessionId, @RequestParam("bookingId") Long bookingId) {
        ResponseEntity<String> res=stripeService.handleSuccess(sessionId, bookingId);
    	return res;	
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> handleCancel() {
        System.out.println("❌ Payment cancelled: false");
        return ResponseEntity.ok("Payment cancelled");
    }
}