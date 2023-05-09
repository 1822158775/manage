package com.example.manage.entity.data_statistics;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/5/9
 */
@Data
@ToString
public class PunchingCardRecordReturn {
    public List<PunchingCardRecordStatistcs> statistcs;
    public ArrayList<Object> title;

    public PunchingCardRecordReturn() {
    }

    public PunchingCardRecordReturn(List<PunchingCardRecordStatistcs> statistcs, ArrayList<Object> title) {
        this.statistcs = statistcs;
        this.title = title;
    }
}
