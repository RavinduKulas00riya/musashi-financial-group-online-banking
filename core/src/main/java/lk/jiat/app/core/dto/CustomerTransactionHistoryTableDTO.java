package lk.jiat.app.core.dto;

import lk.jiat.app.core.model.Transfer;

import java.io.Serializable;
import java.util.List;

public class CustomerTransactionHistoryTableDTO implements Serializable {

    private long totalRowCount;
    private List<Transfer> list;

    public CustomerTransactionHistoryTableDTO() {

    }

    public long getTotalRowCount() {
        return totalRowCount;
    }
    public void setTotalRowCount(long totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public List<Transfer> getList() {
        return list;
    }

    public void setList(List<Transfer> list) {
        this.list = list;
    }
}
