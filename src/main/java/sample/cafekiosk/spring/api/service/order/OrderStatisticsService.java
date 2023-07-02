package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

    private final OrderRepository orderRepository;

    private final MailService mailService;

    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
        // 해당 일자에 결제완료된 주문들을 가져와서
        final List<Order> orders = orderRepository.findOrdersBy(
                // 등록시간을 기준으로 하루치 주문을 가져오자.
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        );

        // 총매출 합계를 계산해서
        final int targetDateTotalPrice = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        // 메일전송
        final boolean result = mailService.sendMail("no-reply@test.com",
                email,
                String.format("[매출통계] %s", orderDate),
                String.format("총 매출 합계는 %s 원 입니다.", targetDateTotalPrice)
        );

        if(!result) {
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        }

        return true;
    }

}
