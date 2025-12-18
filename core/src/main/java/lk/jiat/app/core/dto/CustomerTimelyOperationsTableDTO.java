package lk.jiat.app.core.dto;

import lk.jiat.app.core.model.ScheduledTransfer;
import lk.jiat.app.core.model.Transfer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerTimelyOperationsTableDTO implements Serializable {

    private long totalRowCount;
    private List<ScheduledTransfer> list;
    private Double totalAmount;
    private LocalDateTime nextDateTime;
    private long totalRowCountAfterFiltering;

    public long getTotalRowCountAfterFiltering() {
        return totalRowCountAfterFiltering;
    }

    public void setTotalRowCountAfterFiltering(long totalRowCountAfterFiltering) {
        this.totalRowCountAfterFiltering = totalRowCountAfterFiltering;
    }

    public LocalDateTime getNextDateTime() {
        return nextDateTime;
    }

    public void setNextDateTime(LocalDateTime nextDateTime) {
        this.nextDateTime = nextDateTime;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public CustomerTimelyOperationsTableDTO() {

    }

    public long getTotalRowCount() {
        return totalRowCount;
    }
    public void setTotalRowCount(long totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public List<ScheduledTransfer> getList() {
        return list;
    }
    public void setList(List<ScheduledTransfer> list) {
        this.list = list;
    }
}
