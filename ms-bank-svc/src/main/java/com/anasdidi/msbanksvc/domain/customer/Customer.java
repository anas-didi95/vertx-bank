package com.anasdidi.msbanksvc.domain.customer;

import java.time.OffsetDateTime;
import java.util.UUID;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.RowMapped;

@DataObject
@RowMapped
public class Customer {

  @Column(name = "id")
  private UUID id;

  @Column(name = "nm")
  private String name;

  @Column(name = "created_dt")
  private OffsetDateTime createdDate;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_dt")
  private OffsetDateTime updatedDate;

  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "ver")
  private Integer version;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OffsetDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(OffsetDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public OffsetDateTime getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(OffsetDateTime updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

}
