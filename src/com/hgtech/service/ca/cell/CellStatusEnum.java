package com.hgtech.service.ca.cell;

/**
 * εθηΆζ
 */
public enum CellStatusEnum {

    OFF(0, "off"),
    ON(1, "on"),
    ;


    private final Integer status;

    private final String text;

    CellStatusEnum(Integer status, String text) {
        this.status = status;
        this.text = text;
    }

    public Integer getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }
}
