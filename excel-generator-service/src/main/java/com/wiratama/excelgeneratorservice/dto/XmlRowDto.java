package com.wiratama.excelgeneratorservice.dto;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@XmlRootElement(name = "row")
@Setter
public class XmlRowDto {

  private List<Element> elements;

  @XmlAnyElement
  public List<Element> getElements() {
    return elements;
  }

  public Map<String, String> toMap() {
    Map<String, String> result = new HashMap<>();
    Optional.ofNullable(elements)
      .stream()
      .flatMap(List::stream)
      .forEach(element -> result.put(element.getTagName(), element.getTextContent()));

    return result;
  }
}
