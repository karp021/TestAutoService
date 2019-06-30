package haulmont.karp.backend.models;

public enum OrderStatus {

    SCHEDULED ("Запланирован"),
    COMPLETED ("Выполнен"),
    ACCEPTED ("Принят клиентом");

    private String status;

    OrderStatus (String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String toString() { return this.status; }

    public static OrderStatus fromString(String orderStatus) {
        for (OrderStatus statusStr : OrderStatus.values()) {
            if (statusStr.status.equalsIgnoreCase(orderStatus)) {
                return statusStr;
            }
        }
        return null;
    }

}
