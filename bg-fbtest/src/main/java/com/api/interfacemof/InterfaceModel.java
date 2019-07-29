package com.api.interfacemof;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterfaceModel {

  private String fieldName="";
  private String fieldDesc="";
  private String fieldType="";
  private String fieldLength="";
  private String fieldResvd="";
  private String fieldNecc="";
  private String fieldAccuracy="";
  private String fieldCpack="";
  private String fieldState="init";
  private InterfaceModel[] children;

}
