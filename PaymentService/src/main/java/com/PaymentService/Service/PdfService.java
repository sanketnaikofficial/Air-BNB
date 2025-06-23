package com.PaymentService.Service;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream; 
import org.springframework.stereotype.Service;

import com.PaymentService.Dto.APIResponse;
import com.PaymentService.Dto.BookingDto;
import com.itextpdf.html2pdf.HtmlConverter;

@Service
public class PdfService {

    public byte[] generatePdf(APIResponse<BookingDto> booking ) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            String html = """
                <html>
                    <body>
                        <h1>Booking Confirmation</h1>
                        <p>Booking ID: %d</p>
                        <p>Name: %s</p>
                        <p>Mobile: %s</p>
                        <p>Email: %s</p>
                        <p>Property Name: %s</p>
                        <p>Total Nights: %d</p>
                        <p>Number of rooms: %d</p>
                        <p>Booking Dates: %s</p>
                        <p>Room Id: %d</p>
                        <p>Amount Paid: %s</p>
                        <p>Payment Status: %s</p>
                    </body>
                </html>
                """.formatted(
                	booking.getData().getId(),
                    booking.getData().getName(),
                    booking.getData().getMobile(),
                    booking.getData().getEmail(),
                    booking.getData().getPropertyName(),
                    booking.getData().getTotalNights(),
                    booking.getData().getNumberOfRooms(),
                    booking.getData().getBookingDate(),
                    booking.getData().getRoomId(),
                    booking.getData().getPrice(),
                    booking.getData().getStatus()
                    
                );

            HtmlConverter.convertToPdf(html, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}