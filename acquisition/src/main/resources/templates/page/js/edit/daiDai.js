console.log("load:js/edit/roomUser.js")
/*
setInterval(function (){
 $("#query_gzb_b001").click()
},2000)*/
let initListPage_001=gzb.initListPage
gzb.initListPage=function (){

    gzb.api.query+="?sortField=room_user_time&sortType=desc";
    initListPage_001();
}