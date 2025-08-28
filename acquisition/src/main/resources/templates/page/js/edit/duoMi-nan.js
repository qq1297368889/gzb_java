console.log("load:js/edit/duoMi-nan.js")
/*
setInterval(function (){
 $("#query_gzb_b001").click()
},2000)*/
let initListPage_001=gzb.initListPage
gzb.initListPage=function (){

    gzb.api.query+="?" +
        "sortField=room_user_time&sortType=desc" +
        "&field=room_user_sex&symbol=1&value=1&montage=1" +
        "&field=room_user_type&symbol=1&value=2&montage=1";
    initListPage_001();
}