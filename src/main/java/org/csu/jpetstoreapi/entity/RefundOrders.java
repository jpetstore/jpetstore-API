package org.csu.jpetstoreapi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("refundorders")
public class RefundOrders {
    String orderid;
    BigDecimal refund_amount;
    String refund_reason;
    boolean is_processed;
    boolean is_refused;
    String refuse_reason;
}
