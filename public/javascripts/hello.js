if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}

function disable(page,total){
    
    if(page<1){
        $('#prev').attr('disabled','disabled');
        
    }
    else{
        $('#prev').removeAttr('disabled');
    }
    if((page+1)*5>=total){
        $('#next').attr('disabled','disabled');
    }
    else{
        $('#next').removeAttr('disabled');
    }
}

function search(){
    var search = $('#searchBox').val();
    window.location.href = "/show?search="+search;
    
}