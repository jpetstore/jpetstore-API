package org.csu.jpetstoreapi.VO;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;

@Data
public class ReviewVO {
    private String review;

    private Integer grade;

    private Integer top;

    private String time;

}
