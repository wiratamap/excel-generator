package com.wiratama.excelgeneratorservice.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

import java.util.List;

@XmlRootElement(name = "data")
@Setter
public class XmlDataDto {

  private List<XmlRowDto> rows;

  @XmlElement(name = "row")
  public List<XmlRowDto> getRows() {
    return rows;
  }
}
