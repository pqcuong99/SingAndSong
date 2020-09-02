package com.example.appnghenhac;

public class Bai_Hat  {
   public String TenBH, Casi, id,uri;
    public String image;
    public Bai_Hat(String tenBH, String casi, String id) {
        TenBH = tenBH;
        Casi = casi;
        this.id = id;
    }

    public Bai_Hat(String tenBH, String casi, String id, String uri) {
        TenBH = tenBH;
        Casi = casi;
        this.id = id;
        this.uri = uri;
    }

    public Bai_Hat(String tenBH, String casi, String id, String uri, String image) {
        TenBH = tenBH;
        Casi = casi;
        this.id = id;
        this.uri = uri;
        this.image = image;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTenBH() {
        return TenBH;
    }

    public void setTenBH(String tenBH) {
        TenBH = tenBH;
    }

    public String getCasi() {
        return Casi;
    }

    public void setCasi(String casi) {
        Casi = casi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNameShort(){
        StringBuffer string = new StringBuffer();
        for(int i=0;i<8;i++){
            try{
                string.append(TenBH.charAt(i));
            }catch (Exception ex){

            }
        }
        string.append("...");
        return  string.toString();
    }
    public String getCaSiShort(){
        StringBuffer string = new StringBuffer();
        for(int i=0;i<8;i++){
            try{
                string.append(Casi.charAt(i));
            }catch (Exception ex){

            }
        }
        string.append("...");
        return  string.toString();
    }
}
