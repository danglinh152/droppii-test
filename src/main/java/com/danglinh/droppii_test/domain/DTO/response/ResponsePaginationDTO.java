package com.danglinh.droppii_test.domain.DTO.response;





public class ResponsePaginationDTO {
    private Meta meta;
    private Object data;

    public ResponsePaginationDTO() {
    }

    public ResponsePaginationDTO(Meta meta, Object data) {
        this.meta = meta;
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
