package com.api.interfacemof;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class InterfaceJsonUtils {
  /**
   * 得到children
   * @param jsonArray
   * @return
   */
  private static JSONArray returnChildren(JSONArray jsonArray) {
    for (Object o : jsonArray) {
      JSONObject jsonObject = JSONObject.fromObject(o);
      if (jsonObject.get("FieldName").equals("messageRequestBody") || jsonObject.get("FieldName").equals("messageResponseBody")) {
        return JSONArray.fromObject(jsonObject.get("children"));
      }
    }
    return null;
  }

public static boolean ifSonArray(JSONArray jsonArray){
    for(Object o : jsonArray){
      if(o instanceof JSONArray){
        return true;
      }
    }
    return false;
}
  /**
   * children转成model
   * @param jsonArray
   * @return
   */
  public static InterfaceModel[] returnInterfaceModel(JSONArray jsonArray) {
    InterfaceModel[] t=new InterfaceModel[jsonArray.size()];
    int i=-1;
    for (Object o : jsonArray) {
      i++;
      t[i]=new InterfaceModel();
      JSONObject jsonObject = JSONObject.fromObject(o);
      if (jsonObject.get("children")!=null&&!"".equals(jsonObject.get("children"))) {
        t[i].setFieldAccuracy(jsonObject.get("FieldAccuracy")!=null?jsonObject.get("FieldAccuracy").toString():"");
        t[i].setFieldDesc(jsonObject.get("FieldDesc")!=null?jsonObject.get("FieldDesc").toString():"");
        t[i].setFieldLength(jsonObject.get("FieldLength")!=null?jsonObject.get("FieldLength").toString():"");
        t[i].setFieldName(jsonObject.get("FieldName")!=null?jsonObject.get("FieldName").toString():"");
        t[i].setFieldNecc(jsonObject.get("FieldNecc")!=null?jsonObject.get("FieldNecc").toString():"");
        t[i].setFieldResvd(jsonObject.get("FieldResvd")!=null?jsonObject.get("FieldResvd").toString():"");
        t[i].setFieldType(jsonObject.get("FieldType")!=null?jsonObject.get("FieldType").toString():"");
        t[i].setFieldCpack(jsonObject.get("FieldCpack")!=null?jsonObject.get("FieldCpack").toString():"");
        //根据children类型做不同处理
        jsonArray=JSONArray.fromObject(jsonObject.get("children"));
        if(ifSonArray(jsonArray)){//-----------------处理cpack多包情况---------------
          int len=jsonArray.size();//cpack 多包
          int k=0;
          InterfaceModel[] x=new InterfaceModel[len];
          for(Object obj:jsonArray){
            if(obj instanceof JSONArray){
              k++;
              JSONArray array=(JSONArray)obj;
              InterfaceModel im=new InterfaceModel();//第n个包
              InterfaceModel[] tmp=new InterfaceModel[array.size()];
              im.setFieldName("响应包"+k);
              for(int l=0;l<array.size();l++){
                tmp[l]= new InterfaceModel();
                JSONObject sonObject = JSONObject.fromObject(array.getJSONObject(l));
                tmp[l].setFieldAccuracy(sonObject.get("FieldAccuracy")!=null?sonObject.get("FieldAccuracy").toString():"");
                tmp[l].setFieldDesc(sonObject.get("FieldDesc")!=null?sonObject.get("FieldDesc").toString():"");
                tmp[l].setFieldLength(sonObject.get("FieldLength")!=null?sonObject.get("FieldLength").toString():"");
                tmp[l].setFieldName(sonObject.get("FieldName")!=null?sonObject.get("FieldName").toString():"");
                tmp[l].setFieldNecc(sonObject.get("FieldNecc")!=null?sonObject.get("FieldNecc").toString():"");
                tmp[l].setFieldResvd(sonObject.get("FieldResvd")!=null?sonObject.get("FieldResvd").toString():"");
                tmp[l].setFieldType(sonObject.get("FieldType")!=null?sonObject.get("FieldType").toString():"");
                tmp[l].setFieldCpack(sonObject.get("FieldCpack")!=null?sonObject.get("FieldCpack").toString():"");
              }
              im.setChildren(tmp);
              x[k-1]=im;
            }
          }
          t[i].setChildren(x);
        }else{//-----------------正常情况---------------
          InterfaceModel[] x= returnInterfaceModel(jsonArray);
          t[i].setChildren(x);
        }
      }else{
        t[i].setFieldAccuracy(jsonObject.get("FieldAccuracy")!=null?jsonObject.get("FieldAccuracy").toString():"");
        t[i].setFieldDesc(jsonObject.get("FieldDesc")!=null?jsonObject.get("FieldDesc").toString():"");
        t[i].setFieldLength(jsonObject.get("FieldLength")!=null?jsonObject.get("FieldLength").toString():"");
        t[i].setFieldName(jsonObject.get("FieldName")!=null?jsonObject.get("FieldName").toString():"");
        t[i].setFieldNecc(jsonObject.get("FieldNecc")!=null?jsonObject.get("FieldNecc").toString():"");
        t[i].setFieldResvd(jsonObject.get("FieldResvd")!=null?jsonObject.get("FieldResvd").toString():"");
        t[i].setFieldType(jsonObject.get("FieldType")!=null?jsonObject.get("FieldType").toString():"");
        t[i].setFieldCpack(jsonObject.get("FieldCpack")!=null?jsonObject.get("FieldCpack").toString():"");
      }
    }
    return t;
  }

  //比较字段删除和修改
  private static void  compareObj(InterfaceModel[] oldm,InterfaceModel[] newm,InterfaceModel temp){
    for(int i=0;i<oldm.length;i++){
      String fieldname=oldm[i].getFieldName();
      boolean ishas=true;
      for(int k=0;k<newm.length;k++){
        InterfaceModel im=newm[k];
        if(fieldname.equals(im.getFieldName())){
          if(!oldm[i].getFieldDesc().equals(im.getFieldDesc())){
            temp.getChildren()[k].setFieldState("modify");
            temp.getChildren()[k].setFieldDesc(oldm[i].getFieldDesc()+"->"+im.getFieldDesc());
          }
          if(!oldm[i].getFieldAccuracy().equals(im.getFieldAccuracy())){
            temp.getChildren()[k].setFieldState("modify");
            temp.getChildren()[k].setFieldAccuracy(oldm[i].getFieldAccuracy()+"->"+im.getFieldAccuracy());
          }
          if(!oldm[i].getFieldLength().equals(im.getFieldLength())){
            temp.getChildren()[k].setFieldState("modify");
            temp.getChildren()[k].setFieldLength(oldm[i].getFieldLength()+"->"+im.getFieldLength());
          }
          if(!oldm[i].getFieldNecc().equals(im.getFieldNecc())){
            temp.getChildren()[k].setFieldState("modify");
            temp.getChildren()[k].setFieldNecc(oldm[i].getFieldNecc()+"->"+im.getFieldNecc());
          }
          if(!oldm[i].getFieldType().equals(im.getFieldType())){
            temp.getChildren()[k].setFieldState("modify");
            temp.getChildren()[k].setFieldType(oldm[i].getFieldType()+"->"+im.getFieldType());
          }
          if(!oldm[i].getFieldResvd().equals(im.getFieldResvd())){
            temp.getChildren()[k].setFieldState("modify");
            temp.getChildren()[k].setFieldResvd(oldm[i].getFieldResvd()+"->"+im.getFieldResvd());
          }
          if(!oldm[i].getFieldCpack().equals(im.getFieldCpack())){
            temp.getChildren()[k].setFieldState("modify");
            temp.getChildren()[k].setFieldCpack(oldm[i].getFieldCpack()+"->"+im.getFieldCpack());
          }
          if(oldm[i].getChildren()!=null){//存在子节点，处理子节点
            if(im.getChildren()!=null){//都包含子节点
              compareObj(oldm[i].getChildren(),im.getChildren(),temp.getChildren()[k]);
            }else{//新的没有子节点
              temp.getChildren()[k].setFieldState("modify");
              for(int j=0;j<oldm[i].getChildren().length;j++){
                  modifyState(oldm[i].getChildren()[j],"delete");
              }
              temp.getChildren()[k].setChildren(oldm[i].getChildren());
            }
          }
          ishas=false;
        }
      }
      //判断是否是删除节点
      if(ishas){
        InterfaceModel mid=new InterfaceModel();
        mid.setChildren(null);
        mid.setFieldDesc(oldm[i].getFieldDesc());
        mid.setFieldName(oldm[i].getFieldName());
        mid.setFieldAccuracy(oldm[i].getFieldAccuracy());
        mid.setFieldLength(oldm[i].getFieldLength());
        mid.setFieldType(oldm[i].getFieldType());
        mid.setFieldNecc(oldm[i].getFieldNecc());
        mid.setFieldResvd(oldm[i].getFieldResvd());
        mid.setFieldCpack(oldm[i].getFieldCpack());
        mid.setFieldState("delete");
        int len=temp.getChildren().length+1;
        InterfaceModel[] tempArray=new InterfaceModel[len];
        for(int a=0;a<temp.getChildren().length;a++){
          tempArray[a]=temp.getChildren()[a];
        }
        tempArray[len-1]=mid;
        temp.setChildren(tempArray);
      }
    }
  }

  //设置子集节点状态
  private static void  modifyState(InterfaceModel temp,String state){
    temp.setFieldState(state);
    if(temp.getChildren()!=null){
      for(int i=0;i<temp.getChildren().length;i++){
        modifyState(temp.getChildren()[i],state);
      }
    }
  }

  //比较那些字段是新增的
  private static void addObj(InterfaceModel[] oldm,InterfaceModel[] newm,InterfaceModel temp){
    for(int i=0;i<newm.length;i++){
      String fieldname=newm[i].getFieldName();
      boolean ishas=false;
      for(InterfaceModel im:oldm){
        if(fieldname.equals(im.getFieldName())){
          if(newm[i].getChildren()!=null){
            if(im.getChildren()!=null){
              addObj(im.getChildren(),newm[i].getChildren(),temp.getChildren()[i]);
            }else{
              for(int j=0;j<temp.getChildren().length;j++){
                modifyState(temp.getChildren()[i],"add");
              }
              temp.getChildren()[i].setFieldState("modify");
            }
           }
          ishas=true;
        }
      }
      if(!ishas){
        modifyState(temp.getChildren()[i],"add");
      }

    }
  }

  //判断整体是否变化，初始为false，变化了返回true,并修改状态把不需要的值设为“”
  public static boolean ifChanged(InterfaceModel temp,boolean flag){
    if(temp!=null){
      if(temp.getChildren()!=null){
        for(int i=0;i<temp.getChildren().length;i++){
          flag=ifChanged(temp.getChildren()[i],flag);//处理子节点
          if(flag&&"init".equals(temp.getFieldState())){
            temp.setFieldState("modify");
            temp.setFieldCpack("");
            temp.setFieldAccuracy("");
            temp.setFieldDesc("");
            temp.setFieldNecc("");
            temp.setFieldResvd("");
            temp.setFieldType("");
            temp.setFieldLength("");
          }else{
            if("modify".equals(temp.getFieldState())){//将未变化的值设为“”
              if(!temp.getFieldCpack().contains("->")){
                temp.setFieldCpack("");
              }
              if(!temp.getFieldAccuracy().contains("->")){
                temp.setFieldAccuracy("");
              }
              if(!temp.getFieldDesc().contains("->")){
                temp.setFieldDesc("");
              }
              if(!temp.getFieldLength().contains("->")){
                temp.setFieldLength("");
              }
              if(!temp.getFieldNecc().contains("->")){
                temp.setFieldNecc("");
              }
              if(!temp.getFieldType().contains("->")){
                temp.setFieldType("");
              }
              if(!temp.getFieldResvd().contains("->")){
                temp.setFieldResvd("");
              }
            }
          }
        }
      }else{
        if(!"init".equals(temp.getFieldState())){
          flag=true;
          if("modify".equals(temp.getFieldState())){//将未变化的值设为“”
            if(!temp.getFieldCpack().contains("->")){
              temp.setFieldCpack("");
            }
            if(!temp.getFieldAccuracy().contains("->")){
              temp.setFieldAccuracy("");
            }
            if(!temp.getFieldDesc().contains("->")){
              temp.setFieldDesc("");
            }
            if(!temp.getFieldLength().contains("->")){
              temp.setFieldLength("");
            }
            if(!temp.getFieldNecc().contains("->")){
              temp.setFieldNecc("");
            }
            if(!temp.getFieldType().contains("->")){
              temp.setFieldType("");
            }
            if(!temp.getFieldResvd().contains("->")){
              temp.setFieldResvd("");
            }
          }
        }
      }
    }
    return flag;
  }


  private static String getInterfaceModify(InterfaceModel[] oldm, InterfaceModel[] newm){
    InterfaceModel interfaceModel=new InterfaceModel();
    interfaceModel.setFieldName("");
    interfaceModel.setFieldState("");
    interfaceModel.setFieldDesc("");
    interfaceModel.setFieldResvd("");
    interfaceModel.setFieldType("");
    interfaceModel.setFieldNecc("");
    interfaceModel.setFieldLength("");
    interfaceModel.setFieldAccuracy("");
    interfaceModel.setFieldCpack("");
    interfaceModel.setChildren(newm);
    //判断删除和修改节点
    compareObj(oldm,newm,interfaceModel);
    //判断新增节点
    addObj(oldm,newm,interfaceModel);
    //根据变化情况删除未变化的节点数据和设置父节点状态
    ifChanged(interfaceModel,false);
    JSONObject jsonObject = JSONObject.fromObject(interfaceModel);
    //删除未变化的节点
    getEnd(jsonObject);
    return jsonObject.getJSONArray("children").toString();
  }

  public static void getEnd(JSONObject jsonObject){
    if(jsonObject!=null){
      if("init".equals(jsonObject.get("fieldState"))){
        jsonObject.clear();
      }else{
        if(jsonObject.getJSONArray("children")!=null&&jsonObject.getJSONArray("children").size()!=0){
          int k= jsonObject.getJSONArray("children").size();
          for(int i=0;i<k;i++){
            getEnd(jsonObject.getJSONArray("children").getJSONObject(i));
          }
        }
      }
    }
  }


  public String getNoNullJson(JSONObject jsonObject){


    return  "";
  }

//  private static String getInterfaceModify(InterfaceModel[] oldm, InterfaceModel[] newm){
//    InterfaceModel interfaceModel=new InterfaceModel();
//    interfaceModel.setFieldName("接口变更");
//    interfaceModel.setFieldState("init-初始;add-新增;modify-修改;delete-删除");
//    interfaceModel.setFieldDesc("版本变更描述，虚拟root节点");
//    interfaceModel.setFieldResvd("V.0.0");
//    interfaceModel.setFieldType("0-String;1-Int;2-Object;3-Array;4-Double;5-Byte;6-short;7-char;8-float;9-long;10-boolean;11-decimal;12-BigDecimal");
//    interfaceModel.setFieldNecc("0-否;1-是");
//    interfaceModel.setFieldLength("12");
//    interfaceModel.setFieldAccuracy("0");
//    interfaceModel.setFieldCpack("");
//    interfaceModel.setChildren(newm);
//    compareObj(oldm,newm,interfaceModel);
//    addObj(oldm,newm,interfaceModel);
//    if(ifChanged(interfaceModel,false)){
//      JSONObject jsonObject = JSONObject.fromObject(interfaceModel);
//      return jsonObject.toString();
//    }else{
//      return "";
//    }
//  }


  public static String jsonDB(JSONArray oldJson, JSONArray newJson, boolean p) {
    JSONArray oldChildren ;
    JSONArray newChildren ;
    if(p){
      oldChildren = returnChildren(oldJson);
      newChildren = returnChildren(newJson);
      if (oldChildren == null || newChildren == null) {
        return null;
      }
    } else {
      oldChildren = oldJson;
      newChildren = newJson;
    }
    InterfaceModel[] oldmodel =returnInterfaceModel(oldChildren);
    InterfaceModel[] newmodel =returnInterfaceModel(newChildren);
    return getInterfaceModify(oldmodel,newmodel);
  }







  public static void main(String[] args) {

  }

}
